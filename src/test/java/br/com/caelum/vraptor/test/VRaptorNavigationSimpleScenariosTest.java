package br.com.caelum.vraptor.test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.naming.NamingException;
import javax.servlet.jsp.JspFactory;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.jasper.JspC;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.runtime.HttpJspBase;
import org.apache.tomcat.InstanceManager;
import org.jboss.weld.el.WeldExpressionFactory;
import org.jboss.weld.environment.tomcat7.WeldInstanceManager;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import br.com.caelum.vraptor.test.models.Task;

import static org.junit.Assert.assertEquals;

public class VRaptorNavigationSimpleScenariosTest extends VRaptorIntegration {

	private static final class InstanceManagerImplementation implements InstanceManager {
		@Override
		public Object newInstance(String arg0, ClassLoader arg1) throws IllegalAccessException, InvocationTargetException,
				NamingException, InstantiationException, ClassNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void newInstance(Object arg0) throws IllegalAccessException, InvocationTargetException, NamingException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object newInstance(String arg0) throws IllegalAccessException, InvocationTargetException, NamingException,
				InstantiationException, ClassNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void destroyInstance(Object arg0) throws IllegalAccessException, InvocationTargetException {
			// TODO Auto-generated method stub
			
		}
	}

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
	
	public static void main(String[] args) throws Exception {
		JspC jspC = new JspC();		
		jspC.setUriroot(".");
		jspC.setJspFiles("test.jsp");
		jspC.setOutputDir("jsp-compilation");
		jspC.setCompile(true);
		jspC.execute();
		File compilationDir = new File("jsp-compilation");
		
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {compilationDir.toURI().toURL()});
		Class<?> cls = Class.forName("org.apache.jsp.test_jsp", true, classLoader);
		HttpJspBase instance = (HttpJspBase) cls.newInstance();
		MockServletConfig servletConfig = new MockServletConfig(new MockServletContext());
		servletConfig.getServletContext().setAttribute(InstanceManager.class.getName(), new InstanceManagerImplementation());
		instance.init(servletConfig);
		JspFactory.setDefaultFactory(new org.apache.jasper.runtime.JspFactoryImpl());
		instance._jspInit();
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute("title","funciona por favor");
		request.setAttribute("list",new String[]{"a,b,c"});
		instance._jspService(request,response);
		System.out.println(response.getContentAsString().trim());
	}

}
