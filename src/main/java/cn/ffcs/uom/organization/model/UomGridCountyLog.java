package cn.ffcs.uom.organization.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.group.dao.GroupDao;

public class UomGridCountyLog {

	/**
	 * 标识.
	 **/
	@Getter
	@Setter
	private Long uomGridCountyAttrId;
	/**
	 * 组织ID.
	 **/
	@Getter
	@Setter
	private Long orgId;

	@Getter
	@Setter
	private Long orgAttrSpecId;

	@Getter
	@Setter
	private String orgAttrCd;

	@Getter
	@Setter
	private String orgAttrValue;

	@Getter
	@Setter
	private String statusCd;

	/**
	 * 创建时间.
	 **/
	@Getter
	@Setter
	private Date createDate;

	@Getter
	@Setter
	private Long createStaff;

	@Getter
	@Setter
	private String modifyIden;

}
