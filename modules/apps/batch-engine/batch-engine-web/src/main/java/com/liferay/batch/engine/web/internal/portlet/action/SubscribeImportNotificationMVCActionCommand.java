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

package com.liferay.batch.engine.web.internal.portlet.action;

import com.liferay.batch.engine.constants.BatchEngineImportTaskConstants;
import com.liferay.batch.engine.web.internal.portlet.BatchEngineAdminPortlet;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.SubscriptionLocalService;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * class SubscribeMVCActionCommand: The action command to handle subscription and unsubscription.
 *
 * @author Joe Duffy
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BatchEngineImportTaskConstants.BATCH_ENGINE_ADMIN_IMPORT,
		"mvc.command.name=/update_subscription/update"
	},
	service = MVCActionCommand.class
)
public class SubscribeImportNotificationMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		long userId = PortalUtil.getUserId(actionRequest);

		if (Constants.SUBSCRIBE.equals(cmd)) {
			_subscriptionLocalService.addSubscription(
				userId, 0, BatchEngineAdminPortlet.class.getName(), 0);
		}
		else if (Constants.UNSUBSCRIBE.equals(cmd)) {
			_subscriptionLocalService.deleteSubscription(
				userId, BatchEngineAdminPortlet.class.getName(), 0);
		}
	}

	@Reference(unbind = "-")
	protected void setSubscriptionLocalService(
		SubscriptionLocalService subscriptionLocalService) {

		_subscriptionLocalService = subscriptionLocalService;
	}

	private SubscriptionLocalService _subscriptionLocalService;

}