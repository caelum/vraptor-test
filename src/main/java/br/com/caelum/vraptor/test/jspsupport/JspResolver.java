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

import org.apache.commons.io.FileUtils;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;
import org.apache.jasper.runtime.HttpJspBase;
import org.apache.log4j.Logger;
import org.apache.tomcat.InstanceManager;
import org.jboss.weld.environment.servlet.util.Reflections;
import org.jboss.weld.manager.BeanManagerImpl;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

public class JspResolver {
	
	private String webContentPath;
	private BeanManagerImpl manager;
    private static final String EXPRESSION_FACTORY_NAME = "org.jboss.weld.el.ExpressionFactory";
	private MockServletContext servletContext;
	private JspApplicationContext jspApplicationContext;
	private MockServletConfig servletConfig;
	private File compilationDir;
	private URLClassLoader classLoader;
	private static Logger LOG = Logger.getLogger(JspResolver.class); 

	@Deprecated
	public JspResolver() {}
	
	public JspResolver(String webContentPath, BeanManagerImpl manager) {
		this.servletContext = new MockServletContext("file:"+new File(".").getAbsolutePath()+webContentPath);
		this.webContentPath = webContentPath;
		this.manager = manager;
		this.compilationDir = new File("jsp-compilation");
		this.classLoader = buildClassLoader();
		init();
	}

	private URLClassLoader buildClassLoader() {
		try {
			return URLClassLoader.newInstance(new URL[] {compilationDir.toURI().toURL()});
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void init() {
		servletConfig = new MockServletConfig(servletContext);
		servletConfig.getServletContext().setAttribute(InstanceManager.class.getName(), new InstanceManagerImplementation());
		
		JspFactory.setDefaultFactory(new org.apache.jasper.runtime.JspFactoryImpl());
        jspApplicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(servletConfig.getServletContext());
        // Register the ELResolver with JSP
        jspApplicationContext.addELResolver(manager.getELResolver());

        // Register ELContextListener with JSP
        jspApplicationContext.addELContextListener(Reflections.<ELContextListener>newInstance("org.jboss.weld.el.WeldELContextListener"));

        // Push the wrapped expression factory into the servlet context so that Tomcat or Jetty can hook it in using a container code
        servletConfig.getServletContext().setAttribute(EXPRESSION_FACTORY_NAME, manager.wrapExpressionFactory(jspApplicationContext.getExpressionFactory()));
	}

	public void resolve(String forwardedUrl, HttpServletRequest request, HttpServletResponse response) {
		File jspFile = new File(webContentPath, "." + forwardedUrl);
		if (!jspFile.exists()) {
			response.setStatus(404);
			writeToResponse(forwardedUrl + " not found", response);
			return;
		}
		compileAndExecuteJsp(forwardedUrl,request,response);
	}

	private void compileAndExecuteJsp(String forwardedUrl, HttpServletRequest request, HttpServletResponse response) {
		try {
			compileJsp(forwardedUrl);
			
			HttpJspBase instance = loadJsp(forwardedUrl, compilationDir);
			instance._jspService(request, response);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ServletException | IOException e) {
			throw new RuntimeException("could not compile jsp", e);
		}
	}

	private HttpJspBase loadJsp(String forwardedUrl, File compilationDir)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, ServletException {
		String jspClassName = toJspClassName(forwardedUrl);
		Class<?> cls = Class.forName(jspClassName, true, classLoader);
		HttpJspBase instance = (HttpJspBase) cls.newInstance();
		instance.init(servletConfig);
		instance._jspInit();
		return instance;
	}

	private void compileJsp(String forwardedUrl) throws JasperException {
		cleanCompilationDir();
		JspC jspC = new JspC();
		jspC.setUriroot(webContentPath);
		jspC.setJspFiles("." + forwardedUrl);
		jspC.setOutputDir(compilationDir.getAbsolutePath());
		jspC.setClassPath(compilationDir.getAbsolutePath());
		jspC.setCompile(true);
		LOG.debug("Compiling jsp " + forwardedUrl + " to: " + compilationDir.getAbsolutePath());
		jspC.execute();
	}

	private void cleanCompilationDir() {
		try {
			FileUtils.deleteDirectory(compilationDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private String toJspClassName(String forwardedUrl) {
		String path = forwardedUrl;
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
	
	private void writeToResponse(String body,
			HttpServletResponse response) {
		try {
			response.getWriter().println(body);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public MockServletContext getServletContext() {
		return servletContext;
	}
}
