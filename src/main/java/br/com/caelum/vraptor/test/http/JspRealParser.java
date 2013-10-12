package br.com.caelum.vraptor.test.http;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.test.jspsupport.JspResolver;

@Vetoed
public class JspRealParser implements JspParser {

	private JspResolver jsp;

	public JspRealParser(JspResolver jsp) {
		this.jsp = jsp;
	}

	@Override
	public void parse(String forwardedUrl,HttpServletRequest request,HttpServletResponse response) {
		jsp.resolve(forwardedUrl,request,response);
	}

}
