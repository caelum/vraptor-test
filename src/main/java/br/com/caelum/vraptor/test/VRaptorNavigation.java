package br.com.caelum.vraptor.test;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.VRaptor;
import br.com.caelum.vraptor.controller.HttpMethod;

@ApplicationScoped
public class VRaptorNavigation {
	
	@Inject	
	private VRaptor filter;
	@Inject	
	@RequestScoped
	private Instance<Result> result;
	
	@PostConstruct
	private void init() throws ServletException{
		filter.init(new MockFilterConfig());
	}
	
	public VRaptorTestResult to(String url,HttpMethod httpMethod) {
		MockServletContext context = new MockServletContext();		
		MockHttpServletRequest request = new MockHttpServletRequest(context,httpMethod.toString(),url);		
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();
		try {	
			filter.doFilter(request,response,chain);			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new VRaptorTestResult(result.get(),response);
	}
	
	public VRaptorTestResult get(String url){
		return to(url,HttpMethod.GET);
	}
	
	public VRaptorTestResult post(String url){
		return to(url,HttpMethod.POST);
	}

}
