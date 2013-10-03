package br.com.caelum.vraptor.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class VRaptorNavigationSimpleScenariosTest extends VRaptorIntegration{
		
	@Test
	public void shouldExecuteDefaultForward() throws Exception {
		VRaptorTestResult result = navigate().to("/test/test");
		assertEquals("/WEB-INF/jsp/test/test.jsp",result.getPagePath());
		assertEquals("vraptor",result.getObject("name"));
	}
	
	@Test
	public void shouldExecuteLogicForward() throws Exception {
		VRaptorTestResult result = navigate().to("/test/test2");
		assertEquals("/WEB-INF/jsp/test/test.jsp",result.getPagePath());
	}
	
}
