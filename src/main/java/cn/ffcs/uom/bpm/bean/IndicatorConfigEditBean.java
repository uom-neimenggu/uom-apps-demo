package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class IndicatorConfigEditBean {
    @Getter
    @Setter
    private Window indicatorConfigEditWin;
    @Getter
    @Setter
    private Textbox indicatorName;
    @Getter
    @Setter
    private Textbox indicatorCode;
    @Getter
    @Setter
    private Textbox indicatorType;
    @Getter
    @Setter
    private Textbox system;
    @Getter
    @Setter
    private Button okButton;
    @Getter
    @Setter
    private Button cancelButton;
    
}
