package br.com.caelum.vraptor.test.controllers;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.test.models.Task;

@Controller
public class TestController {
	
	@Inject
	private Result result;
	@Inject
	private HttpSession session;

	public void test(){
		result.include("name","vraptor");
	}
	
	public void test2(){
		result.forwardTo(this).test();
	}
	
	@Post
	public void test3(){
		result.redirectTo(RedirectedController.class).test();
	}
	
	@Post
	public void test4(Task task){
		result.include("task",task);
	}
	
	@Post
	public void test5(){
		Task task = new Task();
		task.setDescription("test");
		session.setAttribute("task", task);
	}	
	
	@Get
	public void test6(){
		result.include("taskInSession",session.getAttribute("task"));
	}
}
