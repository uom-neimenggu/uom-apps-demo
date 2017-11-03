package cn.ffcs.uom.organization.model;

// Generated 2017-6-13 12:30:13 by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

public class HanaOrgDept implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4548054121298401680L;
	
	private Long id;
	private Integer monthId;
	private String unitId;
	private String pkDept;
	private String shortname;
	private String code;
	private String createdate;
	private String principal;
	private String pkFatherorg;
	private BigDecimal depttype;
	private BigDecimal displayorder;
	private String name;
	private String hrcanceled;
	private String deptcanceldate;
	private String deptduty;
	private String deptlevel;
	private String memo;
	private String address;
	private String creationtime;
	private String creator;
	private BigDecimal dataoriginflag;
	private String def1;
	private String def10;
	private String def11;
	private String def12;
	private String def13;
	private String def14;
	private String def15;
	private String def16;
	private String def17;
	private String def18;
	private String def19;
	private String def2;
	private String def20;
	private String def3;
	private String def4;
	private String def5;
	private String def6;
	private String def7;
	private String def8;
	private String def9;
	private Long dr;
	private BigDecimal enablestate;
	private String innercode;
	private String islastversion;
	private String isretail;
	private String mnecode;
	private String modifiedtime;
	private String modifier;
	private String name2;
	private String name3;
	private String name4;
	private String name5;
	private String name6;
	private String orgtype13;
	private String orgtype17;
	private String pkGroup;
	private String pkOrg;
	private String pkVid;
	private String resposition;
	private String shortname2;
	private String shortname3;
	private String shortname4;
	private String shortname5;
	private String shortname6;
	private String tel;
	private String ts;
	private String venddate;
	private String vname;
	private String vname2;
	private String vname3;
	private String vname4;
	private String vname5;
	private String vname6;
	private String vno;
	private String vstartdate;
	private String glbdef1;
	private String glbdef2;
	private String glbdef3;
	private String glbdef4;
	private String glbdef5;
	private String glbdef6;
	private String glbdef7;
	private String glbdef8;
	private String glbdef9;
	private String deptleve;
	private Date loadDate;
	private Long etlDay;
	private Byte localPartitionId;

	/**
	 * 构造方法
	 */
	public HanaOrgDept() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return HanaOrgDept
	 */
	public static HanaOrgDept newInstance() {
		return new HanaOrgDept();
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
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMonthId() {
		return this.monthId;
	}

	public void setMonthId(Integer monthId) {
		this.monthId = monthId;
	}

	public String getUnitId() {
		return this.unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getPkDept() {
		return this.pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPkFatherorg() {
		return this.pkFatherorg;
	}

	public void setPkFatherorg(String pkFatherorg) {
		this.pkFatherorg = pkFatherorg;
	}

	public BigDecimal getDepttype() {
		return this.depttype;
	}

	public void setDepttype(BigDecimal depttype) {
		this.depttype = depttype;
	}

	public BigDecimal getDisplayorder() {
		return this.displayorder;
	}

	public void setDisplayorder(BigDecimal displayorder) {
		this.displayorder = displayorder;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHrcanceled() {
		return this.hrcanceled;
	}

	public void setHrcanceled(String hrcanceled) {
		this.hrcanceled = hrcanceled;
	}

	public String getDeptcanceldate() {
		return this.deptcanceldate;
	}

	public void setDeptcanceldate(String deptcanceldate) {
		this.deptcanceldate = deptcanceldate;
	}

	public String getDeptduty() {
		return this.deptduty;
	}

	public void setDeptduty(String deptduty) {
		this.deptduty = deptduty;
	}

	public String getDeptlevel() {
		return this.deptlevel;
	}

	public void setDeptlevel(String deptlevel) {
		this.deptlevel = deptlevel;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreationtime() {
		return this.creationtime;
	}

	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public BigDecimal getDataoriginflag() {
		return this.dataoriginflag;
	}

	public void setDataoriginflag(BigDecimal dataoriginflag) {
		this.dataoriginflag = dataoriginflag;
	}

	public String getDef1() {
		return this.def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef10() {
		return this.def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	public String getDef11() {
		return this.def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	public String getDef12() {
		return this.def12;
	}

	public void setDef12(String def12) {
		this.def12 = def12;
	}

	public String getDef13() {
		return this.def13;
	}

	public void setDef13(String def13) {
		this.def13 = def13;
	}

	public String getDef14() {
		return this.def14;
	}

	public void setDef14(String def14) {
		this.def14 = def14;
	}

	public String getDef15() {
		return this.def15;
	}

	public void setDef15(String def15) {
		this.def15 = def15;
	}

	public String getDef16() {
		return this.def16;
	}

	public void setDef16(String def16) {
		this.def16 = def16;
	}

	public String getDef17() {
		return this.def17;
	}

	public void setDef17(String def17) {
		this.def17 = def17;
	}

	public String getDef18() {
		return this.def18;
	}

	public void setDef18(String def18) {
		this.def18 = def18;
	}

	public String getDef19() {
		return this.def19;
	}

	public void setDef19(String def19) {
		this.def19 = def19;
	}

	public String getDef2() {
		return this.def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef20() {
		return this.def20;
	}

	public void setDef20(String def20) {
		this.def20 = def20;
	}

	public String getDef3() {
		return this.def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return this.def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return this.def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public String getDef6() {
		return this.def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getDef7() {
		return this.def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}

	public String getDef8() {
		return this.def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}

	public String getDef9() {
		return this.def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}

	public Long getDr() {
		return this.dr;
	}

	public void setDr(Long dr) {
		this.dr = dr;
	}

	public BigDecimal getEnablestate() {
		return this.enablestate;
	}

	public void setEnablestate(BigDecimal enablestate) {
		this.enablestate = enablestate;
	}

	public String getInnercode() {
		return this.innercode;
	}

	public void setInnercode(String innercode) {
		this.innercode = innercode;
	}

	public String getIslastversion() {
		return this.islastversion;
	}

	public void setIslastversion(String islastversion) {
		this.islastversion = islastversion;
	}

	public String getIsretail() {
		return this.isretail;
	}

	public void setIsretail(String isretail) {
		this.isretail = isretail;
	}

	public String getMnecode() {
		return this.mnecode;
	}

	public void setMnecode(String mnecode) {
		this.mnecode = mnecode;
	}

	public String getModifiedtime() {
		return this.modifiedtime;
	}

	public void setModifiedtime(String modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getName2() {
		return this.name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return this.name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getName4() {
		return this.name4;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}

	public String getName5() {
		return this.name5;
	}

	public void setName5(String name5) {
		this.name5 = name5;
	}

	public String getName6() {
		return this.name6;
	}

	public void setName6(String name6) {
		this.name6 = name6;
	}

	public String getOrgtype13() {
		return this.orgtype13;
	}

	public void setOrgtype13(String orgtype13) {
		this.orgtype13 = orgtype13;
	}

	public String getOrgtype17() {
		return this.orgtype17;
	}

	public void setOrgtype17(String orgtype17) {
		this.orgtype17 = orgtype17;
	}

	public String getPkGroup() {
		return this.pkGroup;
	}

	public void setPkGroup(String pkGroup) {
		this.pkGroup = pkGroup;
	}

	public String getPkOrg() {
		return this.pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkVid() {
		return this.pkVid;
	}

	public void setPkVid(String pkVid) {
		this.pkVid = pkVid;
	}

	public String getResposition() {
		return this.resposition;
	}

	public void setResposition(String resposition) {
		this.resposition = resposition;
	}

	public String getShortname2() {
		return this.shortname2;
	}

	public void setShortname2(String shortname2) {
		this.shortname2 = shortname2;
	}

	public String getShortname3() {
		return this.shortname3;
	}

	public void setShortname3(String shortname3) {
		this.shortname3 = shortname3;
	}

	public String getShortname4() {
		return this.shortname4;
	}

	public void setShortname4(String shortname4) {
		this.shortname4 = shortname4;
	}

	public String getShortname5() {
		return this.shortname5;
	}

	public void setShortname5(String shortname5) {
		this.shortname5 = shortname5;
	}

	public String getShortname6() {
		return this.shortname6;
	}

	public void setShortname6(String shortname6) {
		this.shortname6 = shortname6;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTs() {
		return this.ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getVenddate() {
		return this.venddate;
	}

	public void setVenddate(String venddate) {
		this.venddate = venddate;
	}

	public String getVname() {
		return this.vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getVname2() {
		return this.vname2;
	}

	public void setVname2(String vname2) {
		this.vname2 = vname2;
	}

	public String getVname3() {
		return this.vname3;
	}

	public void setVname3(String vname3) {
		this.vname3 = vname3;
	}

	public String getVname4() {
		return this.vname4;
	}

	public void setVname4(String vname4) {
		this.vname4 = vname4;
	}

	public String getVname5() {
		return this.vname5;
	}

	public void setVname5(String vname5) {
		this.vname5 = vname5;
	}

	public String getVname6() {
		return this.vname6;
	}

	public void setVname6(String vname6) {
		this.vname6 = vname6;
	}

	public String getVno() {
		return this.vno;
	}

	public void setVno(String vno) {
		this.vno = vno;
	}

	public String getVstartdate() {
		return this.vstartdate;
	}

	public void setVstartdate(String vstartdate) {
		this.vstartdate = vstartdate;
	}

	public String getGlbdef1() {
		return this.glbdef1;
	}

	public void setGlbdef1(String glbdef1) {
		this.glbdef1 = glbdef1;
	}

	public String getGlbdef2() {
		return this.glbdef2;
	}

	public void setGlbdef2(String glbdef2) {
		this.glbdef2 = glbdef2;
	}

	public String getGlbdef3() {
		return this.glbdef3;
	}

	public void setGlbdef3(String glbdef3) {
		this.glbdef3 = glbdef3;
	}

	public String getGlbdef4() {
		return this.glbdef4;
	}

	public void setGlbdef4(String glbdef4) {
		this.glbdef4 = glbdef4;
	}

	public String getGlbdef5() {
		return this.glbdef5;
	}

	public void setGlbdef5(String glbdef5) {
		this.glbdef5 = glbdef5;
	}

	public String getGlbdef6() {
		return this.glbdef6;
	}

	public void setGlbdef6(String glbdef6) {
		this.glbdef6 = glbdef6;
	}

	public String getGlbdef7() {
		return this.glbdef7;
	}

	public void setGlbdef7(String glbdef7) {
		this.glbdef7 = glbdef7;
	}

	public String getGlbdef8() {
		return this.glbdef8;
	}

	public void setGlbdef8(String glbdef8) {
		this.glbdef8 = glbdef8;
	}

	public String getGlbdef9() {
		return this.glbdef9;
	}

	public void setGlbdef9(String glbdef9) {
		this.glbdef9 = glbdef9;
	}

	public String getDeptleve() {
		return this.deptleve;
	}

	public void setDeptleve(String deptleve) {
		this.deptleve = deptleve;
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
