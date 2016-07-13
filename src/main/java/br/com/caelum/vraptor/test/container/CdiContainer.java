package br.com.caelum.vraptor.test.container;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class CdiContainer {

        private Weld weld;
        private WeldContainer weldContainer;
        private Contexts contexts;
    	
    	public CdiContainer(Weld weld, WeldContainer weldContainer, Contexts contexts) {
    		this.weld = weld;
    		this.weldContainer = weldContainer;
    		this.contexts = contexts;
    	}

    	public CdiContainer() {
    	}

    	public void start() {
    		if (weld == null) {
    			weld = new Weld();
    		}
    		if (weldContainer == null) {
    			weldContainer = weld.initialize();
    		}
    		if (contexts == null) {
    			contexts = getContexts();
    		}
    	}
        
        public BeanManager getBeanManager(){
                return weldContainer.getBeanManager();
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private Contexts getContexts() {
                Contexts instance = new Contexts();

                CreationalContext creationalContext = getBeanManager().createCreationalContext(null);

                AnnotatedType<Contexts> annotatedType = getBeanManager().createAnnotatedType(Contexts.class);
                InjectionTarget<Contexts> injectionTarget = getBeanManager().createInjectionTarget(annotatedType);
                injectionTarget.inject(instance, creationalContext);
                return instance;
                
        }

        public void shutdown() {
                weld.shutdown();
        }
        
        public void stopAllContexts(){
                contexts.stopRequestScope();
                contexts.stopConversationScope();
                contexts.stopSessionScope();
                contexts.stopApplicationScope();
        }
        
        public void startRequest(){
                contexts.startRequestScope();
        }
        
        public void stopRequest(){
                contexts.stopRequestScope();
        }
        
        public void startSession(){
                contexts.startSessionScope();
        }
        
        public void stopSession(){
                contexts.stopSessionScope();
        }
}
