package cn.ffcs.uom.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * @author 曾臻
 * @date 2012-10-20
 * 
 */
public class AjaxUtil {
	public static Object[] createReturnValueError() {
		return new Object[] { "error", "遇到错误。" };
	}

	public static Object[] createReturnValueSuccess(Object... objs) {
		Object[] ret = new Object[objs.length + 1];
		ret[0] = "ok";
		int i = 1;
		for (Object o : objs)
			ret[i++] = o;
		return ret;
	}

	public static Object[] createReturnValueError(Object... objs) {
		Object[] ret = new Object[objs.length + 1];
		ret[0] = "error";
		int i = 1;
		for (Object o : objs)
			ret[i++] = o;
		return ret;
	}

	public static HttpSession getHttpSession() {
		WebContext ctx = WebContextFactory.get();
		HttpSession _session = null;
		if (ctx != null) {
			_session = ctx.getSession();
		}
		if (_session == null) {
			_session = session;
		}
		return _session;
	}

	public static HttpServletRequest getHttpRequest() {
		WebContext ctx = WebContextFactory.get();
		return ctx.getHttpServletRequest();
	}

	public static Object getSessionAttribute(String key) {
		return getHttpSession().getAttribute(key);
	}

	private static HttpSession session = null;

	/**
	 * 给通过servlet方式访问ajax方法的连接存储全局session 最好在使用完毕后立即清楚该session，如：
	 * setHttpSession(session); doSomeThing... setHttpSession(null);
	 */
	public static void setHttpSession(HttpSession _session) {
		session = _session;
	}
}
