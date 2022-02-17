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
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.BaseUserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationHandler;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.StringUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Joe Duffy
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + BatchEngineImportTaskConstants.BATCH_ENGINE_ADMIN_IMPORT,
	service = UserNotificationHandler.class
)
public class BatchEngineImportUserNotificationHandler
	extends BaseUserNotificationHandler {

	/**
	 * AdminLoginUserNotificationHandler: Constructor class.
	 */
	public BatchEngineImportUserNotificationHandler() {
		setPortletId(BatchEngineImportTaskConstants.BATCH_ENGINE_ADMIN_IMPORT);
	}

	@Override
	protected String getBody(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		String userName = LanguageUtil.get(
			serviceContext.getLocale(), _UKNOWN_USER_KEY);

		// okay, we need to get the user for the event

		User user = _userLocalService.fetchUser(
			userNotificationEvent.getUserId());

		if (user != null) {

			// get the company the user belongs to.

			Company company = _companyLocalService.fetchCompany(
				user.getCompanyId());

			// based on the company auth type, find the user name to display.
			// so we'll get screen name or email address or whatever they're
			// using to log in.

			String authType = company.getAuthType();

			if (company != null) {
				if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
					userName = user.getEmailAddress();
				}
				else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
					userName = user.getScreenName();
				}
				else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
					userName = String.valueOf(user.getUserId());
				}
			}
		}

		// we'll be stashing the client address in the payload of the event,
		// so let's extract it here.

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			userNotificationEvent.getPayload());

		//      String fromHost = jsonObject.getString(Constants.FROM_HOST);

		String fromHost = jsonObject.getString("fromHost");

		// fetch our strings via the language bundle.

		String title = LanguageUtil.get(serviceContext.getLocale(), _TITLE_KEY);

		String body = LanguageUtil.format(
			serviceContext.getLocale(), _BODY_KEY,
			new Object[] {userName, fromHost});

		// build the html using our template.

		return StringUtil.replace(
			_BODY_TEMPLATE, _BODY_REPLACEMENTS, new String[] {title, body});
	}

	@Reference(unbind = "-")
	protected void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final String _BODY_KEY = "body.batch.engine";

	private static final String[] _BODY_REPLACEMENTS = {
		"[$TITLE$]", "[$BODY$]"
	};

	private static final String _BODY_TEMPLATE =
		"<div class=\"title\">[$TITLE$]</div><div class=\"body\">[$BODY$]</div>";

	private static final String _TITLE_KEY = "title.batch.engine";

	private static final String _UKNOWN_USER_KEY = "unknown.user";

	private CompanyLocalService _companyLocalService;
	private UserLocalService _userLocalService;

}