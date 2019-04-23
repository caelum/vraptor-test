package br.com.caelum.vraptor.test.controllers;

import static br.com.caelum.vraptor.view.Results.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.junit.Ignore;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.test.models.Task;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;

@Controller
@Ignore
public class TestController {
	
	@Inject
	private Result result;
	@Inject
	private HttpSession session;
	@Inject
	private Validator validator;
	@Inject
	private HttpServletResponse response;
	@Inject
	private HttpServletRequest request;
	@Inject
	private ServletContext ctx;
	
	@Get
	public void testQuery(){
		result.include("query", request.getQueryString());
		String name = "someFieldName";
		String value = request.getParameter(name);
		result.include(name, value);
		result.use(json());
	}
	
	@Post
	@Consumes(value = "application/json")
	public void testJson(String value1, String value2){
		Map<String, String> object = new LinkedHashMap<String, String>();
		object.put("value2", value2);
		object.put("value1", value1);
		result.use(json()).withoutRoot().from(object).serialize();
	}
	
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
	public void test6() {
		result.include("taskInSession",session.getAttribute("task"));
	}
	
	@Post
	public void test7(@Valid Task task){
	}	
	
	@Post
	public void test8(){
	}
	
	@Get
	public void test9(){
	}
	
	@Get
	public void test10(){
	}	
	
	@Post
	public void test11(){
		String name = "Authorization";
		String token = request.getHeader(name);
		result.include(name, token);
		result.use(json());
	}
	
	@Post
	public void withValidatorError() {
		validator.add(new SimpleMessage("error", "error"));
		validator.onErrorRedirectTo(this).test();
	}
	
	@Post
	public void buggedMethod() {
		throw new RuntimeException();
	}	
	
	@Post
	public void setCookie() {
		response.addCookie(new Cookie("cookieName", "cookieValue"));
	}
	
	@Post
	public void setCookieAndRedirect() {
		response.addCookie(new Cookie("cookieName", "cookieValue"));
		result.redirectTo(this).getCookie();
	}
	
	@Get
	public void getCookie() {
		String value = findCookie();
		result.include("cookieFromRequest", value);
	}

	private String findCookie() {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("cookieName")) {
				return cookie.getValue();
			}
		}
		throw new NoSuchElementException("could not find cookie");
	}
	
	@Get
	public void servletContextTest() {		
		result.include("realPath", ctx.getRealPath(""));
	}
	
	@Get
	public void emptyCookies() {
		Cookie[] cookies = request.getCookies();
		result.include("isEmpty", cookies == null || cookies.length == 0);
		result.nothing();
	}
	
}
