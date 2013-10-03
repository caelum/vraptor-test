package br.com.caelum.vraptor.test;

import org.junit.Test;
public class VRaptorNavigationSimpleScenariosTest extends VRaptorIntegration{
		
	@Test
	public void shouldExecuteDefaultForward() throws Exception {
		String page = navigate().to("/test/test");
		System.out.println(page);
	}
	
}
