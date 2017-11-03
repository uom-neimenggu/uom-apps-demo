package cn.ffcs.uom.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.party.manager.PartyManager;

/**根据className 表SYS_CLASS 的Java_Code
 *  attrName  表ATTR_SPEC 的Java_code
 * @版权：福富软件 版权所有 (c) 2011
 * @author wangyong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-15
 * @Email wangyong@ffcs.cn
 * @功能说明：
 *
 */
public class StaticParameter {
    
    public Map<String,Map<String,List<Map<String,String>>>> spams = new HashMap<String,Map<String,List<Map<String,String>>>>();
    
    private PartyManager  partyManager = (PartyManager) ApplicationContextUtil.getBean("partyManager");

    /**
     * 得到AttrValue和attrValueName
     * .
     * @param className 表SYS_CLASS 的Java_Code
     * @param attrName  表ATTR_SPEC 的Java_code
     * @author wangyong
     * 2013-6-15 wangyong
     */
    public List<Map<String,String>> getParams(String className, String attrName){
        Map<String,List<Map<String,String>>> suMps = spams.get(className);
        List<Map<String,String>> _twoMap = null;
        if(null!=suMps){
            _twoMap =  suMps.get(attrName);
            if(null==_twoMap){
                _twoMap = partyManager.getListForAttrValue(className, attrName);
                if(null!=_twoMap){
                    suMps.put(attrName, _twoMap);
                }
            }
        } else {
            suMps = new HashMap<String,List<Map<String,String>>>();
            _twoMap = partyManager.getListForAttrValue(className, attrName);
            if(null==_twoMap){
                suMps.put(attrName, _twoMap);
                spams.put(className, suMps);
            }
        }
        return _twoMap;
    }
    
    /**
     * 得到AttrValue和attrValueName的Map键值对map list
     * .
     * 返回以AttrValue为Key的Map
     * @param className 表SYS_CLASS 的Java_Code
     * @param attrName  表ATTR_SPEC 的Java_code
     * @param attrVal   以attrValue为key的值
     * @return
     * @author wangyong
     * 2013-6-15 wangyong
     */
    public Map<String,String> getAttrValue(String... objs){
        List<Map<String,String>> lis = getParams(objs[0], objs[1]);
        for(Map<String,String> m:lis){
            for(String key:m.keySet()){
                if(key.equals(objs[2])){
                    return m;
                }
            }
        }
        return null;
    }
    
    /**获取AttrValueName
     * @param 1 className 表SYS_CLASS 的Java_Code
     * @param 2 attrName  表ATTR_SPEC 的Java_code
     * @param 3 attrVal   以attrValue为key的值
     * @return
     * @author wangyong
     * 2013-6-15 wangyong
     */
    public String handling(String... orgs){
        Map<String,String> mp = getAttrValue(new String[]{orgs[0],orgs[1],orgs[2]});
        if(null!=mp){
            return mp.get(orgs[2]);
        }
        return null;
    }
}
