package cn.ffcs.uom.common.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.areacode.model.AreaCode;
import cn.ffcs.uom.common.action.bean.DiverseAttributeMainBean;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.manager.OperateLogManager;
import cn.ffcs.uom.common.model.DiverseAttribute;
import cn.ffcs.uom.common.model.ForeignKey;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ReflectObject;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.OrgAttrSpec;
import cn.ffcs.uom.organization.model.OrgAttrSpecSort;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.OrganizationTreeRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.organization.model.TreeExtendAttr;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffAttrSpec;
import cn.ffcs.uom.staff.model.StaffAttrValue;
import cn.ffcs.uom.staff.model.StaffExtendAttr;
import cn.ffcs.uom.staff.model.StaffPosition;
import cn.ffcs.uom.systemconfig.model.SysClass;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 日志操作查询.
 * 
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class DiverseAttributeComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private DiverseAttributeMainBean bean = new DiverseAttributeMainBean();
	/**
	 * 选中的组织树
	 */
	private OrgTree orgTree;
	/**
	 * 查询组织树
	 */
	private OrgTree queryOrgTree;
	/**
	 * 员工扩展属性
	 */
	private StaffExtendAttr oldStaffExtendAttr;
	/**
	 * 员工扩展属性
	 */
	private StaffExtendAttr newStaffExtendAttr;
	/**
	 * 选中的操作日志
	 */
	private OperateLog operateLog;
	/**
	 * 查询操作日志
	 */
	private OperateLog queryOperateLog;
	/**
	 * 外键表
	 */
	private ForeignKey foreignKey;
	/**
	 * list
	 */
	private List<List> list;
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("operateLogManager")
	private OperateLogManager operateLogManager = (OperateLogManager) ApplicationContextUtil
			.getBean("operateLogManager");

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
	public void onCreate$diverseAttributeMainWin() throws Exception {

		operateLog = (OperateLog) arg.get("operateLog");

		if (operateLog != null && !StrUtil.isEmpty(operateLog.getOpeObject())) {

			this.list = new ArrayList<List>();
			// this.bindDate(operateLog);

			List<OperateLog> operateLogList = operateLogManager
					.queryOperateLogList(operateLog);// 查询有无关联的操作记录

			if (operateLogList != null && operateLogList.size() > 0) {
				for (OperateLog operateLog : operateLogList) {
					this.bindDate(operateLog);
				}
			}

			if (list != null && list.size() > 0) {
				this.bindDate(list);// 向ListBox中填充数据，解决样式延迟加载的问题
			}

		}

	}

	public void bindDate(OperateLog operateLog)
			throws IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {

		if (operateLog != null && !StrUtil.isEmpty(operateLog.getOpeObject())) {

			ReflectObject reflectObject = new ReflectObject();
			List<DiverseAttribute> diverseAttributeList = new ArrayList<DiverseAttribute>();
			List<DiverseAttribute> newDiverseAttributeList = new ArrayList<DiverseAttribute>();

			this.bean.getDiverseAttributeMainWin().setTitle("查看页面");
			StringBuffer sb = new StringBuffer(
					"SELECT * FROM SYS_CLASS WHERE STATUS_CD = ?");
			List sysClassParams = new ArrayList();
			sysClassParams.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(operateLog.getOpeObject())) {
				sb.append(" AND TABLE_NAME = ?");
				sysClassParams.add(operateLog.getOpeObject());
			}

			SysClass sysClass = (SysClass) SysClass.repository()
					.jdbcFindObject(sb.toString(), sysClassParams,
							SysClass.class);

			if (sysClass != null) {

				StringBuffer hql = new StringBuffer("SELECT * FROM "
						+ sysClass.getTableName() + " WHERE 1=1");// 去除状态的影响
				List params = new ArrayList();

				StringBuffer hqlHis = new StringBuffer("SELECT * FROM "
						+ sysClass.getHisTableName() + " WHERE 1=1");// 去除状态的影响
				List paramsHis = new ArrayList();

				// params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);//添加状态
				// paramsHis.add(BaseUnitConstants.ENTT_STATE_INACTIVE);//添加状态

				// 个人数据比较

				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Individual")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND INDIVIDUAL_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params, Individual.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									Individual.class));
				}

				// 员工数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Staff")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Staff.repository().jdbcFindObject(hql.toString(),
									params, Staff.class),
							Staff.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis, Staff.class));
				}

				// 员工扩展属性数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("StaffExtendAttr")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_EXTEND_ATTR_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					StaffExtendAttr exp = new StaffExtendAttr();

					newStaffExtendAttr = (StaffExtendAttr.repository()
							.jdbcFindObject(hql.toString(), params,
									StaffExtendAttr.class) != null) ? StaffExtendAttr
							.repository().jdbcFindObject(hql.toString(),
									params, StaffExtendAttr.class) : exp;

					oldStaffExtendAttr = (StaffExtendAttr.repository()
							.jdbcFindObject(hqlHis.toString(), paramsHis,
									StaffExtendAttr.class) != null) ? StaffExtendAttr
							.repository().jdbcFindObject(hqlHis.toString(),
									paramsHis, StaffExtendAttr.class) : exp;

					diverseAttributeList = reflectObject.reflect(
							StaffExtendAttr.repository().jdbcFindObject(
									hql.toString(), params,
									StaffExtendAttr.class),
							StaffExtendAttr.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									StaffExtendAttr.class));
				}

				// 员工账号数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("StaffAccount")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_ACCOUNT_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject
							.reflect(
									Organization.repository().jdbcFindObject(
											hql.toString(), params,
											StaffAccount.class),
									Organization.repository().jdbcFindObject(
											hqlHis.toString(), paramsHis,
											StaffAccount.class));

				}

				// 员工岗位关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("StaffPosition")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_POSITION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository()
									.jdbcFindObject(hql.toString(), params,
											StaffPosition.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									StaffPosition.class));

				}

				// 员工组织关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("StaffOrganization")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_ORG_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							StaffOrganization.repository().jdbcFindObject(
									hql.toString(), params,
									StaffOrganization.class),
							StaffOrganization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									StaffOrganization.class));

				}

				// 参与人数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Party")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND PARTY_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params, Party.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis, Party.class));

				}

				// 参与人联系人数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PartyContactInfo")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND CONTACT_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params,
									PartyContactInfo.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									PartyContactInfo.class));

				}

				// 参与人证件数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PartyCertification")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND PARTY_CERT_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params,
									PartyCertification.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									PartyCertification.class));

				}

				// 参与人组织数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PartyOrganization")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND PARTY_ORGANIZATION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params,
									PartyOrganization.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									PartyOrganization.class));

				}

				// 参与人角色数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PartyRole")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND PARTY_ROLE_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params, PartyRole.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									PartyRole.class));

				}

				// 岗位数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Position")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND POSITION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Position.repository().jdbcFindObject(
									hql.toString(), params, Position.class),
							Position.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									Position.class));
				}

				// 组织数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Organization")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject
							.reflect(
									Organization.repository().jdbcFindObject(
											hql.toString(), params,
											Organization.class),
									Organization.repository().jdbcFindObject(
											hqlHis.toString(), paramsHis,
											Organization.class));
				}

				// 组织扩展属性数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals(
								"OrganizationExtendAttr")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_EXTEND_ATTR_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params,
									OrganizationExtendAttr.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									OrganizationExtendAttr.class));

				}

				// 组织类型数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("OrgType")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_TYPE_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params, OrgType.class),
							Organization.repository()
									.jdbcFindObject(hqlHis.toString(),
											paramsHis, OrgType.class));

				}

				// 组织关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode()
								.equals("OrganizationRelation")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_REL_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							OrganizationRelation.repository().jdbcFindObject(
									hql.toString(), params,
									OrganizationRelation.class),
							OrganizationRelation.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									OrganizationRelation.class));

				}

				// 组织联系信息数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("OrgContactInfo")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORGANIZATION_CONTACT_INFO_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params,
									OrgContactInfo.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									OrgContactInfo.class));

				}

				// 组织树数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("OrgTree")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_TREE_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							OrgTree.repository().jdbcFindObject(hql.toString(),
									params, OrgTree.class),
							OrgTree.repository()
									.jdbcFindObject(hqlHis.toString(),
											paramsHis, OrgTree.class));

				}

				// 组织树引用关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals(
								"OrganizationTreeRelation")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORGANIZATION_TREE_RELATION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params,
									OrganizationTreeRelation.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									OrganizationTreeRelation.class));

				}

				// 组织岗位关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("OrgPosition")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_POSITION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							OrgPosition.repository().jdbcFindObject(
									hql.toString(), params, OrgPosition.class),
							OrgPosition.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									OrgPosition.class));

				}

				// 树扩展属性数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("TreeExtendAttr")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND TREE_EXTEND_ATTR_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							TreeExtendAttr.repository().jdbcFindObject(
									hql.toString(), params,
									TreeExtendAttr.class),
							TreeExtendAttr.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									TreeExtendAttr.class));

				}

				// 行政管理区域数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PoliticalLocation")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND LOCATION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					if (operateLog.getOpeOriObjectId() != null) {
						hqlHis.append(" AND HIS_ID = ?");
						paramsHis.add(operateLog.getOpeOriObjectId());
					}

					diverseAttributeList = reflectObject.reflect(
							Organization.repository().jdbcFindObject(
									hql.toString(), params,
									PoliticalLocation.class),
							Organization.repository().jdbcFindObject(
									hqlHis.toString(), paramsHis,
									PoliticalLocation.class));

				}

				try {

					for (DiverseAttribute diverseAttribute : diverseAttributeList) {
						diverseAttribute.setTableName(sysClass.getClassName());// 赋中文表名
						diverseAttribute.setClassName(sysClass.getJavaCode());// 赋类名

						if (!(diverseAttribute.getAttrName()
								.equalsIgnoreCase("id"))) {

							// 个人数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"Individual")) {
								// 参与人标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("partyId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party oldParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Party.class);

										if (oldParty != null
												&& !StrUtil.isEmpty(oldParty
														.getPartyName())) {
											diverseAttribute
													.setAttrOldVaule(oldParty
															.getPartyName());
										}

										Party newParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Party.class);

										if (newParty != null
												&& !StrUtil.isEmpty(newParty
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(newParty
															.getPartyName());
										}

									}
								}

							}

							// 员工数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals("Staff")) {
								// 员工职位
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("staffPosition")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 用工性质
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("workProp")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 参与人角色标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("partyRoleId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													"partyId");

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party oldParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Party.class);

										if (oldParty != null
												&& !StrUtil.isEmpty(oldParty
														.getPartyName())) {
											diverseAttribute
													.setAttrOldVaule(oldParty
															.getPartyName());
										}

										Party newParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Party.class);

										if (newParty != null
												&& !StrUtil.isEmpty(newParty
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(newParty
															.getPartyName());
										}

									}
								}

								// 行政区域标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("locationId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										PoliticalLocation oldPoliticalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														oldSqlParams,
														PoliticalLocation.class);

										if (oldPoliticalLocation != null
												&& !StrUtil
														.isEmpty(oldPoliticalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrOldVaule(oldPoliticalLocation
															.getLocationName());
										}

										PoliticalLocation newPoliticalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														newSqlParams,
														PoliticalLocation.class);

										if (newPoliticalLocation != null
												&& !StrUtil
														.isEmpty(newPoliticalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrNewVaule(newPoliticalLocation
															.getLocationName());
										}

									}
								}

							}

							// 员工扩展属性数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"StaffExtendAttr")) {
								// 员工属性值
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()

										.equals("staffAttrValue")) {

									StringBuffer newSql = new StringBuffer(
											"SELECT * FROM STAFF_ATTR_VALUE WHERE 1=1");

									List newSqlParams = new ArrayList();

									if (newStaffExtendAttr != null) {
										if (newStaffExtendAttr
												.getStaffAttrSpecId() != null) {
											newSqlParams.add(newStaffExtendAttr
													.getStaffAttrSpecId());
											newSql.append(" AND STAFF_ATTR_SPEC_ID = ?");
										}

										if (!StrUtil.isEmpty(newStaffExtendAttr
												.getStaffAttrValue())) {
											newSqlParams.add(newStaffExtendAttr
													.getStaffAttrValue());
											newSql.append(" AND STAFF_ATTR_VALUE = ?");
										}
									}

									StaffAttrValue newStaffAttrValue = (StaffAttrValue) StaffAttrValue
											.repository().jdbcFindObject(
													newSql.toString(),
													newSqlParams,
													StaffAttrValue.class);

									if (newStaffAttrValue != null) {
										diverseAttribute
												.setAttrNewVaule(newStaffAttrValue
														.getStaffAttrValueName());
									}

									StringBuffer oldSql = new StringBuffer(
											"SELECT * FROM STAFF_ATTR_VALUE WHERE 1=1");

									List oldSqlParams = new ArrayList();

									if (oldStaffExtendAttr != null) {
										if (oldStaffExtendAttr
												.getStaffAttrSpecId() != null) {
											oldSqlParams.add(oldStaffExtendAttr
													.getStaffAttrSpecId());
											oldSql.append(" AND STAFF_ATTR_SPEC_ID = ?");
										}

										if (!StrUtil.isEmpty(oldStaffExtendAttr
												.getStaffAttrValue())) {
											oldSqlParams.add(oldStaffExtendAttr
													.getStaffAttrValue());
											oldSql.append(" AND STAFF_ATTR_VALUE = ?");
										}
									}

									StaffAttrValue oldStaffAttrValue = (StaffAttrValue) StaffAttrValue
											.repository().jdbcFindObject(
													oldSql.toString(),
													oldSqlParams,
													StaffAttrValue.class);

									if (oldStaffAttrValue != null) {
										diverseAttribute
												.setAttrOldVaule(oldStaffAttrValue
														.getStaffAttrValueName());
									}

								}

								// 员工标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("staffId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Staff oldStaff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Staff.class);

										if (oldStaff != null
												&& !StrUtil.isEmpty(oldStaff
														.getStaffName())) {
											diverseAttribute
													.setAttrOldVaule(oldStaff
															.getStaffName());
										}

										Staff newStaff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Staff.class);

										if (newStaff != null
												&& !StrUtil.isEmpty(newStaff
														.getStaffName())) {
											diverseAttribute
													.setAttrNewVaule(newStaff
															.getStaffName());
										}

									}
								}

								// 员工属性规格标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()

										.equals("staffAttrSpecId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										StaffAttrSpec oldStaffAttrSpec = (StaffAttrSpec) StaffAttrSpec
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														StaffAttrSpec.class);

										if (oldStaffAttrSpec != null
												&& !StrUtil
														.isEmpty(oldStaffAttrSpec
																.getAttrName())) {
											diverseAttribute
													.setAttrOldVaule(oldStaffAttrSpec
															.getAttrName());
										}

										StaffAttrSpec newStaffAttrSpec = (StaffAttrSpec) StaffAttrSpec
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														StaffAttrSpec.class);

										if (newStaffAttrSpec != null
												&& !StrUtil
														.isEmpty(newStaffAttrSpec
																.getAttrName())) {
											diverseAttribute
													.setAttrNewVaule(newStaffAttrSpec
															.getAttrName());
										}

									}
								}

							}

							// 员工账号数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"StaffAccount")) {
								// 员工账号密码不显示
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("staffPassword")) {
									diverseAttribute
											.setAttrNewVaule("********");
								}

								// 员工标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("staffId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Staff oldStaff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Staff.class);

										if (oldStaff != null
												&& !StrUtil.isEmpty(oldStaff
														.getStaffName())) {
											diverseAttribute
													.setAttrOldVaule(oldStaff
															.getStaffName());
										}

										Staff newStaff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Staff.class);

										if (newStaff != null
												&& !StrUtil.isEmpty(newStaff
														.getStaffName())) {
											diverseAttribute
													.setAttrNewVaule(newStaff
															.getStaffName());
										}

									}
								}

							}

							// 员工岗位关系数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"StaffPosition")) {
								// 组织岗位关系标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()

										.equals("orgPositionRelaId")) {

									StringBuffer sql = new StringBuffer(
											"SELECT * FROM ORG_POSITION WHERE ORG_POSITION_ID = ?");

									List oldSqlParams = new ArrayList();
									List newSqlParams = new ArrayList();

									oldSqlParams.add(diverseAttribute
											.getAttrOldVaule());
									newSqlParams.add(diverseAttribute
											.getAttrNewVaule());

									OrgPosition oldOrgPosition = (OrgPosition) OrgPosition
											.repository().jdbcFindObject(
													sql.toString(),
													oldSqlParams,
													OrgPosition.class);
									OrgPosition newOrgPosition = (OrgPosition) OrgPosition
											.repository().jdbcFindObject(
													sql.toString(),
													newSqlParams,
													OrgPosition.class);

									DiverseAttribute orgNameDiverseAttribute = new DiverseAttribute();
									DiverseAttribute positionNameDiverseAttribute = new DiverseAttribute();

									orgNameDiverseAttribute
											.setTableName("员工岗位关系");
									orgNameDiverseAttribute.setAttrName("组织名称");

									positionNameDiverseAttribute
											.setTableName("员工岗位关系");
									positionNameDiverseAttribute
											.setAttrName("岗位名称");

									if (oldOrgPosition != null) {
										orgNameDiverseAttribute
												.setAttrOldVaule(oldOrgPosition
														.getOrgName());

										positionNameDiverseAttribute
												.setAttrOldVaule(oldOrgPosition
														.getPositionName());
									}

									if (newOrgPosition != null) {
										orgNameDiverseAttribute
												.setAttrNewVaule(newOrgPosition
														.getOrgName());

										positionNameDiverseAttribute
												.setAttrNewVaule(newOrgPosition
														.getPositionName());
									}

									newDiverseAttributeList
											.add(orgNameDiverseAttribute);
									newDiverseAttributeList
											.add(positionNameDiverseAttribute);

								}

								// 员工标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("staffId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Staff oldStaff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Staff.class);

										if (oldStaff != null
												&& !StrUtil.isEmpty(oldStaff
														.getStaffName())) {
											diverseAttribute
													.setAttrOldVaule(oldStaff
															.getStaffName());
										}

										Staff newStaff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Staff.class);

										if (newStaff != null
												&& !StrUtil.isEmpty(newStaff
														.getStaffName())) {
											diverseAttribute
													.setAttrNewVaule(newStaff
															.getStaffName());
										}

									}
								}

							}

							// 员工组织关系数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"StaffOrganization")) {

								// 关联类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("ralaCd")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 组织名称
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization oldOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Organization.class);

										if (oldOrganization != null
												&& !StrUtil
														.isEmpty(oldOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrganization
															.getOrgFullName());
										}

										Organization newOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Organization.class);

										if (newOrganization != null
												&& !StrUtil
														.isEmpty(newOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(newOrganization
															.getOrgFullName());
										}

									}
								}

								// 员工名称
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("staffId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Staff oldStaff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Staff.class);

										if (oldStaff != null
												&& !StrUtil.isEmpty(oldStaff
														.getStaffName())) {
											diverseAttribute
													.setAttrOldVaule(oldStaff
															.getStaffName());
										}

										Staff newStaff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Staff.class);

										if (newStaff != null
												&& !StrUtil.isEmpty(newStaff
														.getStaffName())) {
											diverseAttribute
													.setAttrNewVaule(newStaff
															.getStaffName());
										}

									}
								}

								// 组织树标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgTreeId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgTree oldOrgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														OrgTree.class);

										if (oldOrgTree != null
												&& !StrUtil.isEmpty(oldOrgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrgTree
															.getOrgTreeName());
										}

										OrgTree newOrgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														OrgTree.class);

										if (newOrgTree != null
												&& !StrUtil.isEmpty(newOrgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrNewVaule(newOrgTree
															.getOrgTreeName());
										}

									}
								}
							}

							// 参与人数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals("Party")) {
								// 参与人类型名称
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("partyType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

							}

							// 参与人联系人数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"PartyContactInfo")) {
								// 是否首选联系人
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("headFlag")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 联系人类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("contactType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 联系人性别
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("contactGender")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													"Individual",
													"gender",
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													"Individual",
													"gender",
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 参与人标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("partyId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party oldParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Party.class);

										if (oldParty != null
												&& !StrUtil.isEmpty(oldParty
														.getPartyName())) {
											diverseAttribute
													.setAttrOldVaule(oldParty
															.getPartyName());
										}

										Party newParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Party.class);

										if (newParty != null
												&& !StrUtil.isEmpty(newParty
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(newParty
															.getPartyName());
										}
									}
								}

							}

							// 参与人证件数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"PartyCertification")) {
								// 证件类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("certType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 证件种类
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("certSort")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 参与人标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("partyId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party oldParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Party.class);

										if (oldParty != null
												&& !StrUtil.isEmpty(oldParty
														.getPartyName())) {
											diverseAttribute
													.setAttrOldVaule(oldParty
															.getPartyName());
										}

										Party newParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Party.class);

										if (newParty != null
												&& !StrUtil.isEmpty(newParty
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(newParty
															.getPartyName());
										}

									}
								}
							}

							// 参与人组织数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"PartyOrganization")) {
								// 组织类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 组织规模
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgScale")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 参与人标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("partyId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party oldParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Party.class);

										if (oldParty != null
												&& !StrUtil.isEmpty(oldParty
														.getPartyName())) {
											diverseAttribute
													.setAttrOldVaule(oldParty
															.getPartyName());
										}

										Party newParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Party.class);

										if (newParty != null
												&& !StrUtil.isEmpty(newParty
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(newParty
															.getPartyName());
										}
									}
								}

							}

							// 参与人角色数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"PartyRole")) {
								// 参与人角色类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("roleType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 参与人标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("partyId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party oldParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Party.class);

										if (oldParty != null
												&& !StrUtil.isEmpty(oldParty
														.getPartyName())) {
											diverseAttribute
													.setAttrOldVaule(oldParty
															.getPartyName());
										}

										Party newParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Party.class);

										if (newParty != null
												&& !StrUtil.isEmpty(newParty
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(newParty
															.getPartyName());
										}
									}
								}

							}

							// 岗位数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode()
											.equals("Position")) {
								// 岗位类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("positionType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}
							}

							// 组织数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"Organization")) {
								// 组织级别名称
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgLeave")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule() != null ? diverseAttribute
															.getAttrOldVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule() != null ? diverseAttribute
															.getAttrNewVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 组织规模名称
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgScale")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule() != null ? diverseAttribute
															.getAttrOldVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule() != null ? diverseAttribute
															.getAttrNewVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 组织类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule() != null ? diverseAttribute
															.getAttrOldVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule() != null ? diverseAttribute
															.getAttrNewVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 组织存在类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("existType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule() != null ? diverseAttribute
															.getAttrOldVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule() != null ? diverseAttribute
															.getAttrNewVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 组织优先级
								// if (!StrUtil.isEmpty(diverseAttribute
								// .getAttrName())
								// && diverseAttribute.getAttrName()
								// .equals("orgPriority")) {
								//
								// diverseAttribute
								// .setAttrOldVaule(reflectObject
								// .getPropertyName(
								// sysClass.getJavaCode()
								// ,
								// diverseAttribute
								// .getAttrName()
								// ,
								// diverseAttribute
								// .getAttrOldVaule()
								// ,
								// BaseUnitConstants.ENTT_STATE_ACTIVE));
								//
								// diverseAttribute
								// .setAttrNewVaule(reflectObject
								// .getPropertyName(
								// sysClass.getJavaCode()
								// ,
								// diverseAttribute
								// .getAttrName()
								// ,
								// diverseAttribute
								// .getAttrNewVaule()
								// ,
								// BaseUnitConstants.ENTT_STATE_ACTIVE));
								//
								// }

								// 农村城镇标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("cityTown")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule() != null ? diverseAttribute
															.getAttrOldVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule() != null ? diverseAttribute
															.getAttrNewVaule()

													: null,
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 行政区域标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("locationId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams
												.add(diverseAttribute
														.getAttrOldVaule() != null ? diverseAttribute
														.getAttrOldVaule()
														: null);

										newSqlParams
												.add(diverseAttribute
														.getAttrNewVaule() != null ? diverseAttribute
														.getAttrNewVaule()
														: null);

										PoliticalLocation oldPoliticalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														oldSqlParams,
														PoliticalLocation.class);

										if (oldPoliticalLocation != null
												&& !StrUtil
														.isEmpty(oldPoliticalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrOldVaule(oldPoliticalLocation
															.getLocationName());
										}

										PoliticalLocation newPoliticalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														newSqlParams,
														PoliticalLocation.class);

										if (newPoliticalLocation != null
												&& !StrUtil
														.isEmpty(newPoliticalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrNewVaule(newPoliticalLocation
															.getLocationName());
										}

									}
								}

								// 电信区域标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()

										.equals("telcomRegionId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams
												.add(diverseAttribute
														.getAttrOldVaule() != null ? diverseAttribute
														.getAttrOldVaule()
														: null);

										newSqlParams
												.add(diverseAttribute
														.getAttrNewVaule() != null ? diverseAttribute
														.getAttrNewVaule()
														: null);

										TelcomRegion oldTelcomRegion = (TelcomRegion) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														TelcomRegion.class);

										if (oldTelcomRegion != null
												&& !StrUtil
														.isEmpty(oldTelcomRegion
																.getRegionName())) {
											diverseAttribute
													.setAttrOldVaule(oldTelcomRegion
															.getRegionName());
										}

										TelcomRegion newTelcomRegion = (TelcomRegion) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														TelcomRegion.class);

										if (newTelcomRegion != null
												&& !StrUtil
														.isEmpty(newTelcomRegion
																.getRegionName())) {
											diverseAttribute
													.setAttrNewVaule(newTelcomRegion
															.getRegionName());
										}

									}
								}

								// 地区区域标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("areaCodeId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams
												.add(diverseAttribute
														.getAttrOldVaule() != null ? diverseAttribute
														.getAttrOldVaule()
														: null);

										newSqlParams
												.add(diverseAttribute
														.getAttrNewVaule() != null ? diverseAttribute
														.getAttrNewVaule()
														: null);

										AreaCode oldAreaCode = (AreaCode) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														AreaCode.class);

										if (oldAreaCode != null
												&& !StrUtil.isEmpty(oldAreaCode
														.getAreaName())) {
											diverseAttribute
													.setAttrOldVaule(oldAreaCode
															.getAreaName());
										}

										AreaCode newAreaCode = (AreaCode) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														AreaCode.class);

										if (newAreaCode != null
												&& !StrUtil.isEmpty(newAreaCode
														.getAreaName())) {
											diverseAttribute
													.setAttrNewVaule(newAreaCode
															.getAreaName());
										}

									}
								}

								// 参与人标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("partyId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams
												.add(diverseAttribute
														.getAttrOldVaule() != null ? diverseAttribute
														.getAttrOldVaule()
														: null);

										newSqlParams
												.add(diverseAttribute
														.getAttrNewVaule() != null ? diverseAttribute
														.getAttrNewVaule()
														: null);

										Party oldParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Party.class);

										if (oldParty != null
												&& !StrUtil.isEmpty(oldParty
														.getPartyName())) {
											diverseAttribute
													.setAttrOldVaule(oldParty
															.getPartyName());
										}

										Party newParty = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Party.class);

										if (newParty != null
												&& !StrUtil.isEmpty(newParty
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(newParty
															.getPartyName());
										}

									}
								}

							}

							// 组织扩展属性数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"OrganizationExtendAttr")) {
								// 组织标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization oldOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Organization.class);

										if (oldOrganization != null
												&& !StrUtil
														.isEmpty(oldOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrganization
															.getOrgFullName());
										}

										Organization newOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Organization.class);

										if (newOrganization != null
												&& !StrUtil
														.isEmpty(newOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(newOrganization
															.getOrgFullName());
										}

									}
								}

								// 属性规格标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgAttrSpecId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgAttrSpec oldOrgAttrSpec = (OrgAttrSpec) OrgAttrSpec
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														OrgAttrSpec.class);

										if (oldOrgAttrSpec != null
												&& !StrUtil
														.isEmpty(oldOrgAttrSpec
																.getAttrName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrgAttrSpec
															.getAttrName());
										}

										OrgAttrSpec newOrgAttrSpec = (OrgAttrSpec) OrgAttrSpec
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														OrgAttrSpec.class);

										if (newOrgAttrSpec != null
												&& !StrUtil
														.isEmpty(newOrgAttrSpec
																.getAttrName())) {
											diverseAttribute
													.setAttrNewVaule(newOrgAttrSpec
															.getAttrName());
										}

									}
								}

							}

							// 组织类型比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals("OrgType")) {
								// 关系类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgTypeCd")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 组织标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization oldOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Organization.class);

										if (oldOrganization != null
												&& !StrUtil
														.isEmpty(oldOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrganization
															.getOrgFullName());
										}

										Organization newOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Organization.class);

										if (newOrganization != null
												&& !StrUtil
														.isEmpty(newOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(newOrganization
															.getOrgFullName());
										}

									}
								}

							}

							// 组织关系数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"OrganizationRelation")) {
								// 关系类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("relaCd")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 组织标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization oldOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Organization.class);

										if (oldOrganization != null
												&& !StrUtil
														.isEmpty(oldOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrganization
															.getOrgFullName());
										}

										Organization newOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Organization.class);

										if (newOrganization != null
												&& !StrUtil
														.isEmpty(newOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(newOrganization
															.getOrgFullName());
										}

									}
								}

								// 关联组织标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("relaOrgId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization oldOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Organization.class);

										if (oldOrganization != null
												&& !StrUtil
														.isEmpty(oldOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrganization
															.getOrgFullName());
										}

										Organization newOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Organization.class);

										if (newOrganization != null
												&& !StrUtil
														.isEmpty(newOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(newOrganization
															.getOrgFullName());
										}

									}
								}
							}

							// 组织联系信息数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"OrgContactInfo")) {

								// 组织标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization oldOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Organization.class);

										if (oldOrganization != null
												&& !StrUtil
														.isEmpty(oldOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrganization
															.getOrgFullName());
										}

										Organization newOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Organization.class);

										if (newOrganization != null
												&& !StrUtil
														.isEmpty(newOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(newOrganization
															.getOrgFullName());
										}

									}
								}

							}

							// 组织树数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals("OrgTree")) {
								// 是否推导
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("isCalc")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

							}

							// 组织树引用关系数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"OrganizationTreeRelation")) {

								// 组织树标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgTreeId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgTree oldOrgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														OrgTree.class);

										if (oldOrgTree != null
												&& !StrUtil.isEmpty(oldOrgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrgTree
															.getOrgTreeName());
										}

										OrgTree newOrgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														OrgTree.class);

										if (newOrgTree != null
												&& !StrUtil.isEmpty(newOrgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrNewVaule(newOrgTree
															.getOrgTreeName());
										}

									}
								}

								// 关联关系标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgRelId")) {

									StringBuffer sql = new StringBuffer(
											"SELECT * FROM ORGANIZATION_RELATION WHERE ORG_REL_ID = ?");

									List oldSqlParams = new ArrayList();
									List newSqlParams = new ArrayList();

									oldSqlParams.add(diverseAttribute
											.getAttrOldVaule());

									newSqlParams.add(diverseAttribute
											.getAttrNewVaule());

									OrganizationRelation oldOrganizationRelation = (OrganizationRelation) OrganizationRelation
											.repository().jdbcFindObject(
													sql.toString(),
													oldSqlParams,
													OrganizationRelation.class);

									OrganizationRelation newOrganizationRelation = (OrganizationRelation) OrganizationRelation
											.repository().jdbcFindObject(
													sql.toString(),
													newSqlParams,
													OrganizationRelation.class);

									DiverseAttribute relaOrgIdDiverseAttribute = new DiverseAttribute();
									DiverseAttribute orgIdDiverseAttribute = new DiverseAttribute();

									relaOrgIdDiverseAttribute
											.setTableName("组织树引用关系表");
									relaOrgIdDiverseAttribute
											.setAttrName("父组织名称");
									orgIdDiverseAttribute
											.setTableName("组织树引用关系表");
									orgIdDiverseAttribute.setAttrName("组织名称");

									if (oldOrganizationRelation != null) {

										// 关联组织标识
										foreignKey = ForeignKey.repository()
												.jdbcFindForeignKey(
														"OrganizationRelation",
														"relaOrgId");

										if (foreignKey != null) {

											StringBuffer relaOrgIdSql = new StringBuffer(
													"SELECT * FROM ")
													.append(foreignKey
															.getFkTableName())
													.append(" WHERE ")
													.append(foreignKey
															.getFkColumnName())
													.append(" = ?");

											List relaOrgIdSqlParams = new ArrayList();

											relaOrgIdSqlParams
													.add(oldOrganizationRelation
															.getOrgRelId()
															.toString());

											Organization relaOrgIdOrganization = (Organization) Organization
													.repository()
													.jdbcFindObject(
															relaOrgIdSql
																	.toString(),
															relaOrgIdSqlParams,
															Organization.class);

											if (relaOrgIdOrganization != null
													&& !StrUtil
															.isEmpty(relaOrgIdOrganization
																	.getOrgFullName())) {
												relaOrgIdDiverseAttribute
														.setAttrOldVaule(relaOrgIdOrganization
																.getOrgFullName());
											}
										}

										// 组织标识
										foreignKey = ForeignKey.repository()
												.jdbcFindForeignKey(
														"OrganizationRelation",
														"orgId");

										if (foreignKey != null) {

											StringBuffer orgIdSql = new StringBuffer(
													"SELECT * FROM ")
													.append(foreignKey
															.getFkTableName())
													.append(" WHERE ")
													.append(foreignKey
															.getFkColumnName())
													.append(" = ?");

											List orgIdSqlParams = new ArrayList();

											orgIdSqlParams
													.add(oldOrganizationRelation
															.getOrgId()
															.toString());

											Organization orgIdOrganization = (Organization) Organization
													.repository()
													.jdbcFindObject(
															orgIdSql.toString(),
															orgIdSqlParams,
															Organization.class);

											if (orgIdOrganization != null
													&& !StrUtil
															.isEmpty(orgIdOrganization
																	.getOrgFullName())) {
												orgIdDiverseAttribute
														.setAttrOldVaule(orgIdOrganization
																.getOrgFullName());
											}
										}

									}

									if (newOrganizationRelation != null) {

										// 关联组织标识
										foreignKey = ForeignKey.repository()
												.jdbcFindForeignKey(
														"OrganizationRelation",
														"relaOrgId");

										if (foreignKey != null) {

											StringBuffer relaOrgIdSql = new StringBuffer(
													"SELECT * FROM ")
													.append(foreignKey
															.getFkTableName())
													.append(" WHERE ")
													.append(foreignKey
															.getFkColumnName())
													.append(" = ?");

											List relaOrgIdSqlParams = new ArrayList();

											relaOrgIdSqlParams
													.add(newOrganizationRelation
															.getOrgRelId()
															.toString());

											Organization relaOrgIdOrganization = (Organization) Organization
													.repository()
													.jdbcFindObject(
															relaOrgIdSql
																	.toString(),
															relaOrgIdSqlParams,
															Organization.class);

											if (relaOrgIdOrganization != null
													&& !StrUtil
															.isEmpty(relaOrgIdOrganization
																	.getOrgFullName())) {
												relaOrgIdDiverseAttribute
														.setAttrNewVaule(relaOrgIdOrganization
																.getOrgFullName());
											}
										}

										// 组织标识
										foreignKey = ForeignKey.repository()
												.jdbcFindForeignKey(
														"OrganizationRelation",
														"orgId");

										if (foreignKey != null) {

											StringBuffer orgIdSql = new StringBuffer(
													"SELECT * FROM ")
													.append(foreignKey
															.getFkTableName())
													.append(" WHERE ")
													.append(foreignKey
															.getFkColumnName())
													.append(" = ?");

											List orgIdSqlParams = new ArrayList();

											orgIdSqlParams
													.add(newOrganizationRelation
															.getOrgId()
															.toString());

											Organization orgIdOrganization = (Organization) Organization
													.repository()
													.jdbcFindObject(
															orgIdSql.toString(),
															orgIdSqlParams,
															Organization.class);

											if (orgIdOrganization != null
													&& !StrUtil
															.isEmpty(orgIdOrganization
																	.getOrgFullName())) {
												orgIdDiverseAttribute
														.setAttrNewVaule(orgIdOrganization
																.getOrgFullName());
											}
										}

									}

									newDiverseAttributeList
											.add(relaOrgIdDiverseAttribute);
									newDiverseAttributeList
											.add(orgIdDiverseAttribute);

								}

							}

							// 组织岗位关系数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"OrgPosition")) {

								// 组织标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization oldOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Organization.class);

										if (oldOrganization != null
												&& !StrUtil
														.isEmpty(oldOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrganization
															.getOrgFullName());
										}

										Organization newOrganization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Organization.class);

										if (newOrganization != null
												&& !StrUtil
														.isEmpty(newOrganization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(newOrganization
															.getOrgFullName());
										}

									}
								}

								// 岗位名称
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("positionId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Position oldPosition = (Position) Position
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														Position.class);

										if (oldPosition != null
												&& !StrUtil.isEmpty(oldPosition
														.getPositionName())) {
											diverseAttribute
													.setAttrOldVaule(oldPosition
															.getPositionName());
										}

										Position newPosition = (Position) Position
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														Position.class);

										if (newPosition != null
												&& !StrUtil.isEmpty(newPosition
														.getPositionName())) {
											diverseAttribute
													.setAttrNewVaule(newPosition
															.getPositionName());
										}

									}
								}

								// 组织树标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgTreeId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgTree oldOrgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														OrgTree.class);

										if (oldOrgTree != null
												&& !StrUtil.isEmpty(oldOrgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrgTree
															.getOrgTreeName());
										}

										OrgTree newOrgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														OrgTree.class);

										if (newOrgTree != null
												&& !StrUtil.isEmpty(newOrgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrNewVaule(newOrgTree
															.getOrgTreeName());
										}

									}
								}
							}

							// 树扩展属性数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"TreeExtendAttr")) {
								// 组织树标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgTreeId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgTree oldOrgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														OrgTree.class);

										if (oldOrgTree != null
												&& !StrUtil.isEmpty(oldOrgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrgTree
															.getOrgTreeName());
										}

										OrgTree newOrgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														OrgTree.class);

										if (newOrgTree != null
												&& !StrUtil.isEmpty(newOrgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrNewVaule(newOrgTree
															.getOrgTreeName());
										}

									}
								}

								// 组织属性规格分类标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()

										.equals("orgAttrSpecSortId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgAttrSpecSort oldOrgAttrSpecSort = (OrgAttrSpecSort) OrgAttrSpecSort
												.repository().jdbcFindObject(
														sql.toString(),
														oldSqlParams,
														OrgAttrSpecSort.class);

										if (oldOrgAttrSpecSort != null
												&& !StrUtil
														.isEmpty(oldOrgAttrSpecSort
																.getSortName())) {
											diverseAttribute
													.setAttrOldVaule(oldOrgAttrSpecSort
															.getSortName());
										}

										OrgAttrSpecSort newOrgAttrSpecSort = (OrgAttrSpecSort) OrgAttrSpecSort
												.repository().jdbcFindObject(
														sql.toString(),
														newSqlParams,
														OrgAttrSpecSort.class);

										if (newOrgAttrSpecSort != null
												&& !StrUtil
														.isEmpty(newOrgAttrSpecSort
																.getSortName())) {
											diverseAttribute
													.setAttrNewVaule(newOrgAttrSpecSort
															.getSortName());
										}

									}
								}

							}

							// 行政管理区域数据比较
							if (!StrUtil.isEmpty(sysClass.getJavaCode())
									&& sysClass.getJavaCode().equals(
											"PoliticalLocation")) {
								// 行政区域类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("locationType")) {

									diverseAttribute
											.setAttrOldVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrOldVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));

								}

								// 上级行政区域标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("upLocationId")) {

									foreignKey = ForeignKey.repository()
											.jdbcFindForeignKey(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName());

									if (foreignKey != null) {

										StringBuffer sql = new StringBuffer(
												"SELECT * FROM ")
												.append(foreignKey
														.getFkTableName())
												.append(" WHERE ")
												.append(foreignKey
														.getFkColumnName())
												.append(" = ?");

										List oldSqlParams = new ArrayList();
										List newSqlParams = new ArrayList();

										oldSqlParams.add(diverseAttribute
												.getAttrOldVaule());

										newSqlParams.add(diverseAttribute
												.getAttrNewVaule());

										PoliticalLocation oldPoliticalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														oldSqlParams,
														PoliticalLocation.class);

										if (oldPoliticalLocation != null
												&& !StrUtil
														.isEmpty(oldPoliticalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrOldVaule(oldPoliticalLocation
															.getLocationName());
										}

										PoliticalLocation newPoliticalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														newSqlParams,
														PoliticalLocation.class);

										if (newPoliticalLocation != null
												&& !StrUtil
														.isEmpty(newPoliticalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrNewVaule(newPoliticalLocation
															.getLocationName());
										}

									}
								}

							}

							// 状态
							if (!StrUtil
									.isEmpty(diverseAttribute.getAttrName())
									&& diverseAttribute.getAttrName().equals(
											"statusCd")) {

								diverseAttribute
										.setAttrOldVaule(reflectObject
												.getPropertyName(
														"UomEntity",
														diverseAttribute
																.getAttrName(),
														diverseAttribute
																.getAttrOldVaule(),
														BaseUnitConstants.ENTT_STATE_ACTIVE));

								diverseAttribute
										.setAttrNewVaule(reflectObject
												.getPropertyName(
														"UomEntity",
														diverseAttribute
																.getAttrName(),
														diverseAttribute
																.getAttrNewVaule(),
														BaseUnitConstants.ENTT_STATE_ACTIVE));

							}

							if (!(diverseAttribute.getAttrName()
									.equalsIgnoreCase("rootOrgId"))) {
								diverseAttribute
										.setAttrName(reflectObject.getColumnName(
												sysClass.getClassId(),
												diverseAttribute.getAttrName(),
												BaseUnitConstants.ENTT_STATE_ACTIVE));// 对类属性赋相应的中文字段名

								newDiverseAttributeList.add(diverseAttribute);
							}
						}
					}
				} catch (Exception e) {
					this.bean.getSystemInfoDiv().setVisible(true);
					this.bean.getDiverseAttributeListboxDiv().setVisible(false);
				}

				if (newDiverseAttributeList == null
						|| newDiverseAttributeList.size() <= 0
						|| newDiverseAttributeList.get(0).getAttrName()
								.equals("失效时间")) {
					this.bean.getSystemInfoDiv().setVisible(true);
					this.bean.getDiverseAttributeListboxDiv().setVisible(false);
				} else {
					// 向ListBox中填充数据，存在样式延迟加载的问题。
					/*
					 * ListModel dataList = new BindingListModelList(
					 * newDiverseAttributeList, true);
					 * this.bean.getDiverseAttributeListbox
					 * ().setModel(dataList);
					 */
					this.list.add(newDiverseAttributeList);
				}
			} else {
				this.bean.getSystemInfoDiv().setVisible(true);
				this.bean.getDiverseAttributeListboxDiv().setVisible(false);
			}
		} else {
			this.bean.getSystemInfoDiv().setVisible(true);
			this.bean.getDiverseAttributeListboxDiv().setVisible(false);
		}

	}

	public void bindDate(List<List> list) {

		if (list != null && list.size() > 0) {

			Div div = (Div) this.bean.getDiverseAttributeListboxDiv();// 获取Div
			div.setStyle("height:436px;overflow:auto;");

			for (List<DiverseAttribute> diverseAttributeList : list) {

				Panel panel = new Panel();
				panel.setParent(div);

				Panelchildren panelChildren = new Panelchildren();
				panelChildren.setParent(panel);

				Listbox listResult = new Listbox();
				listResult.setParent(panelChildren);

				listResult.setRows(diverseAttributeList.size());

				Listhead listHead = new Listhead();// 创建行标题
				listHead.setSizable(true);//行可调整
				listHead.setParent(listResult);// 设置父容器
				Listheader tListheader=new Listheader("表 名");
				tListheader.setWidth("100px");

				Listheader cListheader=new Listheader("字段名");
				cListheader.setWidth("115px");
				
				Listheader uaListheader=new Listheader("修改后的值");
				Listheader ubListheader=new Listheader("修改前的值");

				listHead.appendChild(tListheader);
				listHead.appendChild(cListheader);
				listHead.appendChild(uaListheader);
				listHead.appendChild(ubListheader);

				for (DiverseAttribute diverseAttribute : diverseAttributeList) {
					Listitem li = new Listitem();// 创建行
					li.setParent(listResult);// 设置父容器
					li.appendChild(new Listcell(diverseAttribute.getTableName()));// 添加列
					li.appendChild(new Listcell(diverseAttribute.getAttrName()));// 添加列
					li.appendChild(new Listcell(diverseAttribute
							.getAttrNewVaule()));// 添加列
					li.appendChild(new Listcell(diverseAttribute
							.getAttrOldVaule()));// 添加列

					if (diverseAttribute.isDifference()) {// 数据存在差异，则用黄底色显示
						li.setStyle("background-color:yellow;");
					}

				}

			}

		}

	}

}
