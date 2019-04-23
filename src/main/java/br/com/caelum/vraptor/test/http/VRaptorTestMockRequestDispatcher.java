package br.com.caelum.vraptor.test.http;

import javax.enterprise.inject.Vetoed;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockRequestDispatcher;

@Vetoed
public class VRaptorTestMockRequestDispatcher extends MockRequestDispatcher {

	private JspParser jspParser;

	public VRaptorTestMockRequestDispatcher(String resource,JspParser jspParser) {
		super(resource);
		this.jspParser = jspParser;
	}
	
	@Override
	public void forward(ServletRequest request, ServletResponse response) {
		super.forward(request, response);		
		MockHttpServletResponse mockResponse = getMockHttpServletResponse(response);
		if(mockResponse.getForwardedUrl()!=null){
			jspParser.parse(mockResponse.getForwardedUrl(),(HttpServletRequest)request,(HttpServletResponse)response);
		}
	}

}
