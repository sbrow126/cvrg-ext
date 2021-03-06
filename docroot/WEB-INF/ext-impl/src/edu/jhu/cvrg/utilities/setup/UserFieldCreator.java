package edu.jhu.cvrg.utilities.setup;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.DuplicateColumnNameException;
import com.liferay.portlet.expando.DuplicateTableNameException;
import com.liferay.portlet.expando.NoSuchColumnException;
import com.liferay.portlet.expando.NoSuchTableException;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

public class UserFieldCreator {

	public static void createCustomFields() {

		long companyId = PortalUtil.getDefaultCompanyId();

		try {
			ExpandoTable userExpandoTable = null;

			try {
				userExpandoTable = ExpandoTableLocalServiceUtil
						.getDefaultTable(companyId, User.class.getName());
			} catch (NoSuchTableException nste) {
				userExpandoTable = ExpandoTableLocalServiceUtil
						.addDefaultTable(companyId, User.class.getName());
			}
			
			System.out.println("Creating custom user fields using companyId " + companyId);

			addCustomAttribute(companyId, userExpandoTable, "Department",
					ExpandoColumnConstants.STRING, null);
			addCustomAttribute(companyId, userExpandoTable, "Grant Number",
					ExpandoColumnConstants.STRING, null);
			addCustomAttribute(companyId, userExpandoTable, "Reason",
					ExpandoColumnConstants.STRING, null);
			addCustomAttribute(companyId, userExpandoTable, "Institution",
					ExpandoColumnConstants.STRING, null);
			addCustomAttribute(companyId, userExpandoTable,
					"NewUserFormComplete", ExpandoColumnConstants.BOOLEAN, false);
			addCustomAttribute(companyId, userExpandoTable,
					"NewUserFormRequired", ExpandoColumnConstants.BOOLEAN, true);
			
			System.out.println("User fields created.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ExpandoColumn addCustomAttribute(long companyId, ExpandoTable userExpandoTable, String attributeName, int type, Object defaultValue) {
		
		ExpandoColumn column = null;
		try {
			column = ExpandoColumnLocalServiceUtil.getColumn(userExpandoTable.getTableId(), attributeName);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if(column == null){
			try {
				column = ExpandoColumnLocalServiceUtil.addColumn(userExpandoTable.getTableId(), attributeName, type, defaultValue);
				System.out.println("Added column " + column.getName() + " on table " + column.getTableId());
				System.out.println("Setting permissions on " + column.getName());
				setAttributePermissions(companyId, attributeName);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return column;
	}

	private static void setAttributePermissions(long companyId,	String attributeName) throws PortalException, SystemException {

		ExpandoBridge expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(companyId, User.class.getName());

		Role userRole = RoleLocalServiceUtil.getRole(companyId,	RoleConstants.USER);

		ExpandoColumn column = ExpandoColumnLocalServiceUtil.getColumn(companyId, expandoBridge.getClassName(),
				ExpandoTableConstants.DEFAULT_TABLE_NAME, attributeName);
		
		System.out.println("Adding role " + userRole.getName() + " to column " + column.getName() + " on table " + column.getTableId());

		
		// Give ordinary users read / write power of the new attributes
		// Updates the Liferay "ResourcePermission" table
		ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId,
				ExpandoColumn.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(column.getColumnId()), userRole.getRoleId(),
				new String[] { ActionKeys.VIEW, ActionKeys.UPDATE });

	}

}