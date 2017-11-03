package cn.ffcs.uom.common.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 * 级联关系实体.
 * 
 * @author
 * 
 **/
public class CascadeRelation extends UomEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5113391950034100319L;

	/**
	 * 级联关系标识.
	 **/
	public Long getCascadeRelaId() {
		return super.getId();
	}

	public void setCascadeRelaId(Long cascadeRelaId) {
		super.setId(cascadeRelaId);
	}

	/**
	 * 级联类型.
	 **/
	@Getter
	@Setter
	private String relaCd;
	/**
	 * 级联类型名.
	 **/
	@Getter
	@Setter
	private String relaCdName;
	/**
	 * 本级级联值.
	 **/
	@Getter
	@Setter
	private String cascadeValue;
	/**
	 * 上级级联值.
	 **/
	@Getter
	@Setter
	private String relaCascadeValue;

}
