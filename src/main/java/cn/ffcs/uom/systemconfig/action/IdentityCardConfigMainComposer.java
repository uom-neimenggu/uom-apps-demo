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
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
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
import cn.ffcs.uom.systemconfig.action.bean.IdentityCardConfigMainBean;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

@Controller
@Scope("prototype")
public class IdentityCardConfigMainComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private IdentityCardConfigMainBean bean = new IdentityCardConfigMainBean();

	private IdentityCardConfigManager identityCardConfigManager = (IdentityCardConfigManager) ApplicationContextUtil
			.getBean("identityCardConfigManager");
	/**
	 * 选中的业务系统
	 */
	private IdentityCardConfig identityCardConfig;
	/**
	 * 查询身份证类型
	 */
	private IdentityCardConfig queryIdentityCardConfig;
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
	public void onCreate$identityCardConfigMainWin() throws Exception {
		onIdentityCardConfigQuery();
		setIdentityCardConfigButtonValid(true, false, false);
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onIdentityCardConfigAdd() throws Exception {
		this.openIdentityCardConfigEditWin("add");
		setIdentityCardConfigButtonValid(true, false, false);
	}

	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	public void onIdentityCardConfigEdit() throws Exception {
		this.openIdentityCardConfigEditWin("mod");
		setIdentityCardConfigButtonValid(true, false, false);
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openIdentityCardConfigEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("mod".equals(type)) {
			arg.put("identityCardConfig", identityCardConfig);
		}
		Window win = (Window) Executions
				.createComponents(
						"/pages/system_config/identity_card_config_edit.zul",
						null, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					IdentityCardConfig newIdentityCardConfig = (IdentityCardConfig) event
							.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getIdentityCardConfigListBox(),
								newIdentityCardConfig, "add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getIdentityCardConfigListBox(),
								newIdentityCardConfig, "mod");
						identityCardConfig = newIdentityCardConfig;
					}
				}
			}
		});
	}

	/**
	 * 删除
	 */
	public void onIdentityCardConfigDel() throws Exception {
		if (identityCardConfig != null) {
			ZkUtil.showQuestion("你确定要删除身份证类型配置信息吗?", "提示信息",
					new EventListener() {
						@Override
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							if (result == Messagebox.OK) {
								identityCardConfigManager
										.removeIdentityCardConfig(identityCardConfig);
								PubUtil.reDisplayListbox(
										bean.getIdentityCardConfigListBox(),
										identityCardConfig, "del");
								setIdentityCardConfigButtonValid(true, false,
										false);
							}
						}
					});
		} else {
			ZkUtil.showError("请选择你要删除的身份证类型配置信息!", "提示信息");
			return;
		}
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onIdentityCardConfigListboxPaging() throws Exception {
		PageInfo pageInfo = this.identityCardConfigManager
				.queryPageInfoByIdentityCardConfig(queryIdentityCardConfig,
						this.bean.getIdentityCardConfigListboxPaging()
								.getActivePage() + 1, this.bean
								.getIdentityCardConfigListboxPaging()
								.getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getIdentityCardConfigListBox().setModel(dataList);
		this.bean.getIdentityCardConfigListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 点击查询
	 * 
	 * @throws Exception
	 */
	public void onIdentityCardConfigQuery() throws Exception {

		this.bean.getIdentityCardConfigListboxPaging().setActivePage(0);

		this.queryIdentityCardConfig = new IdentityCardConfig();

		if (!StrUtil.isEmpty(this.bean.getIdentityCardName().getValue())) {
			this.queryIdentityCardConfig.setIdentityCardName(this.bean
					.getIdentityCardName().getValue());
		}

		if (!StrUtil.isEmpty(this.bean.getIdentityCardPrefix().getValue())) {
			this.queryIdentityCardConfig.setIdentityCardPrefix(this.bean
					.getIdentityCardPrefix().getValue());
		}

		this.onIdentityCardConfigListboxPaging();
	}

	/**
	 * .重置查询内容 .
	 */
	public void onIdentityCardConfigReset() {
		bean.getIdentityCardName().setValue(null);
		bean.getIdentityCardPrefix().setValue(null);
	}

	/**
	 * 选中类列表
	 * 
	 * @throws Exception
	 */
	public void onSelectIdentityCardConfigListBox() throws Exception {
		identityCardConfig = (IdentityCardConfig) this.bean
				.getIdentityCardConfigListBox().getSelectedItem().getValue();
		setIdentityCardConfigButtonValid(true, true, true);
	}

	/**
	 * 设置按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setIdentityCardConfigButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canDelete) {
		this.bean.getAddIdentityCardConfigButton().setDisabled(!canAdd);
		this.bean.getEditIdentityCardConfigButton().setDisabled(!canEdit);
		this.bean.getDelIdentityCardConfigButton().setDisabled(!canDelete);
	}
}
