package cn.ffcs.uom.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

public class HttpUtil {

	static String defaultEncoding = "GBK";

	public static String httpPost(String intfUrl, String msgContent) {
		return httpPost(intfUrl, msgContent, null);
	}

	public static String httpPost(String intfUrl, String msgContent,
			String encoding) {
		Logger logger = Logger.getLogger(HttpUtil.class);
		try {
			if (StrUtil.isEmpty(encoding)) {
				encoding = defaultEncoding;
			}
			URL url = new URL(intfUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			// 设置参数
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(5000);
			uc.setRequestMethod("POST");
			uc.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			uc.setRequestProperty("Content-Length",
					String.valueOf(msgContent.length()));
			// 发送
			OutputStreamWriter out = new OutputStreamWriter(
					uc.getOutputStream(), encoding);
			out.write(msgContent);
			out.flush();
			out.close();
			// 返回结果
			InputStreamReader inputReader = new InputStreamReader(
					uc.getInputStream(), encoding);
			BufferedReader reader = new BufferedReader(inputReader);
			reader.mark(uc.getContentLength() + 1);
			StringBuffer sInputXml = new StringBuffer();
			String sLine = null;
			while ((sLine = reader.readLine()) != null) {
				sInputXml.append(sLine);
			}
			return sInputXml.toString();
		} catch (MalformedURLException e) {
			logger.info("create url error");
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("open url error");
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("other error");
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String intfUrl = "http://134.128.152.225:7071/tr8006322/sv8006322?svid=800632201&data_pool=jdbc/IBSDB";
		// String
		// msgContent="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Query_Balance_Detail_Imp><area_Code>0591</area_Code><acc_Nbr>18906916266</acc_Nbr><mdse_Type>3</mdse_Type><start_Time>201304</start_Time><end_Time>201304</end_Time></Query_Balance_Detail_Imp>";
		String msgContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?><sv800632201><inparam><areacode>0591</areacode><accnbr>18905001999</accnbr><mdsetype>3</mdsetype><billingcycleid>201304</billingcycleid></inparam></sv800632201>";
	}
}
