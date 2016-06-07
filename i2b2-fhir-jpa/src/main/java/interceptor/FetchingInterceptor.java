package interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

public class FetchingInterceptor implements IServerInterceptor {
	

	@Override
	public boolean handleException(RequestDetails arg0, BaseServerResponseException arg1, HttpServletRequest arg2,
			HttpServletResponse arg3) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean incomingRequestPostProcessed(RequestDetails arg0, HttpServletRequest arg1, HttpServletResponse arg2)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		
		
		for(int i=1;i<10;i++) System.out.println(">>>>>FETCH INTERCEPTOR incomingRequestPostProcessed"+arg0);
		return true;
	}

	@Override
	public void incomingRequestPreHandled(RestOperationTypeEnum arg0, ActionRequestDetails arg1) {
		for(int i=1;i<10;i++) System.out.println(">>>>>FETCH INTERCEPTOR incomingRequestPreHandled"+arg0+"\\n"+arg1.getRequestDetails().getCompleteUrl());

	}

	@Override
	public boolean incomingRequestPreProcessed(HttpServletRequest arg0, HttpServletResponse arg1) {
		// TODO Auto-generated method stub
		for(int i=1;i<10;i++) System.out.println(">>>>>FETCH INTERCEPTOR incomingRequestPreProcessed"+arg0);
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails arg0, IBaseResource arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails arg0, TagList arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails arg0, HttpServletRequest arg1, HttpServletResponse arg2)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails arg0, Bundle arg1, HttpServletRequest arg2, HttpServletResponse arg3)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean outgoingResponse(RequestDetails arg0, IBaseResource arg1, HttpServletRequest arg2,
			HttpServletResponse arg3) throws AuthenticationException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails arg0, TagList arg1, HttpServletRequest arg2,
			HttpServletResponse arg3) throws AuthenticationException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public BaseServerResponseException preProcessOutgoingException(RequestDetails arg0, Throwable arg1,
			HttpServletRequest arg2) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}
}