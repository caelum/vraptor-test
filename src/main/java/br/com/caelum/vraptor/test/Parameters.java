package br.com.caelum.vraptor.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;

public class Parameters {
	
	private List<Parameter> list = new ArrayList<>();
	
	private Parameters(Parameter parameter){
		list.add(parameter);		
	}
	
	public Parameters(){}

	public static Parameters initWith(String name, Object value) {
		return new Parameters(new Parameter(name, value.toString()));
	}

	public Parameters add(String name, Object value) {
		list.add(new Parameter(name, value.toString()));
		return this;
	}

	public void fill(MockHttpServletRequest request) {
		for (Parameter param : list) {
			request.addParameter(param.getName(),param.getValue());
		}
	}

}
