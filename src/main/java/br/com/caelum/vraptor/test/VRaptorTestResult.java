package br.com.caelum.vraptor.test;

import java.util.Map;

import javax.enterprise.inject.Vetoed;

import br.com.caelum.vraptor.Result;

@Vetoed
public class VRaptorTestResult {

	private Result result;
	private String pagePath;
	private Map<String, Object> values;

	public VRaptorTestResult(Result result, String pagePath) {
		super();
		this.result = result;
		this.pagePath = pagePath;
		this.values = result.included();
	}
	
	public String getPagePath() {
		return pagePath;
	}
	
	public boolean isKeyIncluded(String key){
		return values.containsKey(key);
	}
	
	public <T> T getObject(String key){
		return (T) values.get(key);
	}
	

}
