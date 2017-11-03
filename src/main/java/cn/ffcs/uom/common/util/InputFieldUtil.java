package cn.ffcs.uom.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;

import cn.ffcs.uom.mail.constants.GroupMailConstant;

/**
 * 验证输入框
 * 
 * @author faq
 * 
 */
public class InputFieldUtil {
	private static InputFieldUtil ifu = null;
	private static String propertiesPath = null;
	private static Properties properties = null;
	static {

		ifu = new InputFieldUtil();

		propertiesPath = getRootPath();

		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(propertiesPath));
			properties = new Properties();
			properties.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					in = null;
				}
			}
		}
	}

	// 验证联系电话
	public static boolean checkPhone(String phone) {
		if (phone.matches("\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d(3)-\\d(8)")) {
			return true;
		} else if (phone.matches("^[1][2,3,4,5,6,7,8]+\\d{9}")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkPhone1(String phone) {
		Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (phone.startsWith(key.toString())
					&& "true".equalsIgnoreCase(value.toString())) {
				return true;
			}
		}
		return false;
	}

	// 验证邮政编码
	public static boolean checkPost(String post) {
		if (post.matches("[1-9]\\d{5}(?!\\d)")) {
			return true;
		} else {
			return false;
		}
	}

	// 验证邮箱
	public static boolean checkEmail(String email) {
		if (email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 集团统一邮箱验证
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isGrpUnEmail(String email) {
		return Pattern.matches("^([a-z]+)\\d*(" + GroupMailConstant.GRP_UN_EMAIL
				+ ")$", email);
	}

	public static String getRootPath() {
		String classPath = ifu.getClass().getClassLoader().getResource("/")
				.getPath();
		String rootPath = "";
		// windows下
		if ("\\".equals(File.separator)) {
			rootPath = classPath.substring(1, classPath.length() - 1);
			rootPath += "/i18n/number_segment_filter.properties";
			rootPath = rootPath.replace("/", "\\");
		}
		// linux下
		if ("/".equals(File.separator)) {
			rootPath = classPath.substring(0, classPath.length() - 1);
			rootPath += "/i18n/number_segment_filter.properties";
			rootPath = rootPath.replace("\\", "/");
		}
		return rootPath;
	}
}
