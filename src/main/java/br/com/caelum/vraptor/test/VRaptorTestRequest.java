package br.com.caelum.vraptor.test;

import java.io.IOException;
import java.util.Collection;

import javax.enterprise.inject.Vetoed;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.mock.web.MockHttpServletRequest;

import br.com.caelum.vraptor.http.MutableRequest;

@Vetoed
public class VRaptorTestRequest extends MockHttpServletRequest implements MutableRequest {

	private MutableRequest mutableRequest;

	public VRaptorTestRequest(MutableRequest mutableRequest) {
		this.mutableRequest = mutableRequest;
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpServletRequest getOriginalRequest() {
		// TODO Auto-generated method stub
		return this;
	}
	
	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return new VRaptorTestMockRequestDispatcher(path);
	}

}
