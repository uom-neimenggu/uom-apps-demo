package cn.ffcs.uac.staff.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uac.staff.dao.UacCertDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
/**
 * @Title:UacCert
 * @Description:证件信息实体类
 * @版权:福富软件（C）2017 
 * @author Wyr
 * @date 2017年8月31日上午11:08:28
 */
public class UacCert extends UomEntity implements Serializable {

	private static final long serialVersionUID = 6730684453523064556L;

	public Long getCertId() {
		return super.getId();
	}
	public void setCertId(long certId) {
		super.setId(certId);
	}
	@Getter
	@Setter
	private String certNumber;
	@Getter
	@Setter
	private String isReal;
	@Getter
	@Setter
	private String certType;
	@Getter
	@Setter
	private String certName;
	@Getter
	@Setter
	private String certOrg;
	@Getter
	@Setter
	private String certAddr;
	@Getter
	@Setter
	private Date certValidPeriod;
	
	public UacCert() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return UacCert
	 */
	public static UacCert newInstance() {
		return new UacCert();
	}
	
	public static UacCertDao repository() {
		return (UacCertDao) ApplicationContextUtil.getBean("uacCertDao");
	}
}
