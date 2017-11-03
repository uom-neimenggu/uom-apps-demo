package cn.ffcs.uom.zkdemo;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;

/**
 * 基础控制器，定义业务相关策略。用于继承
 * 
 * @author lianch@ffcs.cn
 * 
 */
@SuppressWarnings("serial")
public class BaseComposer extends BasePortletComposer {

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, self);
	}

}
