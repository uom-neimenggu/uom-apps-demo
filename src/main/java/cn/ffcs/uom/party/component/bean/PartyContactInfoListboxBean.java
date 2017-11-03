package cn.ffcs.uom.party.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

/**
 * 参与人联系方式
 * .
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-5
 * @功能说明：
 *
 */
public class PartyContactInfoListboxBean {
    
    @Getter
    @Setter
    private Div partyContactInfoWindowDiv;
    
    @Getter
    @Setter
    private Div partyContactInfoBandboxDiv;
    
    @Getter
    @Setter
    private Listbox partyContactInfoListbox;
    
    @Getter
    @Setter
    private Paging partyContactInfoListboxPaging;
    /**
     * 新增
     */
    @Getter
    @Setter
    private Button addPartyContactInfoButton;
    /**
     * 清空
     */
    @Getter
    @Setter
    private Button cleanPartyContactInfoButton;
    /**
     * 修改编辑
     */
    @Getter
    @Setter
    private Button editPartyContactInfoButton;
    
    /**
     * 删除
     */
    @Getter
    @Setter
    private Button delPartyContactInfoButton;
    
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div partyContactInfoSearchDiv;
	/**
	 * 名称.
	 **/
	@Getter
	@Setter
	private Textbox contactName;
}
