package cn.ffcs.uom.organization.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.organization.action.bean.SupplierInfoMainBean;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.manager.GroupOrganizationManager;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;

/**
 * 组织业务关系 Edit Composer .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-17
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "rawtypes" })
public class SupplierInfoMainComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("groupOrganizationManager")
	private GroupOrganizationManager groupOrganizationManager;

	private SupplierInfoMainBean bean = new SupplierInfoMainBean();

	private UomGroupOrgTran uomGroupOrgTran;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$supplierInfoMainWin() {
		uomGroupOrgTran = (UomGroupOrgTran) arg.get("uomGroupOrgTran");
		Events.postEvent(OrganizationTranConstant.ON_SUPPLIER_INFO_REQUEST,
				bean.getGroupOrganizationListboxExt(), uomGroupOrgTran);
	}

}
