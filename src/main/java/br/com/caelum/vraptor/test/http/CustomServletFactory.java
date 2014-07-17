package br.com.caelum.vraptor.test.http;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.interceptor.Interceptor;
import javax.servlet.ServletContext;

import org.springframework.mock.web.MockServletContext;

import br.com.caelum.vraptor.ioc.cdi.ServletContextFactory;
import br.com.caelum.vraptor.test.hack.StandaloneServletContext;

@Alternative
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
@Specializes
public class CustomServletFactory extends ServletContextFactory {

	private MockServletContext context = new StandaloneServletContext();

	@Override
	@Produces
	@ApplicationScoped
	public ServletContext getInstance() {
		return context;
	}

}
