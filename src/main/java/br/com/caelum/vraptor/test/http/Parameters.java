package br.com.caelum.vraptor.test.http;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;

public class Parameters {	
	private List<Parameter> parameters = new ArrayList<>();
	private List<Parameter> headers = new ArrayList<>();
	
	private Parameters(Parameter parameter){
		parameters.add(parameter);		
	}
	
	public Parameters(){}

	public static Parameters initWith(String name, Object value) {
		return new Parameters(new Parameter(name, value.toString()));
	}

	public Parameters add(String name, Object value) {
		parameters.add(new Parameter(name, value.toString()));
		return this;
	}

	public Parameters addHeader(String name, Object value) {
		headers.add(new Parameter(name, value.toString()));
		return this;
	}
	
	public void fill(MockHttpServletRequest request) {
		if(!request.getQueryString().isEmpty()){			
			for(String query : request.getQueryString().split("\\&")){
				String[] parts = query.split("=");
				request.addParameter(parts[0], parts[1]);
			}
		}
		for (Parameter param : parameters) {
			request.addParameter(param.getName(),param.getValue());
		}		
		for (Parameter param : headers) {
			request.addHeader(param.getName(), param.getValue());
		}		
	}
}
