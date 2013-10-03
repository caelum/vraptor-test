package br.com.caelum.vraptor.test;

import javax.enterprise.inject.spi.CDI;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.ioc.cdi.CDIBasedContainer;
import br.com.caelum.vraptor.ioc.cdi.CdiContainer;

public class VRaptorIntegration {

	private static CdiContainer cdiContainer;
	protected static CDIBasedContainer cdiBasedContainer;

	@BeforeClass
	public static void startCDIContainer(){
		cdiContainer = new br.com.caelum.vraptor.ioc.cdi.CdiContainer();
		cdiContainer.start();
		cdiBasedContainer = CDI.current().select(CDIBasedContainer.class).get();
	}

	@AfterClass
	public static void shutdownCDIContainer() {
		cdiContainer.shutdown();
	}
	
	@Before
	public void newRequest(){
		cdiContainer.startRequest();
		cdiContainer.startSession();		
	}
	
	@After
	public void endOfRequest(){
		cdiContainer.stopRequest();
		cdiContainer.stopSession();
	}
	
	protected static VRaptorNavigation navigate(){		
		return cdiBasedContainer.instanceFor(VRaptorNavigation.class);
	}
		
}
