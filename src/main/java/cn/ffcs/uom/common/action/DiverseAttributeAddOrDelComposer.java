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
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.areacode.model.AreaCode;
import cn.ffcs.uom.common.action.bean.DiverseAttributeAddOrDelMainBean;
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
public class DiverseAttributeAddOrDelComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private DiverseAttributeAddOrDelMainBean bean = new DiverseAttributeAddOrDelMainBean();
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
	private StaffExtendAttr staffExtendAttr;

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
	 * 设置是否有滚动条
	 */
	private boolean vflex = true;
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
	public void onCreate$diverseAttributeAddOrDelMainWin() throws Exception {

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

			if (operateLog.getOpeType().equals(BaseUnitConstants.OPE_TYPE_ADD)) {

				this.bean.getDiverseAttributeAddOrDelMainWin().setTitle("新增页面");

			} else if (operateLog.getOpeType().equals(
					BaseUnitConstants.OPE_TYPE_DEL)) {

				this.bean.getDiverseAttributeAddOrDelMainWin().setTitle("删除页面");

			}

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
						+ sysClass.getTableName() + " WHERE STATUS_CD = ?");
				List params = new ArrayList();

				if (operateLog.getOpeType().equals(
						BaseUnitConstants.OPE_TYPE_ADD)) {

					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

				} else if (operateLog.getOpeType().equals(
						BaseUnitConstants.OPE_TYPE_DEL)) {

					params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);

				}

				// 个人数据比较

				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Individual")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND INDIVIDUAL_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					Individual exp = new Individual();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											Individual.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													Individual.class) : exp,
									null);

				}

				// 员工数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Staff")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					Staff exp = new Staff();

					diverseAttributeList = reflectObject.reflect(
							(Staff.repository().jdbcFindObject(hql.toString(),
									params, Staff.class) != null) ? Staff
									.repository()
									.jdbcFindObject(hql.toString(), params,
											Staff.class) : exp, null);
				}

				// 员工扩展属性数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("StaffExtendAttr")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_EXTEND_ATTR_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					StaffExtendAttr exp = new StaffExtendAttr();

					staffExtendAttr = (StaffExtendAttr.repository()
							.jdbcFindObject(hql.toString(), params,
									StaffExtendAttr.class) != null) ? StaffExtendAttr
							.repository().jdbcFindObject(hql.toString(),
									params, StaffExtendAttr.class) : exp;

					diverseAttributeList = reflectObject
							.reflect(
									(StaffExtendAttr.repository()
											.jdbcFindObject(hql.toString(),
													params,
													StaffExtendAttr.class) != null) ? StaffExtendAttr
											.repository().jdbcFindObject(
													hql.toString(), params,
													StaffExtendAttr.class)
											: exp, null);
				}

				// 员工账号数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("StaffAccount")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_ACCOUNT_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					StaffAccount exp = new StaffAccount();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											StaffAccount.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													StaffAccount.class) : exp,
									null);

				}

				// 员工岗位关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("StaffPosition")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_POSITION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					StaffPosition exp = new StaffPosition();

					diverseAttributeList = reflectObject
							.reflect(
									(StaffPosition.repository().jdbcFindObject(
											hql.toString(), params,
											StaffPosition.class) != null) ? StaffPosition
											.repository().jdbcFindObject(
													hql.toString(), params,
													StaffPosition.class) : exp,
									null);
				}

				// 员工组织关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("StaffOrganization")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND STAFF_ORG_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					StaffOrganization exp = new StaffOrganization();

					diverseAttributeList = reflectObject
							.reflect(
									(StaffOrganization.repository()
											.jdbcFindObject(hql.toString(),
													params,
													StaffOrganization.class) != null) ? StaffOrganization
											.repository().jdbcFindObject(
													hql.toString(), params,
													StaffOrganization.class)
											: exp, null);

				}

				// 参与人数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Party")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND PARTY_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					Party exp = new Party();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository()
											.jdbcFindObject(hql.toString(),
													params, Party.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													Party.class) : exp, null);

				}

				// 参与人联系人数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PartyContactInfo")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND CONTACT_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					PartyContactInfo exp = new PartyContactInfo();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											PartyContactInfo.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													PartyContactInfo.class)
											: exp, null);

				}

				// 参与人证件数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PartyCertification")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND PARTY_CERT_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					PartyCertification exp = new PartyCertification();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											PartyCertification.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													PartyCertification.class)
											: exp, null);

				}

				// 参与人组织数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PartyOrganization")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND PARTY_ORGANIZATION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					PartyOrganization exp = new PartyOrganization();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											PartyOrganization.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													PartyOrganization.class)
											: exp, null);

				}

				// 参与人角色数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PartyRole")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND PARTY_ROLE_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					PartyRole exp = new PartyRole();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											PartyRole.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													PartyRole.class) : exp,
									null);

				}

				// 岗位数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Position")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND POSITION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					Position exp = new Position();

					diverseAttributeList = reflectObject
							.reflect(
									(Position.repository().jdbcFindObject(
											hql.toString(), params,
											Position.class) != null) ? Position
											.repository().jdbcFindObject(
													hql.toString(), params,
													Position.class) : exp, null);
				}

				// 组织数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("Organization")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					Organization exp = new Organization();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											Organization.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													Organization.class) : exp,
									null);
				}

				// 组织扩展属性数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals(
								"OrganizationExtendAttr")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_EXTEND_ATTR_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					OrganizationExtendAttr exp = new OrganizationExtendAttr();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											OrganizationExtendAttr.class) != null) ? Organization
											.repository()
											.jdbcFindObject(
													hql.toString(),
													params,
													OrganizationExtendAttr.class)
											: exp, null);

				}

				// 组织类型数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("OrgType")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_TYPE_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					OrgType exp = new OrgType();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											OrgType.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													OrgType.class) : exp, null);

				}

				// 组织关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode()
								.equals("OrganizationRelation")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_REL_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					OrganizationRelation exp = new OrganizationRelation();

					diverseAttributeList = reflectObject
							.reflect(
									(OrganizationRelation.repository()
											.jdbcFindObject(hql.toString(),
													params,
													OrganizationRelation.class) != null) ? OrganizationRelation
											.repository().jdbcFindObject(
													hql.toString(), params,
													OrganizationRelation.class)
											: exp, null);

				}

				// 组织联系信息数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("OrgContactInfo")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORGANIZATION_CONTACT_INFO_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					OrgContactInfo exp = new OrgContactInfo();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											OrgContactInfo.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													OrgContactInfo.class) : exp,
									null);

				}

				// 组织树数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("OrgTree")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_TREE_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					OrgTree exp = new OrgTree();

					diverseAttributeList = reflectObject
							.reflect(
									(OrgTree.repository().jdbcFindObject(
											hql.toString(), params,
											OrgTree.class) != null) ? OrgTree
											.repository().jdbcFindObject(
													hql.toString(), params,
													OrgTree.class) : exp, null);

				}

				// 组织树引用关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals(
								"OrganizationTreeRelation")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORGANIZATION_TREE_RELATION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					OrganizationTreeRelation exp = new OrganizationTreeRelation();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											OrganizationTreeRelation.class) != null) ? Organization
											.repository()
											.jdbcFindObject(
													hql.toString(),
													params,
													OrganizationTreeRelation.class)
											: exp, null);

				}

				// 组织岗位关系数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("OrgPosition")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND ORG_POSITION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					OrgPosition exp = new OrgPosition();

					diverseAttributeList = reflectObject
							.reflect(
									(OrgPosition.repository().jdbcFindObject(
											hql.toString(), params,
											OrgPosition.class) != null) ? OrgPosition
											.repository().jdbcFindObject(
													hql.toString(), params,
													OrgPosition.class) : exp,
									null);

				}

				// 树扩展属性数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("TreeExtendAttr")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND TREE_EXTEND_ATTR_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					TreeExtendAttr exp = new TreeExtendAttr();

					diverseAttributeList = reflectObject
							.reflect(
									(TreeExtendAttr.repository()
											.jdbcFindObject(hql.toString(),
													params,
													TreeExtendAttr.class) != null) ? TreeExtendAttr
											.repository().jdbcFindObject(
													hql.toString(), params,
													TreeExtendAttr.class) : exp,
									null);

				}

				// 行政管理区域数据比较
				if (!StrUtil.isEmpty(sysClass.getJavaCode())
						&& sysClass.getJavaCode().equals("PoliticalLocation")) {

					if (operateLog.getOpeObjectId() != null) {
						hql.append(" AND LOCATION_ID = ?");
						params.add(operateLog.getOpeObjectId());
					}

					PoliticalLocation exp = new PoliticalLocation();

					diverseAttributeList = reflectObject
							.reflect(
									(Organization.repository().jdbcFindObject(
											hql.toString(), params,
											PoliticalLocation.class) != null) ? Organization
											.repository().jdbcFindObject(
													hql.toString(), params,
													PoliticalLocation.class)
											: exp, null);

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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party party = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Party.class);

										if (party != null
												&& !StrUtil.isEmpty(party
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(party
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party party = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Party.class);

										if (party != null
												&& !StrUtil.isEmpty(party
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(party
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										PoliticalLocation politicalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														sqlParams,
														PoliticalLocation.class);

										if (politicalLocation != null
												&& !StrUtil
														.isEmpty(politicalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrNewVaule(politicalLocation
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

									StringBuffer sql = new StringBuffer(
											"SELECT * FROM STAFF_ATTR_VALUE WHERE 1=1");

									List sqlParams = new ArrayList();

									if (staffExtendAttr != null) {
										if (staffExtendAttr
												.getStaffAttrSpecId() != null) {
											sqlParams.add(staffExtendAttr
													.getStaffAttrSpecId());
											sql.append(" AND STAFF_ATTR_SPEC_ID = ?");
										}

										if (!StrUtil.isEmpty(staffExtendAttr
												.getStaffAttrValue())) {
											sqlParams.add(staffExtendAttr
													.getStaffAttrValue());
											sql.append(" AND STAFF_ATTR_VALUE = ?");
										}
									}

									StaffAttrValue staffAttrValue = (StaffAttrValue) StaffAttrValue
											.repository().jdbcFindObject(
													sql.toString(), sqlParams,
													StaffAttrValue.class);

									if (staffAttrValue != null) {
										diverseAttribute
												.setAttrNewVaule(staffAttrValue
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Staff staff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Staff.class);

										if (staff != null
												&& !StrUtil.isEmpty(staff
														.getStaffName())) {
											diverseAttribute
													.setAttrNewVaule(staff
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										StaffAttrSpec staffAttrSpec = (StaffAttrSpec) StaffAttrSpec
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														StaffAttrSpec.class);

										if (staffAttrSpec != null
												&& !StrUtil
														.isEmpty(staffAttrSpec
																.getAttrName())) {
											diverseAttribute
													.setAttrNewVaule(staffAttrSpec
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Staff staff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Staff.class);

										if (staff != null
												&& !StrUtil.isEmpty(staff
														.getStaffName())) {
											diverseAttribute
													.setAttrNewVaule(staff
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

									List sqlParams = new ArrayList();

									sqlParams.add(diverseAttribute
											.getAttrNewVaule());

									OrgPosition orgPosition = (OrgPosition) OrgPosition
											.repository().jdbcFindObject(
													sql.toString(), sqlParams,
													OrgPosition.class);

									if (orgPosition != null) {
										DiverseAttribute orgNameDiverseAttribute = new DiverseAttribute();
										DiverseAttribute positionNameDiverseAttribute = new DiverseAttribute();

										orgNameDiverseAttribute
												.setTableName("员工岗位关系");
										orgNameDiverseAttribute
												.setAttrName("组织名称");
										orgNameDiverseAttribute
												.setAttrNewVaule(orgPosition
														.getOrgName());
										newDiverseAttributeList
												.add(orgNameDiverseAttribute);

										positionNameDiverseAttribute
												.setTableName("员工岗位关系");
										positionNameDiverseAttribute
												.setAttrName("岗位名称");
										positionNameDiverseAttribute
												.setAttrNewVaule(orgPosition
														.getPositionName());
										newDiverseAttributeList
												.add(positionNameDiverseAttribute);
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Staff staff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Staff.class);

										if (staff != null
												&& !StrUtil.isEmpty(staff
														.getStaffName())) {
											diverseAttribute
													.setAttrNewVaule(staff
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization organization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														Organization.class);

										if (organization != null
												&& !StrUtil
														.isEmpty(organization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(organization
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Staff staff = (Staff) Staff
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Staff.class);

										if (staff != null
												&& !StrUtil.isEmpty(staff
														.getStaffName())) {
											diverseAttribute
													.setAttrNewVaule(staff
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgTree orgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														OrgTree.class);

										if (orgTree != null
												&& !StrUtil.isEmpty(orgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrNewVaule(orgTree
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
											.setAttrNewVaule(reflectObject
													.getPropertyName(
															"Individual",
															"gender",
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party party = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Party.class);

										if (party != null
												&& !StrUtil.isEmpty(party
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(party
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party party = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Party.class);

										if (party != null
												&& !StrUtil.isEmpty(party
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(party
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party party = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Party.class);

										if (party != null
												&& !StrUtil.isEmpty(party
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(party
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party party = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Party.class);

										if (party != null
												&& !StrUtil.isEmpty(party
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(party
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
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));
								}

								// 组织规模名称
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgScale")) {
									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));
								}

								// 组织类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("orgType")) {
									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));
								}

								// 组织存在类型
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("existType")) {
									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
													BaseUnitConstants.ENTT_STATE_ACTIVE));
								}

								// 组织优先级
								// if (!StrUtil.isEmpty(diverseAttribute
								// .getAttrName())
								// && diverseAttribute.getAttrName()
								// .equals("orgPriority")) {
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
								// }

								// 农村城镇标识
								if (!StrUtil.isEmpty(diverseAttribute
										.getAttrName())
										&& diverseAttribute.getAttrName()
												.equals("cityTown")) {
									diverseAttribute
											.setAttrNewVaule(reflectObject.getPropertyName(
													sysClass.getJavaCode(),
													diverseAttribute
															.getAttrName(),
													diverseAttribute
															.getAttrNewVaule(),
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										PoliticalLocation politicalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														sqlParams,
														PoliticalLocation.class);

										if (politicalLocation != null
												&& !StrUtil
														.isEmpty(politicalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrNewVaule(politicalLocation
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										TelcomRegion telcomRegion = (TelcomRegion) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														TelcomRegion.class);

										if (telcomRegion != null
												&& !StrUtil
														.isEmpty(telcomRegion
																.getRegionName())) {
											diverseAttribute
													.setAttrNewVaule(telcomRegion
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										AreaCode areaCode = (AreaCode) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														AreaCode.class);

										if (areaCode != null
												&& !StrUtil.isEmpty(areaCode
														.getAreaName())) {
											diverseAttribute
													.setAttrNewVaule(areaCode
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Party party = (Party) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams, Party.class);

										if (party != null
												&& !StrUtil.isEmpty(party
														.getPartyName())) {
											diverseAttribute
													.setAttrNewVaule(party
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization organization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														Organization.class);

										if (organization != null
												&& !StrUtil
														.isEmpty(organization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(organization
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgAttrSpec orgAttrSpec = (OrgAttrSpec) OrgAttrSpec
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														OrgAttrSpec.class);

										if (orgAttrSpec != null
												&& !StrUtil.isEmpty(orgAttrSpec
														.getAttrName())) {
											diverseAttribute
													.setAttrNewVaule(orgAttrSpec
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization organization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														Organization.class);

										if (organization != null
												&& !StrUtil
														.isEmpty(organization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(organization
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization organization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														Organization.class);

										if (organization != null
												&& !StrUtil
														.isEmpty(organization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(organization
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization organization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														Organization.class);

										if (organization != null
												&& !StrUtil
														.isEmpty(organization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(organization
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization organization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														Organization.class);

										if (organization != null
												&& !StrUtil
														.isEmpty(organization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(organization
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgTree orgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														OrgTree.class);

										if (orgTree != null
												&& !StrUtil.isEmpty(orgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrNewVaule(orgTree
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

									List sqlParams = new ArrayList();

									sqlParams.add(diverseAttribute
											.getAttrNewVaule());

									OrganizationRelation organizationRelation = (OrganizationRelation) OrganizationRelation
											.repository().jdbcFindObject(
													sql.toString(), sqlParams,
													OrganizationRelation.class);
									if (organizationRelation != null) {

										DiverseAttribute relaOrgIdDiverseAttribute = new DiverseAttribute();
										DiverseAttribute orgIdDiverseAttribute = new DiverseAttribute();

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
													.add(organizationRelation
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
														.setTableName("组织树引用关系表");
												relaOrgIdDiverseAttribute
														.setAttrName("父组织名称");
												relaOrgIdDiverseAttribute
														.setAttrNewVaule(relaOrgIdOrganization
																.getOrgFullName());
												newDiverseAttributeList
														.add(relaOrgIdDiverseAttribute);
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
													.add(organizationRelation
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
														.setTableName("组织树引用关系表");
												orgIdDiverseAttribute
														.setAttrName("组织名称");
												orgIdDiverseAttribute
														.setAttrNewVaule(orgIdOrganization
																.getOrgFullName());
												newDiverseAttributeList
														.add(orgIdDiverseAttribute);
											}
										}

									}

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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Organization organization = (Organization) Organization
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														Organization.class);

										if (organization != null
												&& !StrUtil
														.isEmpty(organization
																.getOrgFullName())) {
											diverseAttribute
													.setAttrNewVaule(organization
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										Position position = (Position) Position
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														Position.class);

										if (position != null
												&& !StrUtil.isEmpty(position
														.getPositionName())) {
											diverseAttribute
													.setAttrNewVaule(position
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgTree orgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														OrgTree.class);

										if (orgTree != null
												&& !StrUtil.isEmpty(orgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrNewVaule(orgTree
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgTree orgTree = (OrgTree) OrgTree
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														OrgTree.class);

										if (orgTree != null
												&& !StrUtil.isEmpty(orgTree
														.getOrgTreeName())) {
											diverseAttribute
													.setAttrNewVaule(orgTree
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										OrgAttrSpecSort orgAttrSpecSort = (OrgAttrSpecSort) OrgAttrSpecSort
												.repository().jdbcFindObject(
														sql.toString(),
														sqlParams,
														OrgAttrSpecSort.class);

										if (orgAttrSpecSort != null
												&& !StrUtil
														.isEmpty(orgAttrSpecSort
																.getSortName())) {
											diverseAttribute
													.setAttrNewVaule(orgAttrSpecSort
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

										List sqlParams = new ArrayList();

										sqlParams.add(diverseAttribute
												.getAttrNewVaule());

										PoliticalLocation politicalLocation = (PoliticalLocation) Organization
												.repository()
												.jdbcFindObject(sql.toString(),
														sqlParams,
														PoliticalLocation.class);

										if (politicalLocation != null
												&& !StrUtil
														.isEmpty(politicalLocation
																.getLocationName())) {
											diverseAttribute
													.setAttrNewVaule(politicalLocation
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
					this.bean.getSystemInfoAddOrDelDiv().setVisible(true);
					this.bean.getDiverseAttributeAddOrDelListboxDiv()
							.setVisible(false);
				}

				if (newDiverseAttributeList == null
						|| newDiverseAttributeList.size() <= 0
						|| newDiverseAttributeList.get(0).getAttrName()
								.equals("失效时间")) {
					this.bean.getSystemInfoAddOrDelDiv().setVisible(true);
					this.bean.getDiverseAttributeAddOrDelListboxDiv()
							.setVisible(false);
				} else {
					// 向ListBox中填充数据，存在样式延迟加载的问题。
					/*
					 * ListModel dataList = new BindingListModelList(
					 * newDiverseAttributeList, true);
					 * this.bean.getDiverseAttributeAddOrDelListbox().setModel(
					 * dataList);
					 */
					this.list.add(newDiverseAttributeList);
				}
			} else {
				this.bean.getSystemInfoAddOrDelDiv().setVisible(true);
				this.bean.getDiverseAttributeAddOrDelListboxDiv().setVisible(
						false);
			}
		} else {
			this.bean.getSystemInfoAddOrDelDiv().setVisible(true);
			this.bean.getDiverseAttributeAddOrDelListboxDiv().setVisible(false);
		}
	}

	public void bindDate(List<List> list) {
		if (list != null && list.size() > 0) {

			Div div = (Div) this.bean.getDiverseAttributeAddOrDelListboxDiv();// 获取Div
			div.setStyle("height:436px;overflow:auto;");

			for (List<DiverseAttribute> diverseAttributeList : list) {

				Panel panel = new Panel();
				panel.setParent(div);

				Panelchildren panelChildren = new Panelchildren();
				panelChildren.setParent(panel);

				Listbox listResult = new Listbox();
				listResult.setParent(panelChildren);

				// listResult.getItems().clear();// 清空上次的列表内容
				// listResult.setHeight("400");
				// listResult.setVflex(vflex);
				// listResult.setRows(12);// 设定要显示的行数，如果超过设定的行数，则显示垂直滚动条
				listResult.setRows(diverseAttributeList.size());

				Listhead listHead = new Listhead();// 创建行标题
				listHead.setParent(listResult);// 设置父容器
				listHead.setSizable(true);//行可调整

				Listheader tListheader=new Listheader("表 名");
				tListheader.setWidth("100px");

				Listheader cListheader=new Listheader("字段名");
				cListheader.setWidth("115px");
				
				Listheader aListheader=new Listheader("值");
				
				listHead.appendChild(tListheader);
				listHead.appendChild(cListheader);
				listHead.appendChild(aListheader);

				for (DiverseAttribute diverseAttribute : diverseAttributeList) {
					Listitem li = new Listitem();// 创建行
					li.setParent(listResult);// 设置父容器
					li.appendChild(new Listcell(diverseAttribute.getTableName()));// 添加列
					li.appendChild(new Listcell(diverseAttribute.getAttrName()));// 添加列
					li.appendChild(new Listcell(diverseAttribute
							.getAttrNewVaule()));// 添加列
				}
			}

		}

	}

}
