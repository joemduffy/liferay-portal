/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import HIDE_COMMENTS from '../sxp_elements/hideComments';
import HIDE_DEFAULT_USER from '../sxp_elements/hideDefaultUser';
import HIDE_HIDDEN_CONTENTS from '../sxp_elements/hideHiddenContents';
import LIMIT_SEARCH_TO_HEAD_VERSION from '../sxp_elements/limitSearchToHeadVersion';
import LIMIT_SEARCH_TO_PUBLISHED_CONTENTS from '../sxp_elements/limitSearchToPublishedContents';
import SCHEDULING_AWARE from '../sxp_elements/schedulingAware';
import STAGING_AWARE from '../sxp_elements/stagingAware';
import TEXT_MATCH_OVER_MULTIPLE_FIELDS from '../sxp_elements/textMatchOverMultipleFields';

export const CUSTOM_JSON_SXP_ELEMENT = {
	sxpElementTemplateJSON: {
		category: 'custom',
		clauses: [],
		conditions: {},
		description: Liferay.Language.get('editable-json-text-area'),
		enabled: true,
		icon: 'custom-field',
		title: Liferay.Language.get('custom-json-element'),
	},
};

export const DEFAULT_ADVANCED_CONFIGURATION = {};

export const DEFAULT_BASELINE_SXP_ELEMENTS = [
	TEXT_MATCH_OVER_MULTIPLE_FIELDS,
	STAGING_AWARE,
	SCHEDULING_AWARE,
	LIMIT_SEARCH_TO_HEAD_VERSION,
	LIMIT_SEARCH_TO_PUBLISHED_CONTENTS,
	HIDE_HIDDEN_CONTENTS,
	HIDE_COMMENTS,
	HIDE_DEFAULT_USER,
];

export const DEFAULT_EDIT_SXP_ELEMENT = TEXT_MATCH_OVER_MULTIPLE_FIELDS;

export const DEFAULT_SXP_ELEMENT_ICON = 'code';

export const DEFAULT_HIGHLIGHT_CONFIGURATION = {};

export const DEFAULT_PARAMETER_CONFIGURATION = {};

export const DEFAULT_SORT_CONFIGURATION = [];

