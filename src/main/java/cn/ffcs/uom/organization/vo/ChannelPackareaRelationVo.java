package cn.ffcs.uom.organization.vo;

import java.util.Date;
import java.util.List;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年9月19日
 * @功能说明：包区和网点关系vo
 *
 */
public class ChannelPackareaRelationVo {
    
    /**
     * 关系id
     */
    @Setter
    @Getter
    private Long id;
    
    //关于渠道部分
    /**
     * 代理商渠道标识
     */
    @Setter
    @Getter
    private Long agentChannelId;
    
    /**
     * 渠道编码
     */
    @Setter
    @Getter
    private String agentChannelNbr;
    
    /**
     * 渠道名称
     */
    @Setter
    @Getter
    private String agentChannelName;
    
    /**
     * 集团编码
     */
    @Setter
    @Getter
    private String agentChannelGroupCode;
    
    /**
     * 渠道划小组织类型编码,默认这个
     */
    @Setter
    @Getter
    private String tranRelaType = "100300";
    
    /**
     * 渠道划小组织类型
     */
    @Setter
    @Getter
    private String tranRelaTypeValue;
    
    /**
     * 以店包区标识
     */
    @Setter
    @Getter
    private String storeAreaFlag;
    
    /**
     * 渠道状态
     */
    @Setter
    @Getter
    private String statusCd;
    
    /**
     * 更新时间
     */
    @Setter
    @Getter
    private Date updateDate;
    
    /**
     * 营销包区id
     */
    @Setter
    @Getter
    private String custPackareaId;
    
    
    /**
     * 组织名称
     */
    @Setter
    @Getter
    private String custPackareaName;
    
    /**
     * 组织编码
     */
    @Setter
    @Getter
    private String custPackareaCode;
    
    /**
     * eadCode
     */
    @Setter
    @Getter
    private String custPackareaEdaCode;
    
    /**
     * 账号所在管理区域，限定账号只能查询规定区域下信息
     */
    @Setter
    @Getter
    private Long telcomRegionId;
    
    /**
     * 批量操作的时候，判断是增加修改删除或其他
     */
    @Getter
    @Setter
    private String operatorType;
    
    /**
     * 做审批的时候，对应
     */
    @Getter
    @Setter
    private Long processId;
    
    /**
     * 获取属性值
     * 
     * @return
     */
    public String getStatusCdName() {
        List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
                "UomEntity", "statusCd", this.getStatusCd(),
                BaseUnitConstants.ENTT_STATE_ACTIVE);
        if (list != null && list.size() > 0
                && !StrUtil.isEmpty(this.getStatusCd())) {
            return list.get(0).getAttrValueName();
        }
        return "";
    }
    
    public String getStoreAreaFlagName() {
    	List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
                "UomGroupOrgTran", "storeAreaFlag", this.getStoreAreaFlag(),
                BaseUnitConstants.ENTT_STATE_ACTIVE);
        if (list != null && list.size() > 0
                && !StrUtil.isEmpty(this.getStatusCd())) {
            return list.get(0).getAttrValueName();
        }
        return "";
	}
    
}
