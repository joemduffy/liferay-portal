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

package com.liferay.headless.delivery.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.odata.entity.EntityField;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class StructuredContentFolderResourceTest
	extends BaseStructuredContentFolderResourceTestCase {

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"description", "name"};
	}

	@Override
	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		List<EntityField> entityFields = super.getEntityFields(type);

		return entityFields.stream(
		).filter(
			entityField -> !Objects.equals(entityField.getName(), "creatorId")
		).collect(
			Collectors.toList()
		);
	}

	@Override
	protected Long
			testGetStructuredContentFolderStructuredContentFoldersPage_getIrrelevantParentStructuredContentFolderId()
		throws Exception {

		JournalFolder journalFolder = JournalTestUtil.addFolder(
			irrelevantGroup.getGroupId(), RandomTestUtil.randomString());

		return journalFolder.getFolderId();
	}

	@Override
	protected Long
			testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId()
		throws Exception {

		JournalFolder journalFolder = JournalTestUtil.addFolder(
			testGroup.getGroupId(), RandomTestUtil.randomString());

		return journalFolder.getFolderId();
	}

}