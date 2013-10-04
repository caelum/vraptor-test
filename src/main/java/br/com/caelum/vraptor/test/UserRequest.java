package br.com.caelum.vraptor.test;

import javax.servlet.http.HttpSession;

import org.springframework.mock.web.MockHttpSession;

/**
 * Represents interaction flow
 * @author Alberto Souza
 *
 */
public interface UserRequest<T> {

	/**
	 * 
	 * @param session shared session between requests 
	 * @return
	 */
	public T call(HttpSession session);
}
