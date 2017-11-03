package cn.ffcs.uom.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.staff.dao.StaffPositionDao;

/**
 *员工岗位关系实体.
 * 
 * @author
 * 
 **/
public class StaffPosition extends UomEntity implements Serializable {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    
    /**
	 *员工岗位标识.
	 **/
	public Long getStaffPositionId() {
		return super.getId();
	}

	public void setStaffPositionId(Long staffPositionId) {
		super.setId(staffPositionId);
	}
	
	/**
	 *员工标识.
	 **/
	@Getter
	@Setter
	private Long staffId;
	
	/**
	 *组织岗位关系标识.
	 **/
	@Getter
	@Setter
	private Long orgPositionRelaId;

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static StaffPositionDao repository() {
		return (StaffPositionDao) ApplicationContextUtil
				.getBean("staffPositionDao");
	}

	/**
	 * 获取员工岗位关系的组织岗位
	 * 
	 * @return
	 */
	public OrgPosition getOrgPosition() {
		if (this.getOrgPositionRelaId() != null) {
			return (OrgPosition) OrgPosition.repository().getObject(
					OrgPosition.class, this.getOrgPositionRelaId());
		}
		return null;
	}
}
