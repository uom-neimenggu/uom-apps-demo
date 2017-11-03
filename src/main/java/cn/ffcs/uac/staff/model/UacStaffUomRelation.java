package cn.ffcs.uac.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uac.staff.dao.UacStaffUomRelationDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

/**
 * @Title:UacStaffUomRelation
 * @Description:UacStaff员工Uom关系表
 * @版权:福富软件（C）2017
 * @author Wyr
 * @date 2017年8月31日上午11:02:03
 */
public class UacStaffUomRelation extends UomEntity implements Serializable {

	private static final long serialVersionUID = -4029750619408761101L;

	public Long getRelaId() {
		return super.getId();
	}

	public void setRelaId(Long relaId) {
		super.setId(relaId);
	}

	@Getter
	@Setter
	private Long uacStaffId;
	@Getter
	@Setter
	private Long uacCertId;
	@Getter
	@Setter
	private Long uacContactId;
	@Getter
	@Setter
	private Long attachedInfoId;
	@Getter
	@Setter
	private String extendIds;
	@Getter
	@Setter
	private Long staffId;
	@Getter
	@Setter
	private Long staffAccountId;
	@Getter
	@Setter
	private Long partyId;
	@Getter
	@Setter
	private Long partyRoleId;
	@Getter
	@Setter
	private Long contactId;
	@Getter
	@Setter
	private Long partyCertId;
	@Getter
	@Setter
	private Long individualId;

	public UacStaffUomRelation() {
		super();
	}

	public static UacStaffUomRelation newInstance() {
		return new UacStaffUomRelation();
	}

	public static UacStaffUomRelationDao repository() {
		return (UacStaffUomRelationDao) ApplicationContextUtil.getBean("uacStaffUomRelationDao");
	}
}
