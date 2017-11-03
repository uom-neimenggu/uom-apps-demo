package cn.ffcs.uom.party.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 参与人角色
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-29
 * @功能说明：
 *
 */
public class PartyRole extends UomEntity implements Serializable{

    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 参与人角色标识
     */    
    public Long getPartyRoleId() {
		return super.getId();
	}

	public void setPartyRoleId(Long partyRoleId) {
		super.setId(partyRoleId);
	}
	/**
     * 参与人标识
     */
    @Getter
    @Setter
    private Long partyId;
    
    /**
     * 参与人角色类型
     */
    @Getter
    @Setter
    private String roleType;
    
    public PartyRole(){
    	super();
    }

    public static PartyRole newInstance(){
       return new PartyRole();
    }
    
    public String getRoleTypeName(){
        if(!StrUtil.isNullOrEmpty(this.getRoleType())){
            List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
                "PartyRole", "roleType", this.getRoleType(),
                BaseUnitConstants.ENTT_STATE_ACTIVE);
            if (list != null && list.size() > 0) {
                return list.get(0).getAttrValueName();
            }
        }
        return "";
    }
}
