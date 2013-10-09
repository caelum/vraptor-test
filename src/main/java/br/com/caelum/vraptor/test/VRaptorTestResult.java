package br.com.caelum.vraptor.test;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import br.com.caelum.vraptor.Result;

@Vetoed
public class VRaptorTestResult {

	private Result result;
	private Map<String, Object> values;
	private MockHttpServletResponse response;
	private MockHttpServletRequest request;
	private Throwable applicationError;

	public VRaptorTestResult(Result result, MockHttpServletResponse response, MockHttpServletRequest request) {
		super();
		this.result = result;
		this.response = response;
		this.request = request;
		this.values = result.included();
	}
	
	/**
	 * 
	 * @return redirected url or jsp path
	 */
	public String getLastPath() {
		if(response.getRedirectedUrl()!=null){
			return response.getRedirectedUrl();
		}
		return response.getForwardedUrl();
	}
	
	public boolean isKeyIncluded(String key){
		return values.containsKey(key);
	}
	
	public <T> T getObject(String key){
		return (T) values.get(key);
	}

	public HttpSession getCurrentSession() {
		return request.getSession();
	}
	
	public MockHttpServletRequest getRequest() {
		return request;
	}
	
	public MockHttpServletResponse getResponse() {
		return response;
	}
	
	public String getResponseBody() {
		try {
			return response.getContentAsString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public VRaptorTestResult wasStatusOk() {
		int status = response.getStatus();
		if (status != 200) {
			Assert.fail("Response status was " + status + " and not 200");
		}
		return this;
	}

	public void setApplicationError(Throwable applicationError) {
		this.applicationError = applicationError;
	}
	
}
