package cn.ffcs.uom.organization.model;

// Generated 2017-6-13 12:02:44 by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;

public class HanaTelDeptCostCon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4651056215014209432L;

	private Long id;
	private int monthId;
	private String unitId;
	private String pkDeptCostCon;
	private String pkOrg;
	private String pkDept;
	private String pkCost;
	private String defStr1;
	private String defStr2;
	private String defStr3;
	private String defStr4;
	private String defStr5;
	private String pkGroup;
	private String ts;
	private Long dr;
	private Date loadDate;
	private Long etlDay;
	private Byte localPartitionId;

	/**
	 * 构造方法
	 */
	public HanaTelDeptCostCon() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return HanaOrgDept
	 */
	public static HanaTelDeptCostCon newInstance() {
		return new HanaTelDeptCostCon();
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
	 * @param id
	 *            the id to set
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

	public String getPkDeptCostCon() {
		return this.pkDeptCostCon;
	}

	public void setPkDeptCostCon(String pkDeptCostCon) {
		this.pkDeptCostCon = pkDeptCostCon;
	}

	public String getPkOrg() {
		return this.pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkDept() {
		return this.pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkCost() {
		return this.pkCost;
	}

	public void setPkCost(String pkCost) {
		this.pkCost = pkCost;
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

	public String getPkGroup() {
		return this.pkGroup;
	}

	public void setPkGroup(String pkGroup) {
		this.pkGroup = pkGroup;
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

	/**
	 * 获取部门
	 * 
	 * @return
	 */
	public String getPkDeptName() {
		if (!StrUtil.isEmpty(this.getPkDept())) {
			String sql = "SELECT a.* FROM HANA_ORG_DEPT a WHERE a.pk_dept = ? AND a.month_id = ? ";
			List<Object> params = new ArrayList<Object>();
			params.add(this.getPkDept());
			params.add(this.getMonthId());

			List<HanaOrgDept> list = HanaOrgDept.repository().jdbcFindList(sql,
					params, HanaOrgDept.class);

			if (list != null && list.size() > 0) {
				return list.get(0).getName();
			}
		}
		return "";
	}
	
	/**
	 * 获取成本中心
	 * 
	 * @return
	 */
	public String getPkCostName() {
		if (!StrUtil.isEmpty(this.getPkCost())) {
			String sql = "SELECT a.* FROM HANA_TEL_SYNC_COSTCENTER a WHERE a.kostl = ? AND a.month_id = ? ";
			List<Object> params = new ArrayList<Object>();
			params.add(this.getPkCost());
			params.add(this.getMonthId());

			List<HanaTelSyncCostcenter> list = HanaTelSyncCostcenter.repository().jdbcFindList(sql,
					params, HanaTelSyncCostcenter.class);

			if (list != null && list.size() > 0) {
				return list.get(0).getKtext();
			}
		}
		return "";
	}
}
