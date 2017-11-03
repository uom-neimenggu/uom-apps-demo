package cn.ffcs.uom.organization.vo;

import java.util.ArrayList;
import java.util.List;

import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年9月9日
 * @功能说明：
 *
 */
public class OrganizationHXImportVo {
    /**
     * 存放等待上传的划小单元组织数据列表
     */
    private Organization hxOrganization;
    
    /**
     * 如果是网格单元的话就要设定这个属性
     */
    private List<OrganizationExtendAttr> hxOrganizationExtendAttrList;

    public Organization getHxOrganization() {
        return hxOrganization;
    }

    public void setHxOrganization(Organization hxOrganization) {
        this.hxOrganization = hxOrganization;
    }

    public List<OrganizationExtendAttr> getHxOrganizationExtendAttrList() {
        return hxOrganizationExtendAttrList;
    }

    public void setHxOrganizationExtendAttrList(
        List<OrganizationExtendAttr> hxOrganizationExtendAttrList) {
        this.hxOrganizationExtendAttrList = hxOrganizationExtendAttrList;
    }

}
