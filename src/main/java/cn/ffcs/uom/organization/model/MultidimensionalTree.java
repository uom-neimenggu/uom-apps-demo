package cn.ffcs.uom.organization.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 * 组织实体.
 * 
 * @author
 * 
 **/
@SuppressWarnings("serial")
public class MultidimensionalTree extends UomEntity implements Serializable {

	/**
	 * 组织树志标识.
	 */
	public Long getOrgTreeConfigId() {
		return super.getId();
	}

	public void setOrgTreeConfigId(Long orgTreeConfigId) {
		super.setId(orgTreeConfigId);
	}
	/**
	 * 组织树ID
	 */
	@Getter
	@Setter
	private Long orgTreeId;
	/**
	 * 关系
	 */
	@Getter
	@Setter
	private String relaCd;
	/**
	 * 是否默认关系0否 1是
	 */
	@Getter
	@Setter
	private Integer defaultRela;
	/**
	 * 默认打开层级
	 */
	@Getter
	@Setter
	private Integer defaultOpenLevel;
	
	/**
	 * 构造方法
	 */
	public MultidimensionalTree() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OperateLog
	 */
	public static MultidimensionalTree newInstance() {
		return new MultidimensionalTree();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	/*public static OrgTreeDao repository() {
		return (OrgTreeDao) ApplicationContextUtil.getBean("orgTreeDao");
	}*/

}