package cn.ffcs.uac.staff.dao;

import java.util.List;

import cn.ffcs.uac.staff.model.UacStaff;
import cn.ffcs.uac.staff.vo.DemoStaff;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
/**
 * 
 * @Title:UacStaffDao
 * @Description:
 * @版权:福富软件（C）2017 
 * @author Wyr
 * @date 2017年9月1日上午9:49:38
 */
public interface UacStaffDao extends BaseDao {
	/**
	 * 
	 * @param uacStaff
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryUacStaffPage(UacStaff uacStaff, int currentPage, int pageSize);
	
	public PageInfo queryDemoStaffPage(DemoStaff demoStaff, int currentPage, int pageSize);

	public String getSeqEcode();

	public List<UacStaff> queryUacStaffList(UacStaff uacStaff);

	public String getSeqStaffNbr();

}
