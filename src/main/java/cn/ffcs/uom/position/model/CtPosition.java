package cn.ffcs.uom.position.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.position.dao.CtPositionDao;

/**
 * ct岗位实体.
 * 
 * @author
 * 
 **/
public class CtPosition extends UomEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 岗位标识.
	 **/
	public Long getCtPositionId() {
		return super.getId();
	}

	public void setCtPositionId(Long ctPositionId) {
		super.setId(ctPositionId);
	}
	
	/**
	 * 岗位名称.
	 **/
	@Getter
	@Setter
	private String positionName;
	
	/**
	 * 岗位编码.
	 **/
	@Setter
	private String positionCode;
	
	/**
	 * @return the positionCode
	 */
	public String getPositionCode() {
		String posCodeFristLetterString = positionCode.substring(0, 1);
		boolean isLetter = posCodeFristLetterString.matches("[a-zA-Z]");
		if(isLetter) {
			return positionCode.substring(1);
		} else {
			return positionCode;
		}
		
	}

	/**
	 * 岗位类型.
	 **/
	@Getter
	@Setter
	private String positionType;
	
	/**
	 * 岗位描述.
	 **/
	@Getter
	@Setter
	private String posDesc;

	/**
	 * 获取仓库.
	 * 
	 * @return CrmRepository
	 */
	public static CtPositionDao repository() {
		return (CtPositionDao) ApplicationContextUtil.getBean("ctPositionDao");
	}

	public CtPosition() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return Position
	 */
	public static CtPosition newInstance() {
		return new CtPosition();
	}
	
}
