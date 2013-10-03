package br.com.caelum.vraptor.test;

import java.util.Map;

import javax.enterprise.inject.Vetoed;

import org.springframework.mock.web.MockHttpServletResponse;

import br.com.caelum.vraptor.Result;

@Vetoed
public class VRaptorTestResult {

	private Result result;
	private Map<String, Object> values;
	private MockHttpServletResponse response;

	public VRaptorTestResult(Result result, MockHttpServletResponse response) {
		super();
		this.result = result;
		this.response = response;
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
	

}
