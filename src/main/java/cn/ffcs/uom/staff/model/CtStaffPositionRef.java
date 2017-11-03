package cn.ffcs.uom.staff.model;

import java.io.Serializable;
import java.util.List;

import com.opensymphony.xwork2.util.Key;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.position.model.CtPosition;
import cn.ffcs.uom.staff.dao.CtStaffPositionRefDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 *人员岗位关系实体.
 * 
 * @author
 * 
 **/
public class CtStaffPositionRef extends UomEntity implements Serializable {
	/**
     *
     */
    private static final long serialVersionUID = 1L;
    
    /**
	 *员工岗位标识.
	 **/
	public Long getCtStaffPositionRefid() {
		return super.getId();
	}

	public void setCtStaffPositionRefid(Long ctStaffPositionRefid) {
		super.setId(ctStaffPositionRefid);
	}
	
	/**
	 *员工标识.
	 **/
	@Getter
	@Setter
	private Long staffId;
	
	/**
	 *岗位标识.
	 **/
	@Getter
	@Setter
	private Long ctPositionId;
	
	/**
	 * 组织标识
	 */
	@Getter
	@Setter
	private Long orgId;
	
	/**
	 * 执行单据(可用与后期ITM校验)
	 */
	@Getter
	@Setter
	private String execDoc;
	
	/**
	 * 岗位类型
	 */
	@Getter
	@Setter
	private String ralaCd;
	
	/**
	 * 岗位类型名称
	 * 
	 * @return
	 */
	public String getRalaCdName() {
		if (!StrUtil.isEmpty(this.getRalaCd())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"CtStaffPositionRef", "ralaCd", this.getRalaCd(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static CtStaffPositionRefDao repository() {
		return (CtStaffPositionRefDao) ApplicationContextUtil
				.getBean("ctStaffPositionRefDao");
	}
	
	/**
	 * 获取员工岗位关系的组织
	 * 
	 * @return
	 */
	public Organization getOrganization() {
		if (this.getOrgId() != null) {
			return (Organization) Organization.repository().getObject(
					Organization.class, this.getOrgId());
		}
		return null;
	}
	
	/**
	 * 获取员工岗位关系的岗位
	 * 
	 * @return
	 */
	public CtPosition getCtPosition() {
		if (this.getCtPositionId() != null) {
			return (CtPosition) CtPosition.repository().getObject(
					CtPosition.class, this.getCtPositionId());
		}
		return null;
	}
}
