package br.com.caelum.vraptor.test.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface JspParser {

	void parse(String forwardedUrl,HttpServletRequest request,HttpServletResponse response);

	
}
