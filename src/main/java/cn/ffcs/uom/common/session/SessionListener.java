/**
 * 
 */
package cn.ffcs.uom.common.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	private SessionContext myc = SessionContext.getInstance();

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		myc.addSession(httpSessionEvent.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		myc.delSession(session);
	}
}