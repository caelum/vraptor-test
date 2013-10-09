package br.com.caelum.vraptor.test.requestflow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.http.Parameters;
import br.com.caelum.vraptor.test.jspsupport.JspResolver;


public class UserFlow {

	private List<UserRequest<VRaptorTestResult>> flows = new ArrayList<>();
	private MockServletContext context;
	protected CdiContainer cdiContainer;
	private VRaptor filter;
	private Instance<Result> result;
	private JspResolver jsp;
	private boolean followRedirect;

	public UserFlow(VRaptor filter, CdiContainer cdiContainer, 
			MockServletContext context, Instance<Result> result, JspResolver jsp) {
		this.filter = filter;
		this.cdiContainer = cdiContainer;
		this.context = context;
		this.result = result;
		this.jsp = jsp;
	}

	public VRaptorTestResult execute() {
		cdiContainer.startSession();
		try{
			VRaptorTestResult result = execute(new LinkedList<UserRequest<VRaptorTestResult>>(flows), null,null);
			jsp.resolve(result);
			return result;
		}
		finally{
			cdiContainer.stopSession();
		}
	}

	private VRaptorTestResult execute(LinkedList<UserRequest<VRaptorTestResult>> flows, VRaptorTestResult result,HttpSession session) {
		if (flows.isEmpty()) {
			return result;
		}
		UserRequest<VRaptorTestResult> req = flows.removeFirst();
		cdiContainer.startRequest();				
		try {
			result = req.call(session);					
			session = result.getCurrentSession();
			if (followRedirect && isAnyKindOfRedirect(result)) {
				flows.addFirst(buildRequest(result.getLastPath(), HttpMethod.GET, new Parameters()));
			}
		}
		finally {
			cdiContainer.stopRequest();
		}
		return execute(flows, result,session);
		
	}

	private boolean isAnyKindOfRedirect(VRaptorTestResult result) {
		int status = result.getResponse().getStatus();
		return status == 302 || status == 301;
	}

	public UserFlow to(final String url,final HttpMethod httpMethod,final Parameters parameters) {
		flows.add(buildRequest(url, httpMethod, parameters));
		return this;

	}

	private UserRequest<VRaptorTestResult> buildRequest(final String url, final HttpMethod httpMethod,
			final Parameters parameters) {
		return new UserRequest<VRaptorTestResult>() {
			@Override
			public VRaptorTestResult call(HttpSession session) {				
				MockHttpServletRequest request = new MockHttpServletRequest(context,httpMethod.toString(),url);
				parameters.fill(request);
				if (session!=null){
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
		};
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

	public UserFlow followRedirect() {
		this.followRedirect = true;
		return this;
	}
	
	

}
