package br.com.caelum.vraptor.test;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.jboss.weld.manager.BeanManagerImpl;

import br.com.caelum.vraptor.test.jspsupport.JspResolver;

@ApplicationScoped
@Alternative
@Priority(value=Interceptor.Priority.LIBRARY_AFTER)
public class JspTestProducer {
	
	@Inject
	private BeanManagerImpl beanManagerImpl;	

	@Produces
	@ApplicationScoped
	public JspResolver jspResolver() {
		return new JspResolver("src/test/resources/WebContent/",beanManagerImpl);
	}
}
