package cn.ffcs.uom.organization.action;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

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
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.organization.action.bean.OrganizationPositionExtBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.position.manager.OrgPositonManager;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.model.Staff;

public class OrganizationPositionExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3695647753612258247L;
	/**
	 * 页面
	 */
	private final String zul = "/pages/organization/organization_position_ext.zul";
	/**
	 * 页面bean
	 */
	private OrganizationPositionExtBean bean = new OrganizationPositionExtBean();

	/**
	 * 选中的组织
	 */
	private Organization organization;
	/**
	 * 选中的组织岗位关系
	 */
	private OrgPosition orgPosition;
	/*
	 * 根据条件查询关联的岗位信息
	 */
	private Position queryPosition;
	/**
	 * manager
	 */
	private OrgPositonManager orgPositonManager = (OrgPositonManager) ApplicationContextUtil
			.getBean("orgPositonManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");
	/**
	 * bandBox选项
	 */
	private String bandBoxOptype;

	/**
	 * bandbox传递
	 */
	private Staff staff;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 是否是推导树
	 */
	@Getter
	private Boolean isDuceTree = false;
	/**
	 * 树TAB区分
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;
	/**
	 * 组织管理区分
	 */
	@Getter
	@Setter
	private String variablePagePosition;

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
	 * 构造方法
	 */
	public OrganizationPositionExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 组织查看岗位
		 */
		this.addForward(OrganizationConstant.ON_ORGANIZATION_POSITION_QUERY,
				this, "onSelectOrganizationResponse");
		/**
		 * 员工查看岗位
		 */
		this.addForward(
				OrganizationRelationConstant.ON_STAFF_OR_ORG_QUERY_POSITION,
				this, "onSelectStaffOrOrgResponse");
		/**
		 * 组织列表查询事件响应，清除列表
		 */
		this.addForward(OrganizationConstant.ON_ORGANIZATION_QUERY, this,
				"onOrganiztionQueryResponse");
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
	 * 选择组织响应事件
	 * 
	 * @param event
	 */
	public void onSelectOrganizationResponse(ForwardEvent event)
			throws Exception {
		this.bean.getPositionCode().setValue("");
		this.bean.getPositionName().setValue("");
		organization = (Organization) event.getOrigin().getData();
		if (organization != null && organization.getOrgId() != null) {
			this.setButtonValid(true, false);
			this.onQueryPositionList();
		} else {
			/**
			 * 组织树未选择组织清理数据
			 */
			ListboxUtils.clearListbox(this.bean
					.getOrganizationPositionListbox());
		}

		if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
			this.setOrgTreeTabName(variableOrgTreeTabName);
		}

		if (!StrUtil.isEmpty(variablePagePosition)) {
			this.setPagePosition(variablePagePosition);
		}

	}

	/**
	 * 组织列表查询响应，清空列表
	 * 
	 * @throws Exception
	 */
	public void onOrganiztionQueryResponse() throws Exception {
		this.organization = null;
		cn.ffcs.uom.common.util.PubUtil.clearListbox(this.bean
				.getOrganizationPositionListbox());
		this.setButtonValid(false, false);
	}

	/**
	 * 选择员工或者组织查询岗位时间
	 * 
	 * @param event
	 */
	public void onSelectStaffOrOrgResponse(ForwardEvent event) throws Exception {
		Map map = (Map) event.getOrigin().getData();
		if (map != null) {
			bandBoxOptype = (String) map.get("opType");
			if ("staffPosition".equals(bandBoxOptype)) {
				staff = (Staff) map.get("staff");
			} else if ("orgPosition".equals(bandBoxOptype)) {
				organization = (Organization) map.get("organization");
			}
		}
		this.onQueryPositionList();
	}

	/**
	 * 查询组织岗位
	 */
	public void onQueryPositionList() throws Exception {
		queryPosition = new Position();
		queryPosition.setPositionCode(this.bean.getPositionCode().getValue());
		queryPosition.setPositionName(this.bean.getPositionName().getValue());

		PageInfo pageInfo = null;
		if ("staffPosition".equals(bandBoxOptype) && staff != null
				&& staff.getStaffId() != null) {
			pageInfo = this.orgPositonManager
					.queryPageInfoByStaff(staff,
							this.bean.getOrganizationPositionPaging()
									.getActivePage() + 1, this.bean
									.getOrganizationPositionPaging()
									.getPageSize());
		} else {
			/**
			 * 查组织岗位
			 */
			OrgPosition queryVo = new OrgPosition();
			if (organization != null && organization.getOrgId() != null) {
				queryVo.setOrgId(organization.getOrgId());

				pageInfo = this.orgPositonManager
						.queryPageInfoByQueryOrgPosition(queryPosition,
								queryVo, this.bean
										.getOrganizationPositionPaging()
										.getActivePage() + 1, this.bean
										.getOrganizationPositionPaging()
										.getPageSize());
			}
		}
		if (pageInfo != null) {
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getOrganizationPositionListbox().setModel(dataList);
			this.bean.getOrganizationPositionPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryPositionOrganization() throws Exception {
		this.bean.getOrganizationPositionPaging().setActivePage(0);
		this.onQueryPositionList();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetPositionOrganization() throws Exception {

		this.bean.getPositionCode().setValue("");

		this.bean.getPositionName().setValue("");

	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onAddOrganizationPosition() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("organization", organization);
		Window window = (Window) Executions
				.createComponents(
						"/pages/organization/organization_position_edit.zul",
						this, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					/*
					 * 岗位批量新增后无法定位到具体某一条 OrgPosition orgPosition = (OrgPosition)
					 * event.getData(); Position position = new Position();
					 * position.setPositionId(orgPosition.getPositionId());
					 * position = (Position) Position.repository().getObject(
					 * Position.class, position.getPositionId()); if (position
					 * != null) { // 新增岗位组织关系后，用于显示该条记录
					 * bean.getPositionCode().setValue(
					 * position.getPositionCode());
					 * bean.getPositionName().setValue(
					 * position.getPositionName()); }
					 */
					onQueryPositionList();

				}
			}
		});
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onDelOrganizationPosition() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		if (this.orgPosition != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						orgPositonManager
								.removeOrganizationPosition(orgPosition);
						PubUtil.reDisplayListbox(
								bean.getOrganizationPositionListbox(),
								orgPosition, "del");
						orgPosition = null;
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
		/**
		 * 推导树默认不让编辑且不然修改
		 */
		if (isDuceTree) {
			return;
		}
		this.bean.getAddButton().setDisabled(!canAdd);
		this.bean.getDelButton().setDisabled(!candel);
	}

	/**
	 * 选择列表
	 */
	public void onSelectOrganizationPositionList() throws Exception {
		this.setButtonValid(true, true);
		orgPosition = (OrgPosition) this.bean.getOrganizationPositionListbox()
				.getSelectedItem().getValue();
		Events.postEvent(
				OrganizationConstant.ON_SELECT_ORGANIZATION_POSITION_REQUEST,
				this, orgPosition);
	}

	/**
	 * 清空
	 * 
	 * @throws Exception
	 */
	public void onCleanOrganizationPosition() throws Exception {
		Events.postEvent(
				OrganizationConstant.ON_CLEAN_ORGANIZATION_POSITION_REQUEST,
				this, null);
	}

	/**
	 * 关闭
	 * 
	 * @throws Exception
	 */
	public void onCloseOrganizationPosition() throws Exception {
		Events.postEvent(
				OrganizationConstant.ON_CLOSE_ORGANIZATION_POSITION_REQUEST,
				this, null);
	}

	/**
	 * 设置按钮隐藏
	 * 
	 * @param canClean
	 * @param canClose
	 * @throws Exception
	 */
	public void setBandboxButtonValid(boolean canAdd, boolean candel,
			boolean canClean, boolean canClose) throws Exception {
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(candel);
		this.bean.getCleanButton().setVisible(canClean);
		this.bean.getCloseButton().setVisible(canClose);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canDel = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
		} else if ("orgTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_POSITION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_POSITION_DEL)) {
				canDel = true;
			}
		} else if ("organizationPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_POSITION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_POSITION_DEL)) {
				canDel = true;
			}
		} else if ("marketingUnitPage".equals(page)) {

			aroleOrganizationLevel = new AroleOrganizationLevel();
			aroleOrganizationLevel
					.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
			aroleOrganizationLevel
					.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

			if (!StrUtil.isNullOrEmpty(organization)
					&& aroleOrganizationLevelManager
							.aroleOrganizationLevelValid(
									aroleOrganizationLevel, organization)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORG_POSITION_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORG_POSITION_DEL)) {
					canDel = true;
				}

			}

		} else if ("newMarketingUnitPage".equals(page)) {

			aroleOrganizationLevel = new AroleOrganizationLevel();
			aroleOrganizationLevel
					.setOrgId(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
			aroleOrganizationLevel
					.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

			if (!StrUtil.isNullOrEmpty(organization)
					&& aroleOrganizationLevelManager
							.aroleOrganizationLevelValid(
									aroleOrganizationLevel, organization)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORG_POSITION_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_ORG_POSITION_DEL)) {
					canDel = true;
				}

			}

        } else if ("newSeventeenMarketingUnitPage".equals(page)) {
            
            aroleOrganizationLevel = new AroleOrganizationLevel();
            aroleOrganizationLevel.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
            aroleOrganizationLevel
                .setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
            
            if (!StrUtil.isNullOrEmpty(organization)
                && aroleOrganizationLevelManager.aroleOrganizationLevelValid(
                    aroleOrganizationLevel, organization)) {
                
                if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                    ActionKeys.MARKETING_UNIT_ORG_POSITION_ADD)) {
                    canAdd = true;
                }
                
                if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                    ActionKeys.MARKETING_UNIT_ORG_POSITION_DEL)) {
                    canDel = true;
                }
                
            }
            
        } else if ("costUnitPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORG_POSITION_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_ORG_POSITION_DEL)) {
				canDel = true;
			}
		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
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
						ActionKeys.ORG_TREE_POLITICAL_ORG_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_POSITION_DEL)) {
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
							ActionKeys.ORG_TREE_MARKETING_ORG_POSITION_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_POSITION_DEL)) {
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
							ActionKeys.ORG_TREE_MARKETING_ORG_POSITION_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_POSITION_DEL)) {
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
                        ActionKeys.ORG_TREE_MARKETING_ORG_POSITION_ADD)) {
                        canAdd = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_POSITION_DEL)) {
                        canDel = true;
                    }
                    
                }
                
            } else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_POSITION_DEL)) {
					canDel = true;
				}
			} else if ("multidimensionalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_POSITION_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_POSITION_DEL)) {
					canDel = true;
				}
			}
		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
	}
}
