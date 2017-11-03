package cn.ffcs.uom.webservices.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import cn.ffcs.uom.common.util.DesUtils;
import cn.ffcs.uom.common.util.JacksonUtil;
import cn.ffcs.uom.common.util.MD5;
import cn.ffcs.uom.webservices.bean.wechat.Msg;

/**
 * MD5 签名字符串
 * 
 * @ClassName: MD5Encryption
 * @Description: TODO
 * @author <a href="mailto:wcngs@qq.com">Swarz</a>
 * @date 2016年6月22日 上午9:24:32 * *
 */
public class WechatUtil {

    private static String _privateKey_1 = "EfTe91FP||__<ahdx>__||rEf5a9V";
    
    private static String _privateKey_2 = "xAhmTa**##<dmmnjiayan_EeeMAFG5893651286@mew!mmsd*ewef()sd~~~kk__33ionas#+_)323><asdel;asde][ase()9(@--_@eMAFG58";
    
    private static String _privateKey_3 = "nd#$%&*#$%^&^$%^#$EAMOMPGO4567$%^&*$%^&&&^NBVNKLMLKHKYCVKH%$^&*()^&GVCD^T&jojHIUJOJidojf94sdsdfdlf583405034523%";
    
    private static String _fixed = "E3";
    
    /**
     * @throws UnsupportedEncodingException 
     * @Title: 加密方法
     * @Description: TODO
     * @param @param _inpStr 输入串
     * @param @param msg JSON反序列化对象
     * @param @return 设定文件
     * @return String 返回类型
     * @author <a href="mailto:wcngs@qq.com">Swarz</a>
     * @throws
     */
    public static String encry(String _inpStr, Msg msg) throws UnsupportedEncodingException {
        String _md5 = MD5.getMD5String(_privateKey_1 + _inpStr + _privateKey_2);
        // /====================================II
        Integer _endTime = Integer.valueOf(msg.getTimeStr()
            .substring(msg.getTimeStr().length() - 1));
        if (_endTime == 0) {
            _endTime = 6;
        }
        // =====================================III
        String _minSign = _md5.substring(0, _md5.length() - _endTime);
        // =====================================IV
        // 进行再次的md5
        _md5 = MD5.getMD5String(_privateKey_2 + _minSign + _privateKey_1);
        // =====================================V
        // 对上一步的MD5字符串 截取前20个字符
        _md5 = _md5.substring(0, 20);
        // =====================================VI
        // 对秘要三进行md5加密， 并从第21 个字符串向后截取10个字符的字符串
        String _s = MD5.getMD5String(_privateKey_3);
        _s = _s.substring(20, 30);
        // =====================================VII
        _md5 = MD5.getMD5String(_md5 + _s + _fixed);
        return _md5;
    }
    
    /**
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     *             mvn install:install-file
     *             -Dfile=D:\Tools\Jars\json-lib-2.4-jdk15.jar
     *             -DgroupId=net.sf.json-lib -DartifactId=json-lib -Dversion=2.4
     *             -Dpackaging=jar
     * @Title: getObjFromJsonArrStr
     * @Description: TODO
     * @param @param source
     * @param @param beanClass
     * @param @return 设定文件
     * @return Object 返回类型
     * @author <a href="mailto:wcngs@qq.com">Swarz</a>
     * @throws
     */
    /*
     * public static Object getObjFromJsonArrStr(String source,Class<?>
     * beanClass){
     * JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(source);
     * return JSONObject.toBean(jsonObject,beanClass);
     * }
     */
    
    public static boolean checkSignature(Msg msg)
        throws JsonGenerationException, JsonMappingException, IOException {
        String sign1 = msg.getSign();
        msg.setSign("");
        String msgJson = JacksonUtil.Object2JSon(msg, true,"");
        String s = msg.getTransId() + msg.getStaffAccount() + msg.getStaffName() + msg.getTimeStr()
            + msg.getCardId();
        String sign2 = encry(s, msg);
        return sign1.equals(sign2);
    }
    
    public static Msg getDecryptMsg(Msg msg) throws Exception{
        DesUtils desUtils = new DesUtils("leemenz");
        msg.setCardId(desUtils.decrypt(msg.getCardId()));
        msg.setStaffAccount(desUtils.decrypt(msg.getStaffAccount()));
        return msg;
    }
}
