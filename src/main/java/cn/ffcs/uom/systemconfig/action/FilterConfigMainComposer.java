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
import cn.ffcs.uom.systemconfig.action.bean.FilterConfigMainBean;
import cn.ffcs.uom.systemconfig.manager.FilterConfigManager;
import cn.ffcs.uom.systemconfig.model.FilterConfig;

@Controller
@Scope("prototype")
public class FilterConfigMainComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private FilterConfigMainBean bean = new FilterConfigMainBean();

	private FilterConfig queryFilterConfig;

	private FilterConfigManager filterConfigManager = (FilterConfigManager) ApplicationContextUtil.getBean("filterConfigManager");
	/**
	 * 选中的业务系统
	 */
	private FilterConfig filterConfig;
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
	public void onCreate$filterConfigMainWin() throws Exception {
		if(null==queryFilterConfig)
			queryFilterConfig = new FilterConfig();
		onQueryFilterConfig();
		setFilterButtonValid(true,false,false);
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onFilterConfigAdd() throws Exception {
		this.openFilterConfigEditWin("add");
		setFilterButtonValid(true,false,false);
	}

	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	public void onFilterConfigEdit() throws Exception {
		this.openFilterConfigEditWin("mod");
		setFilterButtonValid(true,false,false);
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openFilterConfigEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("mod".equals(type)) {
			arg.put("filterConfig", filterConfig);
		}
		Window win = (Window) Executions.createComponents("/pages/system_config/filter_config_edit.zul", null, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					FilterConfig fc = (FilterConfig) event.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(bean.getFilterConfigListBox(), fc,"add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(bean.getFilterConfigListBox(), fc, "mod");
						filterConfig = fc;
					}
				}
			}
		});
	}
	
	/**
	 * 删除
	 */
	public void onFilterConfigDel() throws Exception {
		if (filterConfig != null) {
			ZkUtil.showQuestion("你确定要删除过滤字符配置信息吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						filterConfigManager.removeFilterConfig(filterConfig);
						PubUtil.reDisplayListbox(bean.getFilterConfigListBox(),filterConfig, "del");
						setFilterButtonValid(true,false,false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的过滤字符配置信息!", "提示信息");
			return;
		}
	}
	
	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryFilterConfig() throws Exception {
		PageInfo pageInfo = this.filterConfigManager.queryPageInfoByQuertFilterConfig(
				queryFilterConfig,
				this.bean.getFilterConfigPaging().getActivePage() + 1, this.bean
						.getFilterConfigPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getFilterConfigListBox().setModel(dataList);
		this.bean.getFilterConfigPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 点击查询
	 * 
	 * @throws Exception
	 */
	public void onFindFilterConfigInfo() throws Exception {
		this.bean.getFilterConfigPaging().setActivePage(0);
		this.queryFilterConfig = new FilterConfig();
		if (this.bean.getFilterConfig() != null && !StrUtil.isEmpty(this.bean.getFilterConfig().getValue())) {
			//this.queryFilterConfig.setFilterConfig(this.bean.getFilterConfig().getValue());
		}
		this.onQueryFilterConfig();
	}

	/**
	 * 选中类列表
	 * 
	 * @throws Exception
	 */
	public void onSelectFilterConfigListBox() throws Exception {
		filterConfig = (FilterConfig) this.bean.getFilterConfigListBox().getSelectedItem().getValue();
		setFilterButtonValid(true,true,true);
	}

	/**
	 * 类保存后的事件
	 * 
	 * @throws Exception
	 */
	public void onSaveFilterConfigResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getFilterConfigPaging().setActivePage(0);
		this.queryFilterConfig = (FilterConfig) event.getOrigin().getData();
		this.onQueryFilterConfig();
	}

	/**
	 * 类删除后的事件
	 * 
	 * @throws Exception
	 */
	public void onDelFilterConfigResponse(final ForwardEvent event)
			throws Exception {
		FilterConfig filterConfig = (FilterConfig) event.getOrigin().getData();
		ListModelList model = (ListModelList) this.bean.getFilterConfigListBox()
				.getModel();
		model.remove(filterConfig);
		ListModel dataList = new BindingListModelList(model, true);
		this.bean.getFilterConfigListBox().setModel(dataList);
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
	private void setFilterButtonValid(final Boolean canAdd,
			final Boolean canEdit,  final Boolean canDelete) {
		this.bean.getAddFilterConfigButton().setDisabled(!canAdd);
		this.bean.getEditFilterConfigButton().setDisabled(!canEdit);
		this.bean.getDelFilterConfigButton().setDisabled(!canDelete);
	}
}
