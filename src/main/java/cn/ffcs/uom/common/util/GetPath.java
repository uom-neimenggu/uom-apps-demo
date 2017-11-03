package cn.ffcs.uom.common.util;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

public class GetPath {

	public static void getPath() {
		
		Logger logger = Logger.getLogger(FileUtil.class);
		
		// 方式一
		logger.info(File.separator);
		logger.info("方式一");
		logger.info(System.getProperty("user.dir").replace(
				File.separator, "/"));
		// 方式二
		logger.info("方式二");
		File directory = new File("");// 设定为当前文件夹
		try {
			logger.info(directory.getCanonicalPath().replace(
					File.separator, "/"));// 获取标准的路径
			logger.info(directory.getAbsolutePath().replace(
					File.separator, "/"));// 获取绝对路径
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 方式三
		logger.info("方式三");
		logger.info(GetPath.class.getResource("/"));
		logger.info(GetPath.class.getResource(""));
		// 方式4
		logger.info("方式四");
		logger.info(GetPath.class.getClassLoader().getResource(""));
		logger.info(GetPath.class.getClassLoader().getResource(
				"application-context-mail.xml"));
	}

	// 获取WebRoot目录
	public static String getWebRootPath() {
		
		Logger logger = Logger.getLogger(FileUtil.class);

		URL urlpath = GetPath.class.getResource("");
		String path = urlpath.toString();
		logger.info("path1==>" + path);
		if (path.startsWith("file")) {
			path = path.substring(5);
		}
		logger.info("path2==>" + path);
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF") - 1);
		}
		logger.info("path3==>" + path);
		path = path.replace(File.separator, "/");
		logger.info("path4==>" + path);
		return path;
	}

	// public static void main(String[] args) {
	// GetPath.getPath();
	// }

}