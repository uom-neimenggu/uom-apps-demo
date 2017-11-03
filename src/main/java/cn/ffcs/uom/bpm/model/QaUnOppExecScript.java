package cn.ffcs.uom.bpm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;

/**
 * QaUnOppExecScript entity. @author MyEclipse Persistence Tools
 */

public class QaUnOppExecScript implements TreeNodeEntity,Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4843979953675865930L;
	
	// Fields
    private Long execSctIdenti;
    private String execKidIdenti;
    private String execName;
    private String execKidName;
    private String execCode;
    private String checkType;
    private String systemCode;
    private String execSctDesc;
    private Date updateDate;
    private String statusCd;
    private String execUnMiddleId;
    private String execSctSql;
    
 
    /**
	 * 是否根节点
	 */
	@Setter
	private Boolean isRoot = false;
	
	
	/**
	 * 展示指定根节点标识
	 */
	@Setter
	private String flag;
    
    // Constructors
    
    /** default constructor */
    public QaUnOppExecScript() {
    }
    
    /** full constructor */
    public QaUnOppExecScript(String execName, String execCode, String checkType, String systemCode,
        String execSctDesc, String execSctSql, Date updateDate, String statusCd) {
        this.execName = execName;
        this.execCode = execCode;
        this.checkType = checkType;
        this.systemCode = systemCode;
        this.execSctDesc = execSctDesc;
        this.execSctSql = execSctSql;
        this.updateDate = updateDate;
        this.statusCd = statusCd;
    }
    
    // Property accessors
    
    public Long getExecSctIdenti() {
        return this.execSctIdenti;
    }
    
    public void setExecSctIdenti(Long execSctIdenti) {
        this.execSctIdenti = execSctIdenti;
    }
    
    public String getExecName() {
        return this.execName;
    }
    
    public void setExecName(String execName) {
        this.execName = execName;
    }
    
    public String getExecCode() {
        return this.execCode;
    }
    
    public void setExecCode(String execCode) {
        this.execCode = execCode;
    }
    
    public String getCheckType() {
        return this.checkType;
    }
    
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    
    public String getSystemCode() {
        return this.systemCode;
    }
    
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
    
    public String getExecSctDesc() {
        return this.execSctDesc;
    }
    
    public void setExecSctDesc(String execSctDesc) {
        this.execSctDesc = execSctDesc;
    }
    
    public String getExecSctSql() {
        return this.execSctSql;
    }
    
    public void setExecSctSql(String execSctSql) {
        this.execSctSql = execSctSql;
    }
    
    public Date getUpdateDate() {
        return this.updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public String getStatusCd() {
        return this.statusCd;
    }
    
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getExecUnMiddleId() {
        return execUnMiddleId;
    }

    public void setExecUnMiddleId(String execUnMiddleId) {
        this.execUnMiddleId = execUnMiddleId;
    }

	/**
	 * @return the execKidIdenti
	 */
	public String getExecKidIdenti() {
		return execKidIdenti;
	}

	/**
	 * @param execKidIdenti the execKidIdenti to set
	 */
	public void setExecKidIdenti(String execKidIdenti) {
		this.execKidIdenti = execKidIdenti;
	}

	/**
	 * @return the execKidName
	 */
	public String getExecKidName() {
		return execKidName;
	}

	/**
	 * @param execKidName the execKidName to set
	 */
	public void setExecKidName(String execKidName) {
		this.execKidName = execKidName;
	}

	@Override
	public boolean isGetRoot() {
		return this.isRoot;
	}
	
	@Getter
	@Setter
	private String zflag;
	
	//获取根节点
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		StringBuilder sql = new StringBuilder("select distinct t.exec_sct_identi,t.exec_sct_identi exec_kid_identi,t.exec_name exec_kid_name from QA_UN_OPP_EXEC_SCRIPT2 t WHERE T.STATUS_CD=?");
		
		@SuppressWarnings("rawtypes")
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		
		if(!StrUtil.isNullOrEmpty(flag)) {
			sql.append(" AND t.exec_sct_identi = ? ");		
			params.add(flag);
		}
		
		return (ArrayList<TreeNodeEntity>) DefaultDaoFactory
				.getDefaultDao().jdbcFindList(sql.toString(), params,
						QaUnOppExecScript.class);
	}
	
	//获取子节点
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		String sql = "SELECT * FROM QA_UN_OPP_EXEC_SCRIPT2 T WHERE T.STATUS_CD=? AND T.exec_sct_identi=? ";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getExecKidIdenti());	
		
		//如果是，拼接字符串直接过滤出想要的子节点的孙节点
//		if("10".equals(this.getExecKidIdenti())){
//			sql.append(" and exec_kid_identi in ('10027','10028','10029') ");
//		}

		
		return (ArrayList<TreeNodeEntity>) DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, QaUnOppExecScript.class);
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.execKidName)) {
			return this.execKidName;
		}
		return "";
	}

	public String getFlag() {
		return flag;
	}
    
	
}

