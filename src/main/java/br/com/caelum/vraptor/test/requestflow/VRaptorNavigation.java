package br.com.caelum.vraptor.test.requestflow;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;

import org.jboss.weld.el.WeldELResolver;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockServletContext;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.VRaptor;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.cdi.CdiContainer;
import br.com.caelum.vraptor.test.jspsupport.JspResolver;

@ApplicationScoped
public class VRaptorNavigation {
	
	@Inject	
	private VRaptor filter;
	@Inject	
	@RequestScoped
	private Instance<Result> result;
	@Inject	
	@RequestScoped
	private Instance<Validator> validator;
	private MockServletContext context = new MockServletContext();
	private CdiContainer cdiContainer;
	@Inject	
	private JspResolver jspResolver;
	
	@PostConstruct
	private void init() throws ServletException{
		filter.init(new MockFilterConfig(context));
	}
	
	public UserFlow start(){
		if(this.cdiContainer==null){
			throw new IllegalStateException("Container must be set to enable scope control");
		}
		return new UserFlow(filter, cdiContainer, context, result, validator, jspResolver);
	}
	
	public void setContainer(CdiContainer cdiContainer) {
		this.cdiContainer = cdiContainer;
	}

}
