package cn.ffcs.uom.staff.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

public interface StaffAccountDao extends BaseDao {
    /**
     * 根据员工id获取员工账号
     * .
     * 一个员工一个账号
     * @return
     * @author xiaof
     * 2016年11月7日 xiaof
     */
    public StaffAccount queryStaffAccountByStaffUuid(Staff staff);
}
