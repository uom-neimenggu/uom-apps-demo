package cn.ffcs.uom.identity.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Window;

/**
 * 全量增量发布Bean
 * 
 * @author
 **/
public class IdentityMainBean {
	/**
	 * window
	 **/
	@Getter
	@Setter
	private Window identityMainWin;

	/**
	 * 身份证类型
	 */
	@Getter
	@Setter
	private Listbox identityCardNameListbox;

	/**
	 * 生成数量
	 */
	@Setter
	@Getter
	private Longbox identityCardCount;

	/**
	 * 生成按钮
	 */
	@Getter
	@Setter
	private Button identityButton;

}
