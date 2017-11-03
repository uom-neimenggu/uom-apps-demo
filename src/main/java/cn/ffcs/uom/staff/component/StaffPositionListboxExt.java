package cn.ffcs.uom.staff.component;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.component.bean.StaffPositionListboxExtBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.manager.StaffPositionManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffPosition;

public class StaffPositionListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private StaffPositionListboxExtBean bean = new StaffPositionListboxExtBean();
	/**
	 * 选中的员工
	 */
	private Staff staff;
	/**
	 * 组织树中选中的组织
	 */
	@Setter
	private Organization organization;
	/**
	 * 树TAB区分
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;
	/**
	 * 查询岗位
	 */
	private Position queryPosition;
	/**
	 * 查询组织
	 */
	private Organization queryOrganization;

	/**
	 * 选中的关系
	 */
	private StaffPosition staffPosition;
	/**
	 * 员工
	 */
	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");
	/**
	 * 员工岗位
	 */
	private StaffPositionManager staffPositionManager = (StaffPositionManager) ApplicationContextUtil
			.getBean("staffPositionManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public StaffPositionListboxExt() {
		Executions.createComponents(
				"/pages/staff/comp/staff_position_listbox_ext.zul", this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_POSITION_QUERY, this,
				"onSelectStaffResponse");
		this.addForward(SffOrPtyCtants.ON_CLEAN_STAFF_POSITI, this,
				SffOrPtyCtants.ON_CLEAN_STAFF_POSITI_RES);
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false);
	}

	/**
	 * 选择组织响应
	 */
	public void onSelectStaffResponse(ForwardEvent event) throws Exception {
		staff = (Staff) event.getOrigin().getData();
		if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
			this.setOrgTreeTabName(variableOrgTreeTabName);
		}
		if (staff != null) {
			this.setButtonValid(true, false);
			this.bean.getOrgName().setValue("");
			this.bean.getPositionName().setValue("");
			this.onQueryOrganizationList();
		}
	}

	/**
	 * 查询
	 */
	public void onQueryOrganizationList() throws Exception {

		queryPosition = new Position();
		queryOrganization = new Organization();

		queryPosition.setPositionName(this.bean.getPositionName().getValue());
		queryOrganization.setOrgName(this.bean.getOrgName().getValue());

		if (staff != null) {
			PageInfo pageInfo = staffManager
					.queryPageInfoByStaffPosition(queryPosition,
							queryOrganization, staff, this.bean
									.getStaffPositionListboxPaging()
									.getActivePage() + 1, this.bean
									.getStaffPositionListboxPaging()
									.getPageSize());
			ListModel list = new BindingListModelList(pageInfo.getDataList(),
					true);
			this.bean.getStaffPositionListbox().setModel(list);
			this.bean.getStaffPositionListboxPaging().setTotalSize(
					pageInfo.getTotalCount());
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryStaffPosition() throws Exception {
		this.bean.getStaffPositionListboxPaging().setActivePage(0);
		this.onQueryOrganizationList();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetStaffPosition() throws Exception {

		this.bean.getOrgName().setValue("");

		this.bean.getPositionName().setValue("");

	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onAddStaffPosition() throws Exception {
		// if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
		// ActionKeys.DATA_OPERATING)){
		// return;
		// }
		if (staff != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("opType", "add");
			map.put("staff", staff);
			Window window = (Window) Executions.createComponents(
					"/pages/staff/comp/staff_position_edit.zul", this, map);
			window.doModal();
			window.addEventListener("onOK", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						// 新增或修改完成后，列表从数据库更新
						StaffPosition staffPosition = (StaffPosition) event
								.getData();
						OrgPosition orgPosition = (OrgPosition) OrgPosition
								.repository().getObject(OrgPosition.class,
										staffPosition.getOrgPositionRelaId());
						bean.getOrgName().setValue(orgPosition.getOrgName());
						bean.getPositionName().setValue(
								orgPosition.getPositionName());

						onQueryOrganizationList();

					}
				}
			});
		}
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onDelStaffPosition() throws Exception {
		// if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
		// ActionKeys.DATA_OPERATING)){
		// return;
		// }
		if (this.staffPosition != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						staffPositionManager.removeStaffPosition(staffPosition);
						PubUtil.reDisplayListbox(
								bean.getStaffPositionListbox(), staffPosition,
								"del");
						staffPosition = null;
						setButtonValid(true, false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录", "提示信息");
			return;
		}
	}

	/**
	 * 设置按钮
	 * 
	 * @param canAdd
	 * @param candel
	 * @throws Exception
	 */
	private void setButtonValid(boolean canAdd, boolean candel) {
		if (isDuceTree) {
			return;
		}
		this.bean.getAddButton().setDisabled(!canAdd);
		this.bean.getDelButton().setDisabled(!candel);
	}

	/**
	 * 选择列表
	 */
	public void onSelectStaffPositionList() throws Exception {
		this.setButtonValid(true, true);
		staffPosition = (StaffPosition) this.bean.getStaffPositionListbox()
				.getSelectedItem().getValue();
	}

	/**
	 * 
	 * .
	 * 
	 * @author wangyong 2013-6-11 wangyong
	 */
	public void onCleanStaffPositiRespons(final ForwardEvent event) {
		this.bean.getOrgName().setValue("");
		this.bean.getPositionName().setValue("");
		staffPosition = null;
		staff = null;
		ListboxUtils.clearListbox(bean.getStaffPositionListbox());
	}

	/**
	 * 是否是推导树
	 */
	@Getter
	private boolean isDuceTree = false;

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		if (isDuceTree) {
			this.setButtonValid(false, false);
		}
		this.isDuceTree = isDuceTree;
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
		} else if ("orgTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_POSITION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_POSITION_DEL)) {
				canDel = true;
			}
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_POSITION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_POSITION_DEL)) {
				canDel = true;
			}
		} else if ("marketingStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_POSITION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_POSITION_DEL)) {
				canDel = true;
			}
		} else if ("costStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_POSITION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_POSITION_DEL)) {
				canDel = true;
			}
		}

		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canAdd = false;
		boolean canDel = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("marketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_POSITION_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_POSITION_DEL)) {
						canDel = true;
					}

				}

			} else if ("newMarketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_POSITION_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_POSITION_DEL)) {
						canDel = true;
					}

				}

            } else if ("newSeventeenMarketingTab".equals(orgTreeTabName)) {
                
                aroleOrganizationLevel = new AroleOrganizationLevel();
                aroleOrganizationLevel.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
                aroleOrganizationLevel
                    .setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
                
                if (!StrUtil.isNullOrEmpty(organization)
                    && aroleOrganizationLevelManager.aroleOrganizationLevelValid(
                        aroleOrganizationLevel, organization)) {
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_STAFF_POSITION_ADD)) {
                        canAdd = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_STAFF_POSITION_DEL)) {
                        canDel = true;
                    }
                    
                }
                
            } else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("multidimensionalTab".equals(orgTreeTabName)) {
				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MULTIDIMENSIONAL_STAFF_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MULTIDIMENSIONAL_STAFF_POSITION_DEL)) {
					canDel = true;
				}
			}

		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
	}
}
