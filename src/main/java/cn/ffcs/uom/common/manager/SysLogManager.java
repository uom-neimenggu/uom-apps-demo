package cn.ffcs.uom.common.manager;

import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.uom.common.model.SysLog;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年11月7日
 * @功能说明：
 *
 */
public interface SysLogManager {
    /**
     * 保存日志
     * .
     * 
     * @param msg   具体操作内容
     * @param sceneCode 操作场景编码
     * @param operatorObject 操作业务对象
     * @param operatorType  操作类型
     * @author xiaof
     * 2016年10月27日 xiaof
     */
    public void saveLog(SysLog sysLog);
}
