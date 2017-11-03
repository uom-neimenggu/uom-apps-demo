package cn.ffcs.uac.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uac.staff.component.UacStaffListboxExt;

public class UacStaffMainBean {
    @Setter
    @Getter
    private Window uacStaffMainWin;
    
    @Setter
    @Getter
    private UacStaffListboxExt uacStaffListboxExt;
}
