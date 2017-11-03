package cn.ffcs.uac.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uac.staff.dao.UacContactDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

/**
 * @Title:UacContact
 * @Description:联系方式实体类
 * @版权:福富软件（C）2017
 * @author Wyr
 * @date 2017年8月31日上午11:06:47
 */
public class UacContact extends UomEntity implements Serializable {

	private static final long serialVersionUID = 5469650193213871185L;

	public Long getContactId() {
		return super.getId();
	}

	public void setContactId(long contactId) {
		super.setId(contactId);
	}

	@Getter
	@Setter
	private String mobilePhone;
	@Getter
	@Setter
	private String sparePhone;
	@Getter
	@Setter
	private String telephone;
	@Getter
	@Setter
	private String innerEmail;
	@Getter
	@Setter
	private String email;
	@Getter
	@Setter
	private String unifiedEmail;
	@Getter
	@Setter
	private String address;
	@Getter
	@Setter
	private String fax;
	@Getter
	@Setter
	private String qq;
	@Getter
	@Setter
	private String wechat;
	
	public UacContact() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return UacContact
	 */
	public static UacContact newInstance() {
		return new UacContact();
	}
	
	public static UacContactDao repository() {
		return (UacContactDao) ApplicationContextUtil.getBean("uacContactDao");
	}
}
