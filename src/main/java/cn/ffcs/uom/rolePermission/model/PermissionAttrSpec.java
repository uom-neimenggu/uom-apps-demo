package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.rolePermission.dao.PermissionAttrSpecDao;

/**
 * 权限属性规格实体.
 * 
 * @author
 * 
 **/
public class PermissionAttrSpec extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 属性规格标识.
	 **/
	public Long getPermissionAttrSpecId() {
		return super.getId();
	}

	public void setPermissionAttrSpecId(Long permissionAttrSpecId) {
		super.setId(permissionAttrSpecId);
	}

	/**
	 * 属性种类.
	 **/
	@Getter
	@Setter
	private Long permissionAttrSpecSortId;

	/**
	 * 属性名称.
	 **/
	@Getter
	@Setter
	private String permissionAttrName;

	/**
	 * 属性类型.
	 **/
	@Getter
	@Setter
	private Long permissionAttrSpecType;

	/**
	 * 下拉值
	 */
	private List<PermissionAttrValue> permissionAttrValueList;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static PermissionAttrSpecDao repository() {
		return (PermissionAttrSpecDao) ApplicationContextUtil
				.getBean("permissionAttrSpecDao");
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PermissionAttrValue> getPermissionAttrValueList() {
		if (this.permissionAttrValueList == null) {
			if (this.getPermissionAttrSpecId() != null
					&& permissionAttrSpecType == OrganizationConstant.ATTR_SPEC_TYPE_SELECT) {
				String sql = "SELECT * FROM PERMISSION_ATTR_VALUE WHERE STATUS_CD = ? AND PERMISSION_ATTR_SPEC_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getPermissionAttrSpecId());
				permissionAttrValueList = PermissionAttrValue.repository()
						.jdbcFindList(sql, params, PermissionAttrValue.class);
			}
		}
		return permissionAttrValueList;
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<NodeVo> getPermissionAttrValueNodeVoList() {
		List<NodeVo> list = new ArrayList<NodeVo>();
		List<PermissionAttrValue> permissionAttrValueList = this
				.getPermissionAttrValueList();
		if (permissionAttrValueList != null) {
			for (PermissionAttrValue permissionAttrValue : permissionAttrValueList) {
				NodeVo nodeVo = new NodeVo();
				nodeVo.setId(permissionAttrValue.getPermissionAttrValue());
				nodeVo.setName(permissionAttrValue.getPermissionAttrValueName());
				list.add(nodeVo);
			}
		}
		return list;
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<NodeVo> getPermissionAttrValueNodeVoList(
			List<PermissionAttrValue> permissionAttrValueList) {
		List<NodeVo> list = new ArrayList<NodeVo>();
		if (permissionAttrValueList != null) {
			for (PermissionAttrValue permissionAttrValue : permissionAttrValueList) {
				NodeVo nodeVo = new NodeVo();
				nodeVo.setId(permissionAttrValue.getPermissionAttrValue());
				nodeVo.setName(permissionAttrValue.getPermissionAttrValueName());
				list.add(nodeVo);
			}
		}
		return list;
	}
}
