package cn.ffcs.uom.party.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;

/**
 * 参与人、参与人证件类型、参与人联系方式 ZUL Bean
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-30
 * @功能说明：
 *
 */
public class PartyDetailBean {

    /**
     * Window
     */
    @Setter
    @Getter
    private Window partyDetailWindow;
    /**
     * 参与人名称.
     */
    @Setter
    @Getter
    private Textbox partyName;
    
    /**
     * 名称简拼.
     */
    @Setter
    @Getter
    private Textbox partyAbbrname;
    
    /**
     * 曾用名.
     */
    @Setter
    @Getter
    private Textbox partyNameFirst;
    
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
    /**
     *保存.
     */
    @Setter
    @Getter
    private Button saveStaff;
    /**
     *取消.
     */
    @Setter
    @Getter
    private Button cancelStaff;
	/**
	 *编辑按钮.
	 **/
	@Getter
	@Setter
	private Toolbarbutton editButton;
	/**
	 *保存按钮.
	 **/
	@Getter
	@Setter
	private Toolbarbutton saveButton;
	/**
	 *回复按钮.
	 **/
	@Getter
	@Setter
	private Toolbarbutton recoverButton;
	
    @Getter
    @Setter
    private Toolbar btnToolBar;
	
    @Getter
    @Setter
    private Toolbar viewBtnTB;
    
}
