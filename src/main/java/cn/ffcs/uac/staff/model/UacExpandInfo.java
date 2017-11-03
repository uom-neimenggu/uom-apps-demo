package cn.ffcs.uac.staff.model;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uac.staff.dao.UacExpandInfoDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

/**
 * @Title:UacExpandInfo
 * @Description:人员拓展信息实体类
 * @版权:福富软件（C）2017
 * @author Wyr
 * @date 2017年8月31日上午11:03:54
 */
public class UacExpandInfo extends UomEntity implements java.io.Serializable {

	private static final long serialVersionUID = -8506609863352774101L;

	public Long getExtendId() {
		return super.getId();
	}

	public void setExtendId(long extendId) {
		super.setId(extendId);
	}

	@Getter
	@Setter
	private long staffId;
	@Getter
	@Setter
	private String attrId;
	@Getter
	@Setter
	private String attrValue;
	
	public UacExpandInfo() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return UacExpandInfo
	 */
	public static UacExpandInfo newInstance() {
		return new UacExpandInfo();
	}
	
	public static UacExpandInfoDao repository() {
		return (UacExpandInfoDao) ApplicationContextUtil.getBean("uacExpandInfoDao");
	}
}
