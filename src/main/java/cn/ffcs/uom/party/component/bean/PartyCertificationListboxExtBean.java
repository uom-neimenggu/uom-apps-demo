package cn.ffcs.uom.party.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

/**
 * 参与人证件列表显示的Bean
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-7
 * @功能说明：
 *
 */
public class PartyCertificationListboxExtBean {
    
    @Getter
    @Setter
    private Div partyCertificationBandboxDiv;
    
    @Getter
    @Setter
    private Listbox partyCertificationListbox;
    
    @Getter
    @Setter
    private Paging partyCertificationListboxPaging;
    
    @Getter
    @Setter
    private Button delPartyCertificationButton;
    
    @Getter
    @Setter
    private Button addPartyCertificationButton;
    
    @Getter
    @Setter
    private Button editPartyCertificationButton;
    
    /**
	 * 批量参与人证件，增删改 zhanglu
	 */
	@Getter
	@Setter
	private Button importPartyCertificationButton;
    
    /**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div partyCertificationSearchDiv;
	
	/**
	 * 证件号码.
	 **/
	@Getter
	@Setter
	private Textbox certNumber;
	
	/**
	 * 发证机关.
	 **/
	@Getter
	@Setter
	private Textbox certOrg;
	
}
