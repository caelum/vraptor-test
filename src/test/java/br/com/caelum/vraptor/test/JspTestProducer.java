package br.com.caelum.vraptor.test;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import br.com.caelum.vraptor.test.jspsupport.JspResolver;

@Alternative
@ApplicationScoped
@Priority(value = Interceptor.Priority.LIBRARY_BEFORE + 100)
public class JspTestProducer {

	@Inject
	private BeanManager beanManager;

	@Produces
	@ApplicationScoped
	public JspResolver create() {
		return new JspResolver("src/test/resources/WebContent/", beanManager);
	}
}
