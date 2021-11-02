/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.rest.client.dto.v1_0;

import com.liferay.search.experiences.rest.client.function.UnsafeSupplier;
import com.liferay.search.experiences.rest.client.serdes.v1_0.ElementDefinitionSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@Generated("")
public class ElementDefinition implements Cloneable, Serializable {

	public static ElementDefinition toDTO(String json) {
		return ElementDefinitionSerDes.toDTO(json);
	}

	public SXPBlueprint getSxpBlueprint() {
		return sxpBlueprint;
	}

	public void setSxpBlueprint(SXPBlueprint sxpBlueprint) {
		this.sxpBlueprint = sxpBlueprint;
	}

	public void setSxpBlueprint(
		UnsafeSupplier<SXPBlueprint, Exception> sxpBlueprintUnsafeSupplier) {

		try {
			sxpBlueprint = sxpBlueprintUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected SXPBlueprint sxpBlueprint;

	public UiConfiguration getUiConfiguration() {
		return uiConfiguration;
	}

	public void setUiConfiguration(UiConfiguration uiConfiguration) {
		this.uiConfiguration = uiConfiguration;
	}

	public void setUiConfiguration(
		UnsafeSupplier<UiConfiguration, Exception>
			uiConfigurationUnsafeSupplier) {

		try {
			uiConfiguration = uiConfigurationUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected UiConfiguration uiConfiguration;

	@Override
	public ElementDefinition clone() throws CloneNotSupportedException {
		return (ElementDefinition)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ElementDefinition)) {
			return false;
		}

		ElementDefinition elementDefinition = (ElementDefinition)object;

		return Objects.equals(toString(), elementDefinition.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return ElementDefinitionSerDes.toJSON(this);
	}

}