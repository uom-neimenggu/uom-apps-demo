package cn.ffcs.uom.areacode.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 *地区编码实体.
 * 
 * @author
 * 
 **/
public class AreaCode extends UomEntity implements Serializable {

	public Long getAreaCodeId() {
		return super.getId();
	}

	public void setAreaCodeId(Long areaCodeId) {
		super.setId(areaCodeId);
	}

	/**
	 *地区编码.
	 **/
	@Getter
	@Setter
	private String areaCode;
	/**
	 *地区名称.
	 **/
	@Getter
	@Setter
	private String areaName;
}
