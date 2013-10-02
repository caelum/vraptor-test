package br.com.caelum.vraptor.test;

import org.junit.Test;
public class VRaptorNavigationTest extends VRaptorIntegration{
		
	@Test
	public void shouldExecuteControllerMethod() throws Exception {
		String page = navigate().to("/test/test2");
		System.out.println(page);
	}
	
}
