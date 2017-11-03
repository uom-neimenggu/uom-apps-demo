package cn.ffcs.uom.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import cn.ffcs.uom.mail.model.GroupMailRootOutParam;
import cn.ffcs.uom.restservices.model.OipError;
import cn.ffcs.uom.restservices.util.UserTest;

public class JacksonUtil {
    
    public static final String APPLICATION_JSON = "application/json";
    
    public static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    
    /**
     * 把Object对象转换为json数据,以字符串的形式返回.
     * 
     * @param obj
     * @return 字符串json
     */
    public static String Object2JSon(Object obj)
        throws JsonGenerationException, JsonMappingException, IOException {
        
        Logger logger = Logger.getLogger(JacksonUtil.class);
        
        String json;
        
        if (obj == null) {
            
            json = "{}";
            
        } else {
            
            try {
                
                ObjectMapper mapper = new ObjectMapper();
                
                // 控制格式化输出
                mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
                
                // 设置日期格式
                mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                
                // 是否环绕根元素
                mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
                
                // 设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
                mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
                
                // 设置将MAP转换为JSON时候只转换值不等于NULL的
                mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
                
                json = mapper.writeValueAsString(obj);
                
                json = json.startsWith("[") ? json.substring(1)
                    : json;
                
                json = json.endsWith("]") ? json.substring(0, json.length() - 1)
                    : json;
                
            } catch (Exception ex) {
                
                json = "{}";
                
                logger.info(ex.getMessage());
                
            }
            
        }
        
        return json;
        
    }
    
    public static String Object2JSon(Object obj,boolean isShowNull,String format)
        throws JsonGenerationException, JsonMappingException, IOException {
        
        Logger logger = Logger.getLogger(JacksonUtil.class);
        
        String json;
        
        if (obj == null) {
            
            json = "{}";
            
        } else {
            
            try {
                
                ObjectMapper mapper = new ObjectMapper();
                
                // 控制格式化输出
                mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
                
                // 设置日期格式
                if (!StrUtil.isEmpty(format)) {
                    mapper.setDateFormat(new SimpleDateFormat(format));
                }
                //mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                
                // 是否环绕根元素
                mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
                
                // 设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
                
                if (!isShowNull) {
                    mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
                }
                // 设置将MAP转换为JSON时候只转换值不等于NULL的
                mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
                
                json = mapper.writeValueAsString(obj);
                
                json = json.startsWith("[") ? json.substring(1)
                    : json;
                
                json = json.endsWith("]") ? json.substring(0, json.length() - 1)
                    : json;
                
            } catch (Exception ex) {
                
                json = "{}";
                
                logger.info(ex.getMessage());
                
            }
            
        }
        
        return json;
        
    }
    
    /**
     * 把json数据转换为Object对象，以对象的形式返回
     * 
     * @param json
     * @return Object对象
     */
    public static Object JSon2Object(String json, Class clazz)
        throws JsonGenerationException, JsonMappingException, IOException {
        
        Logger logger = Logger.getLogger(JacksonUtil.class);
        
        Object obj;
        
        if (json == null) {
            
            obj = null;
            
        } else {
            
            try {
                
                ObjectMapper mapper = new ObjectMapper();
                
                // 控制格式化输出
                mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
                // 设置日期格式
                mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                obj = mapper.readValue(json, clazz);
                
            } catch (Exception ex) {
                
                obj = null;
                
                logger.info(ex.getMessage());
                
            }
        }
        
        return obj;
        
    }
    
    /**
     * HTTPPost发送JSON：
     * 
     * @param url
     * @param json
     * @throws Exception
     */
    public static HttpResponse httpPostRequest(String url, String json) {
        
        String encoderJson = null;
        HttpResponse response = null;
        DefaultHttpClient httpClient = null;
        
        try {
            
            // 将JSON进行UTF-8编码,以便传输中文
            // encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
            encoderJson = json;
            
            httpClient = new DefaultHttpClient();
            
            // 请求连接时间5s
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 1000);
            
            // 数据传输时间10s
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);
            
            StringEntity se;
            se = new StringEntity(encoderJson);
            se.setContentEncoding(HTTP.UTF_8);
            se.setContentType(APPLICATION_JSON);
            // se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
            // APPLICATION_JSON));
            
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
            httpPost.setEntity(se);
            
            response = httpClient.execute(httpPost);
            
            // 释放连接资源
            // httpClient.getConnectionManager().shutdown();
            
        } catch (UnsupportedEncodingException e) {
            
            // 释放连接资源
            httpClient.getConnectionManager().shutdown();
            e.printStackTrace();
            return response;
        } catch (ClientProtocolException e) {
            // 释放连接资源
            httpClient.getConnectionManager().shutdown();
            e.printStackTrace();
            return response;
        } catch (IOException e) {
            // 释放连接资源
            httpClient.getConnectionManager().shutdown();
            e.printStackTrace();
            return response;
        }
        
        return response;
    }
    
    /**
     * 处理服务器返回的响应
     * 
     * @param url
     * @param json
     * @throws Exception
     */
    public static String httpPostResponse(HttpResponse response) {
        
        String responseBody = null;
        
        if (!StrUtil.isNullOrEmpty(response)) {
            
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                
                try {
                    
                    responseBody = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    
                    responseBody = URLDecoder.decode(responseBody, HTTP.UTF_8);
                    
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return responseBody;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return responseBody;
                }
                
            }
            
        }
        
        return responseBody;
        
    }
    
    /**
     * 接收HTTPPost中的JSON
     * 
     * @param request
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static String hpptPostReceive(HttpServletRequest request)
        throws IOException, UnsupportedEncodingException {
        
        String requestBody = null;
        
        // 读取请求内容
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        
        // 将资料解码
        requestBody = sb.toString();
        
        requestBody = URLDecoder.decode(requestBody, HTTP.UTF_8);
        
        return requestBody;
        
    }
    
    public static void main(String[] args) {
        
        try {
            
            String entity = "{\"ret\":\"true\",\"user\":{\"account\":\"guanjiaxin.ah@oamailtest.com\",\"accountInfo\""
                + ":\"\",\"addInRootWhenNotDepartMent\":\"false\",\"address\":\"\",\"alias\":\"\",\"birthday\""
                + ":\"2015-11-16 00:00:00\",\"c_name\":\"管家鑫\",\"cityId\":\"河北省\",\"compay_phone\":\"\",\""
                + "confirmPassword\":\"\",\"gender\":\"1\",\"groupids\":\"\",\"hrCode\":\"\",\"invisible\":\"\","
                + "\"ip\":\"\",\"mailBoxSize\":\"4096\",\"mobile_phone\":\"\",\"modifyPartMentName\":\"\","
                + "\"msg\":\"\",\"occupation\":\"\",\"oprationFlag\":\"1073742168\",\"partMent\":\"\","
                + "\"partMentDesc\":\"\",\"partMentNum\":\"\",\"password\":\"\",\"positionNumber\":\"\","
                + "\"proviceId\":\"\",\"recommend\":[\"1\",\"2\"],\"retCode\":0,\"shownum\":\"\",\"status\":\"0\"},"
                + "\"msg\":\"成功！\"}";
            
            GroupMailRootOutParam rootOut = (GroupMailRootOutParam) JacksonUtil.JSon2Object(entity,
                GroupMailRootOutParam.class);
            
            System.out.println(getValue(entity, "ret"));
            
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static String getValue(String json, String nodeName)
        throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        return node.get(nodeName).toString();
    }
}
