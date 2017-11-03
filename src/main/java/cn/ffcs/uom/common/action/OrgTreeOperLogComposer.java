package cn.ffcs.uom.common.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.action.bean.OrgTreeOperLogMainBean;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.OperateLogConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.manager.OperateLogManager;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.DbUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.util.ServerConfigUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.ftpsyncfile.manager.BuildFileSqlManager;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgRelaManager;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgStaffRelaManager;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.publishLog.manager.PublishLogManager;
import cn.ffcs.uom.publishLog.model.PublishLog;
import cn.ffcs.uom.webservices.util.WsClientUtil;

/**
 * 日志操作查询.
 * 
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class OrgTreeOperLogComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * bean.
	 */
	private OrgTreeOperLogMainBean bean = new OrgTreeOperLogMainBean();
	/**
	 * 消息
	 */
	private String msg;
	/**
	 * 选中的组织树
	 */
	@SuppressWarnings("unused")
	private OrgTree orgTree;
	/**
	 * 查询组织树
	 */

	private OrgTree queryOrgTree;
	/**
	 * 选中的操作日志
	 */
	private OperateLog operateLog;
	/**
	 * 查询操作日志
	 */
	private OperateLog queryOperateLog;
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

	@Resource
	private OrgTreeManager orgTreeManager;

	@Resource
	private BuildFileSqlManager buildFileSqlManager;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	@Resource
	private TreeOrgStaffRelaManager treeOrgStaffRelaManager;

	@Resource
	private TreeOrgRelaManager treeOrgRelaManager;

	/**
	 * jdbcTemplate
	 */
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.setPortletInfoProvider(this);
		this.bindCombobox();
		this.setOperateLogButtonValid(false, false);
		initPage();
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
	 * 操作日志选中请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectOperateLogRequest() throws Exception {
		if (this.bean.getOrgTreeOperLogListbox().getSelectedIndex() != -1) {
			operateLog = (OperateLog) bean.getOrgTreeOperLogListbox()
					.getSelectedItem().getValue();
			this.setOperateLogButtonValid(true, true);
		}
	}

	/**
	 * 组织树列表查询请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onQueryOperateLogRequest() throws Exception {
		queryOrgTree = OrgTree.newInstance();
		if (this.bean.getOrgTreeNameListbox() != null
				&& this.bean.getOrgTreeNameListbox().getSelectedItem()
						.getValue() != null) {
			queryOrgTree.setOrgTreeId(Long.valueOf((String) this.bean
					.getOrgTreeNameListbox().getSelectedItem().getValue()));
			queryOrgTree = (OrgTree) OrgTree.repository().getObject(
					OrgTree.class, queryOrgTree.getOrgTreeId());
			queryOperateLog = OperateLog.newInstance();
			queryOperateLog.setCreateDate(queryOrgTree.getLastTime());
			this.onQueryOperateLogResponse();
		}
	}

	/**
	 * 组织树列表查询响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unused")
	public void onQueryOperateLogResponse() throws Exception {
		PageInfo pageInfo = this.operateLogManager.queryPageInfoByOrgTree(
				queryOperateLog, this.bean.getOrganizationListPaging()
						.getActivePage() + 1, this.bean
						.getOrganizationListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOrgTreeOperLogListbox().setModel(dataList);
		this.bean.getOrganizationListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));

		this.setOperateLogButtonValid(false, false);
	}

	/**
	 * 组织树列表查询响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unused")
	public void onQueryOperateLogPaging() throws Exception {
		this.onQueryOperateLogResponse();
	}

	/**
	 * 打开查看窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	public void onViewOperateLogRequest() throws Exception {
		Map arg = new HashMap();

		if (operateLog != null) {

			arg.put("operateLog", operateLog);

			if (operateLog.getOpeType().trim()
					.equals(BaseUnitConstants.OPE_TYPE_UPDATE)) {

				Window win = (Window) Executions.createComponents(
						"/pages/common/operateLog/operateLog_view.zul",
						this.self, arg);
				win.doModal();

			} else {

				Window win = (Window) Executions.createComponents(
						"/pages/common/operateLog/operateLog_add_del.zul",
						this.self, arg);
				win.doModal();

			}
		}
	}

	/**
	 * 操作日志发布请求事件.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPublishOperateLogRequest() throws Exception {

		ZkUtil.showQuestion("确定要发布吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (operateLog == null
							|| operateLog.getOperateLogId() == null) {
						ZkUtil.showError("请选择你要发布的记录", "提示信息");
						return;
					} else {
						OrgTree currentOrgTree = null;
						try {
							/**
							 * 锁住树：只能同时一个在发布
							 */
							currentOrgTree = orgTreeManager
									.getOrgTreeByOrgTreeId(queryOrgTree
											.getOrgTreeId());
							if (currentOrgTree == null) {
								ZkUtil.showError("树状态错误", "发布失败信息");
								return;
							}
							/**
							 * 发布时间必须小于要发布的时间
							 */
							if (null != currentOrgTree.getLastTime()
									&& null != operateLog.getCreateDate()) {
								int r = currentOrgTree.getLastTime().compareTo(
										operateLog.getCreateDate());
								if (r >= 0) {
									ZkUtil.showError("该记录已发布，请重新查询", "发布失败信息");
									return;
								}
							} else {
								ZkUtil.showError("树上次发布时间或者日志记录时间错误无法发布",
										"发布失败信息");
								return;
							}
							// 已经发布中
							if (OperateLogConstants.IS_PUBLISHING
									.equals(currentOrgTree.getIsPublishing())) {
								ZkUtil.showError("该树正在发布中,请稍后再试", "发布失败信息");
								return;
							}
							// 锁定
							currentOrgTree
									.setIsPublishing(OperateLogConstants.IS_PUBLISHING);
							orgTreeManager.updateOrgTree(currentOrgTree);
							String callUrl = UomClassProvider
									.getIntfUrl("localIncrementalDataUrl");
							if (StrUtil.isEmpty(callUrl)) {
								ZkUtil.showError(
										"发布失败,地址localIncrementalDataUrl未配置",
										"发布失败信息");
								// 解除锁定
								currentOrgTree
										.setIsPublishing(OperateLogConstants.NOT_PUBLISHING);
								orgTreeManager.updateOrgTree(currentOrgTree);
								return;
							}
							logger.info("1->"+DateUtil.getDateByDateFormat(
									"yyyy-MM-dd HH:mm:ss", new Date()));
							
							List<Object> paramsList = new ArrayList<Object>();
							paramsList.add(DateUtil.getDateByDateFormat("yyyyMMddHHmmss", queryOrgTree.getLastTime()));
							paramsList.add(DateUtil.getDateByDateFormat("yyyyMMddHHmmss", operateLog.getCreateDate()));
							paramsList.add(queryOrgTree.getOrgTreeId());
							DbUtil.procedureCall(jdbcTemplate, "{call PKG_OPERATELOG_PUBLISH.updateTreeOrgDate(?,?,?)}", paramsList);
							/*treeOrgRelaManager.updateTreeOrgRelaData(
									queryOrgTree.getOrgTreeId(), operateLog
											.getCreateDate());*/
