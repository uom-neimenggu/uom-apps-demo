package cn.ffcs.uom.common.session;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 应用上下文监听器。
 * 
 * @author wuzhb
 * 
 */
public class ContextListener implements ServletContextListener {
	public static String contextRealPath;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// 上下文根路径
		contextRealPath = event.getServletContext().getRealPath(String.valueOf(File.separatorChar));
		if (!contextRealPath.endsWith(String.valueOf(File.separatorChar))) {
			contextRealPath = contextRealPath + File.separatorChar;
		}
	}
}
