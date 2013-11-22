package br.com.caelum.vraptor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.vraptor.test.http.Parameters;
import br.com.caelum.vraptor.test.models.Task;

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
	
	@Test
	public void shouldValidateObject(){
		navigate().post("/test/test7", Parameters.initWith("task.description", "test").add("task.difficulty", 10))
				.execute();	
	}
	
	@Test
	public void shouldCompileAndExecuteAJsp() {
		VRaptorTestResult result = navigate().post("/test/test8").execute();	
		String html = result.getResponseBody();
		assertEquals("Hello world from a jsp", html);
	}
	
	@Test
	public void shouldNotExecuteJsp() {
		VRaptorTestResult result = navigate().post("/test/test8").withoutJsp().execute();	
		String html = result.getResponseBody();
		assertTrue(html.isEmpty());
	}
	
	@Test
	public void shouldFailForStatusCodeDifferentThan200() {
		try {
			navigate()
				.post("/test/buggedMethod")
				.withoutJsp().execute().wasStatus(200);
			fail("it should not get here");
		} catch (AssertionFailedError error) {
			//it worked
		}
	}
	
	@Test
	public void shouldCauseValidationErrors() {
		try {
			navigate().post("/test/withValidatorError").execute().isValid();	
			fail("it should not get here");
		} catch (AssertionFailedError error) {
			//it worked
		}
	}
	
	@Test
	public void shouldSetAndSendCookies() {
		VRaptorTestResult result = navigate().post("/test/setCookie")
				.get("/test/getCookie")
				.execute();	
		assertEquals("cookieValue", result.getObject("cookieFromRequest"));
	}
	
	@Test
	public void shouldSetWithRedirectAndSendCookies() {
		VRaptorTestResult result = navigate().post("/test/setCookie")
				.followRedirect()
				.get("/test/getCookie")
				.execute();	
		assertEquals("cookieValue", result.getObject("cookieFromRequest"));
	}
	
	@Test
	public void shouldGetRealPathFromServletContext() {
		VRaptorTestResult result = navigate().get("/test/servletContextTest").execute();	
		assertNotNull(result.getObject("realPath"));
	}
	
	@Test
	public void shouldSendEmptyCookieArray() {
		VRaptorTestResult result = navigate().get("/test/emptyCookies").execute();	
		result.wasStatus(200);
		assertEquals(true, result.getObject("isEmpty"));
	}
	
}
