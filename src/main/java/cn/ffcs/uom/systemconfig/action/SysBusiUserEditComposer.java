package cn.ffcs.uom.systemconfig.action;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listitem;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.systemconfig.action.bean.SysBusiUserEditBean;
import cn.ffcs.uom.systemconfig.manager.SysBusiUserManager;
import cn.ffcs.uom.webservices.model.SystemBusiUser;

@Controller
@Scope("prototype")
public class SysBusiUserEditComposer extends BasePortletComposer {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 页面bean
	 */
	private SysBusiUserEditBean bean = new SysBusiUserEditBean();
	
	/**
	 * 操作类型
	 */
	@SuppressWarnings("unused")
	private String opType;
	
	/**
	 * 当前选中的人员
	 */
	private SystemBusiUser querySysBusiUser;
	
	/**
	 * manager
	 */
	private SysBusiUserManager sysBusiUserManager = (SysBusiUserManager) ApplicationContextUtil
			.getBean("sysBusiUserManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 */
	public void onCreate$businessSystemEditWin() throws Exception {
		opType = (String) arg.get("opType");
		querySysBusiUser = (SystemBusiUser) arg.get("sysBusiUser");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 */
	private void bindBean() throws Exception {
		this.bean.getBusinessSystemEditWin().setTitle("人员系统关系新增");
		
		List<NodeVo> businessSystemList = sysBusiUserManager.queryBusinessSystemListByUserId(querySysBusiUser.getSystemConfigUserId());
		ListboxUtils.rendererForEditNoSelected(this.bean.getBusinessSystem(), businessSystemList);
	}

	/**
	 * 点击确定
	 */
	@SuppressWarnings({ "rawtypes" })
	public void onSubmit() throws Exception {

		if (this.bean.getBusinessSystem().getSelectedItems().isEmpty()) {
			ZkUtil.showError("配置短信发送系统必填", "提示信息");
			return;
		}
		
		Set businessSystemSet = this.bean.getBusinessSystem().getSelectedItems();
		
		for(Iterator it = businessSystemSet.iterator();it.hasNext();) {
			Listitem listitem = (Listitem) it.next();
			SystemBusiUser sysBusiUser = new SystemBusiUser();
			sysBusiUser.setBusinessSystemId(Long.valueOf(String.valueOf(listitem.getValue())));
			sysBusiUser.setSystemConfigUserId(querySysBusiUser.getSystemConfigUserId());
			sysBusiUser.setSort(1L);
			
			this.sysBusiUserManager.addSysBusiUser(sysBusiUser);
		}
		
		this.bean.getBusinessSystemEditWin().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 点击取消
	 */
	public void onCancel() throws Exception {
		this.bean.getBusinessSystemEditWin().onClose();
	}
}