export const BASELINE_CLAUSE_CONTRIBUTORS_CONFIGURATION = {
	excludes: [
		'com.liferay.account.internal.search.spi.model.query.contributor.AccountEntryKeywordQueryContributor',
		'com.liferay.account.internal.search.spi.model.query.contributor.AccountGroupKeywordQueryContributor',
		'com.liferay.address.internal.search.spi.model.query.contributor.AddressKeywordQueryContributor',
		'com.liferay.asset.categories.internal.search.spi.model.query.contributor.AssetCategoryKeywordQueryContributor',
		'com.liferay.asset.categories.internal.search.spi.model.query.contributor.AssetVocabularyKeywordQueryContributor',
		'com.liferay.asset.tags.internal.search.spi.model.query.contributor.AssetTagKeywordQueryContributor',
		'com.liferay.blogs.internal.search.spi.model.query.contributor.BlogsEntryKeywordQueryContributor',
		'com.liferay.calendar.internal.search.spi.model.query.contributor.CalendarBookingKeywordQueryContributor',
		'com.liferay.calendar.internal.search.spi.model.query.contributor.CalendarKeywordQueryContributor',
		'com.liferay.contacts.internal.search.spi.model.query.contributor.ContactKeywordQueryContributor',
		'com.liferay.data.engine.internal.search.spi.model.query.contributor.DEDataListViewKeywordQueryContributor',
		'com.liferay.depot.internal.search.spi.model.query.contributor.DepotEntryKeywordQueryContributor',
		'com.liferay.document.library.internal.search.spi.model.query.contributor.DLFileEntryKeywordQueryContributor',
		'com.liferay.document.library.internal.search.spi.model.query.contributor.DLFileEntryTypeKeywordQueryContributor',
		'com.liferay.dynamic.data.lists.internal.search.spi.model.query.contributor.DDLRecordKeywordQueryContributor',
		'com.liferay.dynamic.data.lists.internal.search.spi.model.query.contributor.DDLRecordSetKeywordQueryContributor',
		'com.liferay.dynamic.data.mapping.internal.search.spi.model.query.contributor.DDMFormInstanceRecordKeywordQueryContributor',
		'com.liferay.dynamic.data.mapping.internal.search.spi.model.query.contributor.DDMStructureKeywordQueryContributor',
		'com.liferay.dynamic.data.mapping.internal.search.spi.model.query.contributor.DDMStructureLayoutKeywordQueryContributor',
		'com.liferay.dynamic.data.mapping.internal.search.spi.model.query.contributor.DDMTemplateKeywordQueryContributor',
		'com.liferay.exportimport.internal.search.spi.model.query.contributor.ExportImportConfigurationKeywordQueryContributor',
		'com.liferay.layout.internal.search.spi.model.query.contributor.LayoutKeywordQueryContributor',
		'com.liferay.message.boards.internal.search.spi.model.query.contributor.MBMessageKeywordQueryContributor',
		'com.liferay.organizations.internal.search.spi.model.query.contributor.OrganizationKeywordQueryContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.AlwaysPresentFieldsKeywordQueryContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.AssetCategoryTitlesKeywordQueryContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.AssetInternalCategoryTitlesKeywordQueryContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.AssetTagNamesKeywordQueryContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.DefaultKeywordQueryContributor',
		'com.liferay.portal.workflow.kaleo.internal.search.spi.model.query.contributor.KaleoInstanceTokenKeywordQueryContributor',
		'com.liferay.redirect.internal.search.spi.model.query.contributor.RedirectEntryKeywordQueryContributor',
		'com.liferay.redirect.internal.search.spi.model.query.contributor.RedirectNotFoundEntryKeywordQueryContributor',
		'com.liferay.segments.internal.search.spi.model.query.contributor.SegmentsEntryKeywordQueryContributor',
		'com.liferay.translation.internal.search.spi.model.query.contributor.TranslationEntryKeywordQueryContributor',
		'com.liferay.user.groups.admin.internal.search.spi.model.query.contributor.UserGroupKeywordQueryContributor',
		'com.liferay.users.admin.internal.search.spi.model.query.contributor.UserKeywordQueryContributor',
		'com.liferay.account.internal.search.spi.model.query.contributor.AccountEntryModelPreFilterContributor',
		'com.liferay.account.internal.search.spi.model.query.contributor.OrganizationModelPreFilterContributor',
		'com.liferay.account.internal.search.spi.model.query.contributor.UserModelPreFilterContributor',
		'com.liferay.analytics.settings.web.internal.search.spi.model.query.contributor.UserModelPreFilterContributor',
		'com.liferay.asset.categories.internal.search.spi.model.query.contributor.AssetCategoryModelPreFilterContributor',
		'com.liferay.data.engine.internal.search.spi.model.query.contributor.DEDataListViewModelPreFilterContributor',
		'com.liferay.dynamic.data.lists.internal.search.spi.model.query.contributor.DDLRecordSetModelPreFilterContributor',
		'com.liferay.dynamic.data.mapping.internal.search.spi.model.query.contributor.DDMStructureLayoutModelPreFilterContributor',
		'com.liferay.dynamic.data.mapping.internal.search.spi.model.query.contributor.DDMStructureModelPreFilterContributor',
		'com.liferay.dynamic.data.mapping.internal.search.spi.model.query.contributor.DDMTemplateModelPreFilterContributor',
		'com.liferay.exportimport.internal.search.spi.model.query.contributor.ExportImportConfigurationModelPreFilterContributor',
		'com.liferay.message.boards.internal.search.spi.model.query.contributor.MBCategoryModelPreFilterContributor',
		'com.liferay.message.boards.internal.search.spi.model.query.contributor.MBThreadModelPreFilterContributor',
		'com.liferay.organizations.internal.search.spi.model.query.contributor.OrganizationModelPreFilterContributor',
		'com.liferay.portal.workflow.kaleo.internal.search.spi.model.query.contributor.KaleoInstanceModelPreFilterContributor',
		'com.liferay.portal.workflow.kaleo.internal.search.spi.model.query.contributor.KaleoInstanceTokenModelPreFilterContributor',
		'com.liferay.portal.workflow.kaleo.internal.search.spi.model.query.contributor.KaleoLogModelPreFilterContributor',
		'com.liferay.portal.workflow.kaleo.internal.search.spi.model.query.contributor.KaleoTaskInstanceTokenModelPreFilterContributor',
		'com.liferay.redirect.internal.search.spi.model.query.contributor.RedirectNotFoundEntryModelPreFilterContributor',
		'com.liferay.segments.internal.search.spi.model.query.contributor.SegmentsEntryModelPreFilterContributor',
		'com.liferay.segments.internal.search.spi.model.query.contributor.UserModelPreFilterContributor',
	],
	includes: [
		'com.liferay.blogs.internal.search.spi.model.query.contributor.BlogsEntryModelPreFilterContributor',
		'com.liferay.bookmarks.internal.search.spi.model.query.contributor.BookmarksEntryModelPreFilterContributor',
		'com.liferay.bookmarks.internal.search.spi.model.query.contributor.BookmarksFolderModelPreFilterContributor',
		'com.liferay.calendar.internal.search.spi.model.query.contributor.CalendarBookingModelPreFilterContributor',
		'com.liferay.change.tracking.internal.search.spi.model.query.contributor.CTModelPreFilterContributor',
		'com.liferay.document.library.internal.search.spi.model.query.contributor.DLFileEntryModelPreFilterContributor',
		'com.liferay.document.library.internal.search.spi.model.query.contributor.DLFolderModelPreFilterContributor',
		'com.liferay.dynamic.data.lists.internal.search.spi.model.query.contributor.DDLRecordModelPreFilterContributor',
		'com.liferay.dynamic.data.mapping.internal.search.spi.model.query.contributor.DDMFormInstanceRecordModelPreFilterContributor',
		'com.liferay.layout.internal.search.spi.model.query.contributor.LayoutModelPreFilterContributor',
		'com.liferay.message.boards.internal.search.spi.model.query.contributor.MBMessageModelPreFilterContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.StagingModelPreFilterContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.WorkflowStatusModelPreFilterContributor',
		'com.liferay.users.admin.internal.search.spi.model.query.contributor.UserModelPreFilterContributor',
		'com.liferay.asset.internal.search.spi.model.query.contributor.AssetEntryModelPreFilterContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.AssetCategoryIdsQueryPreFilterContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.AssetTagNamesQueryPreFilterContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.FolderIdQueryPreFilterContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.GroupIdQueryPreFilterContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.LayoutQueryPreFilterContributor',
		'com.liferay.portal.search.internal.spi.model.query.contributor.UserIdQueryPreFilterContributor',
	],
};
