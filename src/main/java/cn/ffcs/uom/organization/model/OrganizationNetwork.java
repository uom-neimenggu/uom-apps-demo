/**
 * 
 */
package cn.ffcs.uom.organization.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yahui
 *
 */
public class OrganizationNetwork {
	/**
	 * 实例号
	 */
	@Getter
	@Setter
	private String instance;
	/**
	 * 单据号
	 */
	@Getter
	@Setter
	private String serial;
	/**
	 * 网点编码
	 */
	@Getter
	@Setter
	private String networkCode;
	/**
	 * 本级组织
	 */
	@Getter
	@Setter
	private String theOrgname;
	/**
	 * 上级组织
	 */
	@Getter
	@Setter
	private String thehighOrgname;
	/**
	 * 操作动作
	 */
	@Getter
	@Setter
	private String action;
	/**
	 * 是否被占用
	 */
	@Getter
	@Setter
	private String ifOccupy;

}
