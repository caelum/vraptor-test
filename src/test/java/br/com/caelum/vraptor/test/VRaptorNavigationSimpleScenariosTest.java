package br.com.caelum.vraptor.test;

import org.junit.Test;

import br.com.caelum.vraptor.test.models.Task;

import static org.junit.Assert.assertEquals;

public class VRaptorNavigationSimpleScenariosTest extends VRaptorIntegration {

	@Test
	public void shouldExecuteDefaultForward() throws Exception {
		VRaptorTestResult result = navigate().get("/test/test").execute();
		assertEquals("/WEB-INF/jsp/test/test.jsp", result.getLastPath());
		assertEquals("vraptor", result.getObject("name"));
	}

	@Test
	public void shouldExecuteLogicForward() {
		VRaptorTestResult result = navigate().get("/test/test2").execute();
		assertEquals("/WEB-INF/jsp/test/test.jsp", result.getLastPath());
	}

	@Test
	public void shouldExecuteLogicRedirect() {
		VRaptorTestResult result = navigate().post("/test/test3").execute();
		assertEquals("/redirected/test", result.getLastPath());
	}

	@Test
	public void shouldPassObjectParameter() {
		VRaptorTestResult result = navigate().post("/test/test4",
				Parameters.initWith("task.description", "test").add("task.difficulty", 10)).execute();
		Task task = result.getObject("task");
		assertEquals(10,task.getDifficulty());
		assertEquals("test",task.getDescription());
	}
	
	@Test
	public void shouldKeepObjectsInSession() {
		VRaptorTestResult result = navigate().post("/test/test5").get("/test/test6").execute();
		Task task = result.getObject("taskInSession");
		assertEquals("test",task.getDescription());
	}	

}
