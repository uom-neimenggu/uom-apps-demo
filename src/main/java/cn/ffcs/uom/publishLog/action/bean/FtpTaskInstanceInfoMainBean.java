/**
 * 
 */
package cn.ffcs.uom.publishLog.action.bean;

import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wenyaopeng
 *
 */
public class FtpTaskInstanceInfoMainBean {
	
	@Getter
    @Setter
	private Window ftpTaskInstanceInfoMainWin;
	
	@Getter
    @Setter
	private Div systemInfoDiv;
	
	@Getter
    @Setter
	private Div ftpTaskInstanceInfoDiv;

}
