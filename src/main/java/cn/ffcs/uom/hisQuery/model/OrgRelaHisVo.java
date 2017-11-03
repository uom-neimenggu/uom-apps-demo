package cn.ffcs.uom.hisQuery.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.hisQuery.dao.OrgRelaHisDao;

/**
 *组织关系实体.
 * 
 * @author
 * 
 **/
public class OrgRelaHisVo extends UomEntity implements Serializable {
	/**
	 *组织标识.
	 **/
	@Getter
	@Setter
	private Long orgRelId;
	/**
	 *组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;
	/**
	 *关联组织标识.
	 **/
	@Getter
	@Setter
	private Long relaOrgId;
	/**
	 *关系类型.
	 **/
	@Getter
	@Setter
	private String relaCd;

	/**
	 * 构造方法
	 */
	public OrgRelaHisVo() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OrganizationRelation
	 */
	public static OrgRelaHisVo newInstance() {
		return new OrgRelaHisVo();
	}
	/**
	 * 状态名称
	 * 
	 * @return
	 */
	public String getStatusCdName() {		
		if("1000".equals(this.getStatusCd())){
			return "生效";
		}else{
			return "失效";
		}	
	}
	/**
	 * 状态名称
	 * 
	 * @return
	 */
	public String getRelaCdName() {		
		if("1000".equals(this.getStatusCd())){
			return "上下级关系";
		}else{
			return null;
		}	
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrgRelaHisDao repository() {
		return (OrgRelaHisDao) ApplicationContextUtil
				.getBean("orgRelaHisDao");
	}
}
