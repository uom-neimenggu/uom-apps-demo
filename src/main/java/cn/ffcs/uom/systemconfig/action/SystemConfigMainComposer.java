package cn.ffcs.uom.systemconfig.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.action.bean.SystemConfigMainBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.SysClassManager;
import cn.ffcs.uom.systemconfig.model.SysClass;

@Controller
@Scope("prototype")
public class SystemConfigMainComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3414801544961910332L;
	/**
	 * 页面bean
	 */
	private SystemConfigMainBean bean = new SystemConfigMainBean();

	/**
	 * 查询条件
	 */
	private SysClass querySysClass;

	private SysClassManager sysClassManager = (SysClassManager) ApplicationContextUtil
			.getBean("sysClassManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		this.self.getRoot().addForward(SystemConfigConstant.ON_SYS_CLASS_SAVE,
				comp, SystemConfigConstant.ON_SYS_CLASS_SAVE_RESPONSE);
		this.self.getRoot().addForward(SystemConfigConstant.ON_SYS_CLASS_DEL,
				comp, SystemConfigConstant.ON_SYS_CLASS_DEL_RESPONSE);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$systemConfigMainWin() throws Exception {
		onQuerySysClass();
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQuerySysClass() throws Exception {
		PageInfo pageInfo = this.sysClassManager.queryPageInfoByQuertSysClass(
				querySysClass,
				this.bean.getSysClassPaging().getActivePage() + 1, this.bean
						.getSysClassPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getSysClassListBox().setModel(dataList);
		this.bean.getSysClassPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 点击查询
	 * 
	 * @throws Exception
	 */
	public void onFindSysClassInfo() throws Exception {
		this.bean.getSysClassPaging().setActivePage(0);
		this.querySysClass = new SysClass();
		if (this.bean.getClassName() != null
				&& !StrUtil.isEmpty(this.bean.getClassName().getValue())) {
			this.querySysClass
					.setClassName(this.bean.getClassName().getValue());
		}
		if (this.bean.getJavaCode() != null
				&& !StrUtil.isEmpty(this.bean.getJavaCode().getValue())) {
			this.querySysClass.setJavaCode(this.bean.getJavaCode().getValue());
		}
		if (this.bean.getTableName() != null
				&& !StrUtil.isEmpty(this.bean.getTableName().getValue())) {
			this.querySysClass
					.setClassName(this.bean.getTableName().getValue());
		}
		this.onQuerySysClass();
	}

	/**
	 * 选中类列表
	 * 
	 * @throws Exception
	 */
	public void onSelectSysClassListBox() throws Exception {
		final SysClass sysClass = (SysClass) this.bean.getSysClassListBox()
				.getSelectedItem().getValue();
		Events.postEvent(SystemConfigConstant.ON_SYS_CLASS_SELECT, this.self
				.getRoot(), sysClass);
	}

	/**
	 * 类保存后的事件
	 * 
	 * @throws Exception
	 */
	public void onSaveSysClassResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getSysClassPaging().setActivePage(0);
		this.querySysClass = (SysClass) event.getOrigin().getData();
		this.onQuerySysClass();
	}

	/**
	 * 类删除后的事件
	 * 
	 * @throws Exception
	 */
	public void onDelSysClassResponse(final ForwardEvent event)
			throws Exception {
		SysClass sysClass = (SysClass) event.getOrigin().getData();
		ListModelList model = (ListModelList) this.bean.getSysClassListBox()
				.getModel();
		model.remove(sysClass);
		ListModel dataList = new BindingListModelList(model, true);
		this.bean.getSysClassListBox().setModel(dataList);
	}
}
