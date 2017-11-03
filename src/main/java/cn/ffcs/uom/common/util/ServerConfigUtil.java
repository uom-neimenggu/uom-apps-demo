package cn.ffcs.uom.common.util;

/**
 * 系统主机环境变量配置读取
 * 
 * @author zhanglu
 *
 */
public class ServerConfigUtil {
	/**
	 * 读取COMPONENT_SHOW用户变量 
	 * 读取为"true"返回true,否则返回false
	 * @return boolean
	 */
	public static boolean isComponentShow() {
		String path = System.getenv("COMPONENT_SHOW");

		if ("true".equals(path)) {
			return true;
		} else {
			return false;
		}
	}
}
