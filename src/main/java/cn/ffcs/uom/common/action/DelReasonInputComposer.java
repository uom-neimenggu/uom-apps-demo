package cn.ffcs.uom.common.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.action.bean.DelReasonInputBean;
import cn.ffcs.uom.common.util.StrUtil;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2017
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017-7-7
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class DelReasonInputComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private DelReasonInputBean bean = new DelReasonInputBean();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$delReasonInputWin() throws Exception {
		bean.getDelReasonInputWin().setTitle("删除原因输入");
	}

	/**
	 * 保存.
	 * 
	 * @throws Exception
	 */
	public void onOk() throws Exception {
		String msg = checkData(); // 检验信息格式内容是否完整
		if (msg != null) {
			Messagebox.show(msg);
			return;
		}
		
		String reason = bean.getReason().getValue(); 
		
		Events.postEvent("onOK", this.self, reason);
		this.onCancel();
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getDelReasonInputWin().onClose();
	}

	/**
	 * 校验前台输入值
	 * @return
	 * @author zhanglu 2017-7-7
	 */
	private String checkData() {
		if (StrUtil.isNullOrEmpty(bean.getReason().getValue())) {
			return "请填写删除原因";
		}
		
		return null;
	}
}
