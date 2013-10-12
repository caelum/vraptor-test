package br.com.caelum.vraptor.test.http;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspFakeParser implements JspParser{

	@Override
	public void parse(String forwardedUrl,HttpServletRequest request,HttpServletResponse response) {
	}

}
