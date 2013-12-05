package br.com.caelum.vraptor.test.jspsupport;

import java.io.UnsupportedEncodingException;

import org.jboss.weld.environment.tomcat7.WeldInstanceManager;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import br.com.caelum.vraptor.test.VRaptorIntegration;

public class JspResolverTest extends VRaptorIntegration {

	@Test
	public void test() throws UnsupportedEncodingException {
		JspResolver resolver = cdiBasedContainer.instanceFor(JspResolver.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		resolver.resolve("/WEB-INF/jsp/test/testTag.jsp", request, response);
		resolver.resolve("/WEB-INF/jsp/test/testTag.jsp", request, response);
		
		String body = response.getContentAsString();
		
		System.out.println(body);
		
		
		
	}

}
