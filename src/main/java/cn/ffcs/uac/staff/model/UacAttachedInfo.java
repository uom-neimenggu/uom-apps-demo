package cn.ffcs.uac.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uac.staff.dao.UacAttachedInfoDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

/**
 * @Title:UacAttachedInfo
 * @Description:人员附加信息实体类
 * @版权:福富软件（C）2017
 * @author Wyr
 * @date 2017年8月31日上午11:10:40
 */
public class UacAttachedInfo extends UomEntity implements Serializable {

	private static final long serialVersionUID = -3709728474074442954L;

	public Long getAttachedInfoId() {
		return super.getId();
	}

	public void setAttachedInfoId(Long attachedInfoId) {
		super.setId(attachedInfoId);
	}

	@Getter
	@Setter
	private long staffId;
	@Getter
	@Setter
	private String maritalStatus;
	@Getter
	@Setter
	private String gender;
	@Getter
	@Setter
	private String nation;
	@Getter
	@Setter
	private String religion;
	@Getter
	@Setter
	private String occupation;
	@Getter
	@Setter
	private String educationLevel;
	
	public UacAttachedInfo() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return UacAttachedInfo
	 */
	public static UacAttachedInfo newInstance() {
		return new UacAttachedInfo();
	}
	
	public static UacAttachedInfoDao repository() {
		return (UacAttachedInfoDao) ApplicationContextUtil.getBean("uacAttachedInfoDao");
	}
}
