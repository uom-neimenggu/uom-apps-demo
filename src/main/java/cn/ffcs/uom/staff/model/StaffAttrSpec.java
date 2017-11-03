package cn.ffcs.uom.staff.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.dao.StaffAttrSpecDao;

/**
 *员工属性规格实体.
 * 
 * @author
 * 
 **/
@SuppressWarnings({"unchecked","rawtypes"})
public class StaffAttrSpec extends UomEntity implements Serializable {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 *员工属性规格标识.
	 **/
	public Long getStaffAttrSpecId() {
		return super.getId();
	}

	public void setStaffAttrSpecId(Long staffAttrSpecId) {
		super.setId(staffAttrSpecId);
	}
	/**
	 *员工属性规格分类标识.
	 **/
	@Getter
	@Setter
	private Long staffAttrSpecSortId;
	/**
	 *员工属性规格名称.
	 **/
	@Getter
	@Setter
	private String attrName;
	/**
	 *员工属性规格类型.
	 **/
	@Getter
	@Setter
	private Long attrSpecType;
	/**
	 * 员工属性列表
	 */
	private List<StaffAttrValue> staffAttrValueList;

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static StaffAttrSpecDao repository() {
		return (StaffAttrSpecDao) ApplicationContextUtil
				.getBean("staffAttrSpecDao");
	}

	/**
	 * 获取下拉属性
	 * 
	 * @return
	 */
    public List<StaffAttrValue> getStaffAttrValueList() {
		if (staffAttrValueList == null) {
			if (this.getStaffAttrSpecId() != null
					&& attrSpecType == SffOrPtyCtants.ATTR_SPEC_TYPE_SELECT) {
				String sql = "SELECT * FROM STAFF_ATTR_VALUE WHERE STATUS_CD =? AND STAFF_ATTR_SPEC_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getStaffAttrSpecId());
				staffAttrValueList = StaffAttrValue.repository().jdbcFindList(
						sql, params, StaffAttrValue.class);
			}

		}
		return staffAttrValueList;
	}

	/**
	 * 获取下拉属性
	 * 
	 * @return
	 */
	public List<NodeVo> getStaffAttrValueNodeVoList() {
		List<NodeVo> list = new ArrayList<NodeVo>();
		List<StaffAttrValue> staffAttrValueList = this.getStaffAttrValueList();
		if (staffAttrValueList != null) {
			for (StaffAttrValue staffAttrValue : staffAttrValueList) {
				NodeVo nodeVo = new NodeVo();
				nodeVo.setId(staffAttrValue.getStaffAttrValue());
				nodeVo.setName(staffAttrValue.getStaffAttrValueName());
				list.add(nodeVo);
			}
		}
		return list;
	}
}
