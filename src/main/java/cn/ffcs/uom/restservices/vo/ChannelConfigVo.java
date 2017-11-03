package cn.ffcs.uom.restservices.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置开关以及url的vo类
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年12月14日
 * @功能说明：
 *
 */
public class ChannelConfigVo {
    //// 集团渠道信息入主数据库开关
    private boolean channelInfoInterFaceSwitch;
    
    //// 省渠道信息通知开关
    private boolean channelInfoNoticeInterFaceSwitch;
    
    //oipServiceCodeChannelInfo
    private String oipServiceCode;
    
    private String oipHttpUrl;

    private String javaCode;
    private String attrValues;
    public String getJavaCode() {
        return javaCode;
    }
    public void setJavaCode(String javaCode) {
        this.javaCode = javaCode;
    }
    public String getAttrValues() {
        return attrValues;
    }
    public void setAttrValues(String attrValues) {
        this.attrValues = attrValues;
    }
    public boolean isChannelInfoInterFaceSwitch() {
        return channelInfoInterFaceSwitch;
    }
    public void setChannelInfoInterFaceSwitch(boolean channelInfoInterFaceSwitch) {
        this.channelInfoInterFaceSwitch = channelInfoInterFaceSwitch;
    }
    public boolean isChannelInfoNoticeInterFaceSwitch() {
        return channelInfoNoticeInterFaceSwitch;
    }
    public void setChannelInfoNoticeInterFaceSwitch(boolean channelInfoNoticeInterFaceSwitch) {
        this.channelInfoNoticeInterFaceSwitch = channelInfoNoticeInterFaceSwitch;
    }
    public String getOipServiceCode() {
        return oipServiceCode;
    }
    public void setOipServiceCode(String oipServiceCode) {
        this.oipServiceCode = oipServiceCode;
    }
    public String getOipHttpUrl() {
        return oipHttpUrl;
    }
    public void setOipHttpUrl(String oipHttpUrl) {
        this.oipHttpUrl = oipHttpUrl;
    }
    
    /**
     * 吧数据库中查到的数据添加到变量中
     * .
     * 
     * @param lists
     * @author xiaof
     * 2016年12月14日 xiaof
     */
    public void bindValues(List<ChannelConfigVo> lists)
    {
        Map<String, String> map = new HashMap();
        for(ChannelConfigVo channelConfigVo : lists)
        {
            String temp[] = channelConfigVo.getAttrValues().split(",");
            for(String s : temp)
            {
                String sss[] = s.split("-");
                map.put(sss[0], sss[1]);
            }
        }
//        System.out.println(map.get("channelInfoNoticeInterFaceSwitch"));
//        System.out.println(map.get("channelInfoInterFaceSwitch"));
//        System.out.println(map.get("oipRestUrlChannelInfo"));
//        System.out.println(map.get("oipServiceCodeChannelInfo"));
        //1是开启，2是关闭
        this.channelInfoInterFaceSwitch = map.get("channelInfoInterFaceSwitch").equals("1") ? true : false;
        this.channelInfoNoticeInterFaceSwitch = map.get("channelInfoNoticeInterFaceSwitch").equals("1") ? true : false;
        this.oipHttpUrl = map.get("oipRestUrlChannelInfo");
        this.oipServiceCode = map.get("oipServiceCodeChannelInfo");
    }
}
