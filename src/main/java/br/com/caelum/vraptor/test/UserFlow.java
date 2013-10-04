package br.com.caelum.vraptor.test;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.servlet.http.HttpSession;

import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.VRaptor;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.ioc.cdi.CdiContainer;


public class UserFlow {

	private List<UserRequest<VRaptorTestResult>> flows = new ArrayList<>();
	private MockServletContext context;
	protected CdiContainer cdiContainer;
	private VRaptor filter;
	private Instance<Result> result;

	public UserFlow(VRaptor filter, CdiContainer cdiContainer, MockServletContext context, Instance<Result> result) {
		this.filter = filter;
		this.cdiContainer = cdiContainer;
		this.context = context;
		this.result = result;
	}

	public VRaptorTestResult execute() {
		try {
			cdiContainer.startSession();
			HttpSession session = null;
			VRaptorTestResult result = null;
			for (UserRequest<VRaptorTestResult> req : flows) {
				cdiContainer.startRequest();				
				try{
					result = req.call(session);
					session = result.getCurrentSession();
				}
				finally{
					cdiContainer.stopRequest();
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			cdiContainer.stopSession();
		}
	}

	public UserFlow to(final String url,final HttpMethod httpMethod,final Parameters parameters) {
		flows.add(new UserRequest<VRaptorTestResult>() {
			@Override
			public VRaptorTestResult call(HttpSession session) {				
				MockHttpServletRequest request = new MockHttpServletRequest(context,httpMethod.toString(),url);
				parameters.fill(request);
				if(session!=null){
					request.setSession(session);
				}
				MockHttpServletResponse response = new MockHttpServletResponse();
				MockFilterChain chain = new MockFilterChain();
				try {	
					filter.doFilter(request,response,chain);			
					Result vraptorResult = (Result) ((TargetInstanceProxy)result.get()).getTargetInstance();
					return new VRaptorTestResult(vraptorResult,response,request);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return this;

	}
	
	public UserFlow get(String url){
		return get(url,new Parameters());
	}
	
	public UserFlow get(String url, Parameters parameters) {
		return to(url,HttpMethod.GET,parameters);
	}

	public UserFlow post(String url){
		return post(url,new Parameters());
	}

	public UserFlow post(String url, Parameters parameters) {
		return to(url,HttpMethod.POST,parameters);
	}
	
	

}
