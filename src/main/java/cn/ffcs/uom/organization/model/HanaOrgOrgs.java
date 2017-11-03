package cn.ffcs.uom.organization.model;

// Generated 2017-6-13 12:02:44 by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

public class HanaOrgOrgs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4571138857299265835L;
	
	private Long id;
	private int monthId;
	private String unitId;
	private String address;
	private String code;
	private String countryzone;
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
	private String entitytype;
	private String glbdef1;
	private String glbdef10;
	private String glbdef11;
	private String glbdef12;
	private String glbdef13;
	private String glbdef14;
	private String glbdef15;
	private long glbdef16;
	private String glbdef2;
	private String glbdef3;
	private String glbdef5;
	private BigDecimal glbdef6;
	private String glbdef7;
	private String glbdef8;
	private String glbdef9;
	private String innercode;
	private String isbusinessunit;
	private String islastversion;
	private String isretail;
	private String memo;
	private String mnecode;
	private String modifiedtime;
	private String modifier;
	private String name;
	private String name2;
	private String name3;
	private String name4;
	private String name5;
	private String name6;
	private String ncindustry;
	private String organizationcode;
	private String orgtype1;
	private String orgtype10;
	private String orgtype11;
	private String orgtype12;
	private String orgtype13;
	private String orgtype14;
	private String orgtype15;
	private String orgtype16;
	private String orgtype17;
	private String orgtype18;
	private String orgtype19;
	private String orgtype2;
	private String orgtype20;
	private String orgtype21;
	private String orgtype22;
	private String orgtype23;
	private String orgtype24;
	private String orgtype25;
	private String orgtype26;
	private String orgtype27;
	private String orgtype28;
	private String orgtype29;
	private String orgtype3;
	private String orgtype30;
	private String orgtype31;
	private String orgtype32;
	private String orgtype33;
	private String orgtype34;
	private String orgtype35;
	private String orgtype36;
	private String orgtype37;
	private String orgtype38;
	private String orgtype39;
	private String orgtype4;
	private String orgtype40;
	private String orgtype5;
	private String orgtype6;
	private String orgtype7;
	private String orgtype8;
	private String orgtype9;
	private String pkAccperiodscheme;
	private String pkControlarea;
	private String pkCorp;
	private String pkCurrtype;
	private String pkExratescheme;
	private String pkFatherorg;
	private String pkFormat;
	private String pkGroup;
	private String pkOrg;
	private String pkTimezone;
	private String pkVid;
	private String principal;
	private String reportconfirm;
	private String shortname;
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
	private String workcalendar;
	private String glbdef17;
	private String glbdef18;
	private Date loadDate;
	private Long etlDay;
	private Byte localPartitionId;

	/**
	 * 构造方法
	 */
	public HanaOrgOrgs() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return HanaOrgDept
	 */
	public static HanaOrgOrgs newInstance() {
		return new HanaOrgOrgs();
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

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountryzone() {
		return this.countryzone;
	}

	public void setCountryzone(String countryzone) {
		this.countryzone = countryzone;
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

	public String getEntitytype() {
		return this.entitytype;
	}

	public void setEntitytype(String entitytype) {
		this.entitytype = entitytype;
	}

	public String getGlbdef1() {
		return this.glbdef1;
	}

	public void setGlbdef1(String glbdef1) {
		this.glbdef1 = glbdef1;
	}

	public String getGlbdef10() {
		return this.glbdef10;
	}

	public void setGlbdef10(String glbdef10) {
		this.glbdef10 = glbdef10;
	}

	public String getGlbdef11() {
		return this.glbdef11;
	}

	public void setGlbdef11(String glbdef11) {
		this.glbdef11 = glbdef11;
	}

	public String getGlbdef12() {
		return this.glbdef12;
	}

	public void setGlbdef12(String glbdef12) {
		this.glbdef12 = glbdef12;
	}

	public String getGlbdef13() {
		return this.glbdef13;
	}

	public void setGlbdef13(String glbdef13) {
		this.glbdef13 = glbdef13;
	}

	public String getGlbdef14() {
		return this.glbdef14;
	}

	public void setGlbdef14(String glbdef14) {
		this.glbdef14 = glbdef14;
	}

	public String getGlbdef15() {
		return this.glbdef15;
	}

	public void setGlbdef15(String glbdef15) {
		this.glbdef15 = glbdef15;
	}

	public long getGlbdef16() {
		return this.glbdef16;
	}

	public void setGlbdef16(long glbdef16) {
		this.glbdef16 = glbdef16;
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

	public String getGlbdef5() {
		return this.glbdef5;
	}

	public void setGlbdef5(String glbdef5) {
		this.glbdef5 = glbdef5;
	}

	public BigDecimal getGlbdef6() {
		return this.glbdef6;
	}

	public void setGlbdef6(BigDecimal glbdef6) {
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

	public String getInnercode() {
		return this.innercode;
	}

	public void setInnercode(String innercode) {
		this.innercode = innercode;
	}

	public String getIsbusinessunit() {
		return this.isbusinessunit;
	}

	public void setIsbusinessunit(String isbusinessunit) {
		this.isbusinessunit = isbusinessunit;
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

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getNcindustry() {
		return this.ncindustry;
	}

	public void setNcindustry(String ncindustry) {
		this.ncindustry = ncindustry;
	}

	public String getOrganizationcode() {
		return this.organizationcode;
	}

	public void setOrganizationcode(String organizationcode) {
		this.organizationcode = organizationcode;
	}

	public String getOrgtype1() {
		return this.orgtype1;
	}

	public void setOrgtype1(String orgtype1) {
		this.orgtype1 = orgtype1;
	}

	public String getOrgtype10() {
		return this.orgtype10;
	}

	public void setOrgtype10(String orgtype10) {
		this.orgtype10 = orgtype10;
	}

	public String getOrgtype11() {
		return this.orgtype11;
	}

	public void setOrgtype11(String orgtype11) {
		this.orgtype11 = orgtype11;
	}

	public String getOrgtype12() {
		return this.orgtype12;
	}

	public void setOrgtype12(String orgtype12) {
		this.orgtype12 = orgtype12;
	}

	public String getOrgtype13() {
		return this.orgtype13;
	}

	public void setOrgtype13(String orgtype13) {
		this.orgtype13 = orgtype13;
	}

	public String getOrgtype14() {
		return this.orgtype14;
	}

	public void setOrgtype14(String orgtype14) {
		this.orgtype14 = orgtype14;
	}

	public String getOrgtype15() {
		return this.orgtype15;
	}

	public void setOrgtype15(String orgtype15) {
		this.orgtype15 = orgtype15;
	}

	public String getOrgtype16() {
		return this.orgtype16;
	}

	public void setOrgtype16(String orgtype16) {
		this.orgtype16 = orgtype16;
	}

	public String getOrgtype17() {
		return this.orgtype17;
	}

	public void setOrgtype17(String orgtype17) {
		this.orgtype17 = orgtype17;
	}

	public String getOrgtype18() {
		return this.orgtype18;
	}

	public void setOrgtype18(String orgtype18) {
		this.orgtype18 = orgtype18;
	}

	public String getOrgtype19() {
		return this.orgtype19;
	}

	public void setOrgtype19(String orgtype19) {
		this.orgtype19 = orgtype19;
	}

	public String getOrgtype2() {
		return this.orgtype2;
	}

	public void setOrgtype2(String orgtype2) {
		this.orgtype2 = orgtype2;
	}

	public String getOrgtype20() {
		return this.orgtype20;
	}

	public void setOrgtype20(String orgtype20) {
		this.orgtype20 = orgtype20;
	}

	public String getOrgtype21() {
		return this.orgtype21;
	}

	public void setOrgtype21(String orgtype21) {
		this.orgtype21 = orgtype21;
	}

	public String getOrgtype22() {
		return this.orgtype22;
	}

	public void setOrgtype22(String orgtype22) {
		this.orgtype22 = orgtype22;
	}

	public String getOrgtype23() {
		return this.orgtype23;
	}

	public void setOrgtype23(String orgtype23) {
		this.orgtype23 = orgtype23;
	}

	public String getOrgtype24() {
		return this.orgtype24;
	}

	public void setOrgtype24(String orgtype24) {
		this.orgtype24 = orgtype24;
	}

	public String getOrgtype25() {
		return this.orgtype25;
	}

	public void setOrgtype25(String orgtype25) {
		this.orgtype25 = orgtype25;
	}

	public String getOrgtype26() {
		return this.orgtype26;
	}

	public void setOrgtype26(String orgtype26) {
		this.orgtype26 = orgtype26;
	}

	public String getOrgtype27() {
		return this.orgtype27;
	}

	public void setOrgtype27(String orgtype27) {
		this.orgtype27 = orgtype27;
	}

	public String getOrgtype28() {
		return this.orgtype28;
	}

	public void setOrgtype28(String orgtype28) {
		this.orgtype28 = orgtype28;
	}

	public String getOrgtype29() {
		return this.orgtype29;
	}

	public void setOrgtype29(String orgtype29) {
		this.orgtype29 = orgtype29;
	}

	public String getOrgtype3() {
		return this.orgtype3;
	}

	public void setOrgtype3(String orgtype3) {
		this.orgtype3 = orgtype3;
	}

	public String getOrgtype30() {
		return this.orgtype30;
	}

	public void setOrgtype30(String orgtype30) {
		this.orgtype30 = orgtype30;
	}

	public String getOrgtype31() {
		return this.orgtype31;
	}

	public void setOrgtype31(String orgtype31) {
		this.orgtype31 = orgtype31;
	}

	public String getOrgtype32() {
		return this.orgtype32;
	}

	public void setOrgtype32(String orgtype32) {
		this.orgtype32 = orgtype32;
	}

	public String getOrgtype33() {
		return this.orgtype33;
	}

	public void setOrgtype33(String orgtype33) {
		this.orgtype33 = orgtype33;
	}

	public String getOrgtype34() {
		return this.orgtype34;
	}

	public void setOrgtype34(String orgtype34) {
		this.orgtype34 = orgtype34;
	}

	public String getOrgtype35() {
		return this.orgtype35;
	}

	public void setOrgtype35(String orgtype35) {
		this.orgtype35 = orgtype35;
	}

	public String getOrgtype36() {
		return this.orgtype36;
	}

	public void setOrgtype36(String orgtype36) {
		this.orgtype36 = orgtype36;
	}

	public String getOrgtype37() {
		return this.orgtype37;
	}

	public void setOrgtype37(String orgtype37) {
		this.orgtype37 = orgtype37;
	}

	public String getOrgtype38() {
		return this.orgtype38;
	}

	public void setOrgtype38(String orgtype38) {
		this.orgtype38 = orgtype38;
	}

	public String getOrgtype39() {
		return this.orgtype39;
	}

	public void setOrgtype39(String orgtype39) {
		this.orgtype39 = orgtype39;
	}

	public String getOrgtype4() {
		return this.orgtype4;
	}

	public void setOrgtype4(String orgtype4) {
		this.orgtype4 = orgtype4;
	}

	public String getOrgtype40() {
		return this.orgtype40;
	}

	public void setOrgtype40(String orgtype40) {
		this.orgtype40 = orgtype40;
	}

	public String getOrgtype5() {
		return this.orgtype5;
	}

	public void setOrgtype5(String orgtype5) {
		this.orgtype5 = orgtype5;
	}

	public String getOrgtype6() {
		return this.orgtype6;
	}

	public void setOrgtype6(String orgtype6) {
		this.orgtype6 = orgtype6;
	}

	public String getOrgtype7() {
		return this.orgtype7;
	}

	public void setOrgtype7(String orgtype7) {
		this.orgtype7 = orgtype7;
	}

	public String getOrgtype8() {
		return this.orgtype8;
	}

	public void setOrgtype8(String orgtype8) {
		this.orgtype8 = orgtype8;
	}

	public String getOrgtype9() {
		return this.orgtype9;
	}

	public void setOrgtype9(String orgtype9) {
		this.orgtype9 = orgtype9;
	}

	public String getPkAccperiodscheme() {
		return this.pkAccperiodscheme;
	}

	public void setPkAccperiodscheme(String pkAccperiodscheme) {
		this.pkAccperiodscheme = pkAccperiodscheme;
	}

	public String getPkControlarea() {
		return this.pkControlarea;
	}

	public void setPkControlarea(String pkControlarea) {
		this.pkControlarea = pkControlarea;
	}

	public String getPkCorp() {
		return this.pkCorp;
	}

	public void setPkCorp(String pkCorp) {
		this.pkCorp = pkCorp;
	}

	public String getPkCurrtype() {
		return this.pkCurrtype;
	}

	public void setPkCurrtype(String pkCurrtype) {
		this.pkCurrtype = pkCurrtype;
	}

	public String getPkExratescheme() {
		return this.pkExratescheme;
	}

	public void setPkExratescheme(String pkExratescheme) {
		this.pkExratescheme = pkExratescheme;
	}

	public String getPkFatherorg() {
		return this.pkFatherorg;
	}

	public void setPkFatherorg(String pkFatherorg) {
		this.pkFatherorg = pkFatherorg;
	}

	public String getPkFormat() {
		return this.pkFormat;
	}

	public void setPkFormat(String pkFormat) {
		this.pkFormat = pkFormat;
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

	public String getPkTimezone() {
		return this.pkTimezone;
	}

	public void setPkTimezone(String pkTimezone) {
		this.pkTimezone = pkTimezone;
	}

	public String getPkVid() {
		return this.pkVid;
	}

	public void setPkVid(String pkVid) {
		this.pkVid = pkVid;
	}

	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getReportconfirm() {
		return this.reportconfirm;
	}

	public void setReportconfirm(String reportconfirm) {
		this.reportconfirm = reportconfirm;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
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

	public String getWorkcalendar() {
		return this.workcalendar;
	}

	public void setWorkcalendar(String workcalendar) {
		this.workcalendar = workcalendar;
	}

	public String getGlbdef17() {
		return this.glbdef17;
	}

	public void setGlbdef17(String glbdef17) {
		this.glbdef17 = glbdef17;
	}

	public String getGlbdef18() {
		return this.glbdef18;
	}

	public void setGlbdef18(String glbdef18) {
		this.glbdef18 = glbdef18;
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
