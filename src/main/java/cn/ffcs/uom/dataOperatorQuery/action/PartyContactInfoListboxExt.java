package cn.ffcs.uom.dataOperatorQuery.action;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.party.component.bean.PartyContactInfoListboxBean;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 参与人联系人方式列表 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author fangy
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-12-8
 * @功能说明：
 * 
 */
@SuppressWarnings({ "unused" })
public class PartyContactInfoListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	PartyContactInfoListboxBean bean = new PartyContactInfoListboxBean();

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

    private final String zul = "/pages/dataOperatorQuery/party_contactInfo_listbox_ext.zul";
	
	@Getter
	@Setter
	private Party party;

	private PartyContactInfo partyConInfo;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	/**
	 * 是否是只有身份证查询条件的参与人管理页面
	 */
	@Getter
	@Setter
	private Boolean isIdNoQueryPage = false;

	public PartyContactInfoListboxExt() {
		Executions.createComponents(zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_CLEAN_PARTY_CON, this,
				SffOrPtyCtants.ON_CLEAN_PARTY_CON_RES);

	}

	public void onCreate() throws Exception {
		/**
		 * 只有身份证查询条件的参与人管理页面，隐藏CURD功能
		 */
		if (isIdNoQueryPage) {
			this.bean.getPartyContactInfoBandboxDiv().setVisible(false);
		}
	}

	/**
	 * 参与人联系人选择 .
	 * 
	 * @param event
	 * @author Wong 2013-6-8 Wong
	 */
	public void onPartyContactInfoSelectRequest(final ForwardEvent event) {
		if (bean.getPartyContactInfoListbox().getSelectedCount() > 0) {
			partyConInfo = (PartyContactInfo) bean.getPartyContactInfoListbox()
					.getSelectedItem().getValue();
		}
	}

	public void quertyPartyContactInfo() {

		partyConInfo.setContactName(this.bean.getContactName().getValue());

		if (null != partyConInfo) {
			PageInfo pageInfo = partyManager
					.getPartyContactInfos(partyConInfo,
							bean.getPartyContactInfoListboxPaging()
									.getActivePage() + 1, bean
									.getPartyContactInfoListboxPaging()
									.getPageSize());

			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getPartyContactInfoListbox().setModel(dataList);
			bean.getPartyContactInfoListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
	}


	/**
	 * 清空选中的员工.
	 */
	public void onCleaningPartyContactInfo() throws Exception {
		this.bean.getContactName().setValue("");
		ListboxUtils.clearListbox(bean.getPartyContactInfoListbox());
	}

	/**
	 * 分页显示 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	public void onPartyContactInfoListboxPaging() {
		quertyPartyContactInfo();
	}

	/**
	 * 控件初始化 .
	 * 
	 * @throws Exception
	 *             异常
	 * @author
	 */
	public void init() throws Exception {
		this.bean.getContactName().setValue("");
		if (this.getParty() != null) {
			if (null == partyConInfo) {
				partyConInfo = new PartyContactInfo();
			}
			partyConInfo.setPartyId(party.getPartyId());
			this.quertyPartyContactInfo();
		}
	}

	/**
	 * 
	 * .
	 * 
	 * @author wangyong 2013-6-13 wangyong
	 */
	public void onCleanonCleanPartyConRespons() {
		this.bean.getContactName().setValue("");
		party = null;
		partyConInfo = null;
		ListboxUtils.clearListbox(bean.getPartyContactInfoListbox());
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canUpdate = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canDel = true;
		} else if ("orgTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("partyPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("orgTreePage_orgParty".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		}else if ("marketingStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("costStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} 
		
		bean.getAddPartyContactInfoButton().setVisible(canAdd);
		bean.getEditPartyContactInfoButton().setVisible(canUpdate);
		bean.getDelPartyContactInfoButton().setVisible(canDel);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canAdd = false;
		boolean canUpdate = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {

			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			}else if ("marketingTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			}else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("politicalTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("agentTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("ossTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("edwTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			}else if ("marketingTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			}else if ("costTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			}
		}
		bean.getAddPartyContactInfoButton().setVisible(canAdd);
		bean.getEditPartyContactInfoButton().setVisible(canUpdate);
		bean.getDelPartyContactInfoButton().setVisible(canDel);
	}
}
