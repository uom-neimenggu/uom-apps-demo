package cn.ffcs.uom.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.staff.dao.StaffExtendAttrDao;

/**
 *员工扩展属性实体.
 * 
 * @author
 * 
 **/
public class StaffExtendAttr extends UomEntity implements Serializable {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 *员工属性标识.
	 **/
	public Long getStaffExtendAttrId() {
		return super.getId();
	}

	public void setStaffExtendAttrId(Long staffExtendAttrId) {
		super.setId(staffExtendAttrId);
	}
	/**
	 *员工属性规格标识.
	 **/
	@Getter
	@Setter
	private Long staffAttrSpecId;
	/**
	 *员工标识.
	 **/
	@Getter
	@Setter
	private Long staffId;
	/**
	 *员工属性值.
	 **/
	@Getter
	@Setter
	private String staffAttrValue;

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static StaffExtendAttrDao repository() {
		return (StaffExtendAttrDao) ApplicationContextUtil
				.getBean("staffExtendAttrDao");
	}
}
