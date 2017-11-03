package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrgTreeExt;

public class OrgTreeConfigMainBean {
    @Getter
    @Setter
    private Window     orgTreeConfigMainWin;
    @Getter
    @Setter
    private Listbox    orgTreeRootNode;
    @Getter
    @Setter
    private Paging     orgTreeRootNodeListPaging;
    @Getter
    @Setter
    private Button     addOrgButton;
    @Getter
    @Setter
    private Button     editOrgButton;
    @Getter
    @Setter
    private Button     delOrgButton;
    /**
     * 当前选中的tab
     */
    @Getter
    @Setter
    private Tab        selectTab;
    /**
     * tabBox
     */
    @Getter
    @Setter
    private Tabbox     tabBox;
    /**
     * 组织树
     */
    @Getter
    @Setter
    private OrgTreeExt orgTreeExt;
}
