package cn.ffcs.uom.party.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2017
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年8月17日
 *
 */
public class CertIdVarInfo {

	/**
	 * 身份证号
	 */
	@Getter
	@Setter
	private String identity;

	/**
	 * 证件名
	 */
	@Getter
	@Setter
	private String name;
}
