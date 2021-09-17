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

package com.liferay.calendar.internal.notification;

import com.liferay.calendar.constants.CalendarNotificationTemplateConstants;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.model.CalendarNotificationTemplate;
import com.liferay.calendar.notification.NotificationField;
import com.liferay.calendar.notification.NotificationRecipient;
import com.liferay.calendar.notification.NotificationSender;
import com.liferay.calendar.notification.NotificationSenderException;
import com.liferay.calendar.notification.NotificationTemplateContext;
import com.liferay.calendar.notification.NotificationUtil;
import com.liferay.calendar.service.impl.CalendarBookingLocalServiceImpl;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.SubscriptionSender;

import java.io.File;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eduardo Lundgren
 */
@Component(
	immediate = true, property = "notification.type=email",
	service = NotificationSender.class
)
public class EmailNotificationSender implements NotificationSender {

	@Override
	public void sendNotification(
			String fromAddress, String fromName,
			NotificationRecipient notificationRecipient,
			NotificationTemplateContext notificationTemplateContext)
		throws NotificationSenderException {

		try {
			CalendarNotificationTemplate calendarNotificationTemplate =
				notificationTemplateContext.getCalendarNotificationTemplate();

			String fromAddressValue = NotificationUtil.getTemplatePropertyValue(
				calendarNotificationTemplate,
				CalendarNotificationTemplateConstants.PROPERTY_FROM_ADDRESS,
				fromAddress);
			String fromNameValue = NotificationUtil.getTemplatePropertyValue(
				calendarNotificationTemplate,
				CalendarNotificationTemplateConstants.PROPERTY_FROM_NAME,
				fromName);

			notificationTemplateContext.setFromAddress(fromAddressValue);
			notificationTemplateContext.setFromName(fromNameValue);
			notificationTemplateContext.setToAddress(
				notificationRecipient.getEmailAddress());
			notificationTemplateContext.setToName(
				notificationRecipient.getName());

			_sendNotification(
				notificationRecipient, notificationTemplateContext,
				NotificationTemplateRenderer.render(
					notificationTemplateContext, NotificationField.BODY,
					NotificationTemplateRenderer.MODE_HTML),
				fromAddressValue, fromNameValue,
				NotificationTemplateRenderer.render(
					notificationTemplateContext, NotificationField.SUBJECT,
					NotificationTemplateRenderer.MODE_PLAIN));
		}
		catch (Exception exception) {
			throw new NotificationSenderException(exception);
		}
	}

	private void _sendNotification(
			NotificationRecipient notificationRecipient,
			NotificationTemplateContext notificationTemplateContext,
			String body, String fromEmail, String fromName, String subject)
		throws NotificationSenderException {

		try {
			SubscriptionSender subscriptionSender = new SubscriptionSender();

			subscriptionSender.addFileAttachment(
				(File)notificationTemplateContext.getAttribute("icsFile"));
			subscriptionSender.setClassName(
				CalendarBookingLocalServiceImpl.class.getName());
			subscriptionSender.setClassPK(
				notificationTemplateContext.getCalendarId());
			subscriptionSender.setCompanyId(
				notificationTemplateContext.getCompanyId());
			subscriptionSender.setContextAttributes(
				"[$COMPANY_ID$]", notificationTemplateContext.getCompanyId(),
				"[$CALENDAR_NAME$]",
				notificationTemplateContext.getAttribute("calendarName"),
				"[$EVENT_END_DATE$]",
				notificationTemplateContext.getAttribute("endTime"),
				"[$EVENT_LOCATION$]",
				notificationTemplateContext.getAttribute("location"),
				"[$EVENT_START_DATE$]",
				notificationTemplateContext.getAttribute("startTime"),
				"[$EVENT_TITLE$]",
				notificationTemplateContext.getAttribute("title"),
				"[$EVENT_URL$]",
				notificationTemplateContext.getAttribute("url"),
				"[$INSTANCE_START_TIME$]",
				notificationTemplateContext.getAttribute("instanceStartTime"),
				"[$PORTAL_URL$]",
				notificationTemplateContext.getAttribute("portalURL"),
				"[$PORTLET_NAME$]",
				notificationTemplateContext.getAttribute("portletName"),
				"[$SITE_NAME$]",
				notificationTemplateContext.getAttribute("siteName"),
				"[$TO_NAME$]", notificationRecipient.getName());
			subscriptionSender.setContextCreatorUserPrefix("EVENT");
			subscriptionSender.setFrom(fromEmail, fromName);
			subscriptionSender.setHtmlFormat(
				notificationRecipient.isHTMLFormat());

			CalendarNotificationTemplate calendarNotificationTemplate =
				notificationTemplateContext.getCalendarNotificationTemplate();

			if (calendarNotificationTemplate != null) {
				subscriptionSender.setCreatorUserId(
					calendarNotificationTemplate.getUserId());
				subscriptionSender.setLocalizedBodyMap(
					LocalizationUtil.getLocalizationMap(
						calendarNotificationTemplate.getBody()));
				subscriptionSender.setLocalizedSubjectMap(
					LocalizationUtil.getLocalizationMap(
						calendarNotificationTemplate.getSubject()));
			}
			else {
				subscriptionSender.setBody(body);
				subscriptionSender.setSubject(subject);
			}

			subscriptionSender.setMailId(
				"event", notificationTemplateContext.getCalendarId());
			subscriptionSender.setPortletId(
				PortletProviderUtil.getPortletId(
					CalendarBooking.class.getName(),
					PortletProvider.Action.EDIT));
			subscriptionSender.setScopeGroupId(
				notificationTemplateContext.getGroupId());
			subscriptionSender.addRuntimeSubscribers(
				notificationRecipient.getEmailAddress(),
				notificationRecipient.getName());

			subscriptionSender.flushNotificationsAsync();
		}
		catch (Exception exception) {
			throw new NotificationSenderException(
				"Unable to send mail message", exception);
		}
	}

}