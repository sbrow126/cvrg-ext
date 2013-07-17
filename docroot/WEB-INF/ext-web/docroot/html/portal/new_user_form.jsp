<%
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
* 
*/
%>

<%@ include file="/html/portal/init.jsp" %>
<%@ page import="com.liferay.portal.NewUserMissingFieldException" %>
<%
String institution = ParamUtil.getString(request, "institution");
String department = ParamUtil.getString(request, "department");
String reason = ParamUtil.getString(request, "reason");
String grant = ParamUtil.getString(request, "grant");

String inst = "";

if(request.getCookies() != null){
	for (Cookie ck : request.getCookies()) {
		if ("CILOGON-USER_INST".equals(ck.getName()) && !ck.getValue().equals("")) {
			inst = (String)ck.getValue();
			institution = inst;
		}
	}
}

institution = inst;
%>
	Thank you for logging into the CVRG Portal!  Before you can continue it will be necessary to provide a few details about yourself.
	<p/>
	<aui:form action='<%= themeDisplay.getPathMain() + "/portal/update_new_user_form" %>' method="post" name="nu_fm">
		<aui:input name="doAsUserId" type="hidden" value="<%= themeDisplay.getDoAsUserId() %>" />
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
		<aui:input name="<%= WebKeys.REFERER %>" type="hidden" value='<%= themeDisplay.getPathMain() + "?doAsUserId=" + themeDisplay.getDoAsUserId() %>' />
	

		<c:if test="<%= SessionErrors.contains(request, NewUserMissingFieldException.class.getName()) %>">
			<div class="portlet-msg-error">
				<liferay-ui:message key="new-user-missing-field" />
			</div>
		</c:if>

	
		<aui:fieldset>
			<aui:input class="lfr-input-text-container" disabled="true" label="Name:" name="name" value="<%= user.getFullName() %>"/>
			<aui:input class="lfr-input-text-container" disabled="true" label="Institution:" name="institution" type="text" value="<%= institution %>"/>
			<aui:input class="lfr-input-text-container" label="Department:" name="department" type="text" value="<%= department %>"/>
			<aui:input class="lfr-input-textarea-container" label="What brings you to the CVRG Portal?" name="reason" style="width:300px; height:50px;" type="textarea" value="<%= reason %>"/>
			<aui:input class="lfr-input-text-container" label="(Optional) Supporting Research Grant #:" name="grant" type="text" value="<%= grant %>"/>
		</aui:fieldset>

		<hr>

		<aui:button-row>
			<aui:button type="submit" />
		</aui:button-row>
	</aui:form>
	
<aui:script>
	Liferay.Util.focusFormField(document.fm.institution);
</aui:script>
