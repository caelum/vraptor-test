package br.com.caelum.vraptor.test.requestflow;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.mock.web.MockFilterConfig;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.VRaptor;
import br.com.caelum.vraptor.test.container.CdiContainer;
import br.com.caelum.vraptor.test.jspsupport.JspResolver;
import br.com.caelum.vraptor.validator.Validator;

@ApplicationScoped
public class VRaptorNavigation {
	
	private VRaptor filter = new VRaptor();
	@Inject	
	@RequestScoped
	private Instance<Result> result;
	@Inject	
	@RequestScoped
	private Instance<Validator> validator;
	@Inject
	private ServletContext context;
	private CdiContainer cdiContainer;
	@Inject	
	private JspResolver jspResolver;
	
	@PostConstruct
	public void init() throws ServletException {
		BeanManager manager = CDI.current().getBeanManager();
		AnnotatedType<VRaptor> type = manager.createAnnotatedType(VRaptor.class);
		InjectionTarget<VRaptor> target = manager.createInjectionTarget(type);
		CreationalContext<VRaptor> ctx = manager.createCreationalContext(null);
		target.inject(filter, ctx);
		MockFilterConfig cfg = new MockFilterConfig(context);
		filter.init(cfg);
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
