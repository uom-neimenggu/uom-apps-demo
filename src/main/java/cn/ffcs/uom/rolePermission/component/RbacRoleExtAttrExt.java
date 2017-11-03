package cn.ffcs.uom.rolePermission.component;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.rolePermission.manager.RoleAttrSpecManager;
import cn.ffcs.uom.rolePermission.manager.RoleAttrSpecSortManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;
import cn.ffcs.uom.rolePermission.model.RoleAttrSpec;
import cn.ffcs.uom.rolePermission.model.RoleAttrSpecSort;

public class RbacRoleExtAttrExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 数据来源
	 */
	@SuppressWarnings("unused")
	private static final Long roleAttrSpecId1 = 1L;

	/**
	 * 关联角色ID
	 */
	@SuppressWarnings("unused")
	private static final Long roleAttrSpecId2 = 2L;

	/**
	 * 主数据角色扩展属性
	 */
	@SuppressWarnings("unused")
	private static final Long roleAttrSpecSortId1 = 1L;

	/**
	 * 组建id前缀
	 */
	private static final String preId = "roleAttrSpec";

	/**
	 * 组建id前缀
	 */
	private static final String groupPreId = "roleAttrSpecSort";

	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_role_ext_attr_ext.zul";

	/**
	 * 每行显示几个属性个数
	 */
	@Getter
	@Setter
	private String perAttrNum = "2";

	/**
	 * 每行textBox的长度
	 */
	@Getter
	@Setter
	private String textBoxWidth = "98%";

	/**
	 * 每行listBox的长度
	 */
	@Getter
	@Setter
	private String listBoxWidth = "98%";

	/**
	 * 每行bandBox的长度
	 */
	@Getter
	@Setter
	private String bandBoxWidth = "98%";

	/**
	 * 每行listBox的长度
	 */
	@Getter
	@Setter
	private String lableWidth = "100%";

	@Getter
	@Setter
	private Groupbox rbacRoleExtAttrGroupBox;

	@Getter
	@Setter
	private Radiogroup radiogroupBody;

	/**
	 * 属性规格种类
	 */
	List<RoleAttrSpecSort> roleAttrSpecSortList;
	/**
	 * manager
	 */

	private RoleAttrSpecSortManager roleAttrSpecSortManager = (RoleAttrSpecSortManager) ApplicationContextUtil
			.getBean("roleAttrSpecSortManager");

	private RoleAttrSpecManager roleAttrSpecManager = (RoleAttrSpecManager) ApplicationContextUtil
			.getBean("roleAttrSpecManager");

	public RbacRoleExtAttrExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
	}

	public void onCreate() {

		this.initExt();

	}

	/**
	 * 初始化控件
	 */
	private void initExt() {

		Radiogroup radiogroupBody = this.getRadiogroupBody();

		roleAttrSpecSortList = roleAttrSpecSortManager
				.getRoleAttrSpecSortList();

		int perAttrNum = new Integer(this.getPerAttrNum());
		int columnNum = perAttrNum * 2;

		if (roleAttrSpecSortList != null && roleAttrSpecSortList.size() > 0) {
			for (RoleAttrSpecSort roleAttrSpecSort : roleAttrSpecSortList) {
				List<RoleAttrSpec> roleAttrSpecList = roleAttrSpecSort
						.getRoleAttrSpecList();
				if (roleAttrSpecList != null && roleAttrSpecList.size() > 0) {
					Groupbox groupbox = new Groupbox();
					groupbox.setId(groupPreId
							+ roleAttrSpecSort.getRoleAttrSpecSortId());
					groupbox.setParent(radiogroupBody);

					Caption caption = new Caption();
					caption.setLabel(roleAttrSpecSort.getRoleSortName());
					caption.setParent(groupbox);

					Radiogroup radiogroup = new Radiogroup();
					radiogroup.setParent(groupbox);

					Grid grid = new Grid();
					Columns columns = new Columns();
					columns.setParent(grid);

					grid.setParent(radiogroup);
					for (int i = 0; i < columnNum; i++) {
						Column column = new Column();

						if (i % 2 == 0) {
							column.setWidth("15%");
							column.setAlign("center");
						} else {
							if (i == 1) {
								column.setWidth("30%");
							} else {
								column.setWidth("40%");
							}
						}

						column.setParent(columns);
					}

					/**
					 * 属性行数
					 */
					int rowNum = roleAttrSpecList.size() % perAttrNum == 0 ? roleAttrSpecList
							.size() / perAttrNum
							: (roleAttrSpecList.size() / perAttrNum) + 1;

					Rows rows = new Rows();
					rows.setParent(grid);
					/**
					 * 当前行
					 */
					int current = 0;
					for (int i = 0; i < rowNum; i++) {
						Row row = new Row();
						row.setParent(rows);
						for (int j = 0; j < perAttrNum; j++) {
							Label label = new Label();
							if (current < roleAttrSpecList.size()) {

								label.setValue(roleAttrSpecList.get(current)
										.getRoleAttrName());

								if (!StrUtil.isEmpty(this.getLableWidth())) {
									label.setWidth(this.getLableWidth());
								}

								label.setParent(row);

								if (roleAttrSpecList.get(current)
										.getRoleAttrSpecType() == OrganizationConstant.ATTR_SPEC_TYPE_INPUT) {
									/**
									 * 手工输入
									 */
									Textbox textbox = new Textbox();
									textbox.setId(preId
											+ roleAttrSpecList.get(current)
													.getRoleAttrSpecId());
									if (!StrUtil
											.isEmpty(this.getTextBoxWidth())) {
										textbox.setWidth(this.getTextBoxWidth());
									}

									if (!this.canInput(roleAttrSpecSort)) {
										textbox.setDisabled(true);
									}

									textbox.setParent(row);

								} else if (roleAttrSpecList.get(current)
										.getRoleAttrSpecType() == OrganizationConstant.ATTR_SPEC_TYPE_SELECT) {
									/**
									 * 有下拉选项
									 */
									List<NodeVo> roleAttrValueNodeVoList = roleAttrSpecList
											.get(current)
											.getRoleAttrValueNodeVoList();

									if (roleAttrValueNodeVoList != null) {

										Listbox listbox = new Listbox();

										listbox.setId(preId
												+ roleAttrSpecList.get(current)
														.getRoleAttrSpecId());

										listbox.setMold("select");

										listbox.setId(preId
												+ roleAttrSpecList.get(current)
														.getRoleAttrSpecId());

										if (!StrUtil.isEmpty(this
												.getListBoxWidth())) {
											listbox.setWidth(this
													.getListBoxWidth());
										}

										ListboxUtils.rendererForEdit(listbox,
												roleAttrValueNodeVoList);

										if (!this.canInput(roleAttrSpecSort)) {
											listbox.setDisabled(true);
										}

										listbox.setParent(row);

										listbox.setId(preId
												+ roleAttrSpecList.get(current)
														.getRoleAttrSpecId());
									}
								}
							}
							current++;
						}
					}

				}
			}
		}
	}

	/**
	 * 获取扩展属性值
	 * 
	 * @param roleAttrCd
	 * @return
	 */
	public String getExtendValue(Long roleAttrSpecId) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + roleAttrSpecId);
		if (comp != null) {
			if (comp instanceof Textbox) {
				return ((Textbox) comp).getValue();
			} else if (comp instanceof Listbox) {
				return (String) ((Listbox) comp).getSelectedItem().getValue();
			}
		}
		return "";
	}

	/**
	 * 获取属性值列表
	 * 
	 * @param list
	 */
	public List<RbacRoleExtAttr> getExtendValueList() {

		List<RbacRoleExtAttr> rbacRoleExtAttrList = new ArrayList<RbacRoleExtAttr>();

		if (roleAttrSpecSortList == null) {
			roleAttrSpecSortList = roleAttrSpecSortManager
					.getRoleAttrSpecSortList();
		}

		if (roleAttrSpecSortList != null) {

			for (RoleAttrSpecSort roleAttrSpecSort : roleAttrSpecSortList) {

				List<RoleAttrSpec> roleAttrSpecList = roleAttrSpecSort
						.getRoleAttrSpecList();

				if (roleAttrSpecList != null) {

					for (RoleAttrSpec roleAttrSpec : roleAttrSpecList) {

						RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

						rbacRoleExtAttr.setRbacRoleAttrSpecId(roleAttrSpec
								.getRoleAttrSpecId());

						rbacRoleExtAttr.setRbacRoleAttrValue(this
								.getExtendValue(roleAttrSpec
										.getRoleAttrSpecId()));

						rbacRoleExtAttrList.add(rbacRoleExtAttr);

					}

				}

			}

		}

		return rbacRoleExtAttrList;
	}

	/**
	 * 设置属性值
	 * 
	 * @param roleAttrSpecId
	 *            roleAttrValue
	 */
	public void setExtendValue(Long roleAttrSpecId, String roleAttrValue) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + roleAttrSpecId);
		if (comp != null) {
			if (comp instanceof Textbox) {
				((Textbox) comp).setValue(roleAttrValue);
			} else if (comp instanceof Listbox) {
				ListboxUtils.selectByCodeValue((Listbox) comp, roleAttrValue);
			}
		}
	}

	/**
	 * 设置属性值
	 * 
	 * @param list
	 */
	public void setExtendValue(RbacRoleExtAttr rbacRoleExtAttr) {

		Component comp = this.getRadiogroupBody().getFellow(
				preId + rbacRoleExtAttr.getRbacRoleAttrSpecId());

		if (comp != null) {

			if (comp instanceof Textbox) {
				((Textbox) comp).setValue(rbacRoleExtAttr
						.getRbacRoleAttrValue());
			} else if (comp instanceof Listbox) {
				ListboxUtils.selectByCodeValue((Listbox) comp,
						rbacRoleExtAttr.getRbacRoleAttrValue());
			}

		}

	}

	/**
	 * 设置属性值 20150308朱林涛 修复扩展属性展示会保留上一个组织的值的BUG
	 * 
	 * @param list
	 */
	public void setExtendValueList(List<RbacRoleExtAttr> list) {
		if (list != null && list.size() > 0) {

			List<RoleAttrSpec> roleAttrSpecList = roleAttrSpecManager
					.queryRoleAttrSpecList();

			for (RoleAttrSpec roleAttrSpec : roleAttrSpecList) {

				RbacRoleExtAttr newRbacRoleExtAttr = new RbacRoleExtAttr();
				newRbacRoleExtAttr.setRbacRoleAttrSpecId(roleAttrSpec
						.getRoleAttrSpecId());

				for (RbacRoleExtAttr rbacRoleExtAttr : list) {
					if (roleAttrSpec.getRoleAttrSpecId() == rbacRoleExtAttr
							.getRbacRoleAttrSpecId()) {
						newRbacRoleExtAttr = rbacRoleExtAttr;
						break;
					}
				}

				this.setExtendValue(newRbacRoleExtAttr);
			}

		} else {
			List<RoleAttrSpec> roleAttrSpecList = roleAttrSpecManager
					.queryRoleAttrSpecList();
			for (RoleAttrSpec roleAttrSpec : roleAttrSpecList) {
				RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();
				rbacRoleExtAttr.setRbacRoleAttrSpecId(roleAttrSpec
						.getRoleAttrSpecId());
				this.setExtendValue(rbacRoleExtAttr);
			}

		}
	}

	/**
	 * 设置属性不可修改
	 * 
	 * @param roleAttrCd
	 */
	public void setDisable(Long roleAttrSpecId, boolean disable) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + roleAttrSpecId);
		if (comp != null) {
			if (comp instanceof Textbox) {
				((Textbox) comp).setDisabled(disable);
			} else if (comp instanceof Listbox) {
				((Listbox) comp).setDisabled(disable);
			}
		}
	}

	/**
	 * 判断规格是否可以输入
	 * 
	 * @param attrSpecSortId
	 * @return
	 */
	private boolean canInput(RoleAttrSpecSort roleAttrSpecSort) {
		if (roleAttrSpecSort != null) {
			if (OrganizationConstant.ATTR_SPEC_SORT_TYPE_NORMAL
					.equals(roleAttrSpecSort.getRoleSortType())) {
				return true;
			} else if (OrganizationConstant.ATTR_SPEC_SORT_TYPE_ABNORMAL
					.equals(roleAttrSpecSort.getRoleSortType())) {
				return false;
			}
		}
		return false;
	}

	/**
	 * 设置属性不可修改
	 * 
	 * @param roleAttrCd
	 */
	public void setPartDisable(List<RoleAttrSpecSort> roleAttrSpecSortList,
			boolean disable) {
		if (roleAttrSpecSortList != null && roleAttrSpecSortList.size() > 0) {
			for (RoleAttrSpecSort roleAttrSpecSort : roleAttrSpecSortList) {
				List<RoleAttrSpec> roleAttrSpecList = roleAttrSpecSort
						.getRoleAttrSpecList();
				if (roleAttrSpecList != null && roleAttrSpecList.size() > 0) {
					for (RoleAttrSpec roleAttrSpec : roleAttrSpecList) {
						Component comp = this.getRadiogroupBody().getFellow(
								preId + roleAttrSpec.getRoleAttrSpecId());
						if (comp != null) {
							if (comp instanceof Textbox) {
								((Textbox) comp).setDisabled(disable);
							} else if (comp instanceof Listbox) {
								((Listbox) comp).setDisabled(disable);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 设置属性不可修改
	 * 
	 * @param roleAttrCd
	 */
	public void setAllDisable(boolean disable) {
		if (this.roleAttrSpecSortList != null
				&& this.roleAttrSpecSortList.size() > 0) {
			for (RoleAttrSpecSort roleAttrSpecSort : this.roleAttrSpecSortList) {
				List<RoleAttrSpec> roleAttrSpecList = roleAttrSpecSort
						.getRoleAttrSpecList();
				if (roleAttrSpecList != null && roleAttrSpecList.size() > 0) {
					for (RoleAttrSpec roleAttrSpec : roleAttrSpecList) {
						Component comp = this.getRadiogroupBody().getFellow(
								preId + roleAttrSpec.getRoleAttrSpecId());
						if (comp != null) {
							if (comp instanceof Textbox) {
								((Textbox) comp).setDisabled(disable);
							} else if (comp instanceof Listbox) {
								((Listbox) comp).setDisabled(disable);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 设置Groupbox属性是否可见
	 * 
	 */
	public void setVisible(Long roleAttrSpecSortId, boolean visible) {
		Component comp = this.getRadiogroupBody().getFellow(
				groupPreId + roleAttrSpecSortId);
		if (comp != null) {
			if (comp instanceof Groupbox) {
				((Groupbox) comp).setVisible(visible);
			}
		}
	}
}