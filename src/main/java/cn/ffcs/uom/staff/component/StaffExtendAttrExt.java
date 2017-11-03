package cn.ffcs.uom.staff.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffAttrSpecSortManager;
import cn.ffcs.uom.staff.manager.TreeOrgStaffExtendAttrManager;
import cn.ffcs.uom.staff.manager.TreeStaffExtendAttrManager;
import cn.ffcs.uom.staff.model.StaffAttrSpec;
import cn.ffcs.uom.staff.model.StaffAttrSpecSort;
import cn.ffcs.uom.staff.model.StaffExtendAttr;

@SuppressWarnings({ "unchecked" })
public class StaffExtendAttrExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5490875090383048724L;
	/**
	 * 组建id前缀
	 */
	private static final String preId = "staffAttrSpec";
	/**
	 * zul.
	 */
	private final String zul = "/pages/staff/comp/staff_extend_attr_ext.zul";

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
	 * 每行longBox的长度
	 */
	@Getter
	@Setter
	private String longBoxWidth = "98%";

	/**
	 * 每行listBox的长度
	 */
	@Getter
	@Setter
	private String listBoxWidth = "98%";

	/**
	 * 每行listBox的长度
	 */
	@Getter
	@Setter
	private String lableWidth = "100%";
	@Getter
	@Setter
	private Groupbox staffExtendAttrGroupBox;
	@Getter
	@Setter
	private Radiogroup radiogroupBody;
	/**
	 * 属性规格种类
	 */
	List<StaffAttrSpecSort> staffAttrSpecSortList;
	/**
	 * manager
	 */
	private StaffAttrSpecSortManager staffAttrSpecSortManager = (StaffAttrSpecSortManager) ApplicationContextUtil
			.getBean("staffAttrSpecSortManager");

	private TreeStaffExtendAttrManager treeStaffExtendAttrManager = (TreeStaffExtendAttrManager) ApplicationContextUtil
			.getBean("treeStaffExtendAttrManager");

	private TreeOrgStaffExtendAttrManager treeOrgStaffExtendAttrManager = (TreeOrgStaffExtendAttrManager) ApplicationContextUtil
			.getBean("treeOrgStaffExtendAttrManager");

	/**
	 * manager
	 */

	public StaffExtendAttrExt() {
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
		staffAttrSpecSortList = staffAttrSpecSortManager
				.getStaffAttrSpecSortList();

		int perAttrNum = new Integer(this.perAttrNum);
		int columnNum = perAttrNum * 2;

		if (staffAttrSpecSortList != null && staffAttrSpecSortList.size() > 0) {
			for (StaffAttrSpecSort staffAttrSpecSort : staffAttrSpecSortList) {
				List<StaffAttrSpec> staffAttrSpecList = staffAttrSpecSort
						.getStaffAttrSpecList();
				if (staffAttrSpecList != null && staffAttrSpecList.size() > 0) {
					Groupbox groupbox = new Groupbox();
					groupbox.setParent(radiogroupBody);

					Caption caption = new Caption();
					caption.setLabel(staffAttrSpecSort.getSortName());
					caption.setParent(groupbox);

					Radiogroup radiogroup = new Radiogroup();
					radiogroup.setParent(groupbox);

					Grid grid = new Grid();
					Columns columns = new Columns();
					columns.setParent(grid);

					grid.setParent(radiogroup);
					for (int i = 0; i < columnNum; i++) {
						Column column = new Column();
						column.setParent(columns);
					}
					/**
					 * 属性行数
					 */
					int rowNum = staffAttrSpecList.size() % perAttrNum == 0 ? staffAttrSpecList
							.size() / perAttrNum
							: (staffAttrSpecList.size() / perAttrNum) + 1;

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
							if (current < staffAttrSpecList.size()) {
								label.setValue(staffAttrSpecList.get(current)
										.getAttrName());
								if (!StrUtil.isEmpty(this.lableWidth)) {
									label.setWidth(this.lableWidth);
								}
								label.setParent(row);
								if (staffAttrSpecList.get(current)
										.getAttrSpecType() == SffOrPtyCtants.ATTR_SPEC_TYPE_INPUT) {
									/**
									 * 手工输入
									 */
									Textbox textbox = new Textbox();
									textbox.setId(preId
											+ staffAttrSpecList.get(current)
													.getStaffAttrSpecId());
									if (!StrUtil.isEmpty(this.textBoxWidth)) {
										textbox.setWidth(this.textBoxWidth);
									}
									if (!this.canInput(staffAttrSpecSort)) {
										textbox.setDisabled(true);
									}
									textbox.setParent(row);
								} else if (staffAttrSpecList.get(current)
										.getAttrSpecType() == SffOrPtyCtants.ATTR_SPEC_TYPE_LONGBOX_INPUT) {
									/**
									 * 手工输入
									 */
									Longbox lognbox = new Longbox();
									lognbox.setId(preId
											+ staffAttrSpecList.get(current)
													.getStaffAttrSpecId());
									if (!StrUtil.isEmpty(this.longBoxWidth)) {
										lognbox.setWidth(this.longBoxWidth);
									}
									if (!this.canInput(staffAttrSpecSort)) {
										lognbox.setDisabled(true);
									}
									lognbox.setParent(row);
								} else {
									/**
									 * 有下拉选项
									 */
									List<NodeVo> staffAttrValueNodeVoList = staffAttrSpecList
											.get(current)
											.getStaffAttrValueNodeVoList();
									if (staffAttrValueNodeVoList != null) {
										Listbox listbox = new Listbox();
										listbox.setId(preId
												+ staffAttrSpecList
														.get(current)
														.getStaffAttrSpecId());
										listbox.setMold("select");
										if (!StrUtil.isEmpty(this.listBoxWidth)) {
											listbox.setWidth(this.listBoxWidth);
										}
										ListboxUtils.rendererForEdit(listbox,
												staffAttrValueNodeVoList);
										if (!this.canInput(staffAttrSpecSort)) {
											listbox.setDisabled(true);
										}
										listbox.setParent(row);
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
	 * @param orgAttrCd
	 * @return
	 */
	public String getExtendValue(Long orgAttrSpecId) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + orgAttrSpecId);
		if (comp != null) {
			if (comp instanceof Textbox) {
				return ((Textbox) comp).getValue();
			} else if (comp instanceof Longbox) {
				return StrUtil.isNullOrEmpty(((Longbox) comp).getValue()) ? null
						: ((Longbox) comp).getValue().toString();
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
	public List<StaffExtendAttr> getExtendValueList() {
		List<StaffExtendAttr> staffExtendAttrList = new ArrayList<StaffExtendAttr>();
		if (staffAttrSpecSortList == null) {
			staffAttrSpecSortList = staffAttrSpecSortManager
					.getStaffAttrSpecSortList();
		}
		if (staffAttrSpecSortList != null) {
			for (StaffAttrSpecSort staffAttrSpecSort : staffAttrSpecSortList) {
				List<StaffAttrSpec> staffAttrSpecList = staffAttrSpecSort
						.getStaffAttrSpecList();
				if (staffAttrSpecList != null) {
					for (StaffAttrSpec staffAttrSpec : staffAttrSpecList) {
						StaffExtendAttr staffExtendAttr = new StaffExtendAttr();
						staffExtendAttr.setStaffAttrSpecId(staffAttrSpec
								.getStaffAttrSpecId());
						staffExtendAttr.setStaffAttrValue(this
								.getExtendValue(staffAttrSpec
										.getStaffAttrSpecId()));
						staffExtendAttrList.add(staffExtendAttr);
					}
				}
			}
		}
		return staffExtendAttrList;
	}

	/**
	 * 设置属性值
	 * 
	 * @param list
	 */
	public void setExtendValue(StaffExtendAttr staffExtendAttr) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + staffExtendAttr.getStaffAttrSpecId());
		if (comp != null) {
			if (comp instanceof Textbox) {
				((Textbox) comp).setValue(staffExtendAttr.getStaffAttrValue());
			} else if (comp instanceof Longbox) {
				((Longbox) comp).setValue(StrUtil.isEmpty(staffExtendAttr
						.getStaffAttrValue()) ? null : Long
						.parseLong(staffExtendAttr.getStaffAttrValue()));
			} else if (comp instanceof Listbox) {
				ListboxUtils.selectByCodeValue((Listbox) comp,
						staffExtendAttr.getStaffAttrValue());
			}
		}

	}

	/**
	 * 设置属性值
	 * 
	 * @param list
	 */
	public void setExtendValue(List<StaffExtendAttr> list) {
		if (list != null) {
			for (StaffExtendAttr staffExtendAttr : list) {
				this.setExtendValue(staffExtendAttr);
			}
		}
	}

	/**
	 * 设置属性不可修改
	 * 
	 * @param orgAttrCd
	 */
	public void setDisable(Long orgAttrSpecId, boolean disable) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + orgAttrSpecId);
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
	private boolean canInput(StaffAttrSpecSort staffAttrSpecSort) {
		if (staffAttrSpecSort != null) {
			if (SffOrPtyCtants.ATTR_SPEC_SORT_TYPE_NORMAL
					.equals(staffAttrSpecSort.getSortType())) {
				return true;
			} else if (SffOrPtyCtants.ATTR_SPEC_SORT_TYPE_ABNORMAL
					.equals(staffAttrSpecSort.getSortType())) {
				return false;
			}
		}
		return false;
	}

	/**
	 * 设置属性的树类型和组织标识，使扩展属性可编辑
	 * 
	 * @param treeType
	 * @param orgId
	 */
	public void setLocation(String treeType, Long orgId) {
		if (!StrUtil.isEmpty(treeType)) {
			List<StaffAttrSpec> treeStafflist = this.treeStaffExtendAttrManager
					.getStaffAttrSpecListByTreeTypeAndSortType(treeType,
							SffOrPtyCtants.ATTR_SPEC_SORT_TYPE_TREE);
			if (treeStafflist != null && treeStafflist.size() > 0) {
				for (StaffAttrSpec staffAttrSpec : treeStafflist) {
					if (staffAttrSpec != null
							&& staffAttrSpec.getStaffAttrSpecId() != null) {
						this.setDisable(staffAttrSpec.getStaffAttrSpecId(),
								false);
					}
				}
			}
			if (orgId != null) {
				Map params = new HashMap();
				params.put("treeType", treeType);
				params.put("orgId", orgId);
				params.put("sortType", SffOrPtyCtants.ATTR_SPEC_SORT_TYPE_TREE);
				List<StaffAttrSpec> treeOrgStafflist = this.treeOrgStaffExtendAttrManager
						.getStaffAttrSpecListByByParams(params);
				for (StaffAttrSpec staffAttrSpec : treeOrgStafflist) {
					if (staffAttrSpec != null
							&& staffAttrSpec.getStaffAttrSpecId() != null) {
						this.setDisable(staffAttrSpec.getStaffAttrSpecId(),
								false);
					}
				}
			}
		}
	}
}
