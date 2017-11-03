package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class PrincipalListExtBean {
    @Getter
    @Setter
    private Button addPrincipalButton;
    @Getter
    @Setter
    private Button editPrincipalButton;
    @Getter
    @Setter
    private Button delPrincipalButton;
    @Getter
    @Setter
    private Listbox principalListBox;
}
