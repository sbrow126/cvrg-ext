package edu.jhu.cvrg.liferay.authentication;
/*
Copyright 2012 Johns Hopkins University Institute for Computational Medicine

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
/**
 * @author Chris Jurado
 */
import java.util.Locale;
import java.util.Map;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PropsUtil;

import edu.jhu.cvrg.utilities.authentication.AuthenticationMethod;
import edu.jhu.cvrg.utilities.authentication.MainAuthenticator;

public class GlobusNexusAuthenticator implements Authenticator{

	@Override
	public int authenticateByEmailAddress(long companyId, String emailAddress,
			String password, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) throws AuthException {
			
		try {
			User user = UserLocalServiceUtil.getUserByEmailAddress(companyId, emailAddress);
			return authenticateByScreenName(companyId, user.getScreenName(), password, headerMap, null);
		} catch (PortalException e) {
			e.printStackTrace();
			return DNE;
		} catch (SystemException e) {
			e.printStackTrace();
			return FAILURE;
		}
	}

	@Override
	public int authenticateByScreenName(long companyId, String screenName,
			String password, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) throws AuthException {

		MainAuthenticator authenticator = new MainAuthenticator();
		User user = null;

		String[] args = { screenName, password };
		if (authenticator.authenticate(args, AuthenticationMethod.GLOBUS_REST)) {

			try {
				user = UserLocalServiceUtil.getUserByScreenName(companyId, screenName);
			} catch (PortalException e) {
				if(user == null){
					user = createNewUser(authenticator.getUserEmail(), authenticator.getUserFullname().split(" "), companyId);
				}
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
		else{
			return FAILURE;
		}
	}

	@Override
	public int authenticateByUserId(long companyId, long userId,
			String password, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) throws AuthException {

		try {
			User user = UserLocalServiceUtil.getUserById(companyId, userId);
			return authenticateByScreenName(companyId, user.getScreenName(), password, headerMap, null);
		} catch (PortalException e) {
			e.printStackTrace();
			return DNE;
		} catch (SystemException e) {
			e.printStackTrace();
			return FAILURE;
		}
	}
	
	private User createNewUser(String userEmail, String[] userName, long companyId){

		String creatingUserProperty = null;

        creatingUserProperty = PropsUtil.get("LIFERAY_ADMIN_USER");
		
		User creatingUser = null;
		User newUser = null;
		try {
			creatingUser = UserLocalServiceUtil.getUserByEmailAddress(companyId, creatingUserProperty);
				
			UserLocalServiceUtil.addUser(creatingUser.getUserId(), companyId, false, "test", "test", false, (userName[0] + userName[1]), userEmail, 0L, "", Locale.US,
					userName[0], "", userName[1], 0, 0, false, 0, 1,1970, "User", null, null, null,
					null, false, new ServiceContext());
		
		} catch (PortalException e) {

			e.printStackTrace();
		} catch (SystemException e) { 
			e.printStackTrace();
		}		
		return newUser;
	}
}