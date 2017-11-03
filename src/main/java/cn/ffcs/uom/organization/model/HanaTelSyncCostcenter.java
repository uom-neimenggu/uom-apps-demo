package cn.ffcs.uom.organization.model;

// Generated 2017-6-13 12:02:44 by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;


public class HanaTelSyncCostcenter implements Serializable {
	
	private static final long serialVersionUID = -5665092737307568515L;
	
	private Long id;
	private int monthId;
	private String unitId;
	private String pkCostcenter;
	private String kostl;
	private String bukrs;
	private String datbi;
	private String datab;
	private String ktext;
	private String ltext;
	private String verak;
	private String abtei;
	private String kosar;
	private String khinr;
	private String gsber;
	private String funcArea;
	private String waersq;
	private String prctr;
	private String flowSign;
	private String remark1;
	private String remark2;
	private String remark3;
	private String remark4;
	private String remark5;
	private String kokrs;
	private String remark6;
	private String remark7;
	private String remark8;
	private String remark9;
	private String remark10;
	private String datab2;
	private String datbi2;
	private String datastatus;
	private String isVirtual;
	private String ts;
	private Long dr;
	private Date loadDate;
	private Long etlDay;
	private Byte localPartitionId;

	/**
	 * 构造方法
	 */
	public HanaTelSyncCostcenter() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return HanaTelSyncCostcenter
	 */
	public static HanaTelSyncCostcenter newInstance() {
		return new HanaTelSyncCostcenter();
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

	public String getPkCostcenter() {
		return this.pkCostcenter;
	}

	public void setPkCostcenter(String pkCostcenter) {
		this.pkCostcenter = pkCostcenter;
	}

	public String getKostl() {
		return this.kostl;
	}

	public void setKostl(String kostl) {
		this.kostl = kostl;
	}

	public String getBukrs() {
		return this.bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getDatbi() {
		return this.datbi;
	}

	public void setDatbi(String datbi) {
		this.datbi = datbi;
	}

	public String getDatab() {
		return this.datab;
	}

	public void setDatab(String datab) {
		this.datab = datab;
	}

	public String getKtext() {
		return this.ktext;
	}

	public void setKtext(String ktext) {
		this.ktext = ktext;
	}

	public String getLtext() {
		return this.ltext;
	}

	public void setLtext(String ltext) {
		this.ltext = ltext;
	}

	public String getVerak() {
		return this.verak;
	}

	public void setVerak(String verak) {
		this.verak = verak;
	}

	public String getAbtei() {
		return this.abtei;
	}

	public void setAbtei(String abtei) {
		this.abtei = abtei;
	}

	public String getKosar() {
		return this.kosar;
	}

	public void setKosar(String kosar) {
		this.kosar = kosar;
	}

	public String getKhinr() {
		return this.khinr;
	}

	public void setKhinr(String khinr) {
		this.khinr = khinr;
	}

	public String getGsber() {
		return this.gsber;
	}

	public void setGsber(String gsber) {
		this.gsber = gsber;
	}

	public String getFuncArea() {
		return this.funcArea;
	}

	public void setFuncArea(String funcArea) {
		this.funcArea = funcArea;
	}

	public String getWaersq() {
		return this.waersq;
	}

	public void setWaersq(String waersq) {
		this.waersq = waersq;
	}

	public String getPrctr() {
		return this.prctr;
	}

	public void setPrctr(String prctr) {
		this.prctr = prctr;
	}

	public String getFlowSign() {
		return this.flowSign;
	}

	public void setFlowSign(String flowSign) {
		this.flowSign = flowSign;
	}

	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return this.remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return this.remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return this.remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return this.remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public String getKokrs() {
		return this.kokrs;
	}

	public void setKokrs(String kokrs) {
		this.kokrs = kokrs;
	}

	public String getRemark6() {
		return this.remark6;
	}

	public void setRemark6(String remark6) {
		this.remark6 = remark6;
	}

	public String getRemark7() {
		return this.remark7;
	}

	public void setRemark7(String remark7) {
		this.remark7 = remark7;
	}

	public String getRemark8() {
		return this.remark8;
	}

	public void setRemark8(String remark8) {
		this.remark8 = remark8;
	}

	public String getRemark9() {
		return this.remark9;
	}

	public void setRemark9(String remark9) {
		this.remark9 = remark9;
	}

	public String getRemark10() {
		return this.remark10;
	}

	public void setRemark10(String remark10) {
		this.remark10 = remark10;
	}

	public String getDatab2() {
		return this.datab2;
	}

	public void setDatab2(String datab2) {
		this.datab2 = datab2;
	}

	public String getDatbi2() {
		return this.datbi2;
	}

	public void setDatbi2(String datbi2) {
		this.datbi2 = datbi2;
	}

	public String getDatastatus() {
		return this.datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getIsVirtual() {
		return this.isVirtual;
	}

	public void setIsVirtual(String isVirtual) {
		this.isVirtual = isVirtual;
	}

	public String getTs() {
		return this.ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public Long getDr() {
		return this.dr;
	}

	public void setDr(Long dr) {
		this.dr = dr;
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
