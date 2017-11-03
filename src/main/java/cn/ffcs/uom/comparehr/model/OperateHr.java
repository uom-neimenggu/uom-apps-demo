package cn.ffcs.uom.comparehr.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class OperateHr implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Getter
    @Setter
	private Long staffId;
	
	@Getter
    @Setter
	private String staffNbr;
	
	@Getter
    @Setter
	private String workProp;
	
	@Getter
    @Setter
	private String staffAccount;
	
	@Getter
    @Setter
	private Long orgId;
	
	@Getter
    @Setter
	private Long partyId;
	
	@Getter
    @Setter
	private String partyName;
	
	@Getter
    @Setter
	private Date birthdayTemp;
	
	@Getter
    @Setter
	private String gender;
	
	@Getter
    @Setter
	private String certNumber;
	
	@Getter
    @Setter
	private String psnCode;
	
	@Getter
    @Setter
	private String psnBasdoc;
	
	@Getter
    @Setter
	private String psnCl;
	
	@Getter
    @Setter
	private Long psnClScope;
	
	@Getter
    @Setter
	private String iid;
	
	@Getter
    @Setter
	private String psnName;

	@Getter
    @Setter
	private String sex;

	@Getter
    @Setter
	private Date birthday;

	@Getter
    @Setter
	private String psnClassName;

	@Getter
    @Setter
	private String workPop;

	@Getter
    @Setter
	private String workPopName;

	@Getter
    @Setter
	private String flag;

	@Getter
    @Setter
	private String auditResult;

	@Getter
    @Setter
	private String dealWith;

	@Getter
    @Setter
	private String updateStaffAccount;

	@Getter
    @Setter
	private String lastCertNum;

	@Getter
    @Setter
	private String checkBatchs;

	@Getter
    @Setter
	private String staffName;

	@Getter
    @Setter
	private String lastStaffName;

	@Getter
    @Setter
	private String currentStatus;

}
