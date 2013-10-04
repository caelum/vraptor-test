package br.com.caelum.vraptor.test;

import javax.enterprise.inject.Vetoed;
import javax.servlet.ServletResponse;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockRequestDispatcher;

import br.com.caelum.vraptor.http.MutableResponse;

@Vetoed
public class VRaptorTestMockRequestDispatcher extends MockRequestDispatcher{

	public VRaptorTestMockRequestDispatcher(String resource) {
		super(resource);
	}
	
	@Override
	protected MockHttpServletResponse getMockHttpServletResponse(ServletResponse response) {
		//probably there is inconsistency in forwards inside VRaptor. 
		if(response instanceof MutableResponse){
			return super.getMockHttpServletResponse(((MutableResponse)response).getOriginalResponse());
		}
		return super.getMockHttpServletResponse(response);
		
	}

}
