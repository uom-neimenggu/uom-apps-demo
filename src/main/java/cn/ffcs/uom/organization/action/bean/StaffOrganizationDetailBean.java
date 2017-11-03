package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.party.component.PartyBandboxExt;
import cn.ffcs.uom.politicallocation.component.PoliticalLocationTreeBandbox;
import cn.ffcs.uom.staff.component.StaffBandboxExt;
import cn.ffcs.uom.staff.component.StaffExtendAttrExt;

/**
 *组织编辑Bean
 * 
 * @author
 **/
public class StaffOrganizationDetailBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window staffOrganizationDetailWindow;

	@Getter
	@Setter
	private OrganizationBandboxExt org;

	@Getter
	@Setter
	private StaffBandboxExt staffBandboxExt;

	@Getter
	@Setter
	private Listbox ralaCd;

	@Getter
	@Setter
	private Spinner staffSeq;

	@Getter
	@Setter
	private Textbox userCode;

	@Getter
	@Setter
	private Textbox note;
	
	@Getter
	@Setter
	private Textbox reason;

	/****
     * =======================
     * 参与人信息
     * =======================
     */
	/**
     * 参与人名称.
     */
    @Setter
    @Getter
    private Textbox partyName;
    
	/**
     * 参与人曾用名.
     */
    @Setter
    @Getter
    private Textbox partyNameFirst;
    /**
     * 名称简拼.
     */
    @Setter
    @Getter
    private Textbox partyAbbrname;
    
    /**
     * 英文名称.
     */
    @Setter
    @Getter
    private Textbox englishName;
    
    /**
     * 参与人类型.
     */
    @Setter
    @Getter
    private Listbox partyType;
    
    /**
     * 参与人角色类型.
     */
    @Setter
    @Getter
    private TreeChooserBandbox roleType;
	
    /****
     * =======================
     * 员工信息
     * =======================
     */
    /**
     * 行政区域标识
     */
	@Getter
	@Setter
	private PoliticalLocationTreeBandbox locationId;

    /**
     * 参与人角色
     */
	@Getter
	@Setter
	private PartyBandboxExt partyBandboxExt;
	
    /**
     * 用工性质
     */
	@Getter
	@Setter
	private TreeChooserBandbox workProp;

    /**
     * 员工编码
     */
    @Setter
    @Getter
    private Textbox staffCode;
    
    /**
     * 员工名称
     */
    @Setter
    @Getter
    private Textbox staffName;
    
    /**
     * 兼职/全职
     */
    @Setter
    @Getter
    private Listbox partTime;

    /**
     * 员工职位
     */
    @Setter
    @Getter
    private Listbox staffPosition;

    /**
     * 职务标注
     */
    @Setter
    @Getter
    private Textbox titleNote;

    /**
     * 员工描述
     */
    @Setter
    @Getter
    private Textbox staffDesc;
    
    /**
     * 用工性质
     */
	@Getter
	@Setter
	private TreeChooserBandbox staffProperty;
    
    /****
     * =======================
     * 员工账号
     * =======================
     */
    /**
     * 员工账号.
     */
    @Setter
    @Getter
    private Textbox staffNbr;
    
    @Setter
    @Getter
    private Textbox staffPassword;
    
    /****
     * =======================
     * 员工拓展属性
     * =======================
     */
    /**
     * 员工扩展属性
     */
    @Setter
    @Getter
    private StaffExtendAttrExt staffExtendAttrExt;
    
    /****
     * =======================
     * 个人信息
     * =======================
     */
    /**
     * 出生日期.
     */
    @Setter
    @Getter
    private Datebox birthday;
    
    /**
     * 婚姻状况.
     */
    @Setter
    @Getter
    private TreeChooserBandbox marriageStatus;
    
    /**
     * 政治面貌.
     */
    @Setter
    @Getter
    private Listbox politicsStatus;
    
    /**
     * 教育水平.
     */
    @Setter
    @Getter
    private Listbox educationLevel;
    
    /**
     * 性别.
     */
    @Setter
    @Getter
    private Listbox gender;
    
    /**
     * 国籍.
     */
    @Setter
    @Getter
    private TreeChooserBandbox nationality;

    /**
     * 民族.
     */
    @Setter
    @Getter
    private Listbox nation;
    
    /**
     * 祖籍.
     */
    @Setter
    @Getter
    private Textbox nativePlace;
    /**
     * 单位.
     */
    @Setter
    @Getter
    private Textbox employer; 
    /**
     * 宗教.
     */
    @Setter
    @Getter
    private Listbox religion; 
    /**
     * 同名编码.
     */
    @Setter
    @Getter
    private Textbox sameNameCode;
    
    
    @Setter
    @Getter
    private Panel pel;
    
    @Setter
    @Getter
    private Panel orgs;
    
    /**
     * 参与人组织类型.
     */
    @Setter
    @Getter
    private Listbox orgType;
    
    /**
     *组织简介.
     */
    @Setter
    @Getter
    private Textbox orgContent;
    
    /**
     *组织规模.
     */
    @Setter
    @Getter
    private Listbox orgScale;    
    
    /**
     *负责人信息.
     */
    @Setter
    @Getter
    private Textbox principal;
}
