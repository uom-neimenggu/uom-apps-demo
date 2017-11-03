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
import cn.ffcs.uom.rolePermission.manager.PermissionAttrSpecManager;
import cn.ffcs.uom.rolePermission.manager.PermissionAttrSpecSortManager;
import cn.ffcs.uom.rolePermission.model.PermissionAttrSpec;
import cn.ffcs.uom.rolePermission.model.PermissionAttrSpecSort;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;

public class RbacPermissionExtAttrExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 数据来源
	 */
	@SuppressWarnings("unused")
	private static final Long permissionAttrSpecId1 = 1L;

	/**
	 * 权限类型
	 */
	@SuppressWarnings("unused")
	private static final Long permissionAttrSpecId2 = 2L;

	/**
	 * 关联权限ID
	 */
	@SuppressWarnings("unused")
	private static final Long permissionAttrSpecId3 = 3L;

	/**
	 * 关联资源ID
	 */
	@SuppressWarnings("unused")
	private static final Long permissionAttrSpecId4 = 4L;

	/**
	 * 主数据权限扩展属性
	 */
	@SuppressWarnings("unused")
	private static final Long permissionAttrSpecSortId1 = 1L;

	/**
	 * 组建id前缀
	 */
	private static final String preId = "permissionAttrSpec";

	/**
	 * 组建id前缀
	 */
	private static final String groupPreId = "permissionAttrSpecSort";

	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_permission_ext_attr_ext.zul";

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
	private Groupbox rbacPermissionExtAttrGroupBox;

	@Getter
	@Setter
	private Radiogroup radiogroupBody;

	/**
	 * 属性规格种类
	 */
	List<PermissionAttrSpecSort> permissionAttrSpecSortList;
	/**
	 * manager
	 */

	private PermissionAttrSpecSortManager permissionAttrSpecSortManager = (PermissionAttrSpecSortManager) ApplicationContextUtil
			.getBean("permissionAttrSpecSortManager");

	private PermissionAttrSpecManager permissionAttrSpecManager = (PermissionAttrSpecManager) ApplicationContextUtil
			.getBean("permissionAttrSpecManager");

	public RbacPermissionExtAttrExt() {
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

		permissionAttrSpecSortList = permissionAttrSpecSortManager
				.getPermissionAttrSpecSortList();

		int perAttrNum = new Integer(this.getPerAttrNum());
		int columnNum = perAttrNum * 2;

		if (permissionAttrSpecSortList != null
				&& permissionAttrSpecSortList.size() > 0) {
			for (PermissionAttrSpecSort permissionAttrSpecSort : permissionAttrSpecSortList) {
				List<PermissionAttrSpec> permissionAttrSpecList = permissionAttrSpecSort
						.getPermissionAttrSpecList();
				if (permissionAttrSpecList != null
						&& permissionAttrSpecList.size() > 0) {
					Groupbox groupbox = new Groupbox();
					groupbox.setId(groupPreId
							+ permissionAttrSpecSort
									.getPermissionAttrSpecSortId());
					groupbox.setParent(radiogroupBody);

					Caption caption = new Caption();
					caption.setLabel(permissionAttrSpecSort
							.getPermissionSortName());
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
					int rowNum = permissionAttrSpecList.size() % perAttrNum == 0 ? permissionAttrSpecList
							.size() / perAttrNum
							: (permissionAttrSpecList.size() / perAttrNum) + 1;

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
							if (current < permissionAttrSpecList.size()) {

								label.setValue(permissionAttrSpecList.get(
										current).getPermissionAttrName());

								if (!StrUtil.isEmpty(this.getLableWidth())) {
									label.setWidth(this.getLableWidth());
								}

								label.setParent(row);

								if (permissionAttrSpecList.get(current)
										.getPermissionAttrSpecType() == OrganizationConstant.ATTR_SPEC_TYPE_INPUT) {
									/**
									 * 手工输入
									 */
									Textbox textbox = new Textbox();
									textbox.setId(preId
											+ permissionAttrSpecList.get(
													current)
													.getPermissionAttrSpecId());
									if (!StrUtil
											.isEmpty(this.getTextBoxWidth())) {
										textbox.setWidth(this.getTextBoxWidth());
									}

									if (!this.canInput(permissionAttrSpecSort)) {
										textbox.setDisabled(true);
									}

									textbox.setParent(row);

								} else if (permissionAttrSpecList.get(current)
										.getPermissionAttrSpecType() == OrganizationConstant.ATTR_SPEC_TYPE_SELECT) {
									/**
									 * 有下拉选项
									 */
									List<NodeVo> permissionAttrValueNodeVoList = permissionAttrSpecList
											.get(current)
											.getPermissionAttrValueNodeVoList();

									if (permissionAttrValueNodeVoList != null) {

										Listbox listbox = new Listbox();

										listbox.setId(preId
												+ permissionAttrSpecList
														.get(current)
														.getPermissionAttrSpecId());

										listbox.setMold("select");

										listbox.setId(preId
												+ permissionAttrSpecList
														.get(current)
														.getPermissionAttrSpecId());

										if (!StrUtil.isEmpty(this
												.getListBoxWidth())) {
											listbox.setWidth(this
													.getListBoxWidth());
										}

										ListboxUtils.rendererForEdit(listbox,
												permissionAttrValueNodeVoList);

										if (!this
												.canInput(permissionAttrSpecSort)) {
											listbox.setDisabled(true);
										}

										listbox.setParent(row);

										listbox.setId(preId
												+ permissionAttrSpecList
														.get(current)
														.getPermissionAttrSpecId());
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
	 * @param permissionAttrCd
	 * @return
	 */
	public String getExtendValue(Long permissionAttrSpecId) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + permissionAttrSpecId);
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
	public List<RbacPermissionExtAttr> getExtendValueList() {

		List<RbacPermissionExtAttr> rbacPermissionExtAttrList = new ArrayList<RbacPermissionExtAttr>();

		if (permissionAttrSpecSortList == null) {
			permissionAttrSpecSortList = permissionAttrSpecSortManager
					.getPermissionAttrSpecSortList();
		}

		if (permissionAttrSpecSortList != null) {

			for (PermissionAttrSpecSort permissionAttrSpecSort : permissionAttrSpecSortList) {

				List<PermissionAttrSpec> permissionAttrSpecList = permissionAttrSpecSort
						.getPermissionAttrSpecList();

				if (permissionAttrSpecList != null) {

					for (PermissionAttrSpec permissionAttrSpec : permissionAttrSpecList) {

						RbacPermissionExtAttr rbacPermissionExtAttr = new RbacPermissionExtAttr();

						rbacPermissionExtAttr
								.setRbacPermissionAttrSpecId(permissionAttrSpec
										.getPermissionAttrSpecId());

						rbacPermissionExtAttr.setRbacPermissionAttrValue(this
								.getExtendValue(permissionAttrSpec
										.getPermissionAttrSpecId()));

						rbacPermissionExtAttrList.add(rbacPermissionExtAttr);

					}

				}

			}

		}

		return rbacPermissionExtAttrList;
	}

	/**
	 * 设置属性值
	 * 
	 * @param permissionAttrSpecId
	 *            permissionAttrValue
	 */
	public void setExtendValue(Long permissionAttrSpecId,
			String permissionAttrValue) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + permissionAttrSpecId);
		if (comp != null) {
			if (comp instanceof Textbox) {
				((Textbox) comp).setValue(permissionAttrValue);
			} else if (comp instanceof Listbox) {
				ListboxUtils.selectByCodeValue((Listbox) comp,
						permissionAttrValue);
			}
		}
	}

	/**
	 * 设置属性值
	 * 
	 * @param list
	 */
	public void setExtendValue(RbacPermissionExtAttr rbacPermissionExtAttr) {

		Component comp = this.getRadiogroupBody().getFellow(
				preId + rbacPermissionExtAttr.getRbacPermissionAttrSpecId());

		if (comp != null) {

			if (comp instanceof Textbox) {
				((Textbox) comp).setValue(rbacPermissionExtAttr
						.getRbacPermissionAttrValue());
			} else if (comp instanceof Listbox) {
				ListboxUtils.selectByCodeValue((Listbox) comp,
						rbacPermissionExtAttr.getRbacPermissionAttrValue());
			}

		}

	}

	/**
	 * 设置属性值 20150308朱林涛 修复扩展属性展示会保留上一个组织的值的BUG
	 * 
	 * @param list
	 */
	public void setExtendValueList(List<RbacPermissionExtAttr> list) {
		if (list != null && list.size() > 0) {

			List<PermissionAttrSpec> permissionAttrSpecList = permissionAttrSpecManager
					.queryPermissionAttrSpecList();

			for (PermissionAttrSpec permissionAttrSpec : permissionAttrSpecList) {

				RbacPermissionExtAttr newRbacPermissionExtAttr = new RbacPermissionExtAttr();
				newRbacPermissionExtAttr
						.setRbacPermissionAttrSpecId(permissionAttrSpec
								.getPermissionAttrSpecId());

				for (RbacPermissionExtAttr rbacPermissionExtAttr : list) {
					if (permissionAttrSpec.getPermissionAttrSpecId() == rbacPermissionExtAttr
							.getRbacPermissionAttrSpecId()) {
						newRbacPermissionExtAttr = rbacPermissionExtAttr;
						break;
					}
				}

				this.setExtendValue(newRbacPermissionExtAttr);
			}

		} else {
			List<PermissionAttrSpec> permissionAttrSpecList = permissionAttrSpecManager
					.queryPermissionAttrSpecList();
			for (PermissionAttrSpec permissionAttrSpec : permissionAttrSpecList) {
				RbacPermissionExtAttr rbacPermissionExtAttr = new RbacPermissionExtAttr();
				rbacPermissionExtAttr
						.setRbacPermissionAttrSpecId(permissionAttrSpec
								.getPermissionAttrSpecId());
				this.setExtendValue(rbacPermissionExtAttr);
			}

		}
	}

	/**
	 * 设置属性不可修改
	 * 
	 * @param permissionAttrCd
	 */
	public void setDisable(Long permissionAttrSpecId, boolean disable) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + permissionAttrSpecId);
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
	private boolean canInput(PermissionAttrSpecSort permissionAttrSpecSort) {
		if (permissionAttrSpecSort != null) {
			if (OrganizationConstant.ATTR_SPEC_SORT_TYPE_NORMAL
					.equals(permissionAttrSpecSort.getPermissionSortType())) {
				return true;
			} else if (OrganizationConstant.ATTR_SPEC_SORT_TYPE_ABNORMAL
					.equals(permissionAttrSpecSort.getPermissionSortType())) {
				return false;
			}
		}
		return false;
	}

	/**
	 * 设置属性不可修改
	 * 
	 * @param permissionAttrCd
	 */
	public void setPartDisable(
			List<PermissionAttrSpecSort> permissionAttrSpecSortList,
			boolean disable) {
		if (permissionAttrSpecSortList != null
				&& permissionAttrSpecSortList.size() > 0) {
			for (PermissionAttrSpecSort permissionAttrSpecSort : permissionAttrSpecSortList) {
				List<PermissionAttrSpec> permissionAttrSpecList = permissionAttrSpecSort
						.getPermissionAttrSpecList();
				if (permissionAttrSpecList != null
						&& permissionAttrSpecList.size() > 0) {
					for (PermissionAttrSpec permissionAttrSpec : permissionAttrSpecList) {
						Component comp = this.getRadiogroupBody().getFellow(
								preId
										+ permissionAttrSpec
												.getPermissionAttrSpecId());
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
	 * @param permissionAttrCd
	 */
	public void setAllDisable(boolean disable) {
		if (this.permissionAttrSpecSortList != null
				&& this.permissionAttrSpecSortList.size() > 0) {
			for (PermissionAttrSpecSort permissionAttrSpecSort : this.permissionAttrSpecSortList) {
				List<PermissionAttrSpec> permissionAttrSpecList = permissionAttrSpecSort
						.getPermissionAttrSpecList();
				if (permissionAttrSpecList != null
						&& permissionAttrSpecList.size() > 0) {
					for (PermissionAttrSpec permissionAttrSpec : permissionAttrSpecList) {
						Component comp = this.getRadiogroupBody().getFellow(
								preId
										+ permissionAttrSpec
												.getPermissionAttrSpecId());
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
	public void setVisible(Long permissionAttrSpecSortId, boolean visible) {
		Component comp = this.getRadiogroupBody().getFellow(
				groupPreId + permissionAttrSpecSortId);
		if (comp != null) {
			if (comp instanceof Groupbox) {
				((Groupbox) comp).setVisible(visible);
			}
		}
	}
}