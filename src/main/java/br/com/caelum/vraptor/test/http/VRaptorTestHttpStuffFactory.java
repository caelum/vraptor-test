package br.com.caelum.vraptor.test.http;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.ioc.cdi.CDIRequestInfoFactory;

@ApplicationScoped
@Alternative
@Priority(500000)
public class VRaptorTestHttpStuffFactory {
	
	@Inject
	private CDIRequestInfoFactory cdiRequestInfoFactory;
	
	@RequestScoped @Produces
	public MutableRequest createRequest(){
		return new VRaptorTestRequest(cdiRequestInfoFactory.producesRequestInfo().getRequest());
	}
}
