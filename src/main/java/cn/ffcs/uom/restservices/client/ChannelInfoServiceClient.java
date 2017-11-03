package cn.ffcs.uom.restservices.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.restservices.dao.SystemMessageBeforeDao;
import cn.ffcs.uom.restservices.model.SystemMessageBefore;
import cn.ffcs.uom.webservices.constants.WsConstants;

@Component("channelInfoServiceClient")
public class ChannelInfoServiceClient
{
//	private final String URL = "http://127.0.0.1:9007/uom-apps/restServices/channelInfoService/getRestServiceIsOn";
    //测试环境
    //private final String URL = "http://127.0.0.1:9001/uom-apps/restServices/channelInfoService/getRestServiceIsOn";
	private static String UOM_SYSTEM_CODE = "13000";
	
	@Resource
	private SystemMessageBeforeDao systemMessageBeforeDao;
	
	public void getRestServiceIsOnClient()
	{
		String URL = UomClassProvider.getIntfUrl(WsConstants.GROUP_CHANNEL_INFO_URL);
		//创建http返回接受对象
		HttpResponse httpResponse = null;
		//创建http请求客户端
		HttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter("http.socket.timeout", 30000);
		//发送post数据
		HttpPost post = new HttpPost(URL);
		//设置post参数
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("string", "测试数据"));
		String result = "";
		try
		{
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			//返回
			httpResponse = client.execute(post);
			
			result = EntityUtils.toString(httpResponse.getEntity());
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//判断result是否是1，如果是说明服务正常，否则服务不正常，需要短信通知
		if(!result.equals("1"))
		{
			//短信通知,往system_message_before插入一条数据
			SystemMessageBefore smb = new SystemMessageBefore();
			smb.setSystemCode(UOM_SYSTEM_CODE); //系统编码
			//设定短信内容
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			String time = sdf.format(new Date());
			smb.setSystemMessageInfo("【主数据系统】" + time + " rest服务拨测异常。"); //发送的信息内容
			
			//插入要发送短信的表
			systemMessageBeforeDao.saveObject(smb);
		}
	}
}
