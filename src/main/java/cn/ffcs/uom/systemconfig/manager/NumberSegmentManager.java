package cn.ffcs.uom.systemconfig.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.model.NumberSegment;

public interface NumberSegmentManager {
	/**
	 * 分页取类信息
	 * 
	 * @param queryNumberSegment
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByQuertNumberSegment(NumberSegment queryNumberSegment,
			int currentPage, int pageSize);
	
	/**
	 * 获取所有生效的号码段
	 * @return
	 */
	public List<NumberSegment> findAllByActive();
	/**
	 * 删除记录
	 * 
	 * @param numberSegment
	 */
	public void removeNumberSegment(NumberSegment numberSegment);

	/**
	 * 更新记录
	 * 
	 * @param numberSegment
	 */
	public void updateNumberSegment(NumberSegment numberSegment);

	/**
	 * 保存记录
	 * 
	 * @param numberSegment
	 */
	public void saveNumberSegment(NumberSegment numberSegment);

}
