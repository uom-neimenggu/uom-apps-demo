package cn.ffcs.uom.publishLog.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;

import cn.ffcs.raptornuke.plugin.common.util.StringUtil;
import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.manager.OperateLogManager;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.publishLog.action.bean.PublishLogMainBean;
import cn.ffcs.uom.publishLog.manager.PublishLogManager;
import cn.ffcs.uom.publishLog.model.PublishLog;

/**
 * 发布日志查询.
 * 
 * @author faq
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class PublishLogComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private PublishLogMainBean bean = new PublishLogMainBean();

	/**
	 * 查询发布日志
	 */
	private PublishLog queryPublishLog;
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("operateLogManager")
	private OperateLogManager operateLogManager = (OperateLogManager) ApplicationContextUtil
			.getBean("operateLogManager");

	@Autowired
	@Qualifier("publishLogManager")
	private PublishLogManager publishLogManager;

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
	public void onCreate$publishLogMainWin() throws Exception {
		this.bindCombobox();
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void bindCombobox() throws Exception {
		List<NodeVo> orgTreeNameList = operateLogManager.getValuesList();
		ListboxUtils.rendererForEdit(bean.getOrgTreeNameListbox(),
				orgTreeNameList);
	}

	/**
	 * 组织树列表查询请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onQueryPublishLogRequest() throws Exception {
		if (this.bean.getOrgTreeNameListbox() != null) {
			String OrgTreeId = (String) this.bean.getOrgTreeNameListbox()
					.getSelectedItem().getValue();
			if (null != OrgTreeId && !"".equals(StringUtil.trim(OrgTreeId))) {
				queryPublishLog = PublishLog.newInstance();
				queryPublishLog.setOrgTreeId(Long.valueOf(OrgTreeId));
				this.onQueryPublishLogResponse();
			}else{
				ZkUtil.showError("请选择您要查询的组织树。", "提示信息");
			}
		}
	}

	/**
	 * 组织树列表查询响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onQueryPublishLogResponse() throws Exception {
		PageInfo pageInfo = this.publishLogManager.queryPageInfoByOrgTree(
				queryPublishLog, this.bean.getPublishLogListPaging()
						.getActivePage() + 1, this.bean
						.getPublishLogListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getPublishLogListbox().setModel(dataList);
		this.bean.getPublishLogListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 分页响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onQueryPublishLogPaging() throws Exception {
		this.onQueryPublishLogResponse();
	}
}
