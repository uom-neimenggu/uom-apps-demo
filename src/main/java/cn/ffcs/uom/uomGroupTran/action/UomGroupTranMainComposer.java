package cn.ffcs.uom.uomGroupTran.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.uomGroupTran.action.bean.UomGroupTranMainBean;

/**
 * 省内业务关系.
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-04-14
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class UomGroupTranMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private UomGroupTranMainBean bean = new UomGroupTranMainBean();

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getUomGroupOrgTranListboxExt().setPortletInfoProvider(this);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$uomGroupTranMainWin() {
		try {
			initPage();
			initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * tab响应
	 * 
	 * @throws Exception
	 * 
	 */
	public void callTab() {
		try {

			if (bean.getSelectTab() == null) {
				bean.setSelectTab(bean.getTabBox().getSelectedTab());
			}

			String tab = this.bean.getSelectTab().getId();
			if ("uomGroupOrgTranTab".equals(tab)) {
				// Events.postEvent(SffOrPtyCtants.ON_STAFF_Org_QUERY,
				// this.bean.getTranOrgListboxExt(), staff);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClickTab(ForwardEvent event) {
		try {
			Event origin = event.getOrigin();
			if (origin != null) {
				Component comp = origin.getTarget();
				if (comp != null && comp instanceof Tab) {
					bean.setSelectTab((Tab) comp);
					callTab();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getUomGroupOrgTranListboxExt().setPagePosition(
				"uomGroupTranPage");
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		/**
		 * 组织业务关系-可选项
		 */
		List<String> optionAttrValueList = new ArrayList<String>();

		// optionAttrValueList
		// .add(OrganizationTranConstant.UOM_FINACIAL_COMPANY_GROUP_COMPANY_CODE_ONE_TO_ONE);
		// optionAttrValueList
		// .add(OrganizationTranConstant.UOM_FINACIAL_PROFIT_GROUP_PROFIT_CENTER_ONE_TO_ONE);
		// optionAttrValueList
		// .add(OrganizationTranConstant.UOM_FINACIAL_COST_GROUP_COST_CENTER_ONE_TO_ONE);
		optionAttrValueList
				.add(OrganizationTranConstant.UOM_GROUP_DIRECTORY_ONE_TO_ONE);
		optionAttrValueList
				.add(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE);
		optionAttrValueList
				.add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE);
		optionAttrValueList
				.add(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE);
		optionAttrValueList
				.add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_BANK_MANY_TO_ONE);

		this.bean.getUomGroupOrgTranListboxExt().getBean().getTranRelaType()
				.setOptionNodes(optionAttrValueList);
	}
}
