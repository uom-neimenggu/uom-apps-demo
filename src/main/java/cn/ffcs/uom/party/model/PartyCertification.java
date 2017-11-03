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
 * 参与人证件实体.
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-25
 * @功能说明：
 *
 */
public class PartyCertification extends UomEntity implements Serializable {

    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 参与人证件标识 PARTY_CERT_ID
     */
	public Long getPartyCertId() {
		return super.getId();
	}

	public void setPartyCertId(Long partyCertId) {
		super.setId(partyCertId);
	}
	/**
     * 参与人标识
     */
    @Getter
    @Setter
    private Long partyId;
    
    /**
     * 证件名
     */
    @Getter
    @Setter
    private String certName;
    
    /**
     * 是否实名
     */
    @Getter
    @Setter
    private String isRealName;
    
    /**
     * 证件类型
     */
    @Getter
    @Setter
    private String certType;
    /**
     * 发证机关
     */
    @Getter
    @Setter
    private String certOrg;
    
    /**
     * 证件地址
     */
    @Getter
    @Setter
    private String certAddress;
    
    /**
     * 证件号码
     */
    @Getter
    @Setter
    private String certNumber;
    
    /**
     * 证件种类.
     */
    @Getter
    @Setter
    private String certSort;
    
    /**
     * 类型ID.
     */
	@Getter
	@Setter
	private Long identityCardId;
	
	/**
	 * 原因
	 */
	@Setter
	@Getter
	private String reason;
	
	/**
	 * 修改员工接口是否需要更新
	 */
	@Setter
	@Getter
	private Boolean isNeedUpdate = false;
    public String getCertTypeName(){
        if(!StrUtil.isNullOrEmpty(this.getCertType())){
            List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
                "PartyCertification", "certType", this.getCertType(),
                BaseUnitConstants.ENTT_STATE_ACTIVE);
            if (list != null && list.size() > 0) {
                return list.get(0).getAttrValueName();
            }
        }
        return "";
    }
    
    
    public String getCertSortName(){
        if(!StrUtil.isNullOrEmpty(this.getCertSort())){
            List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
                "PartyCertification", "certSort", this.getCertSort(),
                BaseUnitConstants.ENTT_STATE_ACTIVE);
            if (list != null && list.size() > 0) {
                return list.get(0).getAttrValueName();
            }
        }
        return "";
    }
    
    public String getIsRealNameName(){
        if(!StrUtil.isNullOrEmpty(this.getIsRealName())){
            List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
                "PartyCertification", "isRealName", this.getIsRealName(),
                BaseUnitConstants.ENTT_STATE_ACTIVE);
            if (list != null && list.size() > 0) {
                return list.get(0).getAttrValueName();
            }
        }
        return "";
    }

	public static PartyCertification newInstance() {
		return new PartyCertification();
	}
}
