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
import cn.ffcs.uom.bpm.constants.BmpConstants;
import cn.ffcs.uom.bpm.manager.QaInformMethodManager;
import cn.ffcs.uom.bpm.model.QaInformMethod;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class InformMethodListExt extends Div implements IdSpace {

    private static final long serialVersionUID = 1L;

    public InformMethodListExtBean bean = new InformMethodListExtBean();
    /**
     * 操作类型
     * 
     * @throws Exception
     */
    private String opType;
    /**
     * 选中的通知方法
     */
    private QaInformMethod qaInformMethod;
	/**
	 * 页面
	 */
	private String zul = "/pages/bpm/comp/inform_method_list_ext.zul";
    private QaInformMethodManager qaInformMethodManager = (QaInformMethodManager) ApplicationContextUtil
        .getBean("qaInformMethodManager");

	/**
	 * 构造方法
	 */
	public InformMethodListExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(
				SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST,
				this,
				SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_RESPONSE);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
	    onQueryInformMethod();
		this.setButtonValid(true, false);
	}

    public void onQueryInformMethod() throws Exception {
        QaInformMethod qaInformMethod = new QaInformMethod();
        List<QaInformMethod> data = this.qaInformMethodManager.getInformMethod(qaInformMethod);
        ListModel dataList = new BindingListModelList(data, true);
        this.bean.getInformMethodListBox().setModel(dataList);
    }

	/**
	 * 选中
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectInformMethod() throws Exception {
		if (this.bean.getInformMethodListBox().getSelectedCount() > 0) {
		    qaInformMethod = (QaInformMethod) this.bean.getInformMethodListBox().getSelectedItem().getValue();
			if (qaInformMethod != null) {
				this.setButtonValid(true, true);
				Events.postEvent(BmpConstants.ON_INFORM_METHOD_SELECT,this,qaInformMethod);
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
	public void setButtonValid(boolean canAdd, boolean canDel) {
		this.bean.getAddInformMethodButton().setDisabled(!canAdd);
		this.bean.getDelInformMethodButton().setDisabled(!canDel);
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onInformMethodDel() throws Exception {
		if (qaInformMethod != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
					    qaInformMethodManager.removeInformMehtod(qaInformMethod);
					    onQueryInformMethod();
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
    public void onInformMethodAdd() throws Exception {
        String type = "add";
        Map arg = new HashMap();
        this.opType = type;
        arg.put("opType", opType);
        Window win = (Window) Executions.createComponents(
            "/pages/bpm/comp/inform_config_edit.zul", this, arg);
        win.setTitle("新增通知方式");
        win.doModal();
        win.addEventListener(Events.ON_OK, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                onQueryInformMethod();
                if (event.getData() != null) {
                    
                }
            }
        });
    }
}