//							treeOrgRelaManager.updateTreeOrgRelaData(
//									queryOrgTree.getOrgTreeId(), queryOrgTree
//											.getLastTime(), operateLog
//											.getCreateDate());
//							logger.info("2->"+DateUtil.getDateByDateFormat(
//								"yyyy-MM-dd HH:mm:ss", new Date()));
							/*	treeOrgStaffRelaManager.updateTreeOrgStaffRelaData(
									queryOrgTree.getOrgTreeId(), operateLog
											.getCreateDate());*/
//							treeOrgStaffRelaManager.updateTreeOrgStaffRelaData(
//									queryOrgTree.getOrgTreeId(), queryOrgTree
//											.getLastTime(), operateLog
//											.getCreateDate());
							logger.info("2->"+DateUtil.getDateByDateFormat(
									"yyyy-MM-dd HH:mm:ss", new Date()));
							String outXml = WsClientUtil
									.wsCallCreateIncrementalData(callUrl,
											"createIncrementalData",
											queryOrgTree.getOrgTreeId(),
											queryOrgTree.getLastTime(),
											operateLog.getCreateDate(), "0");
							logger.info("3->"+DateUtil.getDateByDateFormat(
									"yyyy-MM-dd HH:mm:ss", new Date()));
							if (!StrUtil.isEmpty(outXml)) {
								ZkUtil.showError("生成FTP文件失败：" + outXml,
										"发布失败信息");
								// 解除锁定（不能在finally中处理）
								currentOrgTree
										.setIsPublishing(OperateLogConstants.NOT_PUBLISHING);
								orgTreeManager.updateOrgTree(currentOrgTree);
								return;
							}
							ZkUtil.showInformation("发布成功!", "发布成功信息");
							/**
							 * 组织树时间信息变更
							 */
							currentOrgTree.setPreTime(queryOrgTree
									.getLastTime());
							currentOrgTree.setLastTime(operateLog
									.getCreateDate());
							orgTreeManager.updateOrgTree(currentOrgTree);
							onQueryOperateLogRequest();

							/**
							 * 记录发布日志 add by:faq 2013-7-18
							 */
							PublishLog publishLog = PublishLog.newInstance();
							publishLog
									.setOrgTreeId(queryOrgTree.getOrgTreeId());
							publishLog.setPublishDate(new Date());
							publishLog.setGenDate(queryOrgTree.getLastTime());
							publishLog
									.setLastGenDate(queryOrgTree.getPreTime());
							publishLogManager.addPublishLog(publishLog);
							// 解除锁定
							currentOrgTree
									.setIsPublishing(OperateLogConstants.NOT_PUBLISHING);
							orgTreeManager.updateOrgTree(currentOrgTree);
						} catch (Exception e) {
							e.printStackTrace();
							ZkUtil.showError("发布失败,系统异常:" + e.getMessage(),
									"发布失败信息");
							currentOrgTree
									.setIsPublishing(OperateLogConstants.NOT_PUBLISHING);
							orgTreeManager.updateOrgTree(currentOrgTree);
						}
					}
				}
			}
		});

	}

	/**
	 * 发布日志请求事件.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPublishLogRequest() throws Exception {
		Window win = (Window) Executions.createComponents(
				"/pages/publishLog/publishLog_main.zul", this.self, arg);
		win.doModal();
	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canView
	 *            查看按钮
	 * @param canPublish
	 *            发岸布按钮
	 */
	public void setOperateLogButtonValid(final Boolean canView,
			final Boolean canPublish) {
		if (canView != null) {
			this.bean.getViewOrgTreeOperLogButton().setDisabled(!canView);
		}
		this.bean.getPublishOrgTreeOperLogButton().setDisabled(!canPublish);
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.setPagePosition("operateLogPage");
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canView = false;
		boolean canPublish = false;
		boolean canPublishLog = false;

		if (PlatformUtil.isAdmin()) {
			canView = true;
			canPublish = true;
			canPublishLog = true;
		} else if ("operateLogPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.OPERLOG_VIEW)) {
				canView = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.OPERLOG_PUBLISH)) {
				canPublish = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.OPERLOG_PUBLISHLOG)) {
				canPublishLog = true;
			}
		}
		
		// 当前server下的COMPONENT_SHOW用户变量未配置或者不为true
		if(!ServerConfigUtil.isComponentShow()) {
			canView = false;
			canPublish = false;
			canPublishLog = false;
		}
		this.bean.getViewOrgTreeOperLogButton().setVisible(canView);
		this.bean.getPublishOrgTreeOperLogButton().setVisible(canPublish);
		this.bean.getPublishOperLogButton().setVisible(canPublishLog);
	}
}
