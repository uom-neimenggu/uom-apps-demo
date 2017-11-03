package cn.ffcs.uom.position.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.position.action.bean.PositionMainBean;
import cn.ffcs.uom.position.constants.PositionConstant;
import cn.ffcs.uom.position.model.Position;

/**
 * 岗位管理实体Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-06-03
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class PositionMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	private PositionMainBean bean = new PositionMainBean();

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	private Component positionOrganizationListbox;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getPositionTreeExt().setPortletInfoProvider(this);
		bean.getPositionEditDiv().setPortletInfoProvider(this);
		positionOrganizationListbox = this.self.getFellow(
				"positionOrganizationListbox").getFellow(
				"positionOrganizationListboxComp");
		this.bean.getPositionTreeExt().addEventListener(
				PositionConstant.ON_SELECT_POSITION, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (event.getData() != null) {
							if (event.getData() != null) {
								Position position = (Position) event.getData();
								Events.postEvent(
										PositionConstant.ON_SELECT_POSITION,
										positionOrganizationListbox, position);
							}
						}
					}
				});

		/**
		 * 展示岗位相关属性
		 */
		this.bean.getPositionTreeExt().addForward(
				PositionConstant.ON_SELECT_POSITION,
				this.bean.getPositionEditDiv(),
				PositionConstant.ON_SELECT_POSITION_RESPONSE);

		/**
		 * 岗位信息保存事件
		 */
		this.bean.getPositionEditDiv().addForward(
				PositionConstant.ON_SAVE_POSITION_NAME, this.self,
				"onSavePositionResponse");

		/**
		 * 删除节点成功事件
		 */
		this.bean.getPositionTreeExt()
				.addForward(PositionConstant.ON_DEL_NODE_OK, this.self,
						"onDelNodeResponse");
		/**
		 * 删除节点成功事件,取消展示与岗位关联的组织信息
		 */
		this.bean.getPositionTreeExt().addForward(
				PositionConstant.ON_DEL_NODE_OK, positionOrganizationListbox,
				null);

		initPage();

	}

	/**
	 * 组织信息保存
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSavePositionResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			Position editPosition = (Position) event.getOrigin().getData();
			if (editPosition != null) {
				/**
				 * 岗位信息保存可能对岗位名称进行了修改
				 */
				Events.postEvent(PositionConstant.ON_SAVE_POSITION_NAME,
						this.bean.getPositionTreeExt(), editPosition);
			}
		}
	}

	/**
	 * 删除节点事件,属性tab清空
	 * 
	 * @throws Exception
	 */
	public void onDelNodeResponse() throws Exception {

		/**
		 * 切换左边tab页的时候，未选择岗位树上的岗位，清理数据等操作
		 */
		// Events.postEvent(PositionConstant.ON_SELECT_TREE_POSITION,
		// this.bean.getPositionEditDiv(), "positionPage");
		Events.postEvent(PositionConstant.ON_SELECT_TREE_POSITION,
				this.bean.getPositionEditDiv(), new Position());
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {

		this.bean.getPositionTreeExt().setPagePosition("positionTreePage");
		this.bean.getPositionEditDiv().setPagePosition("positionTreePage");
		if (positionOrganizationListbox != null) {
			Events.postEvent("onSetPagePosition", positionOrganizationListbox,
					"positionPage");
		}
	}

}
