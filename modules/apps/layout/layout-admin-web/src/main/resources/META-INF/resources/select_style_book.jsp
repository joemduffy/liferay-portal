<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
SelectStyleBookEntryDisplayContext selectStyleBookEntryDisplayContext = new SelectStyleBookEntryDisplayContext(request, layoutsAdminDisplayContext.getSelLayout(), liferayPortletResponse);

List<StyleBookEntry> styleBookEntries = selectStyleBookEntryDisplayContext.getStyleBookEntries();
%>

<aui:form cssClass="container-fluid container-fluid-max-xl container-view" name="fm">
	<ul class="card-page card-page-equal-height">
		<li class="card-page-item card-page-item-asset">
			<div class="form-check form-check-card">
				<clay:vertical-card
					verticalCard="<%= new DefaultStylebookLayoutVerticalCard(selectStyleBookEntryDisplayContext.getDefaultStyleBookLabel(), selectStyleBookEntryDisplayContext.getDefaultStyleBookEntry(), renderRequest, Objects.equals(selectStyleBookEntryDisplayContext.getStyleBookEntryId(), 0L)) %>"
				/>
			</div>
		</li>

		<%
		for (StyleBookEntry styleBookEntry : styleBookEntries) {
		%>

			<li class="card-page-item card-page-item-asset">
				<div class="form-check form-check-card">
					<clay:vertical-card
						verticalCard="<%= new SelectStylebookLayoutVerticalCard(styleBookEntry, renderRequest, Objects.equals(styleBookEntry.getStyleBookEntryId(), selectStyleBookEntryDisplayContext.getStyleBookEntryId())) %>"
					/>
				</div>
			</li>

		<%
		}
		%>

	</ul>
</aui:form>

<liferay-frontend:component
	context="<%= selectStyleBookEntryDisplayContext.getContext() %>"
	module="js/SelectCardHandler"
/>