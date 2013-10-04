package br.com.caelum.vraptor.test;

import javax.enterprise.inject.Vetoed;
import javax.servlet.ServletResponse;

import net.vidageek.mirror.dsl.Mirror;

import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockRequestDispatcher;

import br.com.caelum.vraptor.http.VRaptorResponse;

@Vetoed
public class VRaptorTestMockRequestDispatcher extends MockRequestDispatcher {

	public VRaptorTestMockRequestDispatcher(String resource) {
		super(resource);
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

}
