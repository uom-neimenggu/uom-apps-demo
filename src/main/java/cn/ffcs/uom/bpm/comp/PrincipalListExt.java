package cn.ffcs.uom.bpm.comp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.bpm.bean.InformMethodListExtBean;
import cn.ffcs.uom.bpm.bean.PrincipalListExtBean;
import cn.ffcs.uom.bpm.constants.BmpConstants;
import cn.ffcs.uom.bpm.manager.BmpSystemConfigManager;
import cn.ffcs.uom.bpm.manager.QaInformMethodManager;
import cn.ffcs.uom.bpm.model.QaInformMethod;
import cn.ffcs.uom.bpm.model.QaUnPrincipal;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class PrincipalListExt extends Div implements IdSpace {
	
	private static final long serialVersionUID = 1L;
	
	public PrincipalListExtBean bean = new PrincipalListExtBean();
	/**
	 * 操作类型
	 * 
	 * @throws Exception
	 */
	private String opType;
	/**
	 * 选中的干系人
	 */
	private QaUnPrincipal principal;
	private QaUnPrincipal qryPrincipal;
	/**
	 * 页面
	 */
	private String zul = "/pages/bpm/comp/principal_list_ext.zul";
	private BmpSystemConfigManager bmpSystemConfigManager = (BmpSystemConfigManager) ApplicationContextUtil
		.getBean("bmpSystemConfigManager");
	
	/**
	 * 构造方法
	 */
	public PrincipalListExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}
	
	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
		onQueryPrincipal();
		this.setButtonValid(true, false, false);
	}
	
	public void onQueryPrincipal() throws Exception {
		qryPrincipal = new QaUnPrincipal();
		qryPrincipal.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<QaUnPrincipal> data = this.bmpSystemConfigManager.qryPrincipal(qryPrincipal);
		ListModel dataList = new BindingListModelList(data, true);
		this.bean.getPrincipalListBox().setModel(dataList);
	}
	
	/**
	 * 选中
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectPrincipal() throws Exception {
		if (this.bean.getPrincipalListBox().getSelectedCount() > 0) {
			principal = (QaUnPrincipal) this.bean.getPrincipalListBox().getSelectedItem()
				.getValue();
			if (principal != null) {
				this.setButtonValid(true, true, true);
			}
		}
	}
	
	/**
	 * 设置按钮状态
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDel
	 */
	public void setButtonValid(boolean canAdd, boolean canEdit, boolean canDel) {
		this.bean.getAddPrincipalButton().setDisabled(!canAdd);
		this.bean.getEditPrincipalButton().setDisabled(!canEdit);
		this.bean.getDelPrincipalButton().setDisabled(!canDel);
	}
	
	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onPrincipalDel() throws Exception {
		if (principal != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						bmpSystemConfigManager.delPrincipal(principal);
						onQueryPrincipal();
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的配置", "提示信息");
			return;
		}
	}
	
	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onPrincipalAdd() throws Exception {
		String type = "add";
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		Window win = (Window) Executions.createComponents(
			"/pages/bpm/comp/principal_config_edit.zul", this, arg);
		win.setTitle("新增干系人");
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				onQueryPrincipal();
				if (event.getData() != null) {
					
				}
			}
		});
	}
	
	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	public void onPrincipalEdit() throws Exception {
		String type = "mod";
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		arg.put("principal", principal);
		Window win = (Window) Executions.createComponents(
			"/pages/bpm/comp/principal_config_edit.zul", this, arg);
		win.setTitle("修改干系人");
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				onQueryPrincipal();
				if (event.getData() != null) {
					
				}
			}
		});
	}
}
