package br.com.caelum.vraptor.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class VRaptorNavigationSimpleScenariosTest extends VRaptorIntegration{
		
	@Test
	public void shouldExecuteDefaultForward() throws Exception {
		VRaptorTestResult result = navigate().to("/test/test");
		assertEquals("/WEB-INF/jsp/test/test.jsp",result.getLastPath());
		assertEquals("vraptor",result.getObject("name"));
	}
	
	@Test
	public void shouldExecuteLogicForward() {
		VRaptorTestResult result = navigate().to("/test/test2");
		assertEquals("/WEB-INF/jsp/test/test.jsp",result.getLastPath());
	}
	
	@Test
	public void shouldExecuteLogicRedirect() {
		VRaptorTestResult result = navigate().to("/test/test3");
		assertEquals("/redirected/test",result.getLastPath());
	}	
	
}
