package br.com.caelum.vraptor.test.requestflow;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

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

	public void setCookies(List<Cookie> cookies);
	
	public List<Cookie> getCookies();

}
