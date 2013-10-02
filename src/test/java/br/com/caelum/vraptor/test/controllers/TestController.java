package br.com.caelum.vraptor.test.controllers;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;

@Controller
public class TestController {
	
	@Inject
	private Result result;

	public void test(){
		System.out.println("hi!, i am here");
	}
	
	public void test2(){
		result.forwardTo(this).test();
	}
}
