package cn.ffcs.uom.common.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 功能说明:内部DES加密算法实现,兼容C语言加解密.该方法源码由CRM-曾臻提供.
 * 
 * 创建人:李涌
 * 
 * 创建时间:2013-11-29 下午5:11:18
 * 
 * 修改人 修改时间 修改描述
 * 
 * 
 * Copyright (c)2013 福建富士通信息软件有限公司-版权所有
 * 
 */
public class SimpleDESCry {

	public static final String DES_ALGORITHM = "DES";

	public static final String PRIVATE_KEY = "FFCS_EAM2014";
	// 明文字符串最大长度
	private static final int SOURCE_STRING_MAX_LEN = 2048;
	// 密文字符串最大长度
	private static final int ENCRYPT_STRING_MAX_LEN = 2048;

	private static final Charset default_charset = Charset.forName("UTF-8");

	private static final StringBuffer str_set = new StringBuffer(
			"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_=+{}[]|\':;.>,<?/`~!@#$%^&*()");// ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_=+{}[]|\':;.>,<?/`~!@#$%^&*()";
	private static final StringBuffer str_pw_set = new StringBuffer(
			"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");// ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final char ch_double_quotes = '"';// = '"';
	// StringBuffer key =new StringBuffer();//=
	// "*bv_.azqadec;d7efbikop,01-fre382";

	StringBuffer des = new StringBuffer();
	StringBuffer org = new StringBuffer();
	StringBuffer errormsg = new StringBuffer();

	public SimpleDESCry() {
		// str_set.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_=+{}[]|\':;.>,<?/`~!@#$%^&*()");
		// str_pw_set.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
		// ch_double_quotes = '"';
		// key.append(privateKey);
	}

	public StringBuffer cry(StringBuffer src2, String privateKey) {
		byte[] src = src2.toString().getBytes(default_charset);
		StringBuffer tmpch = new StringBuffer();
		int k;
		int n_invalid = 1;
		int i = src.length;
		int l_set = str_set.length();

		if (!isvalid_org_text(src2))
			return des;

		des.setLength(0);

		for (k = 0; k < i; k++) {
			String s = String.format("%1$02x",
					src[k]
							^ privateKey.getBytes(default_charset)[k
									% privateKey.length()]);
			des.append(s);
		}

		while (k < 16) {
			byte[] des2 = des.toString().getBytes(default_charset);
			byte[] key2 = privateKey.getBytes(default_charset);
			// String s=String.format("%1$02x", des2[2*k]^key2[k%key.length()]);
			String s = String.format("%1$02x",
					0 ^ key2[k % privateKey.length()]);
			des.append(s);
			k++;
		}

		return des;
	}

	/* 判断输入的需要加密的源字符串是否合法 不允许为空格，退格键，回车及其它键盘上没有的字符 */
	boolean isvalid_org_text(StringBuffer src_org) {
		int i = src_org.length();
		short n_invalid;

		int l_set = str_set.length();

		if (i > SOURCE_STRING_MAX_LEN) {
			errormsg.setLength(0);
			errormsg.append("source string is too long!\n");
			return false;
		}

		for (int k = 0; k < i; k++) {
			n_invalid = 1;
			for (int j = 0; j < l_set; j++) {
				if (src_org.charAt(k) == str_set.charAt(j)
						|| src_org.charAt(k) == ch_double_quotes) {
					n_invalid = 0;
					break;
				}
			}

			if (n_invalid == 1) {
				errormsg.setLength(0);
				errormsg.append("Invalid char in source string! \n");
				return false;
			}
		}

		return true;
	}

	/* 判断输入的需要解密的字符串是否合法 */
	boolean isvalid_pw_text(StringBuffer src_pw) {
		if (src_pw.length() > ENCRYPT_STRING_MAX_LEN || src_pw.length() % 2 > 0
				|| src_pw.length() == 0) {
			errormsg.setLength(0);
			errormsg.append("length of the source string is error!\n");
			return false;
		}

		char p_src;
		char p_pw_set;
		short n_valid;
		for (int i = 0; i < src_pw.length(); i++) {
			p_src = src_pw.charAt(i);

			n_valid = 0;
			for (int j = 0; j < str_pw_set.length(); j++) {
				p_pw_set = str_pw_set.charAt(j);
				if (p_pw_set == p_src) {
					n_valid = 1;
					break;
				}
			}

			if (n_valid == 0) {
				errormsg.setLength(0);
				errormsg.append("invalid char in password text!\n");
				return false;
			}
		}

		return true;
	}

	/* 解密字符串，输出结果为解密后的字符串，若输入为非法密文，返回空，记录错误信息 */
	public StringBuffer decry(StringBuffer src, String privateKey) {
		// char tmpch[3]={0};
		// char tmpdesch[2]={0};

		int k = 0;
		if (!isvalid_pw_text(src))
			return new StringBuffer("");

		List<Byte> list = new ArrayList<Byte>();

		for (int i = 0; i < src.length(); i += 2) {
			String s = src.substring(i, i + 2);
			int hex = hexstrtodec(new StringBuffer(s));
			byte b = (byte) hex;
			b ^= privateKey.getBytes(default_charset)[k % privateKey.length()];

			if (b == 0)
				break;

			list.add(b);

			k++;
		}

		Byte[] bytes = (Byte[]) list.toArray(new Byte[list.size()]);
		byte[] bytes2 = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			bytes2[i] = bytes[i].byteValue();
		}
		String ss = new String(bytes2, default_charset);
		return new StringBuffer(ss);
	}

	/* 将十六进制字符串转换为十进制数 */
	int hexstrtodec(StringBuffer strhex) {
		int v = 0;
		int total = 0;
		int l = strhex.length();
		for (int i = 0; i < l; i++) {
			switch (strhex.charAt(i)) {
			case '0':
				v = 0;
				break;
			case '1':
				v = 1;
				break;
			case '2':
				v = 2;
				break;
			case '3':
				v = 3;
				break;
			case '4':
				v = 4;
				break;
			case '5':
				v = 5;
				break;
			case '6':
				v = 6;
				break;
			case '7':
				v = 7;
				break;
			case '8':
				v = 8;
				break;
			case '9':
				v = 9;
				break;
			case 'a':
				v = 10;
				break;
			case 'b':
				v = 11;
				break;
			case 'c':
				v = 12;
				break;
			case 'd':
				v = 13;
				break;
			case 'e':
				v = 14;
				break;
			case 'f':
				v = 15;
				break;
			}
			total = total * 16 + v;
		}
		return total;
	}

	public static final void main(String[] args) {
		// SimpleDESCry sc=new SimpleDESCry();
		// StringBuffer a=sc.cry(new
		// StringBuffer("QmFzZTY0IOWKoOWvhuino+Wvhg=="),"*bv_.azqadec;d7efbikop,01-fre382");
		// System.out.println(a);
		//
		// StringBuffer b=sc.decry(new
		// StringBuffer("7b0f302574352341282b3228542b60130e170005005b7b46594a5b4f"),"*bv_.azqadec;d7efbikop,01-fre382");
		// System.out.println(b);

		// System.out.println(encry("aaa888888"));
		// System.out.println(decry("777772626e74414d3230313446464353"));
	}

	public static String encry(String data) {
		SimpleDESCry sdc = new SimpleDESCry();
		String enData = sdc.cry(new StringBuffer(data), PRIVATE_KEY).toString();
		return enData;
	}

	public static String decry(String enData) {
		SimpleDESCry sdc = new SimpleDESCry();
		StringBuffer data = sdc.decry(new StringBuffer(enData), PRIVATE_KEY);
		return data.toString();
	}

}
