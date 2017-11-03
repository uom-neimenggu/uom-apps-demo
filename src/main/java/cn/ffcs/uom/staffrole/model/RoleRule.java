package cn.ffcs.uom.staffrole.model;

import java.math.BigDecimal;

/**
 * RoleRuleId entity. @author MyEclipse Persistence Tools
 */

public class RoleRule implements java.io.Serializable {
    
    // Fields
    
    private long roleRuleId;
    private String     ruleType;
    private long     role;
    private long     roleRela;
    private String     message;
    private String     statusCd;
    
    // Constructors
    
    /** default constructor */
    public RoleRule() {
    }
    
    /** full constructor */
    public RoleRule(long roleRuleId, String ruleType, long role, long roleRela,
        String statusCd) {
        this.roleRuleId = roleRuleId;
        this.ruleType = ruleType;
        this.role = role;
        this.roleRela = roleRela;
        this.statusCd = statusCd;
    }
    
    // Property accessors
    
    public long getRoleRuleId() {
        return this.roleRuleId;
    }
    
    public void setRoleRuleId(long roleRuleId) {
        this.roleRuleId = roleRuleId;
    }
    
    public String getRuleType() {
        return this.ruleType;
    }
    
    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }
    
    public long getRole() {
        return this.role;
    }
    
    public void setRole(long role) {
        this.role = role;
    }
    
    public long getRoleRela() {
        return this.roleRela;
    }
    
    public void setRoleRela(long roleRela) {
        this.roleRela = roleRela;
    }
    
    public String getStatusCd() {
        return this.statusCd;
    }
    
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    } 
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }    
}
