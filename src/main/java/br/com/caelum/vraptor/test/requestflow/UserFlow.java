package br.com.caelum.vraptor.test.requestflow;

import static com.google.common.base.Objects.firstNonNull;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.VRaptor;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.container.CdiContainer;
import br.com.caelum.vraptor.test.http.JspFakeParser;
import br.com.caelum.vraptor.test.http.JspParser;
import br.com.caelum.vraptor.test.http.JspRealParser;
import br.com.caelum.vraptor.test.http.Parameters;
import br.com.caelum.vraptor.test.http.VRaptorTestMockRequestDispatcher;
import br.com.caelum.vraptor.test.jspsupport.JspResolver;
import br.com.caelum.vraptor.validator.Validator;

public class UserFlow {

	private List<UserRequest<VRaptorTestResult>> flows = new ArrayList<>();
	private ServletContext context;
	protected CdiContainer cdiContainer;
	private VRaptor filter;
	private Instance<Result> result;
	private boolean followRedirect;
	private Instance<Validator> validator;
	private JspParser jspParser;
	private Parameters parameters;
	private static Logger LOG = Logger.getLogger(UserFlow.class);

	public UserFlow(VRaptor filter, CdiContainer cdiContainer, ServletContext context, Instance<Result> result,
			Instance<Validator> validator, JspResolver jsp) {
		this.filter = filter;
		this.cdiContainer = cdiContainer;
		this.context = context;
		this.result = result;
		this.validator = validator;
		this.jspParser = new JspRealParser(jsp);
	}

	public VRaptorTestResult execute() {
		cdiContainer.startSession();
		try {
			VRaptorTestResult result = executeFlow(new LinkedList<UserRequest<VRaptorTestResult>>(flows), null, null);
			return result;
		} finally {
			cdiContainer.stopSession();
		}
	}

	private VRaptorTestResult executeFlow(LinkedList<UserRequest<VRaptorTestResult>> flows, VRaptorTestResult result,
			HttpSession session) {
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
			if (!flows.isEmpty()) {
				List<Cookie> cookies = req.getCookies();
				cookies.addAll(result.getCookies());
				flows.getFirst().setCookies(cookies);
			}
		} finally {
			cdiContainer.stopRequest();
		}
		return executeFlow(flows, result, session);

	}

	private boolean isAnyKindOfRedirect(VRaptorTestResult result) {
		int status = result.getResponse().getStatus();
		return status == 302 || status == 301;
	}

	public UserFlow to(final String url, final HttpMethod httpMethod, final Parameters parameters) {
		this.parameters = parameters;
		flows.add(buildRequest(url, httpMethod, parameters));
		return this;

	}

	private UserRequest<VRaptorTestResult> buildRequest(final String url, final HttpMethod httpMethod,
			final Parameters parameters) {
		final String[] splittedUrl = splitUrl(url);
		final String urlWithoutQuery = splittedUrl[0];
		final String query = splittedUrl[1];
		return new UserRequest<VRaptorTestResult>() {
			private Cookie[] cookies = new Cookie[0];
			private MockHttpServletRequest request;

			@Override
			public VRaptorTestResult call(HttpSession session) {
				LOG.debug("starting request to " + url);
				request = new MockHttpServletRequest(context, httpMethod.toString(), urlWithoutQuery) {
					@Override
					public RequestDispatcher getRequestDispatcher(String path) {
						return new VRaptorTestMockRequestDispatcher(path, jspParser);
					}
					
					@Override
					public String getQueryString() {
						return query;
					}
				};
				request.setCookies(cookies);
				parameters.fill(request);
				if (session != null) {
					request.setSession(session);
				}
				MockHttpServletResponse response = new MockHttpServletResponse();
				MockFilterChain chain = new MockFilterChain();
				VRaptorTestResult vRaptorTestResult = null;
				Throwable applicationError = null;
				try {
					filter.doFilter(request, response, chain);
				//TODO: find a better alternative
				} catch (Exception e) {
					applicationError = e.getCause();
					response.setStatus(500);
				}
				Result vraptorResult = result.get();
				Validator vraptorValidator = validator.get();
				vRaptorTestResult = new VRaptorTestResult(vraptorResult, response, request, vraptorValidator);
				vRaptorTestResult.setApplicationError(applicationError);
				return vRaptorTestResult;
			}

			@Override
			public void setCookies(List<Cookie> cookies) {
				this.cookies = cookies.toArray(new Cookie[cookies.size()]);
			}
			
			@Override
			public List<Cookie> getCookies() {
				return new ArrayList<>(asList(firstNonNull(request.getCookies(), new Cookie[0])));
			}		
		};
	}

	public UserFlow get(String url) {
		return get(url, new Parameters());
	}

	public UserFlow get(String url, Parameters parameters) {
		return to(url, HttpMethod.GET, parameters);
	}

	public UserFlow post(String url) {
		return post(url, new Parameters());
	}

	public UserFlow addHeader(String name, Object value) {
		parameters.addHeader(name, value);
		return this;
	}

	public UserFlow addParameter(String name, Object value) {
		parameters.add(name, value);
		return this;
	}

	public UserFlow setContent(String content) {
		parameters.setContent(content);
		return this;
	}

	public UserFlow post(String url, Parameters parameters) {
		return to(url, HttpMethod.POST, parameters);
	}

	public UserFlow followRedirect() {
		this.followRedirect = true;
		return this;
	}

	public UserFlow withoutJsp() {
		this.jspParser = new JspFakeParser();
		return this;
	}

	protected String[] splitUrl(String url) {
	    if (!url.contains("?")) {
	        return new String[]{url, ""};
	    }
	    return url.split("\\?");
	}	
}
