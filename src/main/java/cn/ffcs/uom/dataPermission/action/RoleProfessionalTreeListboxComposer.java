package cn.ffcs.uom.dataPermission.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.action.bean.RoleProfessionalTreeListboxBean;
import cn.ffcs.uom.dataPermission.constants.RoleConstant;
import cn.ffcs.uom.dataPermission.constants.RoleProfessionalTreeConstant;
import cn.ffcs.uom.dataPermission.manager.AroleProfessionalTreeManager;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;

@Controller
@Scope("prototype")
public class RoleProfessionalTreeListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul.
	 */
	private final String zul = "/pages/dataPermission/role_professional_tree_listbox.zul";

	/**
	 * bean.
	 */
	private RoleProfessionalTreeListboxBean bean = new RoleProfessionalTreeListboxBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("aroleProfessionalTreeManager")
	private AroleProfessionalTreeManager aroleProfessionalTreeManager = (AroleProfessionalTreeManager) ApplicationContextUtil
			.getBean("aroleProfessionalTreeManager");

	/**
	 * 当前选择的organization
	 */
	private AroleProfessionalTree aroleProfessionalTree;

	/**
	 * 查询organization.
	 */
	private AroleProfessionalTree qryAroleProfessionalTree;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RoleProfessionalTreeListboxComposer() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		/**
		 * 选择角色：查询角色组织列表
		 */
		this.addForward(RoleProfessionalTreeConstant.ON_ROLE_PROFESSIONAL_TREE_QUERY,
				this, "onRoleProfessionalTreeQueryResponse");
		/**
		 * 角色列表查询事件:清理list
		 */
		this.addForward(RoleConstant.ON_ROLE_QUERY, this,"onRoleQueryResponse");
	}

	/**
	 * 创建初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false);
	}

	/**
	 * 角色列表查询响应，清空列表
	 * 
	 * @throws Exception
	 */
	public void onRoleQueryResponse() throws Exception {
		this.qryAroleProfessionalTree = null;
		PubUtil.clearListbox(this.bean.getRoleProfessionalTreeListBox());
		this.setButtonValid(false, false);
	}

	/**
	 * 选择角色列表的角色,响应查询处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRoleProfessionalTreeQueryResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin() != null && event.getOrigin().getData() != null) {
			this.qryAroleProfessionalTree = (AroleProfessionalTree) event.getOrigin()
					.getData();
			this.bean.getRoleProfessionalTreeListPaging().setActivePage(0);
			this.queryRoleProfessionalTree();
		}
	}

	/**
	 * 选择角色组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onSelectRoleProfessionalTree() throws Exception {
		if (bean.getRoleProfessionalTreeListBox().getSelectedCount() > 0) {
			aroleProfessionalTree = (AroleProfessionalTree) bean
					.getRoleProfessionalTreeListBox().getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}

	/**
	 * 新增.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRoleProfessionalTreeAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		this.openRoleProfessionalTreeEditWin("add");
	/*	if (qryAroleProfessionalTree != null
				&& qryAroleProfessionalTree.getAroleId() != null) {
			TelcomRegion telcomRegion = PermissionUtil
					.getPermissionTelcomRegion(new long[] { qryAroleProfessionalTree
							.getAroleId() });
			if (telcomRegion != null) {
				this.openRoleProfessionalTreeEditWin("add");
			} else {
				ZkUtil.showError("请先分配管理区域", "提示信息");
				return;
			}
		} else {
			ZkUtil.showError("角色id错误", "提示信息");
			return;
		}*/
	}

	/**
	 * 删除组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRoleProfessionalTreeDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),ActionKeys.DATA_OPERATING)){
			return;
		}
		String info = "";
		final List<AroleProfessionalTree> aptList = aroleProfessionalTreeManager.findProfessionalTreeAuthByNode(aroleProfessionalTree);
		if(aptList!=null&&aptList.size()>1){
			info = "删除权限时子权限也将删除，是否继续？";
		}else if(aptList!=null&&aptList.size()==1){
		    info = "你确定要删除该权限吗?";
		}else{
			ZkUtil.showError("获取权限失败！", "提示信息");
			return;
		}
		
		ZkUtil.showQuestion(info, "提示信息", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					for(AroleProfessionalTree apt :aptList){
						aroleProfessionalTreeManager.removeRoleProfessionalTree(apt);
					}
					setButtonValid(true, false);
					aroleProfessionalTree = null;
					queryRoleProfessionalTree();
				}
			}
		});
	}

	/**
	 * 打开编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openRoleProfessionalTreeEditWin(String opType) throws Exception {
		if (qryAroleProfessionalTree != null&& qryAroleProfessionalTree.getAroleId() != null) {
			Map arg = new HashMap();
			arg.put("opType", opType);
			arg.put("aroleProfessionalTree", qryAroleProfessionalTree);
			Window win = (Window) Executions.createComponents("/pages/dataPermission/role_professional_tree_edit.zul", this,arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					queryRoleProfessionalTree();
				}
			});
		}
	}

	/**
	 * 设置按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		this.bean.getAddRoleProfessionalTreeButton().setDisabled(!canAdd);
		this.bean.getDelRoleProfessionalTreeButton().setDisabled(!canDelete);
	}

	/**
	 * 查询.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryRoleProfessionalTree() throws Exception {
		aroleProfessionalTree = null;
		if (this.qryAroleProfessionalTree != null) {
			this.setButtonValid(true, false);
			ListboxUtils.clearListbox(bean.getRoleProfessionalTreeListBox());
			PageInfo pageInfo = aroleProfessionalTreeManager.queryPageInfoByRoleProfessionalTree(qryAroleProfessionalTree,this.bean.getRoleProfessionalTreeListPaging().getActivePage() + 1, this.bean.getRoleProfessionalTreeListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
			this.bean.getRoleProfessionalTreeListBox().setModel(dataList);
			this.bean.getRoleProfessionalTreeListPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRoleProfessionalTreeListBox());
		}
	}

	/**
	 * 分页
	 */
	public void onRoleProfessionalTreeListPaging() throws Exception {
		this.queryRoleProfessionalTree();
	}
}
