package cn.ffcs.uom.organization.vo;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.model.DefaultDaoFactory;

/**
 * 保存组织名字和
 * 
 * @author ZhaoF
 * 
 */
public class OrganizationRelationNameVo {
	@Getter
	@Setter
	private String orgName;
	@Getter
	@Setter
	private String orgFullName;
	@Getter
	@Setter
	private Long orgId;
	@Getter
	@Setter
	private Long relaOrgId;
	@Getter
	@Setter
	private Long orgRelId;

	/**
	 * 获取
	 * 
	 * @return
	 */
	public static BaseDao repository() {
		return DefaultDaoFactory.getDefaultDao();
	}
}
