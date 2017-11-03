package cn.ffcs.uom.staff.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.model.AttrExtValue;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.webservices.constants.WsConstants;

/***
 * 员工组织业务关系 .
 * 
 * @版权：福富软件 版权所有 (c) 2016
 * @author 朱林涛
 * @see:
 * @创建日期：2016-03-01
 * @功能说明：
 * 
 */
public class StaffOrgTranTemp extends UomEntity implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 员工组织业务关系标识
	 */
	public Long getStaffOrgTranTempId() {
		return super.getId();
	}

	public void setStaffOrgTranTempId(Long staffOrgTranTempId) {
		super.setId(staffOrgTranTempId);
	}

	/**
	 * 流水号标识
	 */
	@Getter
	@Setter
	private String transId;

	/**
	 * 数据来源
	 */
	@Getter
	@Setter
	private String dataSource;

	/**
	 * 操作类型
	 */
	@Getter
	@Setter
	private String operationType;

	/**
	 * 员工名称
	 */
	@Getter
	@Setter
	private String staffName;

	/**
	 * 员工账号
	 */
	@Getter
	@Setter
	private String staffAccount;

	/**
	 * 员工编码
	 */
	@Getter
	@Setter
	private String staffCode;

	/**
	 * 组织编码
	 */
	@Getter
	@Setter
	private String orgCode;

	/**
	 * 组织全称
	 */
	@Getter
	@Setter
	private String orgFullName;

	/**
	 * 关联类型
	 */
	@Getter
	@Setter
	private String ralaCd;

	/**
	 * 员工排序
	 */
	@Getter
	@Setter
	private Long staffSort;

	/**
	 * 处理结果
	 */
	@Getter
	@Setter
	private Long result;

	/**
	 * 错误编码
	 */
	@Getter
	@Setter
	private String errCode;

	/**
	 * 错误信息
	 */
	@Getter
	@Setter
	private String errMsg;

	/**
	 * 员工标识
	 */
	@Getter
	@Setter
	private Long staffId;

	/**
	 * 组织标识
	 */
	@Getter
	@Setter
	private Long orgId;

	public String getDataSourceName() {
		if (!StrUtil.isNullOrEmpty(this.getDataSource())) {
			if (BaseUnitConstants.DATA_SOURCE_UOM.equals(this.getDataSource())) {
				return "主数据平台";
			} else if (BaseUnitConstants.DATA_SOURCE_GRID_UNIT.equals(this
					.getDataSource())) {
				return "全息网格单元";
			}
		}
		return "";
	}

	public String getOperationTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getOperationType())) {
			if (BaseUnitConstants.OPERATION_TYPE_ADD.equals(this
					.getOperationType())) {
				return "新增";
			} else if (BaseUnitConstants.OPERATION_TYPE_DEL.equals(this
					.getOperationType())) {
				return "删除";
			}
		}
		return "";
	}

	public String getResultName() {
		if (!StrUtil.isNullOrEmpty(this.getResult())) {
			if (WsConstants.TASK_SUCCESS.equals(this.getResult())) {
				return "成功";
			} else if (WsConstants.TASK_FAILED.equals(this.getResult())) {
				return "失败";
			} else if (WsConstants.TASK_INIT.equals(this.getResult())) {
				return "初始化";
			}
		}
		return "";
	}

	public String getRalaCdName() {
		if (!StrUtil.isNullOrEmpty(this.getRalaCd())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"StaffOrganizationTran", "ralaCd", this.getRalaCd(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

}
