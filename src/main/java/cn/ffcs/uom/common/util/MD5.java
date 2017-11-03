package cn.ffcs.uom.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import cn.ffcs.uom.webservices.util.WechatUtil;

public class MD5 {
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	protected static MessageDigest messageDigest = null;
	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(MD5.class.getName()+"初始化失败，MessageDigest不支持MD5!");
			nsaex.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		long begin = System.currentTimeMillis();

		File big = new File("文件绝对路径");
	    String md5 = getFileMD5String(big);
		//String md5 = getMD5String("a");
		long end = System.currentTimeMillis();
		System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000) + "s");
	}

	public static String getFileMD5(File file){
		DigestInputStream digestInputStream = null;
		FileInputStream in  = null;
		try {
			in = new FileInputStream(file);
			digestInputStream = new DigestInputStream(in,messageDigest);
			int bufferSize = 256 * 1024;
			byte[] buffer =new byte[bufferSize];
			while (digestInputStream.read(buffer) > 0);
			// 获取最终的MessageDigest
			messageDigest= digestInputStream.getMessageDigest();
			// 拿到结果，也是字节数组，包含16个元素
			byte[] resultByteArray = messageDigest.digest();
			// 同样，把字节数组转换成字符串
			return byteArrayToHex(resultByteArray);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block e.printStackTrace();
			return null;
		} catch (IOException e) {
			return null;
			// TODO Auto-generated catch block e.printStackTrace();
		} finally {
	         try {
	             digestInputStream.close();
	          } catch (Exception e) {
	          }
	          try {
	             in.close();
	          } catch (Exception e) {
	          }
	       }
		
	}
	
	 public static String byteArrayToHex(byte[] byteArray) {
	        String hs = "";   
	        String stmp = "";   
	        for (int n = 0; n < byteArray.length; n++) {   
	            stmp = (Integer.toHexString(byteArray[n] & 0XFF));   
	            if (stmp.length() == 1) {   
	                hs = hs + "0" + stmp;   
	            } else {   
	                hs = hs + stmp;   
	            }   
	            if (n < byteArray.length - 1) {   
	                hs = hs + "";   
	            }   
	        }   
	        // return hs.toUpperCase();   
	        return hs;

	      // 首先初始化一个字符数组，用来存放每个16进制字符

	      /*char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };

	 

	      // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

	      char[] resultCharArray =new char[byteArray.length * 2];

	      // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

	      int index = 0;

	      for (byte b : byteArray) {

	         resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];

	         resultCharArray[index++] = hexDigits[b& 0xf];

	      }

	      // 字符数组组合成字符串返回

	      return new String(resultCharArray);*/

	}
	
	public static String getFileMD5String(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		
		//700000000 bytes are about 670M
		long maxSize=700000000L;
		
		long startPosition=0L;
		long step=file.length()/maxSize;
		
		
		if(in!=null){
			in.close();
		}
		if(step == 0){
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,file.length());
			messageDigest.update(byteBuffer);
			return bufferToHex(messageDigest.digest());
		}
		
		for(int i=0;i<step;i++){
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, startPosition,maxSize);
			messageDigest.update(byteBuffer);
			startPosition+=maxSize;
		}
		
		if(startPosition==file.length()){
			return bufferToHex(messageDigest.digest());
		}

		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, startPosition,file.length()-startPosition);
		messageDigest.update(byteBuffer);

			
		return bufferToHex(messageDigest.digest());
	}

	public static String getMD5String(String s) throws UnsupportedEncodingException {
		return getMD5String(s.getBytes("UTF-8"));
	}

	public static String getMD5String(byte[] bytes) {
		messageDigest.update(bytes);
		return bufferToHex(messageDigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr) throws UnsupportedEncodingException {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}
	
	
}

