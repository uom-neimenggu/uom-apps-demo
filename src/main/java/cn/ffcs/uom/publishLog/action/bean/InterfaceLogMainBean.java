/**
 * 
 */
package cn.ffcs.uom.publishLog.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

/**
 * @author wenyaopeng
 *
 */
public class InterfaceLogMainBean {
	
	@Getter
    @Setter
	private Window interfaceLogMainWin;
	
	@Getter
    @Setter
	private Div interfaceLogQueryDiv;
	
	@Getter
    @Setter
	private Div systemInfoDiv;

}
