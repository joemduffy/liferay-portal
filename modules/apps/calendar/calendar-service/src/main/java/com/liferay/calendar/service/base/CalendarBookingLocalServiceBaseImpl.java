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

package com.liferay.calendar.service.base;

import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalService;
import com.liferay.calendar.service.CalendarBookingLocalServiceUtil;
import com.liferay.calendar.service.persistence.CalendarBookingFinder;
import com.liferay.calendar.service.persistence.CalendarBookingPersistence;
import com.liferay.calendar.service.persistence.CalendarFinder;
import com.liferay.calendar.service.persistence.CalendarNotificationTemplatePersistence;
import com.liferay.calendar.service.persistence.CalendarPersistence;
import com.liferay.calendar.service.persistence.CalendarResourceFinder;
import com.liferay.calendar.service.persistence.CalendarResourcePersistence;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerRegistryUtil;
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
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
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
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the calendar booking local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.calendar.service.impl.CalendarBookingLocalServiceImpl}.
 * </p>
 *
 * @author Eduardo Lundgren
 * @see com.liferay.calendar.service.impl.CalendarBookingLocalServiceImpl
 * @generated
 */
public abstract class CalendarBookingLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, CalendarBookingLocalService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CalendarBookingLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>CalendarBookingLocalServiceUtil</code>.
	 */

	/**
	 * Adds the calendar booking to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CalendarBookingLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param calendarBooking the calendar booking
	 * @return the calendar booking that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CalendarBooking addCalendarBooking(CalendarBooking calendarBooking) {
		calendarBooking.setNew(true);

		return calendarBookingPersistence.update(calendarBooking);
	}

	/**
	 * Creates a new calendar booking with the primary key. Does not add the calendar booking to the database.
	 *
	 * @param calendarBookingId the primary key for the new calendar booking
	 * @return the new calendar booking
	 */
	@Override
	@Transactional(enabled = false)
	public CalendarBooking createCalendarBooking(long calendarBookingId) {
		return calendarBookingPersistence.create(calendarBookingId);
	}

	/**
	 * Deletes the calendar booking with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CalendarBookingLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param calendarBookingId the primary key of the calendar booking
	 * @return the calendar booking that was removed
	 * @throws PortalException if a calendar booking with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CalendarBooking deleteCalendarBooking(long calendarBookingId)
		throws PortalException {

		return calendarBookingPersistence.remove(calendarBookingId);
	}

	/**
	 * Deletes the calendar booking from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CalendarBookingLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param calendarBooking the calendar booking
	 * @return the calendar booking that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CalendarBooking deleteCalendarBooking(
			CalendarBooking calendarBooking)
		throws PortalException {

		return calendarBookingPersistence.remove(calendarBooking);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return calendarBookingPersistence.dslQuery(dslQuery);
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
			CalendarBooking.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return calendarBookingPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.calendar.model.impl.CalendarBookingModelImpl</code>.
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

		return calendarBookingPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.calendar.model.impl.CalendarBookingModelImpl</code>.
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

		return calendarBookingPersistence.findWithDynamicQuery(
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
		return calendarBookingPersistence.countWithDynamicQuery(dynamicQuery);
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

		return calendarBookingPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public CalendarBooking fetchCalendarBooking(long calendarBookingId) {
		return calendarBookingPersistence.fetchByPrimaryKey(calendarBookingId);
	}

	/**
	 * Returns the calendar booking matching the UUID and group.
	 *
	 * @param uuid the calendar booking's UUID
	 * @param groupId the primary key of the group
	 * @return the matching calendar booking, or <code>null</code> if a matching calendar booking could not be found
	 */
	@Override
	public CalendarBooking fetchCalendarBookingByUuidAndGroupId(
		String uuid, long groupId) {

		return calendarBookingPersistence.fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the calendar booking with the primary key.
	 *
	 * @param calendarBookingId the primary key of the calendar booking
	 * @return the calendar booking
	 * @throws PortalException if a calendar booking with the primary key could not be found
	 */
	@Override
	public CalendarBooking getCalendarBooking(long calendarBookingId)
		throws PortalException {

		return calendarBookingPersistence.findByPrimaryKey(calendarBookingId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(calendarBookingLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CalendarBooking.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("calendarBookingId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			calendarBookingLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(CalendarBooking.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"calendarBookingId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(calendarBookingLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CalendarBooking.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("calendarBookingId");
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
					Criterion modifiedDateCriterion =
						portletDataContext.getDateRangeCriteria("modifiedDate");

					if (modifiedDateCriterion != null) {
						Conjunction conjunction =
							RestrictionsFactoryUtil.conjunction();

						conjunction.add(modifiedDateCriterion);

						Disjunction disjunction =
							RestrictionsFactoryUtil.disjunction();

						disjunction.add(
							RestrictionsFactoryUtil.gtProperty(
								"modifiedDate", "lastPublishDate"));

						Property lastPublishDateProperty =
							PropertyFactoryUtil.forName("lastPublishDate");

						disjunction.add(lastPublishDateProperty.isNull());

						conjunction.add(disjunction);

						modifiedDateCriterion = conjunction;
					}

					Criterion statusDateCriterion =
						portletDataContext.getDateRangeCriteria("statusDate");

					if ((modifiedDateCriterion != null) &&
						(statusDateCriterion != null)) {

						Disjunction disjunction =
							RestrictionsFactoryUtil.disjunction();

						disjunction.add(modifiedDateCriterion);
						disjunction.add(statusDateCriterion);

						dynamicQuery.add(disjunction);
					}

					Property workflowStatusProperty =
						PropertyFactoryUtil.forName("status");

					if (portletDataContext.isInitialPublication()) {
						dynamicQuery.add(
							workflowStatusProperty.ne(
								WorkflowConstants.STATUS_IN_TRASH));
					}
					else {
						StagedModelDataHandler<?> stagedModelDataHandler =
							StagedModelDataHandlerRegistryUtil.
								getStagedModelDataHandler(
									CalendarBooking.class.getName());

						dynamicQuery.add(
							workflowStatusProperty.in(
								stagedModelDataHandler.
									getExportableStatuses()));
					}
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setGroupId(
			portletDataContext.getScopeGroupId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<CalendarBooking>() {

				@Override
				public void performAction(CalendarBooking calendarBooking)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, calendarBooking);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(CalendarBooking.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return calendarBookingPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return calendarBookingLocalService.deleteCalendarBooking(
			(CalendarBooking)persistedModel);
	}

	@Override
	public BasePersistence<CalendarBooking> getBasePersistence() {
		return calendarBookingPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return calendarBookingPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns all the calendar bookings matching the UUID and company.
	 *
	 * @param uuid the UUID of the calendar bookings
	 * @param companyId the primary key of the company
	 * @return the matching calendar bookings, or an empty list if no matches were found
	 */
	@Override
	public List<CalendarBooking> getCalendarBookingsByUuidAndCompanyId(
		String uuid, long companyId) {

		return calendarBookingPersistence.findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of calendar bookings matching the UUID and company.
	 *
	 * @param uuid the UUID of the calendar bookings
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of calendar bookings
	 * @param end the upper bound of the range of calendar bookings (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching calendar bookings, or an empty list if no matches were found
	 */
	@Override
	public List<CalendarBooking> getCalendarBookingsByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CalendarBooking> orderByComparator) {

		return calendarBookingPersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the calendar booking matching the UUID and group.
	 *
	 * @param uuid the calendar booking's UUID
	 * @param groupId the primary key of the group
	 * @return the matching calendar booking
	 * @throws PortalException if a matching calendar booking could not be found
	 */
	@Override
	public CalendarBooking getCalendarBookingByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException {

		return calendarBookingPersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the calendar bookings.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.calendar.model.impl.CalendarBookingModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of calendar bookings
	 * @param end the upper bound of the range of calendar bookings (not inclusive)
	 * @return the range of calendar bookings
	 */
	@Override
	public List<CalendarBooking> getCalendarBookings(int start, int end) {
		return calendarBookingPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of calendar bookings.
	 *
	 * @return the number of calendar bookings
	 */
	@Override
	public int getCalendarBookingsCount() {
		return calendarBookingPersistence.countAll();
	}

	/**
	 * Updates the calendar booking in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CalendarBookingLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param calendarBooking the calendar booking
	 * @return the calendar booking that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CalendarBooking updateCalendarBooking(
		CalendarBooking calendarBooking) {

		return calendarBookingPersistence.update(calendarBooking);
	}

	@Deactivate
	protected void deactivate() {
		_setLocalServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			CalendarBookingLocalService.class, IdentifiableOSGiService.class,
			CTService.class, PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		calendarBookingLocalService = (CalendarBookingLocalService)aopProxy;

		_setLocalServiceUtilService(calendarBookingLocalService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CalendarBookingLocalService.class.getName();
	}

	@Override
	public CTPersistence<CalendarBooking> getCTPersistence() {
		return calendarBookingPersistence;
	}

	@Override
	public Class<CalendarBooking> getModelClass() {
		return CalendarBooking.class;
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<CalendarBooking>, R, E>
				updateUnsafeFunction)
		throws E {

		return updateUnsafeFunction.apply(calendarBookingPersistence);
	}

	protected String getModelClassName() {
		return CalendarBooking.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = calendarBookingPersistence.getDataSource();

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
		CalendarBookingLocalService calendarBookingLocalService) {

		try {
			Field field =
				CalendarBookingLocalServiceUtil.class.getDeclaredField(
					"_service");

			field.setAccessible(true);

			field.set(null, calendarBookingLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Reference
	protected CalendarPersistence calendarPersistence;

	@Reference
	protected CalendarFinder calendarFinder;

	protected CalendarBookingLocalService calendarBookingLocalService;

	@Reference
	protected CalendarBookingPersistence calendarBookingPersistence;

	@Reference
	protected CalendarBookingFinder calendarBookingFinder;

	@Reference
	protected CalendarNotificationTemplatePersistence
		calendarNotificationTemplatePersistence;

	@Reference
	protected CalendarResourcePersistence calendarResourcePersistence;

	@Reference
	protected CalendarResourceFinder calendarResourceFinder;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

}