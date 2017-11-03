package cn.ffcs.uom.staff.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.staff.dao.StaffAttrSpecSortDao;

/**
 *员工属性规格种类实体.
 * 
 * @author
 * 
 **/
@SuppressWarnings({"unchecked","rawtypes"})
public class StaffAttrSpecSort extends UomEntity implements Serializable {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 *员工规格标识.
	 **/
	public Long getStaffAttrSpecSortId() {
		return super.getId();
	}

	public void setStaffAttrSpecSortId(Long staffAttrSpecSortId) {
		super.setId(staffAttrSpecSortId);
	}
	/**
	 *类型名称.
	 **/
	@Getter
	@Setter
	private String sortName;
	/**
	 *种类类型.
	 **/
	@Getter
	@Setter
	private String sortType;
	/**
	 * 员工规格
	 */
	private List<StaffAttrSpec> staffAttrSpecList;

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static StaffAttrSpecSortDao repository() {
		return (StaffAttrSpecSortDao) ApplicationContextUtil
				.getBean("staffAttrSpecSortDao");
	}

	/**
	 * 获取员工属性规格
	 * 
	 * @return
	 */
	public List<StaffAttrSpec> getStaffAttrSpecList() {
		if (staffAttrSpecList == null) {
			if (this.getStaffAttrSpecSortId() != null) {
				String sql = "SELECT * FROM STAFF_ATTR_SPEC A WHERE A.STATUS_CD = ? AND A.STAFF_ATTR_SPEC_SORT_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getStaffAttrSpecSortId());
				staffAttrSpecList = StaffAttrSpec.repository().jdbcFindList(
						sql, params, StaffAttrSpec.class);

			}
		}
		return staffAttrSpecList;
	}
}
