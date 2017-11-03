package cn.ffcs.uom.systemconfig.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.systemconfig.action.bean.NumberSegmentConfigMainBean;
import cn.ffcs.uom.systemconfig.manager.NumberSegmentManager;
import cn.ffcs.uom.systemconfig.model.NumberSegment;

@Controller
@Scope("prototype")
public class NumberSegmentConfigMainComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private NumberSegmentConfigMainBean bean = new NumberSegmentConfigMainBean();

	private NumberSegment queryNumberSegment;

	private NumberSegmentManager numberSegmentManager = (NumberSegmentManager) ApplicationContextUtil
			.getBean("numberSegmentManager");
	/**
	 * 选中的业务系统
	 */
	private NumberSegment numberSegment;
	/**
	 * 操作类型
	 */
	private String opType;
	
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
	public void onCreate$numberSegmentConfigMainWin() throws Exception {
		onQueryNumberSegment();
		setNumberSegmentButtonValid(true,false,false);
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onNumberSegmentConfigAdd() throws Exception {
		this.openNumberSegmentConfigEditWin("add");
		setNumberSegmentButtonValid(true,false,false);
	}

	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	public void onNumberSegmentConfigEdit() throws Exception {
		this.openNumberSegmentConfigEditWin("mod");
		setNumberSegmentButtonValid(true,false,false);
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openNumberSegmentConfigEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("mod".equals(type)) {
			arg.put("numberSegment", numberSegment);
		}
		Window win = (Window) Executions.createComponents("/pages/system_config/number_segment_config_edit.zul", null, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					NumberSegment ns = (NumberSegment) event.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(bean.getNumberSegmentListBox(), ns,"add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(bean.getNumberSegmentListBox(), ns, "mod");
						numberSegment = ns;
					}
				}
			}
		});
	}
	
	/**
	 * 删除
	 */
	public void onNumberSegmentConfigDel() throws Exception {
		if (numberSegment != null) {
			ZkUtil.showQuestion("你确定要删除号码段配置信息吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						numberSegmentManager.removeNumberSegment(numberSegment);
						PubUtil.reDisplayListbox(bean.getNumberSegmentListBox(),numberSegment, "del");
						setNumberSegmentButtonValid(true,false,false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的号码段配置信息!", "提示信息");
			return;
		}
	}
	
	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryNumberSegment() throws Exception {
		PageInfo pageInfo = this.numberSegmentManager.queryPageInfoByQuertNumberSegment(
				queryNumberSegment,
				this.bean.getNumberSegmentPaging().getActivePage() + 1, this.bean
						.getNumberSegmentPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getNumberSegmentListBox().setModel(dataList);
		this.bean.getNumberSegmentPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 点击查询
	 * 
	 * @throws Exception
	 */
	public void onFindNumberSegmentInfo() throws Exception {
		this.bean.getNumberSegmentPaging().setActivePage(0);
		this.queryNumberSegment = new NumberSegment();
		if (this.bean.getNumberSegment() != null && !StrUtil.isEmpty(this.bean.getNumberSegment().getValue())) {
			this.queryNumberSegment.setNumberSegment(this.bean.getNumberSegment().getValue());
		}
		this.onQueryNumberSegment();
	}

	/**
	 * 选中类列表
	 * 
	 * @throws Exception
	 */
	public void onSelectNumberSegmentListBox() throws Exception {
		numberSegment = (NumberSegment) this.bean.getNumberSegmentListBox().getSelectedItem().getValue();
		setNumberSegmentButtonValid(true,true,true);
	}

	/**
	 * 类保存后的事件
	 * 
	 * @throws Exception
	 */
	public void onSaveNumberSegmentResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getNumberSegmentPaging().setActivePage(0);
		this.queryNumberSegment = (NumberSegment) event.getOrigin().getData();
		this.onQueryNumberSegment();
	}

	/**
	 * 类删除后的事件
	 * 
	 * @throws Exception
	 */
	public void onDelNumberSegmentResponse(final ForwardEvent event)
			throws Exception {
		NumberSegment numberSegment = (NumberSegment) event.getOrigin().getData();
		ListModelList model = (ListModelList) this.bean.getNumberSegmentListBox()
				.getModel();
		model.remove(numberSegment);
		ListModel dataList = new BindingListModelList(model, true);
		this.bean.getNumberSegmentListBox().setModel(dataList);
	}
	
	
	/**
	 * 设置按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canSave
	 *            保存按钮
	 * @param canRecover
	 *            恢复按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setNumberSegmentButtonValid(final Boolean canAdd,
			final Boolean canEdit,  final Boolean canDelete) {
		this.bean.getAddNumberSegmentConfigButton().setDisabled(!canAdd);
		this.bean.getEditNumberSegmentConfigButton().setDisabled(!canEdit);
		this.bean.getDelNumberSegmentConfigButton().setDisabled(!canDelete);
	}
}
