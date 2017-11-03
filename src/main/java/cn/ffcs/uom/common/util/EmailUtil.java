package cn.ffcs.uom.common.util;

public class EmailUtil {

	public static boolean isEmailAddress(String emailAddress) {
		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		Boolean isValidate = false;
		isValidate = emailAddress.matches(EMAIL_REGEX);
		return isValidate;
	}

}
