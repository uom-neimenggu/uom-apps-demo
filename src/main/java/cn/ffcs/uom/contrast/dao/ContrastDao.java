/**
 * 
 */
package cn.ffcs.uom.contrast.dao;

import cn.ffcs.uom.common.dao.BaseDao;
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
public interface ContrastDao extends BaseDao {

	/**
	 * 
	 * @param Contrast
	 * @return
	 */
	public PageInfo queryPageInfoByContrast(Contrast contrast, int currentPage,
			int pageSize);

	/**
	 * 
	 * @param staffContrastint
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffContrast(
			StaffContrast staffContrastint, int currentPage, int pageSize);

	/**
	 * 分页查询CRM员工对照表信息
	 * 
	 * @param staffContrastCrm
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffContrastCrm(
			StaffContrastCrm staffContrastCrm, int currentPage, int pageSize);
}
