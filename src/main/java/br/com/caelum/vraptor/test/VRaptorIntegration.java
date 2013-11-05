package br.com.caelum.vraptor.test;

import javax.enterprise.inject.spi.CDI;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import br.com.caelum.vraptor.ioc.cdi.CDIBasedContainer;
import br.com.caelum.vraptor.test.container.CdiContainer;
import br.com.caelum.vraptor.test.requestflow.UserFlow;
import br.com.caelum.vraptor.test.requestflow.VRaptorNavigation;

public  class VRaptorIntegration {

	private static CdiContainer cdiContainer;
	protected static CDIBasedContainer cdiBasedContainer;

	@BeforeClass
	public static void startCDIContainer(){
		cdiContainer = new CdiContainer();
		cdiContainer.start();
		cdiBasedContainer = CDI.current().select(CDIBasedContainer.class).get();
	}

	@AfterClass
	public static void shutdownCDIContainer() {
		cdiContainer.shutdown();
	}	
	
	protected static UserFlow navigate(){
		VRaptorNavigation navigation = cdiBasedContainer.instanceFor(VRaptorNavigation.class);
		navigation.setContainer(cdiContainer);
		return navigation.start();
	}	

}
