package cn.ffcs.uom.zkdemo.action;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.zkdemo.BaseComposer;
import cn.ffcs.uom.zkdemo.keys.WebKeys;
import cn.ffcs.uom.zkdemo.pojo.FormResult;

/**
 * 表单结果
 * 
 * @author lianch@ffcs.cn
 * 
 */
@SuppressWarnings("serial")
public class FormResultCtrl extends BaseComposer {

	private Label usernameLbl;
	private Label passwordLbl;
	private Label dateLbl;
	private Label rememberLbl;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 获取参数表
		Map mapArgs = ZkUtil.getExecutionArgs();
		if (mapArgs.containsKey(WebKeys.FORM_RESULT)) {
			FormResult formResult = (FormResult) mapArgs
					.get(WebKeys.FORM_RESULT);
			usernameLbl.setValue(formResult.getUsername());
			passwordLbl.setValue(formResult.getPassword());
			dateLbl.setValue(formResult.getDate().toString());
			rememberLbl.setValue(formResult.isRemember() ? "是" : "否");
		}
	}

}
