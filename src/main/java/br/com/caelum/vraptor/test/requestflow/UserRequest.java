package br.com.caelum.vraptor.test.requestflow;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

	public void setCookies(List<Cookie> cookies);
	
	public List<Cookie> getCookies();

}
