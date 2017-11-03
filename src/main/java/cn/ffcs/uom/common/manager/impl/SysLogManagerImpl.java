package cn.ffcs.uom.common.manager.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Executions;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.dao.SysLogDao;
import cn.ffcs.uom.common.manager.SysLogManager;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staff.dao.StaffAccountDao;
import cn.ffcs.uom.staff.dao.StaffDao;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

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
@Service("sysLogManager")
@Scope("prototype")
public class SysLogManagerImpl implements SysLogManager {
    
    @Resource
    private SysLogDao sysLogDao;
    @Resource
    private StaffDao staffDao;
    @Resource
    private StaffAccountDao staffAccountDao;
    
    @Override
    public void saveLog(SysLog sysLog) {
        /*
         * 保存日志信息
         * 
         * 1、保存到数据库的时候根据序列生成id
         * 2、这里生成流水号，
         * 3、检测消息类似是否为空，如果为空，标记失败
         * 4、检测开始时间，结束时间，总时间是否为空，如果是，标记日志记录失败,计算总耗时
         * 5、在标记失败的时候根据失败编码和原因记录失败信息
         */
        //自动生成流水,需要当前操作的用户账号，以及场景编码
        //业务场景不能为空
        if(StrUtil.isEmpty(sysLog.getMsgType()))
        {
            return;
        }
        
        //耗时总时间
        long consumeTime = -1;
        
        if(sysLog.getBeginDate() != null && sysLog.getEndDate() != null)
        {
            //计算出时间差
            consumeTime = sysLog.getEndDate().getTime() - sysLog.getBeginDate().getTime();
        }
        
        //如果是超级管理员操作是不会有工号的
        User user = sysLog.getUser();
        if(user == null)
        {
            //未知操作对象,如果得不到工号，就是日志失败，未知操作人员
            //根据staff_id得到工号,如果得不到工号，就是日志失败，未知操作人员
            sysLog.setTransId(SysLog.gennerateBatchNumber("unKown", sysLog.getMsgType()));
            sysLog.setResult(SysLogConstrants.RESULT_FAIL);
            sysLog.setErrCode(SysLogConstrants.FAIL_CODE_UNKNOW_USER);
            sysLog.setErrMsg(SysLogConstrants.FAIL_CODE_UNKOW_USER_STR);
            sysLog.setLogLevel(SysLogConstrants.ERROR);
            //保存日志
            sysLogDao.addObject(sysLog);
            return;
        }
        
        //根据staff_id获取staffAccount
        //这里使用UUID来查询
        Staff staff = new Staff();
        staff.setUuid(user.getUuid());
        StaffAccount staffAccount = staffAccountDao.queryStaffAccountByStaffUuid(staff);
        if(staffAccount == null)
        {
            //主数据不存在这个员工,有可能有些员工进来了，但是主数据却没有相对应的数据，这个时候就要小心了
            sysLog.setTransId(SysLog.gennerateBatchNumber("unKown", sysLog.getMsgType()));
            sysLog.setResult(SysLogConstrants.RESULT_FAIL);
            sysLog.setErrCode(SysLogConstrants.FAIL_CODE_UNKNOW_STAFF);
            sysLog.setErrMsg(SysLogConstrants.FAIL_CODE_UNKNOW_STAFF_STR + user.getUserId());
            sysLog.setLogLevel(SysLogConstrants.ERROR);
            //保存日志
            sysLogDao.addObject(sysLog);
            return;
        }
        
        if("Admin".equals(user.getFirstName()))
        {
            //msgtype消息类型就是要进入日志的几个模块编码
            sysLog.setTransId(SysLog.gennerateBatchNumber("admin", sysLog.getMsgType()));
            sysLog.setStaffAccount("admin");
        }
        else
        {
            //得到当前用户,这个user的id就是staff_id,通过前台获取
//            User user = PlatformUtil.getCurrentUser(),这个方法并不靠谱，在LogService中进行获取
            //得到员工的id号
            sysLog.setTransId(SysLog.gennerateBatchNumber(staffAccount.getStaffAccount(), sysLog.getMsgType()));
            sysLog.setStaffAccount(staffAccount.getStaffAccount());
        }
        
        //保存日志
        sysLogDao.addObject(sysLog);
    }
}
