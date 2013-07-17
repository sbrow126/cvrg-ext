package edu.jhu.cvrg.liferay.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;

public class GlobusNexusAutoLogin implements AutoLogin{

	@Override
	public String[] login(HttpServletRequest request,
			HttpServletResponse response) throws AutoLoginException {
		// TODO Auto-generated method stub
		return null;
	}

}
