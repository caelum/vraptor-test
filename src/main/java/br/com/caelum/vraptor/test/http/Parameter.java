package br.com.caelum.vraptor.test.http;

public class Parameter {

	private String name;
	private String value;

	public Parameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Parameter [name=" + name + ", value=" + value + "]";
	}

	
}
