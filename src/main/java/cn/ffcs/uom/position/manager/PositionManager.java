/**
 * 
 */
package cn.ffcs.uom.position.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.position.model.Position;

/**
 * @author yahui
 * 
 */
public interface PositionManager {
	/**
	 * 分页取类信息
	 * 
	 * @param queryPosition
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByPositionId(Organization queryOrganization,
			Position queryPosition, int currentPage, int pageSize);

	/**
	 * 分页取类信息
	 * 
	 * @param querySysClass
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByQuertPosition(Position queryPosition,
			int currentPage, int pageSize);

	/**
	 * 保存
	 * 
	 * @param position
	 */
	public void savePosition(Position position);

	/**
	 * 修改
	 * 
	 * @param position
	 */
	public void updatePosition(Position position);

	/**
	 * 删除
	 * 
	 * @param position
	 */
	public void removePosition(Position position);

	public Position jdbcFindPosition(Position position);

	public String getSeqPositionCode();

	/**
	 * 方法功能: 获取positionType属性取值元数据列表 - 封装成前台下拉框需要的格式 id value
	 * 
	 */
	public List<NodeVo> getValuesList();
	
	/**
	 * 移动员工组织关系时，删除原来的员工岗位关系
	 * @param staffOrganization
	 */
	public void removeStaffPostionByOrganization(StaffOrganization staffOrganization);

}
