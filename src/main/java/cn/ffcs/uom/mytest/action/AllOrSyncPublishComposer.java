package cn.ffcs.uom.mytest.action;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.mytest.bean.AllOrSyncPublishMainBean;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.util.WsClientUtil;

/**
 * 全量增量发布界面.
 * 
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class AllOrSyncPublishComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * bean.
	 */
	private AllOrSyncPublishMainBean bean = new AllOrSyncPublishMainBean();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 全量增量发布请求事件.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onAllOrSyncPublishRequest() throws Exception {

		ZkUtil.showQuestion("确定要发布吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (WsConstants.SERVICE_USERNAME.equals(bean.getUserName()
							.getValue())
							&& WsConstants.SERVICE_PASSWORD.equals(bean
									.getPassWord().getValue())) {
						String callUrl = UomClassProvider
								.getIntfUrl("localIncrementalDataUrl");
						logger.info("callUrl:" + callUrl);
						logger.info("syncType:"
								+ bean.getOrgTreeId().getValue());
						logger.info("lastDate:"
								+ DateUtil.str2date(bean.getLastDate()
										.getValue(), "yyyy-MM-dd hh:mm:ss"));
						logger.info("thisDate:"
								+ DateUtil.str2date(bean.getThisDate()
										.getValue(), "yyyy-MM-dd hh:mm:ss"));
						if (StrUtil.isEmpty(callUrl)
								|| bean.getOrgTreeId().getValue() == null
								|| StrUtil.isEmpty(bean.getLastDate()
										.getValue())
								|| StrUtil.isEmpty(bean.getThisDate()
										.getValue())
								|| StrUtil.isEmpty(bean.getAllOrSync()
										.getValue())) {
							ZkUtil.showError("参数错误", "发布失败信息");
							return;
						}
						String outXml = WsClientUtil
								.wsCallCreateIncrementalData(callUrl,
										"createIncrementalData", bean
												.getOrgTreeId().getValue(),
										DateUtil.str2date(bean.getLastDate()
												.getValue(),
												"yyyy-MM-dd hh:mm:ss"),
										DateUtil.str2date(bean.getThisDate()
												.getValue(),
												"yyyy-MM-dd hh:mm:ss"), bean
												.getAllOrSync().getValue());

						if (!StrUtil.isEmpty(outXml)) {
							ZkUtil.showError("生成FTP文件失败：" + outXml, "发布失败信息");
							return;
						}
						ZkUtil.showInformation("发布成功!", "发布成功信息");
					} else {
						ZkUtil.showError("用户名或密码输入错误!\n", "发布失败信息");
						return;
					}
				}

			}
		});

	}
}
