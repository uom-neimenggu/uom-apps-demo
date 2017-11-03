package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.organization.component.OrganizationBandboxExt;

public class ChannelPackareaTranEditBean {
    
    @Setter
    @Getter
    private Window channelPackareaTranEditComposer;
    
    /**
     * 渠道网点组织
     */
    @Setter
    @Getter
    private OrganizationBandboxExt agentChannelOrg;
    
    /**
     * 营销包区组织
     */
    @Setter
    @Getter
    private OrganizationBandboxExt packAreaOrg;
    
    /**
     * 组织业务关系类型
     */
    @Setter
    @Getter
    private TreeChooserBandbox tranRelaType;
    
    /**
     * 以店包区标识
     */
    @Setter
    @Getter
    private Listbox storeAreaFlag;
    
    private Toolbar inputToolBar;
    
    private Toolbar approveToolBar;
    
    @Getter
   	@Setter
    private Div storeAreaUploadDiv;
    
    /**
   	 * 承包人身份证下载
   	 */
    @Getter
   	@Setter
    private Button downloadCertButton;
    
    /**
	 * 承包人身份证导入
	 */
	@Getter
	@Setter
	private Button uploadCertButton;
	
	/**
	 * 承包人身份证Image
	 */
	@Getter
	@Setter
	private Image certPic;
	
	/**
   	 * 五级承包协议下载
   	 */
    @Getter
   	@Setter
    private Button downloadFiveButton;
	
	/**
	 * 五级承包协议导入
	 */
	@Getter
	@Setter
	private Button uploadFiveButton;
	
	/**
	 * 五级承包协议Image
	 */
	@Getter
	@Setter
	private Image fivePic;
	
	/**
   	 * 门店合作协议下载
   	 */
    @Getter
   	@Setter
    private Button downloadStoreButton;
	
	/**
	 * 门店合作协议导入
	 */
	@Getter
	@Setter
	private Button uploadStoreButton;
	
	/**
	 * 门店合作协议Image
	 */
	@Getter
	@Setter
	private Image storePic;
    
    /**
     * 提交按钮
     */
    private Button submit;
    
    /**
     * 提交按钮
     */
    private Button cancel;
    
    public Button getSubmit() {
        return submit;
    }
    public void setSubmit(Button submit) {
        this.submit = submit;
    }
	/**
	 * @return the cancel
	 */
	public Button getCancel() {
		return cancel;
	}
	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}
	/**
	 * @return the inputToolBar
	 */
	public Toolbar getInputToolBar() {
		return inputToolBar;
	}
	/**
	 * @param inputToolBar the inputToolBar to set
	 */
	public void setInputToolBar(Toolbar inputToolBar) {
		this.inputToolBar = inputToolBar;
	}
	/**
	 * @return the approveToolBar
	 */
	public Toolbar getApproveToolBar() {
		return approveToolBar;
	}
	/**
	 * @param approveToolBar the approveToolBar to set
	 */
	public void setApproveToolBar(Toolbar approveToolBar) {
		this.approveToolBar = approveToolBar;
	}
}
