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

public class IndicatorConfigMainBean {
    @Getter
    @Setter
    private Window indicatorConfigMainWin;
    @Getter
    @Setter
    private Textbox indicatorName;
    @Getter
    @Setter  
    private Button findIndicatorButton;
    @Getter
    @Setter
    private Listbox indicatorListBox;
    @Getter
    @Setter
    private Listbox unAssmCrtListBox;
    @Getter
    @Setter
    private Button addIndicatorButton;
    @Getter
    @Setter
    private Button delIndicatorButton;
    @Getter
    @Setter
    private Button editIndicatorButton;
    @Getter
    @Setter
    private Button addQaUnAssmCrtButton;
    @Getter
    @Setter
    private Button delQaUnAssmCrtButton;
    @Getter
    @Setter
    private Button editQaUnAssmCrtButton;
    @Getter
    @Setter
    private Paging indicatorPaging;
    @Getter
    @Setter
    private Tab qaUnAssmCrtTab;
}
