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

package com.liferay.batch.engine.web.internal.notifications;

import com.liferay.batch.engine.constants.BatchEngineImportTaskConstants;
import com.liferay.portal.kernel.model.UserNotificationDeliveryConstants;
import com.liferay.portal.kernel.notifications.UserNotificationDefinition;
import com.liferay.portal.kernel.notifications.UserNotificationDeliveryType;

import org.osgi.service.component.annotations.Component;

/**
 * @author Joe Duffy
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + BatchEngineImportTaskConstants.BATCH_ENGINE_ADMIN_IMPORT,
	service = UserNotificationDefinition.class
)
public class BatchEngineImportUserNotificationDefinition
	extends UserNotificationDefinition {

	public BatchEngineImportUserNotificationDefinition() {
		super(
			BatchEngineImportTaskConstants.BATCH_ENGINE_ADMIN_IMPORT, 0,
			UserNotificationDefinition.NOTIFICATION_TYPE_ADD_ENTRY,
			"receive-a-notification-when-import-task-completes");

		// add receive-a-notification-when-batch-import-completes to lang
		// and replace key here

		addUserNotificationDeliveryType(
			new UserNotificationDeliveryType(
				"website", UserNotificationDeliveryConstants.TYPE_WEBSITE, true,
				true));
	}

}