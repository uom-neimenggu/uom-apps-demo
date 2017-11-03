package cn.ffcs.uom.bpm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.bpm.bean.IndicatorConfigMainBean;
import cn.ffcs.uom.bpm.manager.IndicatorManager;
import cn.ffcs.uom.bpm.model.QaUnAssmCrt;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.common.vo.PageInfo;

@Controller
@Scope("prototype")
public class IndicatorConfigMainComposer extends BasePortletComposer {
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 3414801544961910332L;
    /**
     * 页面bean
     */
    private IndicatorConfigMainBean bean = new IndicatorConfigMainBean();
    
    /**
     * 选中的
     */
    private QaUnOppExecScript qaUnOppExecScript;
    private QaUnAssmCrt qaUnAssmCrt;
    /**
     * 查询的
     */
    private QaUnOppExecScript qryIndicator;
    private QaUnAssmCrt qryQaUnAssmCrt;
    
    private String opType;
    @Autowired
    private IndicatorManager indicatorManager;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        UomZkUtil.autoFitHeight(comp);
        Components.wireVariables(comp, bean);
    }
    
    /**
     * 界面初始化.
     * 
     * @throws Exception
     *             异常
     */
    public void onCreate$indicatorConfigMainWin() throws Exception {
        onQueryIndicator();
        this.setQaUnAssmCrtButtonValid(false, false, false);
    }
    
    public void onQueryIndicator() throws Exception {
        queryIndicatorListboxForPaging();
    }
    
    /**
     * 选中
     * 
     * @param event
     * @throws Exception
     */
    public void onSelectIndicatorListBox() throws Exception {
        if (this.bean.getIndicatorListBox().getSelectedCount() > 0) {
            qaUnOppExecScript = (QaUnOppExecScript) this.bean.getIndicatorListBox()
                .getSelectedItem().getValue();
            if (qaUnOppExecScript != null) {
                this.setButtonValid(true, true, true);
                // Events.postEvent(BmpConstants.ON_INFORM_METHOD_SELECT, this,
                // qaUnOppExecScript);
                bindUnAssmCrtListBox();
            }
        }
    }
    
    public void bindUnAssmCrtListBox(){
        qryQaUnAssmCrt = new QaUnAssmCrt();
        qryQaUnAssmCrt.setExecSctIdenti(qaUnOppExecScript.getExecSctIdenti());
        qryQaUnAssmCrt.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
        List<QaUnAssmCrt> listQaUnAssmCrt = indicatorManager
            .qryQaUnAssmCrt(qryQaUnAssmCrt);
        ListModel list = new BindingListModelList(listQaUnAssmCrt, true);
        this.bean.getUnAssmCrtListBox().setModel(list);
        this.setQaUnAssmCrtButtonValid(true, false, false);
    }
    
    
    public void onSelectUnAssmCrtListBox() throws Exception{
        if (this.bean.getUnAssmCrtListBox().getSelectedCount() > 0) {
            qaUnAssmCrt = (QaUnAssmCrt) this.bean.getUnAssmCrtListBox()
                .getSelectedItem().getValue();
            if (qaUnAssmCrt != null) {
                this.setQaUnAssmCrtButtonValid(false, true, true);
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
        this.bean.getAddIndicatorButton().setDisabled(!canAdd);
        this.bean.getDelIndicatorButton().setDisabled(!canDel);
        this.bean.getEditIndicatorButton().setDisabled(!canEdit);
    }
    public void setQaUnAssmCrtButtonValid(boolean canAdd, boolean canEdit, boolean canDel) {
        this.bean.getAddQaUnAssmCrtButton().setDisabled(!canAdd);
        this.bean.getDelQaUnAssmCrtButton().setDisabled(!canDel);
        this.bean.getEditQaUnAssmCrtButton().setDisabled(!canEdit);
    }    
    public void queryIndicatorListboxForPaging() {
        try {
            if (qryIndicator == null) {
                qryIndicator = new QaUnOppExecScript();
                qryIndicator.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
            }
            String indicatorName = this.bean.getIndicatorName().getValue();
            qryIndicator.setExecName(indicatorName);
            Paging paging = bean.getIndicatorPaging();
            PageInfo pageInfo = indicatorManager.qryIndicatorPage(qryIndicator,
                paging.getActivePage() + 1, paging.getPageSize());
            ListModel list = new BindingListModelList(pageInfo.getDataList(), true);
            bean.getIndicatorListBox().setModel(list);
            bean.getIndicatorPaging().setTotalSize(pageInfo.getTotalCount());
            
        } catch (WrongValueException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除
     * 
     * @throws Exception
     */
    public void onIndicatorDel() throws Exception {
        if (qaUnOppExecScript != null) {
            ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    Integer result = (Integer) event.getData();
                    if (result == Messagebox.OK) {
                        indicatorManager.delIndicator(qaUnOppExecScript);
                        onQueryIndicator();
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
    public void onIndicatorAdd() throws Exception {
        String type = "add";
        Map arg = new HashMap();
        this.opType = type;
        arg.put("opType", opType);
        Window win = (Window) Executions.createComponents(
            "/pages/bpm/comp/indicator_config_edit.zul", this.self, arg);
        win.setTitle("新增稽核项");
        win.doModal();
        win.addEventListener(Events.ON_OK, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                onQueryIndicator();
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
    public void onIndicatorEdit() throws Exception {
        String type = "mod";
        Map arg = new HashMap();
        this.opType = type;
        arg.put("opType", opType);
        arg.put("qaUnOppExecScript", qaUnOppExecScript);
        Window win = (Window) Executions.createComponents(
            "/pages/bpm/comp/indicator_config_edit.zul", this.self, arg);
        win.setTitle("稽核项修改");
        win.doModal();
        win.addEventListener(Events.ON_OK, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                onQueryIndicator();
                if (event.getData() != null) {
                    
                }
            }
        });
    }
    
    /**
     * 删除
     * 
     * @throws Exception
     */
    public void onIndicatorBaseInfoDel() throws Exception {
        if (qaUnAssmCrt != null) {
            ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    Integer result = (Integer) event.getData();
                    if (result == Messagebox.OK) {
                        indicatorManager.delQaUnAssmCrt(qaUnAssmCrt);
                        onQueryIndicator();
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
    public void onIndicatorBaseInfoAdd() throws Exception {
        if (qaUnOppExecScript == null) {
            ZkUtil.showError("请先选择指标项", "提示信息");
        } 
        String type = "add";
        Map arg = new HashMap();
        this.opType = type;
        arg.put("opType", opType);
        arg.put("qaUnOppExecScript", qaUnOppExecScript);
        Window win = (Window) Executions.createComponents(
            "/pages/bpm/comp/indicator_baseinfo_edit.zul", this.self, arg);
        win.setTitle("新增考核基准");
        win.doModal();
        win.addEventListener(Events.ON_OK, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                bindUnAssmCrtListBox();
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
    public void onIndicatorBaseInfoEdit() throws Exception {
        String type = "mod";
        Map arg = new HashMap();
        this.opType = type;
        arg.put("opType", opType);
        arg.put("qaUnAssmCrt", qaUnAssmCrt);
        Window win = (Window) Executions.createComponents(
            "/pages/bpm/comp/indicator_baseinfo_edit.zul", this.self, arg);
        win.setTitle("考核基准修改");
        win.doModal();
        win.addEventListener(Events.ON_OK, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                bindUnAssmCrtListBox();
                if (event.getData() != null) {
                    
                }
            }
        });
    }
}
