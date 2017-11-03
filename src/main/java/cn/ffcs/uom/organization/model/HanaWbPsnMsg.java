package cn.ffcs.uom.organization.model;

// Generated 2017-6-13 12:02:44 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;


public class HanaWbPsnMsg implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4965544109628006359L;
	
	private Long msgId;
	private int monthId;
	private String unitId;
	private String pkPsnMsg;
	private String pkOrg;
	private String pkGroup;
	private String psncode;
	private String psnname;
	private String id;
	private String sex;
	private String birthdate;
	private String highestEducation;
	private String fulltimeEducation;
	private String degree;
	private String busilife;
	private String defStr1;
	private String defStr2;
	private String defStr3;
	private String defStr4;
	private String defStr5;
	private BigDecimal defNum1;
	private BigDecimal defNum2;
	private BigDecimal defNum3;
	private BigDecimal defNum4;
	private BigDecimal defNum5;
	private BigDecimal isSend;
	private Long dr;
	private String mobtelephone;
	private String officephone;
	private String ts;
	private Date loadDate;
	private Long etlDay;
	private Byte localPartitionId;

	/**
	 * 构造方法
	 */
	public HanaWbPsnMsg() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return HanaTelSyncCostcenter
	 */
	public static HanaWbPsnMsg newInstance() {
		return new HanaWbPsnMsg();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static BaseDao repository() {
		return (BaseDao) ApplicationContextUtil.getBean("baseDao");
	}

	/**
	 * @return the msgId
	 */
	public Long getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public int getMonthId() {
		return this.monthId;
	}

	public void setMonthId(int monthId) {
		this.monthId = monthId;
	}

	public String getUnitId() {
		return this.unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getPkPsnMsg() {
		return this.pkPsnMsg;
	}

	public void setPkPsnMsg(String pkPsnMsg) {
		this.pkPsnMsg = pkPsnMsg;
	}

	public String getPkOrg() {
		return this.pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkGroup() {
		return this.pkGroup;
	}

	public void setPkGroup(String pkGroup) {
		this.pkGroup = pkGroup;
	}

	public String getPsncode() {
		return this.psncode;
	}

	public void setPsncode(String psncode) {
		this.psncode = psncode;
	}

	public String getPsnname() {
		return this.psnname;
	}

	public void setPsnname(String psnname) {
		this.psnname = psnname;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getHighestEducation() {
		return this.highestEducation;
	}

	public void setHighestEducation(String highestEducation) {
		this.highestEducation = highestEducation;
	}

	public String getFulltimeEducation() {
		return this.fulltimeEducation;
	}

	public void setFulltimeEducation(String fulltimeEducation) {
		this.fulltimeEducation = fulltimeEducation;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getBusilife() {
		return this.busilife;
	}

	public void setBusilife(String busilife) {
		this.busilife = busilife;
	}

	public String getDefStr1() {
		return this.defStr1;
	}

	public void setDefStr1(String defStr1) {
		this.defStr1 = defStr1;
	}

	public String getDefStr2() {
		return this.defStr2;
	}

	public void setDefStr2(String defStr2) {
		this.defStr2 = defStr2;
	}

	public String getDefStr3() {
		return this.defStr3;
	}

	public void setDefStr3(String defStr3) {
		this.defStr3 = defStr3;
	}

	public String getDefStr4() {
		return this.defStr4;
	}

	public void setDefStr4(String defStr4) {
		this.defStr4 = defStr4;
	}

	public String getDefStr5() {
		return this.defStr5;
	}

	public void setDefStr5(String defStr5) {
		this.defStr5 = defStr5;
	}

	public BigDecimal getDefNum1() {
		return this.defNum1;
	}

	public void setDefNum1(BigDecimal defNum1) {
		this.defNum1 = defNum1;
	}

	public BigDecimal getDefNum2() {
		return this.defNum2;
	}

	public void setDefNum2(BigDecimal defNum2) {
		this.defNum2 = defNum2;
	}

	public BigDecimal getDefNum3() {
		return this.defNum3;
	}

	public void setDefNum3(BigDecimal defNum3) {
		this.defNum3 = defNum3;
	}

	public BigDecimal getDefNum4() {
		return this.defNum4;
	}

	public void setDefNum4(BigDecimal defNum4) {
		this.defNum4 = defNum4;
	}

	public BigDecimal getDefNum5() {
		return this.defNum5;
	}

	public void setDefNum5(BigDecimal defNum5) {
		this.defNum5 = defNum5;
	}

	public BigDecimal getIsSend() {
		return this.isSend;
	}

	public void setIsSend(BigDecimal isSend) {
		this.isSend = isSend;
	}

	public Long getDr() {
		return this.dr;
	}

	public void setDr(Long dr) {
		this.dr = dr;
	}

	public String getMobtelephone() {
		return this.mobtelephone;
	}

	public void setMobtelephone(String mobtelephone) {
		this.mobtelephone = mobtelephone;
	}

	public String getOfficephone() {
		return this.officephone;
	}

	public void setOfficephone(String officephone) {
		this.officephone = officephone;
	}

	public String getTs() {
		return this.ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public Date getLoadDate() {
		return this.loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}

	public Long getEtlDay() {
		return this.etlDay;
	}

	public void setEtlDay(Long etlDay) {
		this.etlDay = etlDay;
	}

	public Byte getLocalPartitionId() {
		return this.localPartitionId;
	}

	public void setLocalPartitionId(Byte localPartitionId) {
		this.localPartitionId = localPartitionId;
	}
	
}
