package cn.ffcs.uom.organization.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 * 
 * 
 * @author
 * 
 **/
public class MdsionOrgRelType extends UomEntity implements Serializable {

	/**
	 * 组织关系类型标识.
	 **/
	public Long getMdsionOrgRelTypeId() {
		return super.getId();
	}

	public void setMdsionOrgRelTypeId(Long mdsionOrgRelTypeId) {
		super.setId(mdsionOrgRelTypeId);
	}
	/**
	 * 多维组织关系标识.
	 **/
	@Getter
	@Setter
	private Long mdsionOrgRelId;
	/**
	 * 关系类型.
	 **/
	@Getter
	@Setter
	private String mdsionOrgRelTypeCd;
}
