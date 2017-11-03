package cn.ffcs.uom.contrast.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.contrast.dao.ContrastDao;
import cn.ffcs.uom.contrast.manager.ContrastManager;
import cn.ffcs.uom.contrast.model.Contrast;
import cn.ffcs.uom.contrast.model.StaffContrast;
import cn.ffcs.uom.contrast.model.StaffContrastCrm;

/**
 * 员工对照表实惠类 .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-10-28
 * @功能说明：
 * 
 */
@Service("contrastManager")
@Scope("prototype")
public class ContrastManagerImpl implements ContrastManager {

	@Resource(name = "contrastDao")
	private ContrastDao contrastDao;

	/**
	 * 分页查询员工对照表信息
	 * 
	 * @param contrast
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByContrast(Contrast contrast, int currentPage,
			int pageSize) {
		return contrastDao.queryPageInfoByContrast(contrast, currentPage,
				pageSize);
	}

	/**
	 * 分页查询员工对照表信息
	 * 
	 * @param staffContrast
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffContrast(StaffContrast staffContrast,
			int currentPage, int pageSize) {
		return contrastDao.queryPageInfoByStaffContrast(staffContrast,
				currentPage, pageSize);
	}

	/**
	 * 分页查询CRM员工对照表信息
	 * 
	 * @param staffContrastCrm
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffContrastCrm(
			StaffContrastCrm staffContrastCrm, int currentPage, int pageSize) {
		return contrastDao.queryPageInfoByStaffContrastCrm(staffContrastCrm,
				currentPage, pageSize);
	}

}
