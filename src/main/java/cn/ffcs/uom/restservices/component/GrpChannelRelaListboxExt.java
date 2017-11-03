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
import cn.ffcs.uom.restservices.component.bean.GrpChannelRelaListboxExtBean;
import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.GrpChannel;
import cn.ffcs.uom.restservices.model.GrpChannelRela;

public class GrpChannelRelaListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private GrpChannelRelaListboxExtBean bean = new GrpChannelRelaListboxExtBean();
	/**
	 * 选中的渠道
	 */
	private GrpChannel grpChannel;
	/**
	 * 查询集团渠道
	 */
	private GrpChannelRela queryGrpChannelRela;

	/**
	 * 选中的关系
	 */
	private GrpChannelRela grpChannelRela;

	/**
	 * 渠道业务
	 */
	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public GrpChannelRelaListboxExt() {
		Executions.createComponents(
				"/pages/restservices/comp/grp_channel_rela_listbox_ext.zul",
				this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(ChannelInfoConstant.ON_GRP_CHANNEL_RELA_QUERY, this,
				ChannelInfoConstant.ON_SELECT_GRP_CHANNEL_RELA_RESPONS);
		this.addForward(ChannelInfoConstant.ON_CLEAN_GRP_CHANNEL_RELA, this,
				ChannelInfoConstant.ON_CLEAN_GRP_CHANNEL_RELA_RES);
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
	public void onSelectGrpChannelRelaResponse(ForwardEvent event)
			throws Exception {
		grpChannel = (GrpChannel) event.getOrigin().getData();
		if (grpChannel != null) {
			this.bean.getChannelNbr().setValue("");
			this.bean.getChannelName().setValue("");
			this.onQueryGrpChannelRelaList();
		}
	}

	/**
	 * 查询
	 */
	public void onQueryGrpChannelRelaList() throws Exception {

		if (grpChannel != null && !StrUtil.isEmpty(grpChannel.getChannelNbr())) {

			queryGrpChannelRela = new GrpChannelRela();

			queryGrpChannelRela.setRelaChannelNbr(grpChannel.getChannelNbr());
			queryGrpChannelRela.setChannelNbr(this.bean.getChannelNbr()
					.getValue());
			queryGrpChannelRela.setChannelName(this.bean.getChannelName()
					.getValue());

			PageInfo pageInfo = channelInfoManager
					.queryPageInfoByGrpChannelRela(queryGrpChannelRela,
							this.bean.getGrpChannelRelaListboxPaging()
									.getActivePage() + 1, this.bean
									.getGrpChannelRelaListboxPaging()
									.getPageSize());
			ListModel list = new BindingListModelList(pageInfo.getDataList(),
					true);
			this.bean.getGrpChannelRelaListbox().setModel(list);
			this.bean.getGrpChannelRelaListboxPaging().setTotalSize(
					pageInfo.getTotalCount());
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryGrpChannelRela() throws Exception {
		this.bean.getGrpChannelRelaListboxPaging().setActivePage(0);
		this.onQueryGrpChannelRelaList();

	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onGrpChannelRelaListboxPaging() throws Exception {
		this.onQueryGrpChannelRelaList();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetGrpChannelRela() throws Exception {

		this.bean.getChannelNbr().setValue("");

		this.bean.getChannelName().setValue("");

	}

	/**
	 * 选择列表
	 */
	public void onSelectGrpChannelRelaList() throws Exception {
		grpChannelRela = (GrpChannelRela) this.bean.getGrpChannelRelaListbox()
				.getSelectedItem().getValue();
	}

	/**
	 * @author 朱林涛
	 */
	public void onCleanGrpChannelRelaRespons(final ForwardEvent event) {
		this.bean.getChannelNbr().setValue("");
		this.bean.getChannelName().setValue("");
		grpChannelRela = null;
		grpChannel = null;
		ListboxUtils.clearListbox(bean.getGrpChannelRelaListbox());
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
	}

}
