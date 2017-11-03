package cn.ffcs.uom.mytest.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 全量增量发布Bean
 * 
 * @author
 **/
public class AllOrSyncPublishMainBean {
	/**
	 * window
	 **/
	@Getter
	@Setter
	private Window allOrSyncPublishMainWin;

	/**
	 * 组织树名称
	 */
	@Getter
	@Setter
	private Longbox orgTreeId;

	/**
	 * 发布方式
	 */
	@Setter
	@Getter
	private Textbox allOrSync;

	/**
	 * 上次发布时间
	 */
	@Setter
	@Getter
	private Textbox lastDate;

	/**
	 * 本次发布时间
	 */
	@Setter
	@Getter
	private Textbox thisDate;

	/**
	 * 用户名
	 */
	@Setter
	@Getter
	private Textbox userName;

	/**
	 * 密码
	 */
	@Setter
	@Getter
	private Textbox passWord;

	/**
	 * 发布日志按钮
	 */
	@Getter
	@Setter
	private Button allOrSyncPublishButton;

}
