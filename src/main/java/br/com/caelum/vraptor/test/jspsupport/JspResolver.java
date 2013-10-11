package br.com.caelum.vraptor.test.jspsupport;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.el.ELContextListener;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;
import org.apache.jasper.runtime.HttpJspBase;
import org.apache.tomcat.InstanceManager;
import org.jboss.weld.environment.servlet.util.Reflections;
import org.jboss.weld.manager.BeanManagerImpl;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import br.com.caelum.vraptor.test.VRaptorTestResult;

public class JspResolver {
	
	private String webContentPath;
	private BeanManagerImpl manager;
    private static final String WELD_LISTENER_CLASS_NAME = "org.jboss.weld.servlet.WeldListener";
    private static final String EXPRESSION_FACTORY_NAME = "org.jboss.weld.el.ExpressionFactory";

	@Deprecated
	public JspResolver() {}
	
	public JspResolver(String webContentPath,BeanManagerImpl manager) {
		this.webContentPath = webContentPath;
		this.manager = manager;
	}
	
	public void resolve(VRaptorTestResult result) {
		File jspFile = new File(webContentPath, "." + result.getLastPath());
		if (!jspFile.exists()) {
			return;
		}
		compileAndExecuteJsp(result);
	}

	private void compileAndExecuteJsp(VRaptorTestResult result) {
		try {
			File compilationDir = compileJsp(result);
			
			HttpJspBase instance = loadJsp(result, compilationDir);
			instance._jspService(result.getRequest(), result.getResponse());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ServletException | IOException e) {
			throw new RuntimeException("could not compile jsp", e);
		}
	}

	private HttpJspBase loadJsp(VRaptorTestResult result, File compilationDir)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, ServletException {
		String jspClassName = toJspClassName(result);
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {compilationDir.toURI().toURL()});
		Class<?> cls = Class.forName(jspClassName, true, classLoader);
		HttpJspBase instance = (HttpJspBase) cls.newInstance();
		MockServletConfig servletConfig = new MockServletConfig(new MockServletContext());
		servletConfig.getServletContext().setAttribute(InstanceManager.class.getName(), new InstanceManagerImplementation());
        JspApplicationContext jspApplicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(servletConfig.getServletContext());

        // Register the ELResolver with JSP
        jspApplicationContext.addELResolver(manager.getELResolver());

        // Register ELContextListener with JSP
        jspApplicationContext.addELContextListener(Reflections.<ELContextListener>newInstance("org.jboss.weld.el.WeldELContextListener"));

        // Push the wrapped expression factory into the servlet context so that Tomcat or Jetty can hook it in using a container code
        servletConfig.getServletContext().setAttribute(EXPRESSION_FACTORY_NAME, manager.wrapExpressionFactory(jspApplicationContext.getExpressionFactory()));		
		instance.init(servletConfig);
		JspFactory.setDefaultFactory(new org.apache.jasper.runtime.JspFactoryImpl());
		instance._jspInit();
		return instance;
	}

	private File compileJsp(VRaptorTestResult result) throws JasperException {
		JspC jspC = new JspC();
		jspC.setUriroot(webContentPath);
		jspC.setJspFiles("." + result.getLastPath());
		jspC.setOutputDir("jsp-compilation");
		jspC.setCompile(true);
		jspC.execute();
		File compilationDir = new File("jsp-compilation");
		return compilationDir;
	}
	
	
	private String toJspClassName(VRaptorTestResult result) {
		String path = result.getLastPath();
		String[] split = path.split("/");
		String method = split[split.length - 1];
		method = method.substring(0, method.length() - 4);
		String controller = split[split.length - 2];
		
		return "org.apache.jsp.WEB_002dINF.jsp." + controller + "." + method + "_jsp";
	}


	private static final class InstanceManagerImplementation implements InstanceManager {
		@Override
		public Object newInstance(String arg0, ClassLoader arg1) throws IllegalAccessException, InvocationTargetException,
				NamingException, InstantiationException, ClassNotFoundException {
			return null;
		}

		@Override
		public void newInstance(Object arg0) throws IllegalAccessException, InvocationTargetException, NamingException {
		}

		@Override
		public Object newInstance(String arg0) throws IllegalAccessException, InvocationTargetException, NamingException,
				InstantiationException, ClassNotFoundException {
			return null;
		}

		@Override
		public void destroyInstance(Object arg0) throws IllegalAccessException, InvocationTargetException {
		}
	}

}
