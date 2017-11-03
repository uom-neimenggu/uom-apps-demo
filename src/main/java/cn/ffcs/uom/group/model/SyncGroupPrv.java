package cn.ffcs.uom.group.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.group.dao.GroupUomDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public class SyncGroupPrv extends UomEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID.
	 **/
	public Long getResId() {
		return super.getId();
	}

	public void setResId(Long resId) {
		super.setId(resId);
	}

	/**
	 * 主数据组织ID或员工ID.
	 **/
	@Getter
	@Setter
	private Long prvId;
	/**
	 * 集团组织ID或员工ID.
	 **/
	@Getter
	@Setter
	private Long groupId;

	/**
	 * 关系类型.
	 **/
	@Getter
	@Setter
	private String resType;

	/**
	 * 数据类型.
	 **/
	@Getter
	@Setter
	private String dataType;

	/**
	 * 关系类型名称. 对应关系类型【RES_TYPE】 1表示资产 2表示会计 3表示客户【暂定】 需配置成静态表
	 **/
	public String getResTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getResType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"SyncGroupPrv", "resType", this.getResType(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 数据类型名称. 对应数据类型【DATA_TYPE】 1表示组织 2表示员工
	 **/
	public String getDataTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getDataType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"SyncGroupPrv", "dataType", this.getDataType(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public static GroupUomDao repository() {
		return (GroupUomDao) ApplicationContextUtil.getBean("groupUomDao");
	}

}
