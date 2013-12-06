package edu.jhu.cvrg.utilities.setup;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.DuplicateColumnNameException;
import com.liferay.portlet.expando.DuplicateTableNameException;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class UserFieldCreator extends MVCPortlet {

	public static void createCustomFields(){
		long companyId = PortalUtil.getDefaultCompanyId();
		
		try {
			addDepartment(companyId);
			addGrantNumber(companyId);
			addReason(companyId);
			addInstitution(companyId);
			addNewUserFormComplete(companyId);
			addNewUserFormRequired(companyId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	private static void addNewUserFormRequired(long companyId) throws PortalException, SystemException{
		addField(companyId, "addNewUserFormRequired", ExpandoColumnConstants.BOOLEAN, User.class.getName(), true);
	}
	
	private static void addNewUserFormComplete(long companyId) throws PortalException, SystemException{
		addField(companyId, "NewUserFormComplete", ExpandoColumnConstants.BOOLEAN, User.class.getName(), false);
	}
	
	private static void addInstitution(long companyId) throws PortalException, SystemException{
		addField(companyId, "Institution", ExpandoColumnConstants.STRING, User.class.getName(), null);
	}
	
	private static void addReason(long companyId) throws PortalException, SystemException{
		addField(companyId, "Reason", ExpandoColumnConstants.STRING, User.class.getName(), null);
	}
	
	private static void addGrantNumber(long companyId) throws PortalException, SystemException{
		addField(companyId, "Grant Number", ExpandoColumnConstants.STRING, User.class.getName(), null);
	}
	
	private static void addDepartment(long companyId) throws PortalException, SystemException{
		addField(companyId, "Department", ExpandoColumnConstants.STRING, User.class.getName(), null);
	}
	
	private static void addField(long companyId, String columnName, int type, String className, Object data) throws PortalException, SystemException {
		ExpandoTable table = null;
		
		try{
			table = ExpandoTableLocalServiceUtil.addTable(companyId, className, "CUSTOM_FIELDS");
		}catch (DuplicateTableNameException e) {
			table = ExpandoTableLocalServiceUtil.getTable(companyId, className, "CUSTOM_FIELDS");
		}
		
		try {   
			ExpandoColumnLocalServiceUtil.addColumn(table.getTableId(), columnName, type, data);
		}catch (DuplicateColumnNameException e){
			//Normal condition
			return;
		}
	}
}