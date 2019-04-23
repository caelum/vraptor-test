package br.com.caelum.vraptor.test.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.weld.context.ApplicationContext;
import org.jboss.weld.context.bound.BoundConversationContext;
import org.jboss.weld.context.bound.BoundRequestContext;
import org.jboss.weld.context.bound.BoundSessionContext;
import org.jboss.weld.context.bound.MutableBoundRequest;

public class Contexts {
		private static Class<?> klazz;
        
        @Inject
        private ApplicationContext applicationContext;

        @Inject
        private BoundSessionContext sessionContext;

        @Inject
        private BoundRequestContext requestContext;

        @Inject
        private BoundConversationContext conversationContext;
        
        private Map<String, Object> sessionMap = new HashMap<>();;
        private Map<String, Object> requestMap = new HashMap<>();;

        void startApplicationScope() {
                // Welds ApplicationContext is always active
        }

        void stopApplicationScope() {
                if (applicationContext.isActive()) {
                		if (instanceOf(applicationContext )) {
                			final Object beanStore = getBeanStore(applicationContext);
                			clear(beanStore);
                		}
                }
        }

        void startSessionScope() {
                sessionContext.associate(sessionMap);
                sessionContext.activate();
        }

        void stopSessionScope() {
                if (sessionContext.isActive()) {
                        sessionContext.invalidate();
                        sessionContext.deactivate();
                        sessionContext.dissociate(sessionMap);
                        sessionMap = new HashMap<>();
                }
        }

        void startConversationScope(String cid) {
                conversationContext.associate(new MutableBoundRequest(requestMap,sessionMap));
                conversationContext.activate(cid);
        }

        void stopConversationScope() {
                if (conversationContext.isActive()) {
                        conversationContext.invalidate();
                        conversationContext.deactivate();
                        conversationContext.dissociate(new MutableBoundRequest(requestMap,sessionMap));
                }
        }

        void startRequestScope() {
                requestContext.associate(requestMap);
                requestContext.activate();
        }

        void stopRequestScope() {
                if (requestContext.isActive()) {
                        requestContext.invalidate();
                        requestContext.deactivate();
                        requestContext.dissociate(requestMap);
                        requestMap = new HashMap<>();
                }
        }

    	private void clear(final Object object) {
	    		final Method method = getClearMethod();
	    		try {
	    				method.invoke(object);
	    		} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	    				throw new RuntimeException(e);
	    		}
    	}

     	private Object getBeanStore(final Object object) {
	     		final Method method = getBeanStoreMethod();
	    		try {
	    				return method.invoke(object);
	    		} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	    				throw new RuntimeException(e);
	    		}
    	}

     	private Class<?> getBeanStore() {
	    		try {
	    				return Class.forName("org.jboss.weld.context.beanstore.BeanStore");
	    		} catch (ClassNotFoundException e) {
		    			try {
		    					return Class.forName("org.jboss.weld.contexts.beanstore.BeanStore");
		    			} catch (ClassNotFoundException e2) {
		    					throw new RuntimeException(e);
		    			}
	    		}
    	}

     	private Method getClearMethod() {
    		final Class<?> klazz = getBeanStore();
    		try {
    			return klazz.getMethod("clear");
    		} catch (final NoSuchMethodException | SecurityException e) {
    			throw new RuntimeException(e);
    		}
    	}

     	private Method getBeanStoreMethod() {
	    		final Class<?> klazz = getContextKlazz();
	    		try {
	    				return klazz.getMethod("getBeanStore");
	    		} catch (final NoSuchMethodException | SecurityException e) {
	    				throw new RuntimeException(e);
	    		}
    	}

     	private Class<?> getContextKlazz() {
	    		if(klazz == null) {
	
		     			try {
		     					klazz = Class.forName("org.jboss.weld.context.AbstractSharedContext");
		    			} catch (ClassNotFoundException e) {
			    				try {
			    						klazz = Class.forName("org.jboss.weld.contexts.AbstractSharedContext");
			    				} catch (ClassNotFoundException e2) {
			    						throw new RuntimeException(e);
			    				}
		    			}
	    		}
	    		return klazz;
    	}


     	private Boolean instanceOf(final Object object) {
     			return getContextKlazz().isInstance(object);
    	}
}
