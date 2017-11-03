package cn.ffcs.uom.contrast.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.contrast.model.Contrast;
import cn.ffcs.uom.contrast.model.StaffContrast;
import cn.ffcs.uom.contrast.model.StaffContrastCrm;

/**
 * 员工对照表接口 .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-10-28
 * @功能说明：
 * 
 */
public interface ContrastManager {

	/**
	 * 分页查询员工对照表信息
	 * 
	 * @param contrast
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByContrast(Contrast contrast, int currentPage,
			int pageSize);

	/**
	 * 分页查询员工对照表信息
	 * 
	 * @param queryStaffContrast
	 * @param i
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffContrast(StaffContrast staffContrast,
			int currentPage, int pageSize);
	/**
	 * 分页查询CRM员工对照表信息
	 * 
	 * @param staffContrastCrm
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffContrastCrm(StaffContrastCrm staffContrastCrm,
			int currentPage, int pageSize);

}
