package cn.ffcs.uom.restservices.component;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.restservices.component.bean.GrpStaffChannelRelaListboxExtBean;
import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.GrpChannel;
import cn.ffcs.uom.restservices.model.GrpStaffChannelRela;

public class GrpStaffChannelRelaListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private GrpStaffChannelRelaListboxExtBean bean = new GrpStaffChannelRelaListboxExtBean();
	/**
	 * 查询集团渠道
	 */
	private GrpStaffChannelRela queryGrpStaffChannelRela;

	/**
	 * 选中的关系
	 */
	private GrpStaffChannelRela grpStaffChannelRela;

	/**
	 * 渠道业务
	 */
	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public GrpStaffChannelRelaListboxExt() {
		Executions
				.createComponents(
						"/pages/restservices/comp/grp_staff_channel_rela_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(ChannelInfoConstant.ON_GRP_STAFF_CHANNEL_RELA_QUERY,
				this,
				ChannelInfoConstant.ON_SELECT_GRP_STAFF_CHANNEL_RELA_RESPONS);
		this.addForward(ChannelInfoConstant.ON_CLEAN_GRP_STAFF_CHANNEL_RELA,
				this, ChannelInfoConstant.ON_CLEAN_GRP_STAFF_CHANNEL_RELA_RES);
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
	}

	/**
	 * 选择渠道响应
	 */
	public void onSelectGrpStaffChannelRelaResponse(ForwardEvent event)
			throws Exception {
		queryGrpStaffChannelRela = (GrpStaffChannelRela) event.getOrigin()
				.getData();
		if (queryGrpStaffChannelRela != null) {
			this.bean.getSalesCode().setValue("");
			this.bean.getStaffName().setValue("");
			this.onQueryGrpStaffChannelRelaList();
		}
	}

	/**
	 * 查询
	 */
	public void onQueryGrpStaffChannelRelaList() throws Exception {

		if (queryGrpStaffChannelRela != null) {

			if (!StrUtil.isEmpty(this.bean.getSalesCode().getValue())) {
				queryGrpStaffChannelRela.setSalesCode(this.bean.getSalesCode()
						.getValue());
			}

			if (!StrUtil.isEmpty(this.bean.getStaffName().getValue())) {
				queryGrpStaffChannelRela.setStaffName(this.bean.getStaffName()
						.getValue());
			}

			PageInfo pageInfo = channelInfoManager
					.queryPageInfoByGrpStaffChannelRela(
							queryGrpStaffChannelRela, this.bean
									.getGrpStaffChannelRelaListboxPaging()
									.getActivePage() + 1, this.bean
									.getGrpStaffChannelRelaListboxPaging()
									.getPageSize());
			ListModel list = new BindingListModelList(pageInfo.getDataList(),
					true);
			this.bean.getGrpStaffChannelRelaListbox().setModel(list);
			this.bean.getGrpStaffChannelRelaListboxPaging().setTotalSize(
					pageInfo.getTotalCount());
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryGrpStaffChannelRela() throws Exception {
		this.bean.getGrpStaffChannelRelaListboxPaging().setActivePage(0);
		this.onQueryGrpStaffChannelRelaList();

	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onGrpStaffChannelRelaListboxPaging() throws Exception {
		this.onQueryGrpStaffChannelRelaList();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetGrpStaffChannelRela() throws Exception {

		this.bean.getSalesCode().setValue("");

		this.bean.getStaffName().setValue("");

	}

	/**
	 * 选择列表
	 */
	public void onSelectGrpStaffChannelRelaList() throws Exception {
		grpStaffChannelRela = (GrpStaffChannelRela) this.bean
				.getGrpStaffChannelRelaListbox().getSelectedItem().getValue();
	}

	/**
	 * @author 朱林涛
	 */
	public void onCleanGrpStaffChannelRelaRespons(final ForwardEvent event) {
		this.bean.getSalesCode().setValue("");
		this.bean.getStaffName().setValue("");
		grpStaffChannelRela = null;
		ListboxUtils.clearListbox(bean.getGrpStaffChannelRelaListbox());
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
	}

}
