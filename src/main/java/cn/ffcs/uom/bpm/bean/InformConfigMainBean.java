package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.bpm.comp.InformMethodListExt;

public class InformConfigMainBean {
    @Getter
    @Setter
    private Window informConfigMainWin;
    @Getter
    @Setter
    private InformMethodListExt informMethodListExt;
    @Getter
    @Setter
    private Textbox informTemplate;
    @Getter
    @Setter
    private Button editTemplateButton;
    @Getter
    @Setter
    private Button saveTemplateButton;
}
