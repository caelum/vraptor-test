package br.com.caelum.vraptor.test;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.VRaptor;

@RequestScoped
public class VRaptorNavigation {
	
	@Inject
	private VRaptor filter;
	@Inject
	private Result result;
	
	public VRaptorTestResult to(String url) {
		MockServletContext context = new MockServletContext();
		MockHttpServletRequest request = new MockHttpServletRequest(context,"GET",url);		
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();
		try {
			filter.init(new MockFilterConfig());
			filter.doFilter(request,response,chain);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new VRaptorTestResult(result,response.getForwardedUrl());
	}

}
