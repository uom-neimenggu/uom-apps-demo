package cn.ffcs.uom.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.staff.dao.StaffAttrValueDao;

/**
 *员工属性取值实体.
 * 
 * @author
 * 
 **/

public class StaffAttrValue extends UomEntity implements Serializable {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 *员工属性标识.
	 **/
	public Long getStaffAttrValueId() {
		return super.getId();
	}

	public void setStaffAttrValueId(Long staffAttrValueId) {
		super.setId(staffAttrValueId);
	}
	/**
	 *员工属性规格标识.
	 **/
	@Getter
	@Setter
	private Long staffAttrSpecId;
	/**
	 *属性值.
	 **/
	@Getter
	@Setter
	private String staffAttrValue;
	/**
	 *属性名称.
	 **/
	@Getter
	@Setter
	private String staffAttrValueName;

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static StaffAttrValueDao repository() {
		return (StaffAttrValueDao) ApplicationContextUtil
				.getBean("staffAttrValueDao");
	}
}
