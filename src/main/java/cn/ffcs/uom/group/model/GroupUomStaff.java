package cn.ffcs.uom.group.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.group.dao.GroupUomDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public class GroupUomStaff {

	/**
	 * 集团员工ID.
	 **/
	@Getter
	@Setter
	private Long id;

	/**
	 * 集团员工名称.
	 **/
	@Getter
	@Setter
	private String ktext;

	/**
	 * 主数据员工名称.
	 **/
	@Getter
	@Setter
	private String staffName;

	/**
	 * 主数据员工编码.
	 **/
	@Getter
	@Setter
	private String staffCode;

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
	 * 生效时间.
	 **/
	@Getter
	@Setter
	private Date effDate;

	/**
	 * 失效时间.
	 **/
	@Getter
	@Setter
	private Date expDate;

	/**
	 * 关系类型名称. 对应关系类型【RES_TYPE】 1表示资产 2表示会计 3表示客户【暂定】 需配置成静态表
	 **/
	private String getResTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getResType())) {
			if ("1".equals(this.getResType())) {
				return "资产";
			} else if ("2".equals(this.getResType())) {
				return "会计";
			} else if ("3".equals(this.getResType())) {
				return "客户";
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
