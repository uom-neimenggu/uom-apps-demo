package cn.ffcs.uom.group.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.group.component.bean.GroupUomOrgEditBean;
import cn.ffcs.uom.group.manager.GroupUomManager;
import cn.ffcs.uom.group.model.GroupUomOrg;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;

/**
 * 组织关系编辑.
 * 
 * @author 朱林涛
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class GroupUomOrgEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private GroupUomOrgEditBean bean = new GroupUomOrgEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("groupUomManager")
	private GroupUomManager groupUomManager;

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager;

	/**
	 * 修改的组织关系
	 */
	private GroupUomOrg oldGroupUomOrg;

	/**
	 * 修改之前的关联关系
	 */
	private String oldResType;

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
	public void onCreate$groupUomOrgEditWindow() throws Exception {
		this.bindCombobox();
		this.bindBean();
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> resTypeList = UomClassProvider.getValuesList(
				"SyncGroupPrv", "resType");
		ListboxUtils.rendererForEdit(this.bean.getResType(), resTypeList);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {

		opType = StrUtil.strnull(arg.get("opType"));

		oldGroupUomOrg = (GroupUomOrg) arg.get("groupUomOrg");

		if ("add".equals(opType)) {

			this.bean.getGroupUomOrgEditWindow().setTitle("组织关系新增");

		} else if ("mod".equals(opType)) {

			this.bean.getGroupUomOrgEditWindow().setTitle("组织关系修改");

			if (oldGroupUomOrg != null) {

				oldResType = oldGroupUomOrg.getResType();

				PubUtil.fillBeanFromPo(oldGroupUomOrg, this.bean);

			}
		}

		if (oldGroupUomOrg != null && oldGroupUomOrg.getOrgId() != null) {
			Organization org = organizationManager.getById(oldGroupUomOrg
					.getOrgId());
			bean.getOrg().setOrganization(org);
		}
	}

	/**
	 * 保存.
	 */
	@SuppressWarnings("unchecked")
	public void onOk() throws Exception {

		GroupUomOrg groupUomOrg = null;

		if ("add".equals(opType)) {

			groupUomOrg = new GroupUomOrg();

		} else if ("mod".equals(opType)) {
			groupUomOrg = new GroupUomOrg();
		}
		BeanUtils.copyProperties(groupUomOrg, oldGroupUomOrg);
		// 填充对象
		PubUtil.fillPoFromBean(bean, groupUomOrg);

		Organization org = bean.getOrg().getOrganization();

		if (org != null) {
			groupUomOrg.setOrgId(org.getOrgId());
		}

		String msg = this.doValidate(groupUomOrg);

		if (!StrUtil.isNullOrEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		if ("add".equals(opType)) {
			/**
			 * 集团组织和主数据同一个组织，只能有一种关系
			 */

			GroupUomOrg queryGroupUomOrg = new GroupUomOrg();
			queryGroupUomOrg.setId(groupUomOrg.getId());
			queryGroupUomOrg.setOrgId(groupUomOrg.getOrgId());

			List<GroupUomOrg> existList = groupUomManager
					.queryGroupUomOrgList(queryGroupUomOrg);

			if (existList != null && existList.size() > 0) {
				ZkUtil.showError("该组织已存在关系,不能重复添加", "错误信息");
				return;
			}

			queryGroupUomOrg = new GroupUomOrg();
			queryGroupUomOrg.setId(groupUomOrg.getId());
			queryGroupUomOrg.setResType(groupUomOrg.getResType());

			List<GroupUomOrg> existResTypeList = groupUomManager
					.queryGroupUomOrgList(queryGroupUomOrg);

			if (existResTypeList != null && existResTypeList.size() > 0) {

				boolean exitSign = false;

				for (GroupUomOrg newGroupUomOrg : existResTypeList) {

					if (groupUomOrg.getOrgId()
							.equals(newGroupUomOrg.getOrgId())) {
						exitSign = true;
					}

				}
				if (exitSign) {
					ZkUtil.showError("该组织已存在此种关系类型,不能重复添加", "错误信息");
					return;
				}

			}

			groupUomOrg.setDataType("1");// 数据类型：1 组织 2 员工
			this.groupUomManager.addGroupUomOrg(groupUomOrg);
			Events.postEvent(Events.ON_OK, bean.getGroupUomOrgEditWindow(),
					groupUomOrg);

			bean.getGroupUomOrgEditWindow().onClose();

		} else if ("mod".equals(opType)) {

			if (groupUomOrg.getOrgId().equals(oldGroupUomOrg.getOrgId())) {

				GroupUomOrg queryGroupUomOrg = new GroupUomOrg();
				queryGroupUomOrg.setId(groupUomOrg.getId());
				queryGroupUomOrg.setResType(groupUomOrg.getResType());

				List<GroupUomOrg> existList = groupUomManager
						.queryGroupUomOrgList(queryGroupUomOrg);

				if (existList != null && existList.size() > 0) {

					boolean exitSign = false;

					for (GroupUomOrg newGroupUomOrg : existList) {

						if (groupUomOrg.getOrgId().equals(
								newGroupUomOrg.getOrgId())) {
							exitSign = true;
						}

					}
					if (exitSign) {
						ZkUtil.showError("该组织已存在此种关系类型,不能重复添加", "错误信息");
						return;
					}

				}

				this.groupUomManager.updateGroupUomOrg(groupUomOrg);
				Events.postEvent(Events.ON_OK, bean.getGroupUomOrgEditWindow(),
						groupUomOrg);

				bean.getGroupUomOrgEditWindow().onClose();

			} else {

				/**
				 * 集团组织和主数据同一个组织，只能有一种关系
				 */

				GroupUomOrg queryGroupUomOrg = new GroupUomOrg();
				queryGroupUomOrg.setId(groupUomOrg.getId());
				queryGroupUomOrg.setOrgId(groupUomOrg.getOrgId());

				List<GroupUomOrg> existList = groupUomManager
						.queryGroupUomOrgList(queryGroupUomOrg);

				if (existList != null && existList.size() > 0) {
					ZkUtil.showError("该组织已存在关系,不能重复添加", "错误信息");
					return;
				}

				/**
				 * 集团组织和主数据组织，只能有一种关系类型
				 */
				queryGroupUomOrg = new GroupUomOrg();
				queryGroupUomOrg.setId(groupUomOrg.getId());
				queryGroupUomOrg.setResType(groupUomOrg.getResType());

				List<GroupUomOrg> existResTypeList = groupUomManager
						.queryGroupUomOrgList(queryGroupUomOrg);

				if (existResTypeList != null && existResTypeList.size() > 0) {

					boolean exitSign = false;

					for (GroupUomOrg newGroupUomOrg : existResTypeList) {

						if (groupUomOrg.getOrgId().equals(
								newGroupUomOrg.getOrgId())) {
							exitSign = true;
						}

					}
					if (exitSign) {
						ZkUtil.showError("该组织已存在此种关系类型,不能重复添加", "错误信息");
						return;
					}

				}

				this.groupUomManager.updateGroupUomOrg(groupUomOrg);
				Events.postEvent(Events.ON_OK, bean.getGroupUomOrgEditWindow(),
						groupUomOrg);

				bean.getGroupUomOrgEditWindow().onClose();

			}
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getGroupUomOrgEditWindow().onClose();
	}

	/**
	 * 验证数据
	 * 
	 * @param GroupUomOrg
	 * @return
	 */
	private String doValidate(GroupUomOrg GroupUomOrg) {
		if (GroupUomOrg.getOrgId() == null) {
			return "组织不能为空！";
		}

		if (GroupUomOrg.getResType() == null) {
			return "关系类型不能为空！";
		}
		return null;
	}
}
