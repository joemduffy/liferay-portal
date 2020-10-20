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

package com.liferay.asset.info.display.field.util;

import com.liferay.asset.kernel.model.ClassType;
import com.liferay.info.display.contributor.InfoDisplayField;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;
import java.util.Locale;

/**
 * @author Jürgen Kappler
 * @deprecated As of Cavanaugh (7.4.x), with no direct replacement
 */
@Deprecated
public class ClassTypesInfoDisplayFieldProviderUtil {

	public static List<InfoDisplayField> getClassTypeInfoDisplayFields(
			String className, long classTypeId, Locale locale)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

	public static List<ClassType> getClassTypes(
			long groupId, String className, Locale locale)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

}