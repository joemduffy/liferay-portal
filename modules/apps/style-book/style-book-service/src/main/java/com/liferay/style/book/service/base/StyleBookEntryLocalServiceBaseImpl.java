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

package com.liferay.style.book.service.base;

import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.change.tracking.CTService;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;
import com.liferay.portal.kernel.service.version.VersionService;
import com.liferay.portal.kernel.service.version.VersionServiceListener;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.model.StyleBookEntryVersion;
import com.liferay.style.book.service.StyleBookEntryLocalService;
import com.liferay.style.book.service.StyleBookEntryLocalServiceUtil;
import com.liferay.style.book.service.persistence.StyleBookEntryPersistence;
import com.liferay.style.book.service.persistence.StyleBookEntryVersionPersistence;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the style book entry local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.style.book.service.impl.StyleBookEntryLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.style.book.service.impl.StyleBookEntryLocalServiceImpl
 * @generated
 */
public abstract class StyleBookEntryLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, IdentifiableOSGiService, StyleBookEntryLocalService,
			   VersionService<StyleBookEntry, StyleBookEntryVersion> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>StyleBookEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>StyleBookEntryLocalServiceUtil</code>.
	 */

	/**
	 * Adds the style book entry to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect StyleBookEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param styleBookEntry the style book entry
	 * @return the style book entry that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public StyleBookEntry addStyleBookEntry(StyleBookEntry styleBookEntry) {
		styleBookEntry.setNew(true);

		return styleBookEntryPersistence.update(styleBookEntry);
	}

	/**
	 * Creates a new style book entry. Does not add the style book entry to the database.
	 *
	 * @return the new style book entry
	 */
	@Override
	@Transactional(enabled = false)
	public StyleBookEntry create() {
		long primaryKey = counterLocalService.increment(
			StyleBookEntry.class.getName());

		StyleBookEntry draftStyleBookEntry = styleBookEntryPersistence.create(
			primaryKey);

		draftStyleBookEntry.setHeadId(primaryKey);

		return draftStyleBookEntry;
	}

	/**
	 * Deletes the style book entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect StyleBookEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param styleBookEntryId the primary key of the style book entry
	 * @return the style book entry that was removed
	 * @throws PortalException if a style book entry with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public StyleBookEntry deleteStyleBookEntry(long styleBookEntryId)
		throws PortalException {

		StyleBookEntry styleBookEntry =
			styleBookEntryPersistence.fetchByPrimaryKey(styleBookEntryId);

		if (styleBookEntry != null) {
			delete(styleBookEntry);
		}

		return styleBookEntry;
	}

	/**
	 * Deletes the style book entry from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect StyleBookEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param styleBookEntry the style book entry
	 * @return the style book entry that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public StyleBookEntry deleteStyleBookEntry(StyleBookEntry styleBookEntry)
		throws PortalException {

		delete(styleBookEntry);

		return styleBookEntry;
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return styleBookEntryPersistence.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(DSLQuery dslQuery) {
		Long count = dslQuery(dslQuery);

		return count.intValue();
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			StyleBookEntry.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return styleBookEntryPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return styleBookEntryPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return styleBookEntryPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return styleBookEntryPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return styleBookEntryPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public StyleBookEntry fetchStyleBookEntry(long styleBookEntryId) {
		return styleBookEntryPersistence.fetchByPrimaryKey(styleBookEntryId);
	}

	/**
	 * Returns the style book entry with the primary key.
	 *
	 * @param styleBookEntryId the primary key of the style book entry
	 * @return the style book entry
	 * @throws PortalException if a style book entry with the primary key could not be found
	 */
	@Override
	public StyleBookEntry getStyleBookEntry(long styleBookEntryId)
		throws PortalException {

		return styleBookEntryPersistence.findByPrimaryKey(styleBookEntryId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(styleBookEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(StyleBookEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("styleBookEntryId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			styleBookEntryLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(StyleBookEntry.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"styleBookEntryId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(styleBookEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(StyleBookEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("styleBookEntryId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(
						dynamicQuery, "modifiedDate");
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<StyleBookEntry>() {

				@Override
				public void performAction(StyleBookEntry styleBookEntry)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, styleBookEntry);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(StyleBookEntry.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return styleBookEntryPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return styleBookEntryLocalService.deleteStyleBookEntry(
			(StyleBookEntry)persistedModel);
	}

	@Override
	public BasePersistence<StyleBookEntry> getBasePersistence() {
		return styleBookEntryPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return styleBookEntryPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the style book entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of style book entries
	 * @param end the upper bound of the range of style book entries (not inclusive)
	 * @return the range of style book entries
	 */
	@Override
	public List<StyleBookEntry> getStyleBookEntries(int start, int end) {
		return styleBookEntryPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of style book entries.
	 *
	 * @return the number of style book entries
	 */
	@Override
	public int getStyleBookEntriesCount() {
		return styleBookEntryPersistence.countAll();
	}

	/**
	 * Updates the style book entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect StyleBookEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param styleBookEntry the style book entry
	 * @return the style book entry that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public StyleBookEntry updateStyleBookEntry(
			StyleBookEntry draftStyleBookEntry)
		throws PortalException {

		return updateDraft(draftStyleBookEntry);
	}

	@Deactivate
	protected void deactivate() {
		_setLocalServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			StyleBookEntryLocalService.class, IdentifiableOSGiService.class,
			CTService.class, PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		styleBookEntryLocalService = (StyleBookEntryLocalService)aopProxy;

		_setLocalServiceUtilService(styleBookEntryLocalService);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public StyleBookEntry checkout(
			StyleBookEntry publishedStyleBookEntry, int version)
		throws PortalException {

		if (!publishedStyleBookEntry.isHead()) {
			throw new IllegalArgumentException(
				"Unable to checkout with unpublished changes " +
					publishedStyleBookEntry.getHeadId());
		}

		StyleBookEntry draftStyleBookEntry =
			styleBookEntryPersistence.fetchByHeadId(
				publishedStyleBookEntry.getPrimaryKey());

		if (draftStyleBookEntry != null) {
			throw new IllegalArgumentException(
				"Unable to checkout with unpublished changes " +
					publishedStyleBookEntry.getPrimaryKey());
		}

		StyleBookEntryVersion styleBookEntryVersion = getVersion(
			publishedStyleBookEntry, version);

		draftStyleBookEntry = _createDraft(publishedStyleBookEntry);

		styleBookEntryVersion.populateVersionedModel(draftStyleBookEntry);

		draftStyleBookEntry = styleBookEntryPersistence.update(
			draftStyleBookEntry);

		for (VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
				versionServiceListener : _versionServiceListeners) {

			versionServiceListener.afterCheckout(draftStyleBookEntry, version);
		}

		return draftStyleBookEntry;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public StyleBookEntry delete(StyleBookEntry publishedStyleBookEntry)
		throws PortalException {

		if (!publishedStyleBookEntry.isHead()) {
			throw new IllegalArgumentException(
				"StyleBookEntry is a draft " +
					publishedStyleBookEntry.getPrimaryKey());
		}

		StyleBookEntry draftStyleBookEntry =
			styleBookEntryPersistence.fetchByHeadId(
				publishedStyleBookEntry.getPrimaryKey());

		if (draftStyleBookEntry != null) {
			deleteDraft(draftStyleBookEntry);
		}

		for (StyleBookEntryVersion styleBookEntryVersion :
				getVersions(publishedStyleBookEntry)) {

			styleBookEntryVersionPersistence.remove(styleBookEntryVersion);
		}

		styleBookEntryPersistence.remove(publishedStyleBookEntry);

		for (VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
				versionServiceListener : _versionServiceListeners) {

			versionServiceListener.afterDelete(publishedStyleBookEntry);
		}

		return publishedStyleBookEntry;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public StyleBookEntry deleteDraft(StyleBookEntry draftStyleBookEntry)
		throws PortalException {

		if (draftStyleBookEntry.isHead()) {
			throw new IllegalArgumentException(
				"StyleBookEntry is not a draft " +
					draftStyleBookEntry.getPrimaryKey());
		}

		styleBookEntryPersistence.remove(draftStyleBookEntry);

		for (VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
				versionServiceListener : _versionServiceListeners) {

			versionServiceListener.afterDeleteDraft(draftStyleBookEntry);
		}

		return draftStyleBookEntry;
	}

	@Override
	public StyleBookEntryVersion deleteVersion(
			StyleBookEntryVersion styleBookEntryVersion)
		throws PortalException {

		StyleBookEntryVersion latestStyleBookEntryVersion =
			styleBookEntryVersionPersistence.findByStyleBookEntryId_First(
				styleBookEntryVersion.getVersionedModelId(), null);

		if (latestStyleBookEntryVersion.getVersion() ==
				styleBookEntryVersion.getVersion()) {

			throw new IllegalArgumentException(
				"Unable to delete latest version " +
					styleBookEntryVersion.getVersion());
		}

		styleBookEntryVersion = styleBookEntryVersionPersistence.remove(
			styleBookEntryVersion);

		for (VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
				versionServiceListener : _versionServiceListeners) {

			versionServiceListener.afterDeleteVersion(styleBookEntryVersion);
		}

		return styleBookEntryVersion;
	}

	@Override
	public StyleBookEntry fetchDraft(StyleBookEntry styleBookEntry) {
		if (styleBookEntry.isHead()) {
			return styleBookEntryPersistence.fetchByHeadId(
				styleBookEntry.getPrimaryKey());
		}

		return styleBookEntry;
	}

	@Override
	public StyleBookEntry fetchDraft(long primaryKey) {
		return styleBookEntryPersistence.fetchByHeadId(primaryKey);
	}

	@Override
	public StyleBookEntryVersion fetchLatestVersion(
		StyleBookEntry styleBookEntry) {

		long primaryKey = styleBookEntry.getHeadId();

		if (styleBookEntry.isHead()) {
			primaryKey = styleBookEntry.getPrimaryKey();
		}

		return styleBookEntryVersionPersistence.fetchByStyleBookEntryId_First(
			primaryKey, null);
	}

	@Override
	public StyleBookEntry fetchPublished(StyleBookEntry styleBookEntry) {
		if (styleBookEntry.isHead()) {
			return styleBookEntry;
		}

		if (styleBookEntry.getHeadId() == styleBookEntry.getPrimaryKey()) {
			return null;
		}

		return styleBookEntryPersistence.fetchByPrimaryKey(
			styleBookEntry.getHeadId());
	}

	@Override
	public StyleBookEntry fetchPublished(long primaryKey) {
		StyleBookEntry styleBookEntry =
			styleBookEntryPersistence.fetchByPrimaryKey(primaryKey);

		if ((styleBookEntry == null) ||
			(styleBookEntry.getHeadId() == styleBookEntry.getPrimaryKey())) {

			return null;
		}

		return styleBookEntry;
	}

	@Override
	public StyleBookEntry getDraft(StyleBookEntry styleBookEntry)
		throws PortalException {

		if (!styleBookEntry.isHead()) {
			return styleBookEntry;
		}

		StyleBookEntry draftStyleBookEntry =
			styleBookEntryPersistence.fetchByHeadId(
				styleBookEntry.getPrimaryKey());

		if (draftStyleBookEntry == null) {
			draftStyleBookEntry = styleBookEntryLocalService.updateDraft(
				_createDraft(styleBookEntry));
		}

		return draftStyleBookEntry;
	}

	@Override
	public StyleBookEntry getDraft(long primaryKey) throws PortalException {
		StyleBookEntry draftStyleBookEntry =
			styleBookEntryPersistence.fetchByHeadId(primaryKey);

		if (draftStyleBookEntry == null) {
			StyleBookEntry styleBookEntry =
				styleBookEntryPersistence.findByPrimaryKey(primaryKey);

			draftStyleBookEntry = styleBookEntryLocalService.updateDraft(
				_createDraft(styleBookEntry));
		}

		return draftStyleBookEntry;
	}

	@Override
	public StyleBookEntryVersion getVersion(
			StyleBookEntry styleBookEntry, int version)
		throws PortalException {

		long primaryKey = styleBookEntry.getHeadId();

		if (styleBookEntry.isHead()) {
			primaryKey = styleBookEntry.getPrimaryKey();
		}

		return styleBookEntryVersionPersistence.findByStyleBookEntryId_Version(
			primaryKey, version);
	}

	@Override
	public List<StyleBookEntryVersion> getVersions(
		StyleBookEntry styleBookEntry) {

		long primaryKey = styleBookEntry.getPrimaryKey();

		if (!styleBookEntry.isHead()) {
			if (styleBookEntry.getHeadId() == styleBookEntry.getPrimaryKey()) {
				return Collections.emptyList();
			}

			primaryKey = styleBookEntry.getHeadId();
		}

		return styleBookEntryVersionPersistence.findByStyleBookEntryId(
			primaryKey);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public StyleBookEntry publishDraft(StyleBookEntry draftStyleBookEntry)
		throws PortalException {

		if (draftStyleBookEntry.isHead()) {
			throw new IllegalArgumentException(
				"Can only publish drafts " +
					draftStyleBookEntry.getPrimaryKey());
		}

		StyleBookEntry headStyleBookEntry = null;

		int version = 1;

		if (draftStyleBookEntry.getHeadId() ==
				draftStyleBookEntry.getPrimaryKey()) {

			headStyleBookEntry = create();

			draftStyleBookEntry.setHeadId(headStyleBookEntry.getPrimaryKey());
		}
		else {
			headStyleBookEntry = styleBookEntryPersistence.findByPrimaryKey(
				draftStyleBookEntry.getHeadId());

			StyleBookEntryVersion latestStyleBookEntryVersion =
				styleBookEntryVersionPersistence.findByStyleBookEntryId_First(
					draftStyleBookEntry.getHeadId(), null);

			version = latestStyleBookEntryVersion.getVersion() + 1;
		}

		StyleBookEntryVersion styleBookEntryVersion =
			styleBookEntryVersionPersistence.create(
				counterLocalService.increment(
					StyleBookEntryVersion.class.getName()));

		styleBookEntryVersion.setVersion(version);
		styleBookEntryVersion.setVersionedModelId(
			headStyleBookEntry.getPrimaryKey());

		draftStyleBookEntry.populateVersionModel(styleBookEntryVersion);

		styleBookEntryVersionPersistence.update(styleBookEntryVersion);

		styleBookEntryVersion.populateVersionedModel(headStyleBookEntry);

		headStyleBookEntry.setHeadId(-headStyleBookEntry.getPrimaryKey());

		headStyleBookEntry = styleBookEntryPersistence.update(
			headStyleBookEntry);

		for (VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
				versionServiceListener : _versionServiceListeners) {

			versionServiceListener.afterPublishDraft(
				draftStyleBookEntry, version);
		}

		deleteDraft(draftStyleBookEntry);

		return headStyleBookEntry;
	}

	@Override
	public void registerListener(
		VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
			versionServiceListener) {

		_versionServiceListeners.add(versionServiceListener);
	}

	@Override
	public void unregisterListener(
		VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
			versionServiceListener) {

		_versionServiceListeners.remove(versionServiceListener);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public StyleBookEntry updateDraft(StyleBookEntry draftStyleBookEntry)
		throws PortalException {

		if (draftStyleBookEntry.isHead()) {
			throw new IllegalArgumentException(
				"Can only update draft entries " +
					draftStyleBookEntry.getPrimaryKey());
		}

		StyleBookEntry previousStyleBookEntry =
			styleBookEntryPersistence.fetchByPrimaryKey(
				draftStyleBookEntry.getPrimaryKey());

		draftStyleBookEntry = styleBookEntryPersistence.update(
			draftStyleBookEntry);

		if (previousStyleBookEntry == null) {
			for (VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
					versionServiceListener : _versionServiceListeners) {

				versionServiceListener.afterCreateDraft(draftStyleBookEntry);
			}
		}
		else {
			for (VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>
					versionServiceListener : _versionServiceListeners) {

				versionServiceListener.afterUpdateDraft(draftStyleBookEntry);
			}
		}

		return draftStyleBookEntry;
	}

	private StyleBookEntry _createDraft(StyleBookEntry publishedStyleBookEntry)
		throws PortalException {

		StyleBookEntry draftStyleBookEntry = create();

		draftStyleBookEntry.setCtCollectionId(
			publishedStyleBookEntry.getCtCollectionId());
		draftStyleBookEntry.setUuid(publishedStyleBookEntry.getUuid());
		draftStyleBookEntry.setHeadId(publishedStyleBookEntry.getPrimaryKey());
		draftStyleBookEntry.setGroupId(publishedStyleBookEntry.getGroupId());
		draftStyleBookEntry.setCompanyId(
			publishedStyleBookEntry.getCompanyId());
		draftStyleBookEntry.setUserId(publishedStyleBookEntry.getUserId());
		draftStyleBookEntry.setUserName(publishedStyleBookEntry.getUserName());
		draftStyleBookEntry.setCreateDate(
			publishedStyleBookEntry.getCreateDate());
		draftStyleBookEntry.setModifiedDate(
			publishedStyleBookEntry.getModifiedDate());
		draftStyleBookEntry.setDefaultStyleBookEntry(
			publishedStyleBookEntry.getDefaultStyleBookEntry());
		draftStyleBookEntry.setFrontendTokensValues(
			publishedStyleBookEntry.getFrontendTokensValues());
		draftStyleBookEntry.setName(publishedStyleBookEntry.getName());
		draftStyleBookEntry.setPreviewFileEntryId(
			publishedStyleBookEntry.getPreviewFileEntryId());
		draftStyleBookEntry.setStyleBookEntryKey(
			publishedStyleBookEntry.getStyleBookEntryKey());

		draftStyleBookEntry.resetOriginalValues();

		return draftStyleBookEntry;
	}

	private final Set
		<VersionServiceListener<StyleBookEntry, StyleBookEntryVersion>>
			_versionServiceListeners = Collections.newSetFromMap(
				new ConcurrentHashMap
					<VersionServiceListener
						<StyleBookEntry, StyleBookEntryVersion>,
					 Boolean>());

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return StyleBookEntryLocalService.class.getName();
	}

	@Override
	public CTPersistence<StyleBookEntry> getCTPersistence() {
		return styleBookEntryPersistence;
	}

	@Override
	public Class<StyleBookEntry> getModelClass() {
		return StyleBookEntry.class;
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<StyleBookEntry>, R, E>
				updateUnsafeFunction)
		throws E {

		return updateUnsafeFunction.apply(styleBookEntryPersistence);
	}

	protected String getModelClassName() {
		return StyleBookEntry.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = styleBookEntryPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _setLocalServiceUtilService(
		StyleBookEntryLocalService styleBookEntryLocalService) {

		try {
			Field field = StyleBookEntryLocalServiceUtil.class.getDeclaredField(
				"_service");

			field.setAccessible(true);

			field.set(null, styleBookEntryLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	protected StyleBookEntryLocalService styleBookEntryLocalService;

	@Reference
	protected StyleBookEntryPersistence styleBookEntryPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected StyleBookEntryVersionPersistence styleBookEntryVersionPersistence;

}