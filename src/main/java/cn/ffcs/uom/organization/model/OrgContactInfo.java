package cn.ffcs.uom.organization.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.dao.OrganizationContactInfoDao;

/**
 *组织联系信息实体.
 * 
 * @author
 * 
 **/
public class OrgContactInfo extends UomEntity implements Serializable {
	/**
	 *组织标识.
	 **/
	public Long getOrganizationContactInfoId() {
		return super.getId();
	}

	public void setOrganizationContactInfoId(Long orgContactInfoId) {
		super.setId(orgContactInfoId);
	}
	/**
	 *组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;
	/**
	 *邮编.
	 **/
	@Getter
	@Setter
	private String postCode;
	/**
	 *地址.
	 **/
	@Getter
	@Setter
	private String address;
	/**
	 *电子邮箱.
	 **/
	@Getter
	@Setter
	private String email1;
	/**
	 *电子邮箱.
	 **/
	@Getter
	@Setter
	private String email2;
	/**
	 *电子邮箱.
	 **/
	@Getter
	@Setter
	private String email3;
	/**
	 *联系电话.
	 **/
	@Getter
	@Setter
	private String phone1;
	/**
	 *联系电话.
	 **/
	@Getter
	@Setter
	private String phone2;
	/**
	 *联系电话.
	 **/
	@Getter
	@Setter
	private String phone3;
	/**
	 *联系电话.
	 **/
	@Getter
	@Setter
	private String phone4;
	/**
	 *备用联系电话.
	 **/
	@Getter
	@Setter
	private String secondaryPhone;
	/**
	 *全局标识码.
	 **/
	@Getter
	@Setter
	private String uuid;

	/**
	 * 构造方法
	 */
	public OrgContactInfo() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OrganizationContactInfo
	 */
	public static OrgContactInfo newInstance() {
		return new OrgContactInfo();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrganizationContactInfoDao repository() {
		return (OrganizationContactInfoDao) ApplicationContextUtil
				.getBean("organizationContactInfoDao");
	}
}
