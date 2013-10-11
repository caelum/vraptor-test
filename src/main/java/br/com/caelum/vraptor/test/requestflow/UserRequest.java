package br.com.caelum.vraptor.test.requestflow;

import javax.servlet.http.HttpSession;

import org.springframework.mock.web.MockHttpSession;

import br.com.caelum.vraptor.test.jspsupport.JspResolver;

/**
 * Represents interaction flow
 * @author Alberto Souza
 *
 */
public interface UserRequest<T> {

	/**
	 * 
	 * @param session shared session between requests 
	 * @param jsp 
	 * @return
	 */
	public T call(HttpSession session);
}
