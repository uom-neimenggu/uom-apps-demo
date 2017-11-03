package cn.ffcs.uac.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;

public class UacStaffEditBean{
	@Setter
	@Getter
	private Window uacStaffEditWin;
	
	@Setter
	@Getter
	private Div uacStaffDiv;
	
	/******人员基本信息******/
	/**
	 * 人员名
	 */
	@Setter
	@Getter
	private Textbox staffName;
    
    /**
     * 人员性质
     */
    @Setter
    @Getter
    private TreeChooserBandbox property;
    
    /**
     * 类型
     */
    @Setter
    @Getter
    private Listbox type;
	
    /**
     * 认证账号
     */
    @Setter
    @Getter
    private Textbox account;
    
    /**
     * 人员编码
     */
    @Setter
    @Getter
    private Textbox ecode;    

    /**
     * 人力工号
     */
    @Setter
    @Getter
    private Textbox hrCode;
    
    /**
     * 集团人力guid
     */
    @Setter
    @Getter
    private Textbox guid;
    
    /**
     * uuid
     */
    @Setter
    @Getter
    private Textbox uuid;
    
    /**
     * 描述
     */
    @Setter
    @Getter
    private Textbox empDesc;
    
    /**
     * 操作单据
     */
    @Setter
    @Getter
    private Textbox transactionRecord;
    
    /******证件信息******/
    /**
	 * 证件名
	 */
	@Setter
	@Getter
	private Textbox certName;
	
	/**
	 * 证件号码
	 */
	@Setter
	@Getter
	private Textbox certNumber;
	
	/**
     * 证件类型
     */
    @Setter
    @Getter
    private Listbox certType;
    
    /**
	 * 证件有效期
	 */
	@Setter
	@Getter
	private Datebox certValidPeriod;
	
	/**
	 * 证件地址
	 */
	@Setter
	@Getter
	private Textbox certAddr;
	
	/**
	 * 发证机关
	 */
	@Setter
	@Getter
	private Textbox certOrg;
	
	/**
     * 是否实名
     */
    @Setter
    @Getter
    private Listbox isReal;
    
    /******联系方式******/
    /**
	 * 移动手机号码
	 */
	@Setter
	@Getter
	private Textbox mobilePhone;
	
	/**
	 * 备用手机号码
	 */
	@Setter
	@Getter
	private Textbox sparePhone;
	
	/**
	 * 座机
	 */
	@Setter
	@Getter
	private Textbox telephone;
	
	/**
	 * 企业内部邮箱
	 */
	@Setter
	@Getter
	private Textbox innerEmail;
	
	/**
	 * 社会邮箱
	 */
	@Setter
	@Getter
	private Textbox email;
	
	/**
	 * 集团统一邮箱
	 */
	@Setter
	@Getter
	private Textbox unifiedEmail;
	
	/**
	 * QQ
	 */
	@Setter
	@Getter
	private Textbox qq;
	
	/**
	 * 微信号
	 */
	@Setter
	@Getter
	private Textbox wechat;
	
	/**
	 * 传真
	 */
	@Setter
	@Getter
	private Textbox fax;
	
	/**
	 * 联系地址
	 */
	@Setter
	@Getter
	private Textbox address;
	
	/******人员附加信息******/
	/**
     * 性别
     */
    @Setter
    @Getter
    private Listbox gender;
    
    /**
     * 婚姻状况
     */
    @Setter
    @Getter
    private TreeChooserBandbox maritalStatus;
    
    /**
     * 民族
     */
    @Setter
    @Getter
    private Listbox nation;
    
    /**
     * 宗教
     */
    @Setter
    @Getter
    private Listbox religion;
    
    /**
     * 教育水平
     */
    @Setter
    @Getter
    private Listbox educationLevel;
    
    /**
	 * 职业
	 */
	@Setter
	@Getter
	private Textbox occupation;
    
    @Setter
    @Getter
    private Toolbar btnToolBar;
}
