package cn.ffcs.uom.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.model.GroupMailBusinessParam;
import cn.ffcs.uom.mail.model.GroupMailRootInParam;
import cn.ffcs.uom.mail.model.GroupMailRootOutParam;
import cn.ffcs.uom.restservices.model.OipError;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TripleDESUtil {

	public static final String APPLICATION_JSON = "application/json";
	public static final String ENCODING_GBK = "GBK";

	// 定义加密算法,可用DES,DESede,Blowfish
	private static final String ALGORITHM = "DESede";

	// keyByte为加密密钥,长度为24字节,src为被加密的数据缓冲区（源）[加密]
	public static byte[] encryptMode(byte[] keyByte, byte[] src) {
		try {

			// 生成密钥
			SecretKey desKey = new SecretKeySpec(keyByte, ALGORITHM);

			// 加密
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, desKey);

			return cipher.doFinal(src);

		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}

		return null;

	}

	// keyByte为加密密钥,长度为24字节,src为加密后的缓冲区[解密]
	public static byte[] decryptMode(byte[] keyByte, byte[] src) {
		try {

			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keyByte, ALGORITHM);

			// 解密
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, deskey);

			return cipher.doFinal(src);

		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}

		return null;

	}

	/*
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src byte[] data
	 * 
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder("");

		if (src == null || src.length <= 0) {
			return null;
		}

		for (int i = 0; i < src.length; i++) {

			int v = src[i] & 0xFF;

			String hv = Integer.toHexString(v);

			if (hv.length() < 2) {

				stringBuilder.append(0);

			}

			stringBuilder.append(hv);

		}

		return stringBuilder.toString();

	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {

		if (hexString == null || hexString.equals("")) {

			return null;

		}

		hexString = hexString.toUpperCase();

		int length = hexString.length() / 2;

		char[] hexChars = hexString.toCharArray();

		byte[] dst = new byte[length];

		for (int i = 0; i < length; i++) {

			int pos = i * 2;

			dst[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}

		return dst;

	}

	/**
	 * 转换byte为Hex字符串
	 * 
	 * @param value
	 *            要转换的byte数据
	 * @param minlength
	 *            生成hex的最小长度（长度不足时会在前面加0）
	 */
	public static String byteToHex(byte src, int length) {

		String dst = Integer.toHexString(src & 0xFF);

		if (dst.length() < length) {

			for (int i = 0; i < (length - dst.length()); i++) {

				dst = "0" + dst;

			}
		}

		return dst;

	}

	/**
	 * MD5加密字符串
	 */
	public static byte[] MD5(byte[] src) {

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(src);

			return md.digest();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {

		return (byte) "0123456789ABCDEF".indexOf(c);

	}

	/**
	 * 得到当前的时间戳
	 * 
	 * @return
	 */
	public static String getTimeStamp() {
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmss");
		return simple.format(new Date());
	}

	public static void main(String[] args) {

		Logger logger = Logger.getLogger(TripleDESUtil.class);

		String json = null;
		String encryJson = null;
		String msgId = EsbHeadUtil.getOipHttpJsonMsgId(EsbHeadUtil.FTP_SENDER);

		GroupMailRootInParam rootIn = new GroupMailRootInParam();
		rootIn.setSysId(GroupMailConstant.GROUP_MAIL_SYS_ID);

		GroupMailBusinessParam biz = new GroupMailBusinessParam();
		biz.setSysId(GroupMailConstant.GROUP_MAIL_SYS_ID);
		biz.setBizId(GroupMailConstant.GROUP_MAIL_BIZ_ID_0);
		biz.setAppKey(GroupMailConstant.GROUP_MAIL_APP_KEY);

		// String oipServiceCode = UomClassProvider
		// .getIntfUrl("oipServiceCodeGroupMail");
		//
		// if (StrUtil.isNullOrEmpty(oipServiceCode)) {
		// logger.info("集团统一邮箱OIP服务编码没有配置");
		// }
		//
		// String oipHttpUrl =
		// UomClassProvider.getIntfUrl("oipRestUrlGroupMail");
		//
		// if (StrUtil.isNullOrEmpty(oipHttpUrl)) {
		// logger.info("集团统一邮箱接口地址没有配置");
		// }

		try {
			json = JacksonUtil.Object2JSon(biz);
		} catch (JsonGenerationException e) {
			logger.info(e.getMessage());
		} catch (JsonMappingException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}

		try {
			logger.info("查询时间戳====》》" + json);
			rootIn.setAppSignature(TripleDESUtil.bytesToHexString(TripleDESUtil
					.encryptMode(
							GroupMailConstant.GROUP_MAIL_ENCODE_KEY.getBytes(),
							json.getBytes(HTTP.UTF_8))));
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}

		try {
			encryJson = JacksonUtil.Object2JSon(rootIn);
			logger.info("加密后的JSON===》》" + encryJson);
		} catch (JsonGenerationException e) {
			logger.info(e.getMessage());
		} catch (JsonMappingException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}

		Client client = Client.create();

		client.setConnectTimeout(30 * 1000);

		WebResource webResource = client
				.resource("http://mail.chinatelecom.cn/mailinterface/inter");

		@SuppressWarnings("rawtypes")
		MultivaluedMap queryParams = new MultivaluedMapImpl();
		queryParams.add("isRest", "true");
		queryParams.add("sender", EsbHeadUtil.FTP_SENDER);
		// queryParams.add("servCode", oipServiceCode);
		queryParams.add("msgId", msgId);
		queryParams.add("transactionId", msgId);
		queryParams.add("sysId", rootIn.getSysId());
		queryParams.add("appSignature", rootIn.getAppSignature());

		ClientResponse response = webResource.queryParams(queryParams)
				.entity(encryJson, MediaType.APPLICATION_JSON)
				.post(ClientResponse.class);

		String entity = response.getEntity(String.class);

		logger.info(entity);

		if (entity != null) {

			if (new JsonValidator().validate(entity)) {// 验证返回的字符串是不是Json格式
				try {
					if (entity.contains("errorCode")
							&& entity.contains("errorDesc")) {
						OipError oipError = (OipError) JacksonUtil.JSon2Object(
								entity, OipError.class);
						logger.info(oipError.getErrorCode());
						logger.info(oipError.getErrorDesc());
						response.close();
						client.destroy();
					} else {

						GroupMailRootOutParam rootOut = (GroupMailRootOutParam) JacksonUtil
								.JSon2Object(entity,
										GroupMailRootOutParam.class);

						if (rootOut != null) {
							if (rootOut.isRet()) {
								response.close();
								client.destroy();
								logger.info("时间戳=====》》》"
										+ rootOut.getTimeStamp());
							} else {
								response.close();
								client.destroy();
								logger.info(rootOut.getErrorMsg());
							}
						} else {
							response.close();
							client.destroy();
							logger.info("集团统一邮箱返回的应答信息为空");
						}

					}

				} catch (JsonGenerationException e) {
					logger.info(e.getMessage());
				} catch (JsonMappingException e) {
					logger.info(e.getMessage());
				} catch (IOException e) {
					logger.info(e.getMessage());
				}

			} else {
				logger.info("验证返回的字符串不是Json格式");
			}
		}
	}
}