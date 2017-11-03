package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.Date;

import cn.ffcs.uom.common.model.UomEntity;

/**
 * MdsionAccessConfig entity. @author MyEclipse Persistence Tools
 */

public class MdsionAccessConfig extends UomEntity
    implements Serializable {
    
    // Fields
    
    private long   mdsionAccessConfigId;
    private long   mdsionOrgTreeId;
    private long   businessSystemId;
    private Date   lastTime;
    
    // Constructors
    
    /** default constructor */
    public MdsionAccessConfig() {
    }
    
    /** minimal constructor */
    public MdsionAccessConfig(long mdsionAccessConfigId) {
        this.mdsionAccessConfigId = mdsionAccessConfigId;
    }
    
    
    // Property accessors
    
    public long getMdsionAccessConfigId() {
        return this.mdsionAccessConfigId;
    }
    
    public void setMdsionAccessConfigId(long mdsionAccessConfigId) {
        this.mdsionAccessConfigId = mdsionAccessConfigId;
    }
    
    public long getMdsionOrgTreeId() {
        return this.mdsionOrgTreeId;
    }
    
    public void setMdsionOrgTreeId(long mdsionOrgTreeId) {
        this.mdsionOrgTreeId = mdsionOrgTreeId;
    }
    
    public long getBusinessSystemId() {
        return this.businessSystemId;
    }
    
    public void setBusinessSystemId(long businessSystemId) {
        this.businessSystemId = businessSystemId;
    }
    
    public Date getLastTime() {
        return this.lastTime;
    }
    
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
    
}
