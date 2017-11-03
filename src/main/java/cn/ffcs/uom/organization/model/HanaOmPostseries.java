package cn.ffcs.uom.organization.model;

// Generated 2017-6-13 12:02:44 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;


public class HanaOmPostseries implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6122535855787941083L;
	
	private Long id;
	private int monthId;
	private String unitId;
	private String creationtime;
	private String creator;
	private BigDecimal enablestate;
	private String fatherPk;
	private String innercode;
	private String modifiedtime;
	private String modifier;
	private String pkGroup;
	private String pkOrg;
	private String pkPostseries;
	private String postseriescode;
	private String postseriesdesc;
	private String postseriesname;
	private String seq;
	private String postseriesname2;
	private String postseriesname3;
	private String postseriesname4;
	private String postseriesname5;
	private String postseriesname6;
	private String ts;
	private Long dr;
	private BigDecimal dataoriginflag;
	private Date loadDate;
	private Long etlDay;
	private Byte localPartitionId;
	
	/**
	 * 构造方法
	 */
	public HanaOmPostseries() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return HanaOrgDept
	 */
	public static HanaOmPostseries newInstance() {
		return new HanaOmPostseries();
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

	public BigDecimal getEnablestate() {
		return this.enablestate;
	}

	public void setEnablestate(BigDecimal enablestate) {
		this.enablestate = enablestate;
	}

	public String getFatherPk() {
		return this.fatherPk;
	}

	public void setFatherPk(String fatherPk) {
		this.fatherPk = fatherPk;
	}

	public String getInnercode() {
		return this.innercode;
	}

	public void setInnercode(String innercode) {
		this.innercode = innercode;
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

	public String getPkPostseries() {
		return this.pkPostseries;
	}

	public void setPkPostseries(String pkPostseries) {
		this.pkPostseries = pkPostseries;
	}

	public String getPostseriescode() {
		return this.postseriescode;
	}

	public void setPostseriescode(String postseriescode) {
		this.postseriescode = postseriescode;
	}

	public String getPostseriesdesc() {
		return this.postseriesdesc;
	}

	public void setPostseriesdesc(String postseriesdesc) {
		this.postseriesdesc = postseriesdesc;
	}

	public String getPostseriesname() {
		return this.postseriesname;
	}

	public void setPostseriesname(String postseriesname) {
		this.postseriesname = postseriesname;
	}

	public String getSeq() {
		return this.seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getPostseriesname2() {
		return this.postseriesname2;
	}

	public void setPostseriesname2(String postseriesname2) {
		this.postseriesname2 = postseriesname2;
	}

	public String getPostseriesname3() {
		return this.postseriesname3;
	}

	public void setPostseriesname3(String postseriesname3) {
		this.postseriesname3 = postseriesname3;
	}

	public String getPostseriesname4() {
		return this.postseriesname4;
	}

	public void setPostseriesname4(String postseriesname4) {
		this.postseriesname4 = postseriesname4;
	}

	public String getPostseriesname5() {
		return this.postseriesname5;
	}

	public void setPostseriesname5(String postseriesname5) {
		this.postseriesname5 = postseriesname5;
	}

	public String getPostseriesname6() {
		return this.postseriesname6;
	}

	public void setPostseriesname6(String postseriesname6) {
		this.postseriesname6 = postseriesname6;
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

	public BigDecimal getDataoriginflag() {
		return this.dataoriginflag;
	}

	public void setDataoriginflag(BigDecimal dataoriginflag) {
		this.dataoriginflag = dataoriginflag;
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
