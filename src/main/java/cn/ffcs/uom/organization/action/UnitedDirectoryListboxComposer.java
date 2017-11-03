package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.organization.action.bean.UnitedDirectoryListboxBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.UnitedDirectoryManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.UnitedDirectory;

/**
 * 集团统一目录查询
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class UnitedDirectoryListboxComposer extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1383409948363425050L;

	/**
	 * bean.
	 */
	private UnitedDirectoryListboxBean bean = new UnitedDirectoryListboxBean();

	private UnitedDirectoryManager unitedDirectoryManager = (UnitedDirectoryManager) ApplicationContextUtil
			.getBean("unitedDirectoryManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/united_directory_listbox.zul";

	/**
	 * 查询UnitedDirectory.
	 */
	private UnitedDirectory qryUnitedDirectory;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 页面标志
	 */
	@Getter
	@Setter
	private String pagePosition;

	public UnitedDirectoryListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}
	
	/**
	 * 分页响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onQueryUnitedDirectoryPaging() throws Exception {
		this.queryUnitedDirectory();
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryUnitedDirectory() throws Exception {
		qryUnitedDirectory = new UnitedDirectory();
		qryUnitedDirectory.setCtou(this.bean.getCtou().getValue());
		qryUnitedDirectory.setDeptname(this.bean.getDeptname().getValue());

		ListboxUtils.clearListbox(bean.getUnitedDirectoryListBox());
		PageInfo pageInfo = unitedDirectoryManager
				.queryPageInfoByUnitedDirectory(qryUnitedDirectory, this.bean
						.getUnitedDirectoryListPaging().getActivePage() + 1,
						this.bean.getUnitedDirectoryListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getUnitedDirectoryListBox().setModel(dataList);
		this.bean.getUnitedDirectoryListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryUnitedDirectory() throws Exception {
		this.bean.getUnitedDirectoryListPaging().setActivePage(0);
		this.queryUnitedDirectory();
	}
	
	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetUnitedDirectory() throws Exception {

		this.bean.getCtou().setValue("");

		this.bean.getDeptname().setValue("");

	}
}
