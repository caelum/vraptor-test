package br.com.caelum.vraptor.test;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.vraptor.VRaptorException;

/**
 * An entry point for the test API.
 * 
 * @author Guilherme Silveira
 */
public class RealVRaptor {
	
	private final static Logger LOG = LoggerFactory.getLogger(RealVRaptor.class);

	private final Server server;
	private int port;

	/**
	 * Starts the server at this base directory and port
	 */
	public RealVRaptor(File base, int port) {
		this.server = new Server(port);
		WebAppContext context = new WebAppContext();
		
		context.setDescriptor(base.getAbsolutePath() + "/WEB-INF/web.xml");
		context.setResourceBase(base.getAbsolutePath());
		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		LOG.info("Loading port "+ port + " with vraptor context at " + base.getAbsolutePath());

		server.setHandler(context);

		try {
			server.start();
			this.port = port;
			LOG.info("VRaptor context started");
		} catch (Exception e) {
			throw new VRaptorException("Unable to start vraptor", e);
		}
	}
	
	/**
	 * Starts the server at this base directory, using port 8080
	 */
	public RealVRaptor(File base) {
		this(base, 8080);
	}

	/**
	 * Stops the server
	 */
	public void stop() {
		if (server.isRunning()) {
			try {
				server.stop();
				LOG.info("VRaptor context stopped");
			} catch (Exception e) {
				throw new VRaptorException("Unable to stop vraptor", e);
			}
		}
	}
	
	protected void finalize() throws Throwable {
		stop();
	}

	/**
	 * Starts a new request
	 */
	public Request at(String relative) {
		if (!server.isRunning()) {
			throw new IllegalStateException(
					"Jetty did not start properly or already stopped.");
		}
		return Restfulie.at("http://localhost:" + port + relative).accept("text/html");
	}

}
