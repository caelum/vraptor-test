package br.com.caelum.vraptor.test.http;

import javax.enterprise.inject.Vetoed;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockRequestDispatcher;

import br.com.caelum.vraptor.http.VRaptorResponse;

@Vetoed
public class VRaptorTestMockRequestDispatcher extends MockRequestDispatcher {

	private JspParser jspParser;
	private Logger logger = LoggerFactory.getLogger(VRaptorTestMockRequestDispatcher.class);

	public VRaptorTestMockRequestDispatcher(String resource,JspParser jspParser) {
		super(resource);
		this.jspParser = jspParser;
	}

	@Override
	protected MockHttpServletResponse getMockHttpServletResponse(
			ServletResponse response) {
		if (response instanceof TargetInstanceProxy) {
			VRaptorResponse testResponse = (VRaptorResponse) ((TargetInstanceProxy) response)
					.getTargetInstance();
			return super.getMockHttpServletResponse(testResponse);
		}
		return super.getMockHttpServletResponse(response);

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
