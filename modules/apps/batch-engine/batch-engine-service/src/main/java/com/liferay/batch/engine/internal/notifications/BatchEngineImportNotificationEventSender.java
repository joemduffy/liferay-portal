/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.batch.engine.internal.notifications;

import com.liferay.batch.engine.constants.BatchEngineImportTaskConstants;
import com.liferay.batch.engine.model.BatchEngineImportTask;
//import com.liferay.batch.engine.web.internal.portlet.BatchEngineAdminPortlet;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.settings.LocalizedValuesMap;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.GroupSubscriptionCheckSubscriptionSender;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SubscriptionSender;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.sql.Blob;
import java.util.Locale;

/**
 * class BatchPlannerImportNotificationEventSender: This is the admin login notification
 * sender that implements the import completion event sender.
 *
 * @author Joe Duffy
 */
public class BatchEngineImportNotificationEventSender {

	public static void notifySubscribers(
		BatchEngineImportTask batchEngineImportTask, String fromHost,
		ServiceContext serviceContext)
		throws PortalException {

		long userId = batchEngineImportTask.getUserId();
		long companyId = batchEngineImportTask.getCompanyId();

		String entryTitle = "Batch Import Completion";

		String entryURL = "http://localhost:8080/group/guest/~/control_panel/" +
						  "manage?p_p_id=com_liferay_batch_planner_web_intern" +
						  "al_portlet_BatchPlannerPortlet&p_p_lifecycle=0&p_p" +
						  "_state=maximized&p_v_l_s_g_id=38321&p_p_auth=" +
						  "JmrCRq2Y";

		if (Validator.isNull(entryURL)) {
			entryURL = StringBundler.concat(
				serviceContext.getLayoutFullURL(),
				Portal.FRIENDLY_URL_SEPARATOR, "batch-engine/",
				batchEngineImportTask.getClassName());

			// alternative: getBatchEngineImportTaskID.toString

		}

		String fromName = "";
		String fromAddress = "";

		LocalizedValuesMap subjectLocalizedValuesMap = new LocalizedValuesMap();
		LocalizedValuesMap bodyLocalizedValuesMap = new LocalizedValuesMap();

		subjectLocalizedValuesMap.put(
			LocaleUtil.ENGLISH, "Batch Import Completion");
		bodyLocalizedValuesMap.put(
			LocaleUtil.ENGLISH, "Batch Import Has Completed.");

		SubscriptionSender subscriptionSender = new SubscriptionSender();
//			new GroupSubscriptionCheckSubscriptionSender(BatchEngineImportTaskConstants.BATCH_RESOURCE_NAME);

		subscriptionSender.setClassPK(batchEngineImportTask.getBatchEngineImportTaskId());
		subscriptionSender.setClassName(
			"com.liferay.batch.engine.web." +
			"internal.portlet.BatchEngineAdminPortlet");
//			BatchEngineAdminPortlet.class.getName());

		subscriptionSender.setCompanyId(companyId);
	/*	Blob blob = batchEngineImportTask.getContent();
		if (blob != null) {
			byte[] bdata = blob.getBytes(1, (int) blob.length());
		}*/
		subscriptionSender.setContextAttribute(
			"[$BATCH_IMPORT_CONTENT$]",
			"Details of import task",
			false);

		String description = "Import task status";

		if (Validator.isNotNull(description)) {
			subscriptionSender.setContextAttribute(
				"[$BATCH_IMPORT_DESCRIPTION$]", description, false);
		}
		else {
			subscriptionSender.setContextAttribute(
				"[$BATCH_IMPORT_DESCRIPTION$]",
				"More description...",
//				StringUtil.shorten(HtmlUtil.stripHtml(entry.getContent()), 400),
				false);
		}

		subscriptionSender.setContextAttributes(
			"[$BATCH_IMPORT_CREATE_DATE$]",
			Time.getSimpleDate(batchEngineImportTask.getModifiedDate(), "yyyy/MM/dd"),
			"[$BATCH_IMPORT_STATUS_BY_USER_NAME$]", 0,
			"[$BATCH_IMPORT_TITLE$]", "Completion Status",
			"[$BATCH_IMPORT_UPDATE_COMMENT$]", "<done>",
//			HtmlUtil.replaceNewLine(
//				GetterUtil.getString(
//					serviceContext.getAttribute("emailEntryUpdatedComment"))),
			"[$BATCH_IMPORT_URL$]", "localhost:8080",
			"[$BATCH_IMPORT_USER_PORTRAIT_URL$]", "localhost:8080",
//			workflowContext.get(WorkflowConstants.CONTEXT_USER_PORTRAIT_URL),
			"[$BATCH_IMPORT_USER_URL$]", "localhost:8080"
//			workflowContext.get(WorkflowConstants.CONTEXT_USER_URL)
			);
		subscriptionSender.setContextCreatorUserPrefix("BATCH_IMPORT");
		subscriptionSender.setCreatorUserId(userId);
		subscriptionSender.setCurrentUserId(userId);
		subscriptionSender.setEntryTitle(entryTitle);
		subscriptionSender.setEntryURL(entryURL);
		subscriptionSender.setFrom(fromAddress, fromName);
		subscriptionSender.setHtmlFormat(true);

		subscriptionSender.setMailId(null, 0);

		int notificationType =
			BatchEngineImportTaskConstants.BATCH_IMPORT_COMPLETION;

		subscriptionSender.setNotificationType(notificationType);

		String portletId =
			BatchEngineImportTaskConstants.BATCH_ENGINE_ADMIN_IMPORT;

		subscriptionSender.setPortletId(portletId);

		subscriptionSender.setReplyToAddress(fromAddress);
		subscriptionSender.setServiceContext(serviceContext);

		subscriptionSender.setGroupId(serviceContext.getScopeGroupId());
		subscriptionSender.addPersistedSubscribers(
			"com.liferay.batch.engine.web.internal.portlet." +
			"BatchEngineAdminPortlet", batchEngineImportTask.getBatchEngineImportTaskId());
//			BatchEngineAdminPortlet.class.getName(), 0);

		subscriptionSender.flushNotificationsAsync();
	}

}