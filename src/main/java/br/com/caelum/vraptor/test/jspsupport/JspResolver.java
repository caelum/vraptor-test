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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

public class JspResolver {

	private String webContentPath;
	private BeanManagerImpl manager;
	private static final String EXPRESSION_FACTORY_NAME = "org.jboss.weld.el.ExpressionFactory";

	@Deprecated
	public JspResolver() {
	}

	public JspResolver(String webContentPath, BeanManagerImpl manager) {
		this.webContentPath = webContentPath;
		this.manager = manager;
	}

	public void resolve(String forwardedUrl, HttpServletRequest request, HttpServletResponse response) {
		/*
		 * VRaptor always sends a jsp file name. WebContainer does the job looking for
		 * jsps and jspfs
		 */
		File jspFile = new File(webContentPath, "." + forwardedUrl);
		File jspfFile = new File(webContentPath, "." + forwardedUrl + "f");
		if (!jspFile.exists() && !jspfFile.exists()) {
			response.setStatus(404);
			writeToResponse(forwardedUrl + " not found", response);
			return;
		}
		if (jspfFile.exists()) {
			forwardedUrl = forwardedUrl + "f";
		}
		compileAndExecuteJsp(forwardedUrl, request, response);
	}

	private void compileAndExecuteJsp(String forwardedUrl, HttpServletRequest request, HttpServletResponse response) {
		try {
			File compilationDir = compileJsp(forwardedUrl);
			HttpJspBase instance = loadJsp(forwardedUrl, compilationDir);
			instance._jspService(request, response);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ServletException
				| IOException e) {
			throw new RuntimeException("could not compile jsp", e);
		}
	}

	private HttpJspBase loadJsp(String forwardedUrl, File compilationDir) throws MalformedURLException,
			ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException {
		String jspClassName = toJspClassName(forwardedUrl);
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { compilationDir.toURI().toURL() });
		Class<?> cls = Class.forName(jspClassName, true, classLoader);
		HttpJspBase instance = (HttpJspBase) cls.newInstance();
		MockServletConfig servletConfig = new MockServletConfig(new MockServletContext());
		servletConfig.getServletContext().setAttribute(InstanceManager.class.getName(),
				new InstanceManagerImplementation());
		JspApplicationContext jspApplicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(
				servletConfig.getServletContext());

		// Register the ELResolver with JSP
		jspApplicationContext.addELResolver(manager.getELResolver());

		// Register ELContextListener with JSP
		jspApplicationContext.addELContextListener(Reflections
				.<ELContextListener> newInstance("org.jboss.weld.el.WeldELContextListener"));

		// Push the wrapped expression factory into the servlet context so that
		// Tomcat or Jetty can hook it in using a container code
		servletConfig.getServletContext().setAttribute(EXPRESSION_FACTORY_NAME,
				manager.wrapExpressionFactory(jspApplicationContext.getExpressionFactory()));
		instance.init(servletConfig);
		JspFactory.setDefaultFactory(new org.apache.jasper.runtime.JspFactoryImpl());
		instance._jspInit();
		return instance;
	}

	private File compileJsp(String forwardedUrl) throws JasperException {
		JspC jspC = new JspC();
		jspC.setUriroot(webContentPath);
		jspC.setJspFiles("." + forwardedUrl);
		jspC.setOutputDir("jsp-compilation");
		jspC.setCompile(true);
		jspC.execute();
		File compilationDir = new File("jsp-compilation");
		return compilationDir;
	}

	private String toJspClassName(String forwardedUrl) {
		String path = forwardedUrl;
		String[] split = path.split("/");
		String method = split[split.length - 1];
		String className = null;
		if (forwardedUrl.endsWith("jspf")) {
			className = method.substring(0, method.length() - 5)+"_jspf";
		} else {
			className = method.substring(0, method.length() - 4)+"_jsp";
		}
		String controller = split[split.length - 2];
		return "org.apache.jsp.WEB_002dINF.jsp." + controller + "." + className;
	}

	private static final class InstanceManagerImplementation implements InstanceManager {
		@Override
		public Object newInstance(String arg0, ClassLoader arg1) throws IllegalAccessException,
				InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
			return null;
		}

		@Override
		public void newInstance(Object arg0) throws IllegalAccessException, InvocationTargetException, NamingException {
		}

		@Override
		public Object newInstance(String arg0) throws IllegalAccessException, InvocationTargetException,
				NamingException, InstantiationException, ClassNotFoundException {
			return null;
		}

		@Override
		public void destroyInstance(Object arg0) throws IllegalAccessException, InvocationTargetException {
		}

		@Override
		public Object newInstance(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException {
			return null;
		}
	}

	private void writeToResponse(String body, HttpServletResponse response) {
		try {
			response.getWriter().println(body);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
