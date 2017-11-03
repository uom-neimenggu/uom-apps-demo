package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

/**
 * MdsionOrgTree entity. @author MyEclipse Persistence Tools
 */

public class MdsionOrgTree extends UomEntity implements Serializable {
    
    // Fields
    
    private long   mdsionOrgTreeId;
    private String orgTreeName;
    private long   orgId;
    private String mdsionOrgRelTypeCd;
    private long isShow;
    
    // Constructors
    
    /** default constructor */
    public MdsionOrgTree() {
    }
    
    
    // Property accessors
    
    public long getMdsionOrgTreeId() {
        return this.mdsionOrgTreeId;
    }
    
    public void setMdsionOrgTreeId(long mdsionOrgTreeId) {
        this.mdsionOrgTreeId = mdsionOrgTreeId;
    }
    
    public String getOrgTreeName() {
        return this.orgTreeName;
    }
    
    public void setOrgTreeName(String orgTreeName) {
        this.orgTreeName = orgTreeName;
    }
    
    public long getOrgId() {
        return this.orgId;
    }
    
    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }
    
    public String getMdsionOrgRelTypeCd() {
        return this.mdsionOrgRelTypeCd;
    }
    
    public void setMdsionOrgRelTypeCd(String mdsionOrgRelTypeCd) {
        this.mdsionOrgRelTypeCd = mdsionOrgRelTypeCd;
    }
    
    public static BaseDao repository() {
        return (BaseDao) ApplicationContextUtil
                .getBean("baseDao");
    }
    public Organization getOrganization() {
        if (this.orgId != 0L) {
            return (Organization) Organization.repository().getObject(
                    Organization.class, this.orgId);
        }
        return null;
    }

    public long getIsShow() {
        return isShow;
    }
    public void setIsShow(long isShow) {
        this.isShow = isShow;
    }
}
