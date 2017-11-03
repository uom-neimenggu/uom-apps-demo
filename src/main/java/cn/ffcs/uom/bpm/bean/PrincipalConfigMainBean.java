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
import cn.ffcs.uom.bpm.comp.PrincipalListExt;

public class PrincipalConfigMainBean {
    @Getter
    @Setter
    private Window principalConfigMainWin;
    @Getter
    @Setter
    private PrincipalListExt principalListExt;
}
