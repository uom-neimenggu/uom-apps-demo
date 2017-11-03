package cn.ffcs.uom.party.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import cn.ffcs.uom.common.model.UomEntity;

/**
 * 参与人组织
 * .
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-1
 * @功能说明：
 *
 */
public class PartyOrganization extends UomEntity implements Serializable {

    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 组织参与人标识
     */    
	public Long getPratyOrganizationId() {
		return super.getId();
	}

	public void setPratyOrganizationId(Long pratyOrganizationId) {
		super.setId(pratyOrganizationId);
	}
	/**
     * 参与人标识
     */
    @Getter
    @Setter
    private Long partyId;
    
    /**
     * 参与人组织类型
     */
    @Getter
    @Setter
    private String orgType;
    
    
    /**
     * 组织简介
     */
    @Getter
    @Setter
    private String orgContent;
    
    /**
     * 组织规模
     */
    @Getter
    @Setter
    private String orgScale;
    
    /**
     * 负责人信息
     */
    @Getter
    @Setter
    private String principal;
}
