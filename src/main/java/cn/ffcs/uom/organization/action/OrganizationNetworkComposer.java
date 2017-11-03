/**
 * 
 */
package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.tuple.Pair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.JaxbUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.OrganizationInfoExtBean;
import cn.ffcs.uom.organization.action.bean.OrganizationNetworkMainBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationNetwork;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.webservices.bean.network.ObjectFactory;
import cn.ffcs.uom.webservices.bean.network.Root.Data;
import cn.ffcs.uom.webservices.bean.network.Root.Data.BasicInfo;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.IntfLogManager;
import cn.ffcs.uom.webservices.manager.impl.IntfLogManagerImpl;
import cn.ffcs.uom.webservices.model.IntfLog;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;
import cn.ffcs.uom.webservices.util.WsClientUtil;
import cn.ffcs.uom.webservices.util.WsUtil;

/**
 * @author yahui
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class OrganizationNetworkComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrganizationNetwork organizationNetwork = new OrganizationNetwork();
	/**
	 * bean
	 */
	OrganizationNetworkMainBean bean = new OrganizationNetworkMainBean();

	/**
	 * 组织
	 */
	private Organization organization;
	/**
	 * 组织关系
	 */
	private OrganizationRelation organizationRelation;

	/**
	 * Manager.
	 */
	private IntfLogManager intfLogManager = (IntfLogManager) ApplicationContextUtil
			.getBean("intfLogManager");

	/**
	 * Manager.
	 */
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");
	/**
	 * Manager.
	 */

	/**
	 * Manager.
	 */
	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

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
		this.setPortletInfoProvider(this);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$organizationNetworkWin() throws Exception {
		bindCombobox();
		setPage();
	}

	public void onAdd() throws Exception {
		organization = new Organization();
		// 验证数据格式
		String msg = this.doValidDate("add");
		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		// 页面bean填充organization
		this.fillPoFromBean(bean, organization, "add");

		// 判断组织是否存在
		Organization theOrgnameCond = new Organization();
		theOrgnameCond.setOrgCode(bean.getOrgCode().getValue());
		Organization theOrg = this.organizationManager
				.queryOrganizationByOrgCode(theOrgnameCond);
		if (theOrg != null) {
			ZkUtil.showError("组织已经存在", "提示信息");
			return;
		}
		// 获取上级组织信息
		Organization thehighOrgnameCond = new Organization();
		thehighOrgnameCond.setOrgCode(bean.getThehighOrgname().getValue());
		Organization thehighOrg = this.organizationManager
				.queryOrganizationByOrgCode(thehighOrgnameCond);
		if (thehighOrg == null) {
			ZkUtil.showError("上级组织不存在", "提示信息");
			return;
		}
		// 拼接组织全称
		StringBuffer fullName = new StringBuffer();
		fullName.append(thehighOrg.getOrgFullName() == null ? "" : thehighOrg
				.getOrgFullName());
		fullName.append(organization.getOrgName());
		String orgName = organization.getOrgName();
		String orgFullName = fullName.toString();
		/**
		 * 组织全称变更 去除组织名称和组织全称中的空格
		 */
		orgName = StrUtil.removeTabAndEnter(orgName);
		orgFullName = StrUtil.removeTabAndEnter(orgFullName);
		organization.setOrgFullName(orgName);
		organization.setOrgFullName(orgFullName);
		// 本机组织区域编码直接获取上级组织的区域编码
		organization.setAreaCodeId(thehighOrg.getAreaCodeId());
		// 组织关系
		OrganizationRelation or = new OrganizationRelation();
		String relaCd = this.bean.getOrgRelaType().getSelectedItem().getValue()
				.toString();
		// or.setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS);
		or.setRelaCd(relaCd);
		or.setRelaOrgId(thehighOrg.getOrgId());
		List<OrganizationRelation> orList = new ArrayList<OrganizationRelation>();
		orList.add(or);
		organization.setOrganizationRelationList(orList);

		// 组织类型
		OrgType orgType = new OrgType();
		orgType.setOrgTypeCd(this.bean.getOrgTypeCd().getSelectedItem()
				.getValue().toString());
		List<OrgType> addOrgTypeList = new ArrayList<OrgType>();
		addOrgTypeList.add(orgType);
		organization.setAddOrgTypeList(addOrgTypeList);
		Object[] r = new Object[] { false, "操作失败" };
		if ("1".equals(this.bean.getIsCheck().getSelectedItem().getValue()
				.toString())) {// 1标识需要跟ITSM校验
			r = operateNerwork("1");
		} else {
			r = new Object[] { true, "操作成功" };
		}

		if ((Boolean) r[0]) {
			this.organizationManager.addOrgNetwork(organization);
			Messagebox.show((String) r[1]);
			return;
		} else {
			Messagebox.show((String) r[1]);
			return;
		}
	}

	public void onMod() throws Exception {

		organization = new Organization();
		// 验证数据格式
		String msg = this.doValidDate("mod");
		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		// 判断组织是否存在
		Organization theOrgnameCond = new Organization();
		theOrgnameCond.setOrgCode(bean.getOrgCode().getValue());
		Organization theOrg = this.organizationManager
				.queryOrganizationByOrgCode(theOrgnameCond);
		if (theOrg == null) {
			ZkUtil.showError("组织不存在", "提示信息");
			return;
		}
		organization = theOrg;

		// 页面bean填充organization
		this.fillPoFromBean(bean, organization, "mod");
		// 获取上级组织信息
		Organization thehighOrgnameCond = new Organization();
		thehighOrgnameCond.setOrgCode(bean.getThehighOrgname().getValue());
		Organization thehighOrg = this.organizationManager
				.queryOrganizationByOrgCode(thehighOrgnameCond);
		if (thehighOrg == null) {
			ZkUtil.showError("上级组织不存在", "提示信息");
			return;
		}
		// 拼接组织全称
		StringBuffer fullName = new StringBuffer();
		fullName.append(thehighOrg.getOrgFullName() == null ? "" : thehighOrg
				.getOrgFullName());
		fullName.append(organization.getOrgName());
		String orgName = organization.getOrgName();
		String orgFullName = fullName.toString();
		/**
		 * 组织全称变更 去除组织名称和组织全称中的空格
		 */
		orgName = StrUtil.removeTabAndEnter(orgName);
		orgFullName = StrUtil.removeTabAndEnter(orgFullName);
		organization.setOrgFullName(orgName);
		organization.setOrgFullName(orgFullName);
		// 本机组织区域编码直接获取上级组织的区域编码
		organization.setAreaCodeId(thehighOrg.getAreaCodeId());
		// 组织关系
		String relaCd = this.bean.getOrgRelaType().getSelectedItem().getValue()
				.toString();
		List<OrganizationRelation> DelOrList = new ArrayList<OrganizationRelation>();
		List<OrganizationRelation> orList = new ArrayList<OrganizationRelation>();
		for (OrganizationRelation exitOr : organization
				.getOrganizationRelationList()) {
			if (relaCd.equals(exitOr.getRelaCd())
					&& !exitOr.getRelaOrgId().equals(thehighOrg.getOrgId())) {
				DelOrList.add(exitOr);
			}
		}
		boolean isAddFlag = true;
		for (OrganizationRelation exitOr : organization
				.getOrganizationRelationList()) {
			if (relaCd.equals(exitOr.getRelaCd())
					&& thehighOrg.getOrgId().equals(exitOr.getRelaOrgId())) {
				isAddFlag = false;
			}
		}
		if (isAddFlag) {
			OrganizationRelation or = new OrganizationRelation();
			or.setRelaCd(relaCd);
			or.setRelaOrgId(thehighOrg.getOrgId());
			orList.add(or);
		}
		organization.setDelOrganizationRelationList(DelOrList);
		organization.setAddOrganizationRelationList(orList);

		/**
		 * 已经存在的组织类型列表
		 */
		List<OrgType> orgTypeList = organization.getOrgTypeList();
		/**
		 * 删除的组织类型
		 */
		List<OrgType> delOrgTypeList = new ArrayList<OrgType>();
		/**
		 * 新增的组织类型
		 */
		List<OrgType> addOrgTypeList = new ArrayList<OrgType>();

		for (OrgType orgType : orgTypeList) {
			if (orgType.getOrgTypeCd().contains(
					OrganizationConstant.ORG_TYPE_N0202)) {
				if (!orgType.getOrgTypeCd().equals(
						this.bean.getOrgTypeCd().getSelectedItem().getValue()
								.toString())) {
					delOrgTypeList.add(orgType);
				}
			}
			if (!this.bean.getOrgTypeCd().getSelectedItem().getValue()
					.toString().equals(orgType.getOrgTypeCd())) {
				organization.setDelOrgTypeList(delOrgTypeList);
				OrgType addOrgType = new OrgType();
				addOrgType.setOrgTypeCd(this.bean.getOrgTypeCd()
						.getSelectedItem().getValue().toString());
				addOrgTypeList.add(addOrgType);
				organization.setAddOrgTypeList(addOrgTypeList);
			}
		}
		boolean isSuccess = false;
		Object[] r = new Object[] { false, "操作失败" };
		if ("1".equals(this.bean.getIsCheck().getSelectedItem().getValue()
				.toString())) {
			r = operateNerwork("3");// 3代表修改
		} else {
			r = new Object[] { true, "操作成功" };
		}
		// isSuccess = true;
		if ((Boolean) r[0]) {
			this.organizationManager.updateOrgNetwork(organization);
			Messagebox.show((String) r[1]);
		} else {
			Messagebox.show((String) r[1]);
			return;
		}
	}

	public void onDel() throws Exception {

		// 验证数据格式
		String msg = this.doValidDate("del");
		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		/**
		 * 组织存在下级组织不可删除
		 */
		Organization theOrgnameCond = new Organization();
		theOrgnameCond.setOrgCode(bean.getOrgCode().getValue());
		Organization org = this.organizationManager
				.queryOrganizationByOrgCode(theOrgnameCond);
		if (org == null) {
			ZkUtil.showQuestion("组织不存在", "提示信息");
			return;
		}
		List<OrganizationRelation> orgRelaList = org
				.getOrganizationRelationList();
		if (org != null && org.getSubOrganizationList() != null
				&& org.getSubOrganizationList().size() == 1) {
			ZkUtil.showQuestion("该组织存在下级节点，不能删除该唯一关系", "提示信息");
			return;
		}
		Object[] r = new Object[] { false, "操作失败" };
		if ("1".equals(this.bean.getIsCheck().getSelectedItem().getValue()
				.toString())) {
			r = operateNerwork("2");// 2代表删除
		} else {
			r = new Object[] { true, "操作成功" };
		}
		// isSuccess = true;
		if ((Boolean) r[0]) {
			this.organizationManager.delOrgNetwork(org);
			Messagebox.show((String) r[1]);
		} else {
			Messagebox.show((String) r[1]);
			return;
		}
	}

	public void onCheck() throws Exception {
		if (this.bean.getSerial() == null
				|| StrUtil.isEmpty(this.bean.getSerial().getValue())) {
			ZkUtil.showQuestion("单据号不能为空", "提示信息");
			return;
		}

		if (this.bean.getOrgName() == null
				|| StrUtil.isEmpty(this.bean.getOrgName().getValue())) {
			ZkUtil.showQuestion("本级组织名称不能为空", "提示信息");
			return;
		}
		if (this.bean.getThehighOrgname() == null
				|| StrUtil.isEmpty(this.bean.getThehighOrgname().getValue())) {
			ZkUtil.showQuestion("上级组织编码不能为空", "提示信息");
			return;
		}
		String queryNetworkUrl = UomClassProvider
				.getIntfUrl(WsConstants.NETWORK_ITSM_URL);
		// String queryNetworkUrl =
		// "http://134.64.110.182:9999/service/mboss/route";
		if (StrUtil.isEmpty(queryNetworkUrl)) {
			ZkUtil.showError("操作失败,ITSM网点信息查询未配置", "操作失败信息");
			return;
		}
		OrganizationNetwork vo = new OrganizationNetwork();
		vo.setAction("");
		vo.setIfOccupy("0");
		vo.setInstance(DateUtil.getDateTime(new Date()));
		vo.setSerial(bean.getSerial().getValue());
		vo.setThehighOrgname(bean.getThehighOrgname().getValue());
		vo.setTheOrgname(bean.getOrgName().getValue());
		boolean isOccupy = checkNetwork(vo, queryNetworkUrl);
		if (isOccupy) {
			Messagebox.show("所填" + bean.getSerial().getValue()
					+ "单据号在ITSM未被占用，可使用");
		} else {
			Messagebox.show("所填" + bean.getSerial().getValue()
					+ "单据号在ITSM被占用，不可使用");
		}
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPage() throws Exception {

		boolean canAdd = false;
		boolean canMod = false;
		boolean canDel = false;
		boolean canCheck = false;
		boolean canIsCheck = false;
		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canMod = true;
			canDel = true;
			canCheck = true;
			canIsCheck = true;
		} else {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_NETWORK_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_NETWORK_MOD)) {
				canMod = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_NETWORK_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_NETWORK_CHECK)) {
				canCheck = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_NETWORK_IS_CHECK)) {
				canIsCheck = true;
			}
		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getModButton().setVisible(canMod);
		this.bean.getDelButton().setVisible(canDel);
		this.bean.getCheckButton().setVisible(canCheck);
		this.bean.getIsCheck().setVisible(canIsCheck);
		this.bean.getIsCheckL().setVisible(canIsCheck);

	}

	/**
	 * 验证数据
	 * 
	 * @return
	 */
	public String doValidDate(String opType) {
		if (!"add".equals(opType)) {
			if (this.bean.getOrgCode() == null
					|| StrUtil.isEmpty(this.bean.getOrgCode().getValue())) {
				return "网点编码不能为空";
			}
		}
		if ("1".equals(this.bean.getIsCheck().getSelectedItem().getValue()
				.toString())) {// 1代表要跟ITSM校验需要验证单据号
			if (this.bean.getSerial() == null
					|| StrUtil.isEmpty(this.bean.getSerial().getValue())) {
				return "单据号不能为空";
			}
		}

		if (this.bean.getOrgName() == null
				|| StrUtil.isEmpty(this.bean.getOrgName().getValue())) {
			return "本级组织名称不能为空";
		}
		if (this.bean.getThehighOrgname() == null
				|| StrUtil.isEmpty(this.bean.getThehighOrgname().getValue())) {
			return "上级编码不能为空";
		}

		if (this.bean.getOrgType() == null
				|| this.bean.getOrgType().getSelectedItem() == null
				|| this.bean.getOrgType().getSelectedItem().getValue() == null
				|| StrUtil.isEmpty(this.bean.getOrgType().getSelectedItem()
						.getValue().toString())) {
			return "组织性质不能为空";
		}
		if (this.bean.getExistType() == null
				|| this.bean.getExistType().getSelectedItem() == null
				|| this.bean.getExistType().getSelectedItem().getValue() == null
				|| StrUtil.isEmpty(this.bean.getExistType().getSelectedItem()
						.getValue().toString())) {
			return "存在类型称不能为空";
		}
		if (this.bean.getOrgTypeCd() == null
				|| this.bean.getOrgTypeCd().getSelectedItem() == null
				|| this.bean.getOrgTypeCd().getSelectedItem().getValue() == null) {
			return "组织类型不能为空";
		}
		if (this.bean.getOrgPriority() == null
				|| this.bean.getOrgPriority().getValue() == null) {
			return "组织排序号不能为空";
		}
		if (this.bean.getTelcomRegionTreeBandbox() == null
				|| this.bean.getTelcomRegionTreeBandbox().getTelcomRegion() == null) {
			return "电信管理区域不能为空";
		}
		if (this.bean.getPoliticalLocationTreeBandbox() == null
				|| this.bean.getPoliticalLocationTreeBandbox()
						.getPoliticalLocation() == null) {
			return "行政区域不能为空";
		}
		if (this.bean.getOrgCode().equals(
				this.bean.getThehighOrgname().getValue())) {
			return "上级组织编码不能跟本机组织编码一样";
		}
		if (this.bean.getOrgRelaType() == null
				|| this.bean.getOrgRelaType().getSelectedItem() == null
				|| this.bean.getExistType().getSelectedItem().getValue() == null
				|| StrUtil.isEmpty(this.bean.getExistType().getSelectedItem()
						.getValue().toString())) {
			return "组织关系类型不能为空";
		}
		return "";

	}

	/**
	 * 填充po
	 */
	private void fillPoFromBean(OrganizationNetworkMainBean bean,
			Organization organization, String opType) {
		String oldOrgFullName = organization.getOrgFullName();
		String oldOrgName = organization.getOrgName();
		PubUtil.fillPoFromBean(bean, organization);
		if (this.bean.getOrgPriority().getValue() != null) {
			organization.setOrgPriority(this.bean.getOrgPriority().getValue());
		}
		/**
		 * 行政管理区域
		 */
		if (this.bean.getPoliticalLocationTreeBandbox().getPoliticalLocation() != null) {
			organization.setLocationId(this.bean
					.getPoliticalLocationTreeBandbox().getPoliticalLocation()
					.getLocationId());
		}
		/**
		 * 电信管理区域
		 */
		if (this.bean.getTelcomRegionTreeBandbox().getTelcomRegion() != null) {
			organization.setTelcomRegionId(this.bean
					.getTelcomRegionTreeBandbox().getTelcomRegion()
					.getTelcomRegionId());
		}

		String orgName = organization.getOrgName();
		String orgFullName = organization.getOrgFullName();
		if ("add".equals(opType)) {
			/**
			 * 新增的时候组织全称设置为组织名
			 */
			orgFullName = orgName;
		} else if ("mod".equals(opType)) {
			if (!StrUtil.isEmpty(oldOrgFullName)
					&& oldOrgFullName.endsWith(oldOrgName)) {
				int index = oldOrgFullName.indexOf(oldOrgName);
				String preOrgFullName = "";
				if (index != -1 && oldOrgFullName.endsWith(oldOrgName)) {
					preOrgFullName = orgFullName.substring(0, index);
					orgFullName = preOrgFullName + orgName;
				}
			} else {
				// 如果后面名称对不上就查库重新生成组织全称,生成为空即还没上挂组织树用组织名
				String temp = this.organizationRelationManager
						.getOrgFullName(organization.getOrgId());
				if (!StrUtil.isEmpty(temp)) {
					orgFullName = temp;
				} else {
					orgFullName = orgName;
				}
			}
		}
		/**
		 * 组织全称变更 去除组织名称和组织全称中的空格
		 */
		orgName = StrUtil.removeTabAndEnter(orgName);
		orgFullName = StrUtil.removeTabAndEnter(orgFullName);
		organization.setOrgFullName(orgName);
		organization.setOrgFullName(orgFullName);
		if (!StrUtil.isEmpty(oldOrgName) && !StrUtil.isEmpty(orgName)
				&& !oldOrgName.equals(orgName)) {
			organization.setIsChangeOrgName(true);
		}

	}

	/**
	 * 绑定下拉框
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> orgTypeList = UomClassProvider.getValuesList(
				"Organization", "orgType");
		ListboxUtils.rendererForEdit(this.bean.getOrgType(), orgTypeList);

		List<NodeVo> networkTypeList = UomClassProvider.getValuesList(
				"OrgType", "orgTypeCd", OrganizationConstant.ORG_TYPE_N0202);
		ListboxUtils.rendererForEdit(this.bean.getOrgTypeCd(), networkTypeList);

		List<NodeVo> existTypeList = UomClassProvider.getValuesList(
				"Organization", "existType");
		ListboxUtils.rendererForEdit(this.bean.getExistType(), existTypeList);

		List<NodeVo> cityTownList = UomClassProvider.getValuesList(
				"Organization", "cityTown");
		ListboxUtils.rendererForEdit(bean.getCityTown(), cityTownList);

	}

	/**
	 * 网点信息增删改查
	 * 
	 * @param opType
	 * @return
	 */
	public Object[] operateNerwork(String opType) {
		String queryNetworkUrl = UomClassProvider
				.getIntfUrl(WsConstants.NETWORK_ITSM_URL);
		// String queryNetworkUrl =
		// "http://134.64.110.182:9999/service/mboss/route";
		if (StrUtil.isEmpty(queryNetworkUrl)) {
			return new Object[] { false, "操作失败,ITSM网点信息查询地址未配置" };
		}
		OrganizationNetwork vo = new OrganizationNetwork();
		vo.setAction(opType);
		vo.setIfOccupy("0");
		vo.setInstance(DateUtil.getDateTime(new Date()));
		vo.setSerial(bean.getSerial().getValue());
		vo.setThehighOrgname(bean.getThehighOrgname().getValue());
		vo.setTheOrgname(bean.getOrgName().getValue());
		boolean ifOccupy = checkNetwork(vo, queryNetworkUrl);

		if (!ifOccupy) {
			return new Object[] { false, "单据号" + vo.getSerial() + "在ITSM被占用" };
		}
		vo.setIfOccupy("1");
		vo.setNetworkCode(bean.getOrgCode().getValue());

		return renewNetwork(vo, queryNetworkUrl);
	}

	/**
	 * 调用ISTM查询网点信息
	 * 
	 * @param vo
	 * @param url
	 * @return
	 */
	public boolean checkNetwork(OrganizationNetwork vo, String url) {

		String outXml = null;
		String reqXml = createNetworkReqXml(vo);
		String errMsg = null;
		Date beginDate = new Date();
		String msgId = EsbHeadUtil.getEAMMsgId(WsConstants.OIP_SENDER);
		String esbHead = EsbHeadUtil.getEsbHead(WsConstants.OIP_SENDER,
				WsConstants.STSM_OIP_SERVICE_CODE, msgId, "");
		try {
			// outXml = WsClientUtil.wsCallUtil(reqXml, url, "checkNetwork");
			outXml = WsClientUtil.wsCallOnOip(esbHead, reqXml, url,
					"checkNetwork", "xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			errMsg = e.getMessage();
			e.printStackTrace();
		}
		boolean flag = checkResult(outXml);
		IntfLog intfLog = new IntfLog();
		intfLog.setTransId(vo.getInstance());
		intfLog.setSystem("ITSM");
		intfLog.setMsgType("查询网点信息");
		intfLog.setResult(new Long(flag ? 1 : 0));
		intfLog.setRequestContent(reqXml);
		intfLog.setResponseContent(outXml);
		intfLog.setBeginDate(beginDate);
		String errCode = WsUtil.getXmlContent(outXml, "result");
		intfLog.setErrCode(errCode);
		if (errMsg == null) {
			errMsg = WsUtil.getXmlContent(outXml, "desc");
		}
		intfLog.setErrMsg(errMsg);
		Date endDate = new Date();
		intfLog.setEndDate(endDate);
		intfLog.setConsumeTime(Math.abs(endDate.getTime() - beginDate.getTime()));
		intfLogManager.saveIntfLog(intfLog);
		return flag;
	}

	/**
	 * 调用ITSM更新网点信息
	 * 
	 * @param vo
	 * @param url
	 * @return
	 */
	public Object[] renewNetwork(OrganizationNetwork vo, String url) {

		String outXml = null;
		boolean flag = false;
		String errMsg = null;
		String reqXml = createNetworkReqXml(vo);
		Date beginDate = new Date();
		String msgId = EsbHeadUtil.getEAMMsgId(WsConstants.OIP_SENDER);
		String esbHead = EsbHeadUtil.getEsbHead(WsConstants.OIP_SENDER,
				WsConstants.STSM_OIP_SERVICE_CODE, msgId, "");
		try {
			// outXml = WsClientUtil.wsCallUtil(reqXml, url, "RenewNetwork");
			outXml = WsClientUtil.wsCallOnOip(esbHead, reqXml, url,
					"RenewNetwork", "xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			errMsg = e.getMessage();
			e.printStackTrace();
		}
		flag = checkResult(outXml);
		IntfLog intfLog = new IntfLog();
		intfLog.setTransId(DateUtil.getDateTime(new Date()));
		intfLog.setSystem("ITSM");
		String action = null;
		if ("1".equals(vo.getAction())) {
			action = "新增";
		} else if ("2".equals(vo.getAction())) {
			action = "删除";
		} else {
			action = "修改";
		}
		intfLog.setMsgType(action + "网点信息");
		intfLog.setResult(new Long(flag ? 1 : 0));
		intfLog.setRequestContent(reqXml);
		intfLog.setResponseContent(outXml);
		intfLog.setBeginDate(beginDate);
		String errCode = WsUtil.getXmlContent(outXml, "result");
		intfLog.setErrCode(errCode);
		if (errMsg == null) {
			errMsg = WsUtil.getXmlContent(outXml, "desc");
		}
		intfLog.setErrMsg(errMsg);
		Date endDate = new Date();
		intfLog.setEndDate(endDate);
		intfLog.setConsumeTime(Math.abs(endDate.getTime() - beginDate.getTime()));
		intfLogManager.saveIntfLog(intfLog);

		return new Object[] { flag, errMsg };
	}

	/**
	 * 判断接口成功失败
	 * 
	 * @param resultXml
	 * @return
	 */
	public boolean checkResult(String resultXml) {
		boolean flag = false;
		try {
			Document document = DocumentHelper.parseText(resultXml);
			Element root = document.getRootElement();
			Element result = (Element) root.selectSingleNode("//result");
			if ("0".equals(result.getTextTrim())) {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;

	}

	/**
	 * 创建请求报文
	 * 
	 * @param vo
	 * @return
	 */
	public String createNetworkReqXml(OrganizationNetwork vo) {

		ObjectFactory of = new ObjectFactory();
		cn.ffcs.uom.webservices.bean.network.Root root = of.createRoot();
		Data data = of.createRootData();
		BasicInfo basicInfo = of.createRootDataBasicInfo();
		basicInfo.setAction(vo.getAction());
		basicInfo.setIfOccupy(vo.getIfOccupy());
		basicInfo.setInstance(vo.getInstance());
		basicInfo.setNetworkCode(vo.getNetworkCode());
		basicInfo.setSerial(vo.getSerial());
		basicInfo.setThehighOrgname(vo.getThehighOrgname());
		basicInfo.setTheOrgname(vo.getTheOrgname());
		root.setData(data);
		data.setBasicInfo(basicInfo);
		String xml = JaxbUtil.convertToXml(root, "UTF-8");
		return xml;
	}

}
