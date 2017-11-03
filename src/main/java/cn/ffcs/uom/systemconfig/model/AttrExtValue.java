package cn.ffcs.uom.systemconfig.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 * 属性值规格实体.
 * 
 * @author
 * 
 **/
public class AttrExtValue extends UomEntity implements Serializable {

	/**
	 * 扩展属性值ID.
	 **/
	public Long getAttrExtValueId() {
		return super.getId();
	}

	public void setAttrExtValueId(Long attrExtValueId) {
		super.setId(attrExtValueId);
	}

	/**
	 * 属性值ID.
	 */
	@Getter
	@Setter
	private Long attrValueId;

	/**
	 * 属性值.
	 **/
	@Getter
	@Setter
	private String attrExtValue;
	/**
	 * 属性值名称.
	 **/
	@Getter
	@Setter
	private String attrExtValueName;
	/**
	 * 属性值描述.
	 **/
	@Getter
	@Setter
	private String attrExtDesc;

}
