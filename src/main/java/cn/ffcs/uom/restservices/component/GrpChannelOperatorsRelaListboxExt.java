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
import cn.ffcs.uom.restservices.component.bean.GrpChannelOperatorsRelaListboxExtBean;
import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.GrpChannelOperatorsRela;
import cn.ffcs.uom.restservices.model.GrpOperators;

public class GrpChannelOperatorsRelaListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	/**
	 * 页面bean
	 */
	private GrpChannelOperatorsRelaListboxExtBean bean = new GrpChannelOperatorsRelaListboxExtBean();
	/**
	 * 选中的经营主体
	 */
	private GrpOperators grpOperators;
	/**
	 * 查询集团经营主体
	 */
	private GrpChannelOperatorsRela queryGrpChannelOperatorsRela;

	/**
	 * 选中的关系
	 */
	private GrpChannelOperatorsRela grpChannelOperatorsRela;

	/**
	 * 经营主体业务
	 */
	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public GrpChannelOperatorsRelaListboxExt() {
		Executions
				.createComponents(
						"/pages/restservices/comp/grp_channel_operators_rela_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(
				ChannelInfoConstant.ON_GRP_CHANNEL_OPERATORS_RELA_QUERY,
				this,
				ChannelInfoConstant.ON_SELECT_GRP_CHANNEL_OPERATORS_RELA_RESPONS);
		this.addForward(
				ChannelInfoConstant.ON_CLEAN_GRP_CHANNEL_OPERATORS_RELA, this,
				ChannelInfoConstant.ON_CLEAN_GRP_CHANNEL_OPERATORS_RELA_RES);
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
	}

	/**
	 * 选择经营主体响应
	 */
	public void onSelectGrpChannelOperatorsRelaResponse(ForwardEvent event)
			throws Exception {
		grpOperators = (GrpOperators) event.getOrigin().getData();
		if (grpOperators != null) {
			this.bean.getChannelNbr().setValue("");
			this.bean.getChannelName().setValue("");
			this.onQueryGrpChannelOperatorsRelaList();
		}
	}

	/**
	 * 查询
	 */
	public void onQueryGrpChannelOperatorsRelaList() throws Exception {

		if (grpOperators != null
				&& !StrUtil.isEmpty(grpOperators.getOperatorsNbr())) {

			queryGrpChannelOperatorsRela = new GrpChannelOperatorsRela();

			queryGrpChannelOperatorsRela.setOperatorsNbr(grpOperators
					.getOperatorsNbr());
			queryGrpChannelOperatorsRela.setChannelNbr(this.bean
					.getChannelNbr().getValue());
			queryGrpChannelOperatorsRela.setChannelName(this.bean
					.getChannelName().getValue());

			PageInfo pageInfo = channelInfoManager
					.queryPageInfoByGrpChannelOperatorsRela(
							queryGrpChannelOperatorsRela, this.bean
									.getGrpChannelOperatorsRelaListboxPaging()
									.getActivePage() + 1, this.bean
									.getGrpChannelOperatorsRelaListboxPaging()
									.getPageSize());
			ListModel list = new BindingListModelList(pageInfo.getDataList(),
					true);
			this.bean.getGrpChannelOperatorsRelaListbox().setModel(list);
			this.bean.getGrpChannelOperatorsRelaListboxPaging().setTotalSize(
					pageInfo.getTotalCount());
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryGrpChannelOperatorsRela() throws Exception {
		this.bean.getGrpChannelOperatorsRelaListboxPaging().setActivePage(0);
		this.onQueryGrpChannelOperatorsRelaList();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetGrpChannelOperatorsRela() throws Exception {

		this.bean.getChannelNbr().setValue("");

		this.bean.getChannelName().setValue("");

	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onGrpChannelOperatorsRelaListboxPaging() throws Exception {
		this.onQueryGrpChannelOperatorsRelaList();

	}

	/**
	 * 选择列表
	 */
	public void onSelectGrpChannelOperatorsRelaList() throws Exception {
		grpChannelOperatorsRela = (GrpChannelOperatorsRela) this.bean
				.getGrpChannelOperatorsRelaListbox().getSelectedItem()
				.getValue();
	}

	/**
	 * @author 朱林涛
	 */
	public void onCleanGrpChannelOperatorsRelaRespons(final ForwardEvent event) {
		this.bean.getChannelNbr().setValue("");
		this.bean.getChannelName().setValue("");
		grpChannelOperatorsRela = null;
		grpOperators = null;
		ListboxUtils.clearListbox(bean.getGrpChannelOperatorsRelaListbox());
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
	}

}
