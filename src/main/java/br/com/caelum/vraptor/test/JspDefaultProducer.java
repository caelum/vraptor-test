package br.com.caelum.vraptor.test;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.interceptor.Interceptor;

import br.com.caelum.vraptor.test.jspsupport.JspResolver;

@ApplicationScoped
@Alternative
@Priority(value=Interceptor.Priority.LIBRARY_BEFORE)
public class JspDefaultProducer {

	@Produces
	@ApplicationScoped
	public JspResolver create(){
		return new JspResolver("src/main/webapp/");
	}
}
