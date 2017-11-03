package cn.ffcs.uac.staff.manager;

import cn.ffcs.uac.staff.constant.EnumUacStaffInfo;
import cn.ffcs.uac.staff.model.UacStaff;
import cn.ffcs.uac.staff.vo.DemoStaff;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.model.Group;

/**
 * @Title:UacStaffManager
 * @Description:
 * @版权:福富软件（C）2017
 * @author Wyr
 * @date 2017年9月1日上午9:41:22
 */
public interface UacStaffManager {
	
	public void saveRemindInfo(String msg);
	/**
	 * addUacStaffAllInfo
	 * 
	 * @param uacStaff
	 * @return String
	 */
	public String addUacStaffAllInfo(UacStaff uacStaff);

	/**
	 * delUacStaffAllInfo
	 * 
	 * @param uacStaff
	 * @return EnumUacStaffInfo
	 */
	public EnumUacStaffInfo delUacStaffAllInfo(UacStaff uacStaff);

	/**
	 * updateUacStaffAllInfo
	 * 
	 * @param uacStaff
	 */
	public void updateUacStaffAllInfo(UacStaff uacStaff);

	/**
	 * queryUacStaffPage
	 * 
	 * @param uacStaff
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryUacStaffPage(UacStaff uacStaff, int currentPage,
			int pageSize);
	
	public PageInfo queryDemoStaffPage(DemoStaff demoStaff, int currentPage,
			int pageSize);

	/**
	 * 自动生成人员编码
	 * 
	 * @return
	 */
	public String generateEcode();

	/**
	 * 内部使用方法生成AccNumber
	 * 
	 * @param property
	 * @return
	 */
	public String genneratePropertyAccNumber(String property);

	/**
	 * 查询Group对象
	 * 
	 * @param staffName
	 * @param certNumber
	 * @return
	 */
	public Group getGroup(String staffName, String certNumber);

	/**
	 * 根据人员姓名，性质以及身份证号码生成账号
	 * 
	 * @param staffName
	 * @param property
	 * @param certNumber
	 * @return EnumUacStaffInfo(TRUE(value) FALSE(value))
	 */
	public EnumUacStaffInfo generateAccount(String staffName, String property,
			String certNumber);
}
