package br.com.caelum.vraptor.test.hack;

import java.util.EventListener;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.springframework.mock.web.MockServletContext;

public class StandaloneServletContext extends MockServletContext {

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return null;
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String servletName, String className) {
		return null;
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
		return null;
	}

	@Override
	public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
		return null;
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> c) throws ServletException {
		return null;
	}

	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		return null;
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return null;
	}

	@Override
	public FilterRegistration.Dynamic addFilter(String filterName, String className) {
		return null;
	}

	@Override
	public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
		return null;
	}

	@Override
	public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
		return null;
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> c) throws ServletException {
		return null;
	}

	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		return null;
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return null;
	}

	@Override
	public void addListener(Class<? extends EventListener> listenerClass) {
	}

	@Override
	public void addListener(String className) {
	}

	@Override
	public <T extends EventListener> void addListener(T t) {
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> c) throws ServletException {
		return null;
	}

}
