package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class InformMethodListExtBean {
    @Getter
    @Setter
    private Button addInformMethodButton;
    @Getter
    @Setter
    private Button delInformMethodButton;
    @Getter
    @Setter
    private Listbox informMethodListBox;
}
