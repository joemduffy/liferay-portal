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

package com.liferay.headless.foundation.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.foundation.dto.v1_0.WebUrl;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.ListType;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.Website;
import com.liferay.portal.kernel.service.ListTypeServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.WebsiteLocalServiceUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class WebUrlResourceTest extends BaseWebUrlResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_organization = OrganizationTestUtil.addOrganization();
		_user = UserTestUtil.addGroupAdminUser(testGroup);
	}

	@Override
	protected void assertValid(WebUrl webUrl) {
		boolean valid = false;

		if ((webUrl.getId() != null) && (webUrl.getUrl() != null)) {
			valid = true;
		}

		Assert.assertTrue(valid);
	}

	@Override
	protected boolean equals(WebUrl webUrl1, WebUrl webUrl2) {
		if (Objects.equals(webUrl1.getUrl(), webUrl2.getUrl())) {
			return true;
		}

		return false;
	}

	@Override
	protected WebUrl randomWebUrl() {
		return new WebUrl() {
			{
				url = "http://" + RandomTestUtil.randomString();
			}
		};
	}

	@Override
	protected WebUrl testGetOrganizationWebUrlsPage_addWebUrl(
			Long organizationId, WebUrl webUrl)
		throws Exception {

		return _addWebUrl(
			webUrl, _organization.getModelClassName(),
			_organization.getOrganizationId(),
			ListTypeConstants.ORGANIZATION_WEBSITE);
	}

	@Override
	protected Long testGetOrganizationWebUrlsPage_getOrganizationId()
		throws Exception {

		return _organization.getOrganizationId();
	}

	@Override
	protected WebUrl testGetUserAccountWebUrlsPage_addWebUrl(
			Long userAccountId, WebUrl webUrl)
		throws Exception {

		return _addWebUrl(
			webUrl, Contact.class.getName(), _user.getContactId(),
			ListTypeConstants.CONTACT_WEBSITE);
	}

	@Override
	protected Long testGetUserAccountWebUrlsPage_getUserAccountId()
		throws Exception {

		return _user.getUserId();
	}

	@Override
	protected WebUrl testGetWebUrl_addWebUrl() throws Exception {
		WebUrl webUrl = randomWebUrl();

		return _addWebUrl(
			webUrl, Contact.class.getName(), _user.getContactId(),
			ListTypeConstants.CONTACT_WEBSITE);
	}

	private WebUrl _addWebUrl(
			WebUrl webUrl, String className, long classPK, String listTypeId)
		throws Exception {

		return _toWebUrl(
			WebsiteLocalServiceUtil.addWebsite(
				_user.getUserId(), className, classPK, webUrl.getUrl(),
				_getListTypeId(listTypeId), false, new ServiceContext()));
	}

	private long _getListTypeId(String listTypeId) {
		List<ListType> listTypes = ListTypeServiceUtil.getListTypes(listTypeId);

		ListType listType = listTypes.get(0);

		return listType.getListTypeId();
	}

	private WebUrl _toWebUrl(Website website) {
		return new WebUrl() {
			{
				id = website.getWebsiteId();
				url = website.getUrl();
			}
		};
	}

	private Organization _organization;
	private User _user;

}