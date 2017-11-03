package cn.ffcs.uom.organization.component;

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
import org.zkoss.zk.ui.event.Events;
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

import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.manager.CascadeRelationManager;
import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrgAttrSpecManager;
import cn.ffcs.uom.organization.manager.OrgAttrSpecSortManager;
import cn.ffcs.uom.organization.manager.OrgAttrValueManager;
import cn.ffcs.uom.organization.manager.TreeExtendAttrManager;
import cn.ffcs.uom.organization.manager.TreeOrgExtendAttrManager;
import cn.ffcs.uom.organization.model.OrgAttrSpec;
import cn.ffcs.uom.organization.model.OrgAttrSpecSort;
import cn.ffcs.uom.organization.model.OrgAttrValue;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;

public class OrganizationExtendAttrExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5490875090383048724L;

	/**
	 * 机构类型
	 */
	private static final Long orgAttrSpecId4 = 4L;
	/**
	 * 组织机构层次
	 */
	private static final Long orgAttrSpecId5 = 5L;
	/**
	 * 实体/虚拟
	 */
	private static final Long orgAttrSpecId6 = 6L;
	/**
	 * 渠道属性
	 */
	private static final Long orgAttrSpecId7 = 7L;
	/**
	 * 网格类型-级联变动
	 */
	private static final Long orgAttrSpecId8 = 8L;

	/**
	 * 网格小类-级联变动
	 */
	private static final Long orgAttrSpecId9 = 9L;

	/**
	 * 客户群-级联变动
	 */
	private static final Long orgAttrSpecId10 = 10L;

	/**
	 * 专区类型-级联变动
	 */
	private static final Long orgAttrSpecId13 = 13L;

	/**
	 * 组织类型-级联变动
	 */
	private static final Long orgAttrSpecId14 = 14L;

	/**
	 * 渠道类型-级联变动
	 */
	private static final Long orgAttrSpecId15 = 15L;

	/**
	 * 渠道大类-级联变动
	 */
	private static final Long orgAttrSpecId16 = 16L;

	/**
	 * 渠道小类-级联变动
	 */
	private static final Long orgAttrSpecId17 = 17L;

	/**
	 * 营销扩展属性
	 */
	private static final Long orgAttrSpecSortId3 = 3L;

	/**
	 * 网格扩展属性
	 */
	private static final Long orgAttrSpecSortId4 = 4L;

	/**
	 * 渠道扩展属性
	 */
	private static final Long orgAttrSpecSortId5 = 5L;

	/**
	 * 集团4G门户扩展属性
	 */
	private static final Long orgAttrSpecSortId6 = 6L;

	/**
	 * 组建id前缀
	 */
	private static final String preId = "orgAttrSpec";
	/**
	 * 组建id前缀
	 */
	private static final String groupPreId = "orgAttrSpecSort";
	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/comp/organization_extend_attr_ext.zul";
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
	private Groupbox orgExtendAttrGroupBox;
	@Getter
	@Setter
	private Radiogroup radiogroupBody;

	/**
	 * 是否是聚合营销2015tab
	 */
	@Getter
	@Setter
	private Boolean isMarketingTab = false;

	/**
	 * 是否是代理商tab
	 */
	@Getter
	@Setter
	private Boolean isAgentTab = false;

	/**
	 * 是否是内部经营实体tab
	 */
	@Getter
	@Setter
	private Boolean isIbeTab = false;

	/**
	 * 属性规格种类
	 */
	List<OrgAttrSpecSort> orgAttrSpecSortList;
	/**
	 * manager
	 */

	private CascadeRelationManager cascadeRelationManager = (CascadeRelationManager) ApplicationContextUtil
			.getBean("cascadeRelationManager");

	private OrgAttrSpecSortManager orgAttrSpecSortManager = (OrgAttrSpecSortManager) ApplicationContextUtil
			.getBean("orgAttrSpecSortManager");

	private OrgAttrSpecManager orgAttrSpecManager = (OrgAttrSpecManager) ApplicationContextUtil
			.getBean("orgAttrSpecManager");

	private OrgAttrValueManager orgAttrValueManager = (OrgAttrValueManager) ApplicationContextUtil
			.getBean("orgAttrValueManager");

	private TreeExtendAttrManager treeExtendAttrManager = (TreeExtendAttrManager) ApplicationContextUtil
			.getBean("treeExtendAttrManager");

	private TreeOrgExtendAttrManager treeOrgExtendAttrManager = (TreeOrgExtendAttrManager) ApplicationContextUtil
			.getBean("treeOrgExtendAttrManager");

	public OrganizationExtendAttrExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
	}

	public void onCreate() {

		this.initExt();

		((Listbox) this.getRadiogroupBody().getFellow(preId + orgAttrSpecId8))
				.addForward(Events.ON_SELECT, this,
						"onSelectOrgAttrSpec8Response");

		((Listbox) this.getRadiogroupBody().getFellow(preId + orgAttrSpecId9))
				.addForward(Events.ON_SELECT, this,
						"onSelectOrgAttrSpec9Response");

		// ((Listbox) this.getRadiogroupBody().getFellow(preId +
		// orgAttrSpecId15))
		// .addForward(Events.ON_SELECT, this,
		// "onSelectOrgAttrSpec15Response");
		//
		// ((Listbox) this.getRadiogroupBody().getFellow(preId +
		// orgAttrSpecId16))
		// .addForward(Events.ON_SELECT, this,
		// "onSelectOrgAttrSpec16Response");

	}

	/**
	 * 初始化控件
	 */
	private void initExt() {
		Radiogroup radiogroupBody = this.getRadiogroupBody();
		orgAttrSpecSortList = orgAttrSpecSortManager.getOrgAttrSpecSortList();

		int perAttrNum = new Integer(this.getPerAttrNum());
		int columnNum = perAttrNum * 2;

		if (orgAttrSpecSortList != null && orgAttrSpecSortList.size() > 0) {
			for (OrgAttrSpecSort orgAttrSpecSort : orgAttrSpecSortList) {
				List<OrgAttrSpec> orgAttrSpecList = orgAttrSpecSort
						.getOrgAttrSpecList();
				if (orgAttrSpecList != null && orgAttrSpecList.size() > 0) {
					Groupbox groupbox = new Groupbox();
					groupbox.setId(groupPreId
							+ orgAttrSpecSort.getOrgAttrSpecSortId());
					if (orgAttrSpecSort.getOrgAttrSpecSortId() == orgAttrSpecSortId3
							|| orgAttrSpecSort.getOrgAttrSpecSortId() == orgAttrSpecSortId4
							|| orgAttrSpecSort.getOrgAttrSpecSortId() == orgAttrSpecSortId5
							|| orgAttrSpecSort.getOrgAttrSpecSortId() == orgAttrSpecSortId6) {
						groupbox.setVisible(false);
					}
					groupbox.setParent(radiogroupBody);

					Caption caption = new Caption();
					caption.setLabel(orgAttrSpecSort.getSortName());
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
					int rowNum = orgAttrSpecList.size() % perAttrNum == 0 ? orgAttrSpecList
							.size() / perAttrNum
							: (orgAttrSpecList.size() / perAttrNum) + 1;

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
							if (current < orgAttrSpecList.size()) {
								label.setValue(orgAttrSpecList.get(current)
										.getAttrName());
								if (!StrUtil.isEmpty(this.getLableWidth())) {
									label.setWidth(this.getLableWidth());
								}

								if (orgAttrSpecSort.getOrgAttrSpecSortId() == orgAttrSpecSortId4) {
									if (orgAttrSpecList.get(current)
											.getOrgAttrSpecId() == orgAttrSpecId8
											|| orgAttrSpecList.get(current)
													.getOrgAttrSpecId() == orgAttrSpecId9
											|| orgAttrSpecList.get(current)
													.getOrgAttrSpecId() == orgAttrSpecId10) {
										label.setClass("z-info-required");
									}
								}

								label.setParent(row);
								if (orgAttrSpecList.get(current)
										.getAttrSpecType() == OrganizationConstant.ATTR_SPEC_TYPE_INPUT) {
									/**
									 * 手工输入
									 */
									Textbox textbox = new Textbox();
									textbox.setId(preId
											+ orgAttrSpecList.get(current)
													.getOrgAttrSpecId());
									if (!StrUtil
											.isEmpty(this.getTextBoxWidth())) {
										textbox.setWidth(this.getTextBoxWidth());
									}
									if (!this.canInput(orgAttrSpecSort)) {
										textbox.setDisabled(true);
									}
									textbox.setParent(row);
								} else if (orgAttrSpecList.get(current)
										.getAttrSpecType() == OrganizationConstant.ATTR_SPEC_TYPE_SELECT) {
									/**
									 * 有下拉选项
									 */
									List<NodeVo> orgAttrValueNodeVoList = orgAttrSpecList
											.get(current)
											.getOrgAttrValueNodeVoList();
									if (orgAttrValueNodeVoList != null) {
										Listbox listbox = new Listbox();
										listbox.setId(preId
												+ orgAttrSpecList.get(current)
														.getOrgAttrSpecId());
										listbox.setMold("select");
										listbox.setId(preId
												+ orgAttrSpecList.get(current)
														.getOrgAttrSpecId());
										if (!StrUtil.isEmpty(this
												.getListBoxWidth())) {
											listbox.setWidth(this
													.getListBoxWidth());
										}
										ListboxUtils.rendererForEdit(listbox,
												orgAttrValueNodeVoList);
										if (!this.canInput(orgAttrSpecSort)) {
											listbox.setDisabled(true);
										}
										listbox.setParent(row);
										listbox.setId(preId
												+ orgAttrSpecList.get(current)
														.getOrgAttrSpecId());
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
	 * 网格类型-级联变动
	 */
	public void onSelectOrgAttrSpec8Response() {

		// 设置客户群列表不可编辑
		this.setDisable(orgAttrSpecId10, true);

		// 获取网格类别列表选择的值
		String result = this.getExtendValue(orgAttrSpecId8);

		// 设置客户群值
		ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
				.getFellow(preId + orgAttrSpecId10), null);

		if (!StrUtil.isNullOrEmpty(result)) {

			// 清空网格小类列表
			ListboxUtils.clearListbox((Listbox) this.getRadiogroupBody()
					.getFellow(preId + orgAttrSpecId9));

			OrgAttrValue orgAttrValue = new OrgAttrValue();
			orgAttrValue.setOrgAttrSpecId(orgAttrSpecId9);
			orgAttrValue.setOrgAttrValue(result);

			// 根据网格类型级联查询网格小类
			List<OrgAttrValue> orgAttrValueList = orgAttrValueManager
					.queryOrgAttrValueList(orgAttrValue, "R");

			if (!StrUtil.isNullOrEmpty(orgAttrValueList)
					&& orgAttrValueList.size() > 0) {
				// 设置网格小类枚举值
				ListboxUtils.rendererForEdit((Listbox) this.getRadiogroupBody()
						.getFellow(preId + orgAttrSpecId9), new OrgAttrSpec()
						.getOrgAttrValueNodeVoList(orgAttrValueList));
			}

		} else {
			// 设置网格小类值
			ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
					.getFellow(preId + orgAttrSpecId9), null);
		}

	}

	/**
	 * 网格小类-级联变动
	 */
	public void onSelectOrgAttrSpec9Response() {

		// 设置客户群列表不可编辑
		this.setDisable(orgAttrSpecId10, true);

		// 获取网格小类列表选择的值
		String result = this.getExtendValue(orgAttrSpecId9);

		if (!StrUtil.isNullOrEmpty(result)) {

			CascadeRelation cascadeRelation = new CascadeRelation();
			cascadeRelation.setRelaCd(CascadeRelationConstants.RELA_CD_1);
			cascadeRelation.setCascadeValue(result);

			List<CascadeRelation> cascadeRelationList = cascadeRelationManager
					.queryCascadeRelationList(cascadeRelation);

			if (!StrUtil.isNullOrEmpty(cascadeRelationList)
					&& cascadeRelationList.size() > 0) {
				// 设置客户群值
				ListboxUtils.selectByCodeValue(
						(Listbox) this.getRadiogroupBody().getFellow(
								preId + orgAttrSpecId10), cascadeRelationList
								.get(0).getRelaCascadeValue());
			}

		} else {
			// 设置客户群值
			ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
					.getFellow(preId + orgAttrSpecId10), null);
		}

		this.onChangeOrgAttrSpec10Response();
	}

	/**
	 * 客户群-级联变动
	 */
	public void onChangeOrgAttrSpec10Response() {

		// 获取客户群列表选择的值
		String result = this.getExtendValue(orgAttrSpecId10);

		if (!StrUtil.isNullOrEmpty(result)) {

			// 根据客户群级联查询专区类型
			List<OrgAttrValue> newOrgAttrValueList = new ArrayList<OrgAttrValue>();

			CascadeRelation cascadeRelation = new CascadeRelation();
			cascadeRelation.setRelaCd(CascadeRelationConstants.RELA_CD_2);
			cascadeRelation.setRelaCascadeValue(result);

			// 根据客户群属性值级联查询专区类型
			List<CascadeRelation> cascadeRelationList = cascadeRelationManager
					.queryCascadeRelationList(cascadeRelation);

			OrgAttrValue orgAttrValue = new OrgAttrValue();
			orgAttrValue.setOrgAttrSpecId(orgAttrSpecId13);

			// 查询所有的专区类型值
			List<OrgAttrValue> orgAttrValueList = orgAttrValueManager
					.queryOrgAttrValueList(orgAttrValue, null);

			if (!StrUtil.isNullOrEmpty(cascadeRelationList)
					&& cascadeRelationList.size() > 0
					&& !StrUtil.isNullOrEmpty(orgAttrValueList)
					&& orgAttrValueList.size() > 0) {

				// 清空专区类型列表
				ListboxUtils.clearListbox((Listbox) this.getRadiogroupBody()
						.getFellow(preId + orgAttrSpecId13));

				for (CascadeRelation newCascadeRelation : cascadeRelationList) {

					for (OrgAttrValue newOrgAttrValue : orgAttrValueList) {

						if (newCascadeRelation.getCascadeValue().equals(
								newOrgAttrValue.getOrgAttrValue())) {
							newOrgAttrValueList.add(newOrgAttrValue);
						}
					}
				}

			}

			if (!StrUtil.isNullOrEmpty(newOrgAttrValueList)
					&& newOrgAttrValueList.size() > 0) {
				// 设置专区类型枚举值
				ListboxUtils.rendererForEdit((Listbox) this.getRadiogroupBody()
						.getFellow(preId + orgAttrSpecId13), new OrgAttrSpec()
						.getOrgAttrValueNodeVoList(newOrgAttrValueList));
			}

		} else {
			// 设置专区类型值
			ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
					.getFellow(preId + orgAttrSpecId13), null);
		}
	}

	/**
	 * 渠道类型-级联变动
	 */
	public void onSelectOrgAttrSpec15Response() {

		// 获取渠道类型列表选择的值
		String result = this.getExtendValue(orgAttrSpecId15);

		// 设置渠道小类值
		ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
				.getFellow(preId + orgAttrSpecId17), null);

		if (!StrUtil.isNullOrEmpty(result)) {

			// 清空渠道大类列表
			ListboxUtils.clearListbox((Listbox) this.getRadiogroupBody()
					.getFellow(preId + orgAttrSpecId16));

			OrgAttrValue orgAttrValue = new OrgAttrValue();
			orgAttrValue.setOrgAttrSpecId(orgAttrSpecId16);
			orgAttrValue.setOrgAttrValue(result.substring(0, 2));

			// 根据渠道类型级联查询渠道大类
			List<OrgAttrValue> orgAttrValueList = orgAttrValueManager
					.queryOrgAttrValueList(orgAttrValue, "R");

			if (!StrUtil.isNullOrEmpty(orgAttrValueList)
					&& orgAttrValueList.size() > 0) {
				// 设置渠道大类枚举值
				ListboxUtils.rendererForEdit((Listbox) this.getRadiogroupBody()
						.getFellow(preId + orgAttrSpecId16), new OrgAttrSpec()
						.getOrgAttrValueNodeVoList(orgAttrValueList));
			}

		} else {
			// 设置渠道大类值
			ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
					.getFellow(preId + orgAttrSpecId16), null);
		}

	}

	/**
	 * 渠道大类-级联变动
	 */
	public void onSelectOrgAttrSpec16Response() {

		// 获取渠道大类列表选择的值
		String result = this.getExtendValue(orgAttrSpecId16);

		if (!StrUtil.isNullOrEmpty(result)) {

			// 清空渠道小类列表
			ListboxUtils.clearListbox((Listbox) this.getRadiogroupBody()
					.getFellow(preId + orgAttrSpecId17));

			OrgAttrValue orgAttrValue = new OrgAttrValue();
			orgAttrValue.setOrgAttrSpecId(orgAttrSpecId17);
			orgAttrValue.setOrgAttrValue(result.substring(0, 4));

			// 根据渠道类型级联查询渠道小类
			List<OrgAttrValue> orgAttrValueList = orgAttrValueManager
					.queryOrgAttrValueList(orgAttrValue, "R");

			if (!StrUtil.isNullOrEmpty(orgAttrValueList)
					&& orgAttrValueList.size() > 0) {
				// 设置渠道小类枚举值
				ListboxUtils.rendererForEdit((Listbox) this.getRadiogroupBody()
						.getFellow(preId + orgAttrSpecId17), new OrgAttrSpec()
						.getOrgAttrValueNodeVoList(orgAttrValueList));
			}

		} else {
			// 设置渠道小类值
			ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
					.getFellow(preId + orgAttrSpecId17), null);
		}

	}

	/**
	 * 组织层级属性控制--显示
	 * isParent如果为true，那organizationg表示选择是上级组织【新增组织时，其页面显示需要根据上级组织来进行控制
	 * 】。如果为false时，表示选择是本级组织。
	 */
	public void orgLevelControlVisible(Organization organization,
			boolean isParent, String relaCd) {

		if (OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE.equals(relaCd)
				|| OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403
						.equals(relaCd) || OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404.equals(relaCd)
                || OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404
                        .equals(relaCd) ) {

			// 设置所有属性不可编辑
			this.setAllDisable(true);
			// 显示营销扩展属性
			this.setVisible(orgAttrSpecSortId3, true);
			// 隐藏网格扩展属性
			this.setVisible(orgAttrSpecSortId4, false);

			// 设置默认值 1：实体 0：虚拟
			// ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
			// .getFellow(preId + orgAttrSpecId6), "1");

			// 查询营销组织层级
			int orgLevel = organization.getOrganizationLevel(relaCd);

			if (isParent) {
				orgLevel += 1;
			} 

			if (!StrUtil.isNullOrEmpty(organization)) {

				if (7 == orgLevel) {
					this.setVisible(orgAttrSpecSortId4, true);
				} else if (3 == orgLevel || 4 == orgLevel) {

					// 清空渠道属性列表
					ListboxUtils.clearListbox((Listbox) this
							.getRadiogroupBody().getFellow(
									preId + orgAttrSpecId7));
					// 清空组织类型列表
					ListboxUtils.clearListbox((Listbox) this
							.getRadiogroupBody().getFellow(
									preId + orgAttrSpecId14));

					OrgAttrValue orgAttrValue7 = new OrgAttrValue();
					orgAttrValue7.setOrgAttrSpecId(orgAttrSpecId7);
					orgAttrValue7.setOrgAttrValue(orgLevel + "");

					OrgAttrValue orgAttrValue14 = new OrgAttrValue();
					orgAttrValue14.setOrgAttrSpecId(orgAttrSpecId14);
					orgAttrValue14.setOrgAttrValue(orgLevel + "");

					// 查询渠道属性
					List<OrgAttrValue> orgAttrValueList7 = orgAttrValueManager
							.queryOrgAttrValueList(orgAttrValue7, "R");

					// 查询组织类型
					List<OrgAttrValue> orgAttrValueList14 = orgAttrValueManager
							.queryOrgAttrValueList(orgAttrValue14, "R");

					if (!StrUtil.isNullOrEmpty(orgAttrValueList7)
							&& orgAttrValueList7.size() > 0) {
						// 设置渠道属性枚举值
						ListboxUtils
								.rendererForEdit(
										(Listbox) this.getRadiogroupBody()
												.getFellow(
														preId + orgAttrSpecId7),
										new OrgAttrSpec()
												.getOrgAttrValueNodeVoList(orgAttrValueList7));
					}

					if (!StrUtil.isNullOrEmpty(orgAttrValueList14)
							&& orgAttrValueList14.size() > 0) {
						// 设置组织类型枚举值
						ListboxUtils
								.rendererForEdit(
										(Listbox) this
												.getRadiogroupBody()
												.getFellow(
														preId + orgAttrSpecId14),
										new OrgAttrSpec()
												.getOrgAttrValueNodeVoList(orgAttrValueList14));
					}

				}

			}
		}
	}

	/**
	 * 组织层级属性控制--编辑
	 * isParent如果为true，那organizationg表示选择是上级组织【新增组织时，其页面编辑需要根据上级组织来进行控制
	 * 】。如果为false时，表示选择是本级组织。
	 */
	public void orgLevelControlDisable(Organization organization,
			boolean isParent, String relaCd) {

		if (OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE.equals(relaCd)
				|| OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403
						.equals(relaCd)
                || OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404
                        .equals(relaCd)) {
			// 设置所有属性可编辑
			// this.setAllDisable(false);
			// 使机构类型可编辑
			this.setDisable(orgAttrSpecId4, false);
			// 使组织机构层次可编辑
			this.setDisable(orgAttrSpecId5, false);
			// 使实体/虚拟可编辑
			this.setDisable(orgAttrSpecId6, false);
			// 使渠道属性不可编辑
			this.setDisable(orgAttrSpecId7, true);
			// 使组织类型不可编辑
			this.setDisable(orgAttrSpecId14, true);

			// 设置默认值 1：实体 0：虚拟
			// ListboxUtils.selectByCodeValue((Listbox) this.getRadiogroupBody()
			// .getFellow(preId + orgAttrSpecId6), "1");

			// 查询营销组织层级
			int orgLevel = organization.getOrganizationLevel(relaCd);

			if (isParent) {
				orgLevel += 1;
			}

			if (!StrUtil.isNullOrEmpty(organization)) {

				if (7 == orgLevel) {

					this.setDisable(orgAttrSpecId6, false);

					List<OrgAttrSpecSort> orgAttrSpecSortList = new ArrayList<OrgAttrSpecSort>();
					OrgAttrSpecSort orgAttrSpecSort = new OrgAttrSpecSort();
					orgAttrSpecSort.setOrgAttrSpecSortId(orgAttrSpecSortId4);
					orgAttrSpecSortList.add(orgAttrSpecSort);

					if (!StrUtil.isEmpty(this.getExtendValue(orgAttrSpecId8))
							&& this.getExtendValue(orgAttrSpecId8)
									.equals(OrganizationConstant.ATTR_SPEC_8_ATTR_VALUE_13)) {

						// 设置网格属性不可编辑
						this.setPartDisable(orgAttrSpecSortList, true);

					} else {

						// 设置网格属性可编辑
						this.setPartDisable(orgAttrSpecSortList, false);

						// 设置客户群不可编辑
						this.setDisable(orgAttrSpecId10, true);

					}

				} else if (2 == orgLevel) {

					if (!isParent) {
						if (StrUtil.isEmpty(organization.getTelcomRegion()
								.getAreaCode())) {
							this.setDisable(orgAttrSpecId5, true);
						}
					}

				} else if (3 == orgLevel || 4 == orgLevel) {

					this.setDisable(orgAttrSpecId7, false);

					this.setDisable(orgAttrSpecId14, false);

				}

			}
		}
	}

	/**
	 * 组织属性控制--显示
	 * isParent如果为true，那organizationg表示选择是上级组织【新增组织时，其页面显示需要根据上级组织来进行控制
	 * 】。如果为false时，表示选择是本级组织。
	 */
	public void orgChannelControlVisible(Organization organization,
			boolean isParent) {

		if (isAgentTab || isIbeTab) {

			// 设置所有属性不可编辑
			this.setAllDisable(true);

			// 查询营销组织2015层级
			int orgLevel = 0;

			if (organization != null) {
				orgLevel = organization
						.getOrganizationLevel(OrganizationConstant.RELA_CD_EXTER);
			}

			if (isParent) {
				orgLevel += 1;
			}

			if (orgLevel > 0) {
				// 显示渠道扩展属性
				// this.setVisible(orgAttrSpecSortId5, true);
				// 显示集团4G门户扩展属性
				this.setVisible(orgAttrSpecSortId6, true);
			}

		} else {
			// 隐藏渠道扩展属性
			// this.setVisible(orgAttrSpecSortId5, false);
			// 隐藏集团4G门户扩展属性
			this.setVisible(orgAttrSpecSortId6, false);
		}
	}

	/**
	 * 组织层级属性控制--编辑
	 * isParent如果为true，那organizationg表示选择是上级组织【新增组织时，其页面编辑需要根据上级组织来进行控制
	 * 】。如果为false时，表示选择是本级组织。
	 */
	public void orgChannelControlDisable(Organization organization,
			boolean isParent) {

		// if (isAgentTab || isIbeTab) {
		//
		// int orgLevel = organization
		// .getOrganizationLevel(OrganizationConstant.RELA_CD_EXTER);
		//
		// if (isParent) {
		// orgLevel += 1;
		// }
		//
		// if (orgLevel > 0) {
		// // 设置渠道扩展属性是否可编辑
		// List<OrgAttrSpecSort> orgAttrSpecSortList = new
		// ArrayList<OrgAttrSpecSort>();
		// OrgAttrSpecSort orgAttrSpecSort = new OrgAttrSpecSort();
		// orgAttrSpecSort.setOrgAttrSpecSortId(orgAttrSpecSortId5);
		// orgAttrSpecSortList.add(orgAttrSpecSort);
		// this.setPartDisable(orgAttrSpecSortList, false);
		// }
		//
		// }
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
	public List<OrganizationExtendAttr> getExtendValueList() {
		List<OrganizationExtendAttr> organizationExtendAttrList = new ArrayList<OrganizationExtendAttr>();
		if (orgAttrSpecSortList == null) {
			orgAttrSpecSortList = orgAttrSpecSortManager
					.getOrgAttrSpecSortList();
		}
		if (orgAttrSpecSortList != null) {
			for (OrgAttrSpecSort orgAttrSpecSort : orgAttrSpecSortList) {
				List<OrgAttrSpec> orgAttrSpecList = orgAttrSpecSort
						.getOrgAttrSpecList();
				if (orgAttrSpecList != null) {
					for (OrgAttrSpec orgAttrSpec : orgAttrSpecList) {
						OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
						organizationExtendAttr.setOrgAttrSpecId(orgAttrSpec
								.getOrgAttrSpecId());
						organizationExtendAttr
								.setOrgAttrValue(this
										.getExtendValue(orgAttrSpec
												.getOrgAttrSpecId()));
						organizationExtendAttrList.add(organizationExtendAttr);
					}
				}
			}
		}
		return organizationExtendAttrList;
	}

	/**
	 * 设置属性值
	 * 
	 * @param orgAttrSpecId
	 *            orgAttrValue
	 */
	public void setExtendValue(Long orgAttrSpecId, String orgAttrValue) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + orgAttrSpecId);
		if (comp != null) {
			if (comp instanceof Textbox) {
				((Textbox) comp).setValue(orgAttrValue);
			} else if (comp instanceof Listbox) {
				ListboxUtils.selectByCodeValue((Listbox) comp, orgAttrValue);
			}
		}
	}

	/**
	 * 设置属性值
	 * 
	 * @param list
	 */
	public void setExtendValue(OrganizationExtendAttr organizationExtendAttr) {
		Component comp = this.getRadiogroupBody().getFellow(
				preId + organizationExtendAttr.getOrgAttrSpecId());
		if (comp != null) {
			if (comp instanceof Textbox) {
				((Textbox) comp).setValue(organizationExtendAttr
						.getOrgAttrValue());
			} else if (comp instanceof Listbox) {
				ListboxUtils.selectByCodeValue((Listbox) comp,
						organizationExtendAttr.getOrgAttrValue());
			}
		}
	}

	/**
	 * 设置属性值 20150308朱林涛 修复扩展属性展示会保留上一个组织的值的BUG
	 * 
	 * @param list
	 */
	public void setExtendValueList(List<OrganizationExtendAttr> list) {
		if (list != null && list.size() > 0) {

			List<OrgAttrSpec> orgAttrSpecList = orgAttrSpecManager
					.queryOrgAttrSpecList();

			for (OrgAttrSpec orgAttrSpec : orgAttrSpecList) {

				OrganizationExtendAttr newOrganizationExtendAttr = new OrganizationExtendAttr();
				newOrganizationExtendAttr.setOrgAttrSpecId(orgAttrSpec
						.getOrgAttrSpecId());

				for (OrganizationExtendAttr organizationExtendAttr : list) {
					if (orgAttrSpec.getOrgAttrSpecId() == organizationExtendAttr
							.getOrgAttrSpecId()) {
						newOrganizationExtendAttr = organizationExtendAttr;
						break;
					}
				}

				this.setExtendValue(newOrganizationExtendAttr);
			}

		} else {
			List<OrgAttrSpec> orgAttrSpecList = orgAttrSpecManager
					.queryOrgAttrSpecList();
			for (OrgAttrSpec orgAttrSpec : orgAttrSpecList) {
				OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
				organizationExtendAttr.setOrgAttrSpecId(orgAttrSpec
						.getOrgAttrSpecId());
				this.setExtendValue(organizationExtendAttr);
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
	private boolean canInput(OrgAttrSpecSort orgAttrSpecSort) {
		if (orgAttrSpecSort != null) {
			if (OrganizationConstant.ATTR_SPEC_SORT_TYPE_NORMAL
					.equals(orgAttrSpecSort.getSortType())) {
				return true;
			} else if (OrganizationConstant.ATTR_SPEC_SORT_TYPE_ABNORMAL
					.equals(orgAttrSpecSort.getSortType())) {
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
			List<OrgAttrSpec> treeAttrlist = treeExtendAttrManager
					.getOrgAttrSpecListByTreeTypeAndSortType(treeType,
							OrganizationConstant.ATTR_SPEC_SORT_TYPE_TREE);
			if (treeAttrlist != null && treeAttrlist.size() > 0) {
				for (OrgAttrSpec orgAttrSpec : treeAttrlist) {
					if (orgAttrSpec != null
							&& orgAttrSpec.getOrgAttrSpecId() != null) {
						this.setDisable(orgAttrSpec.getOrgAttrSpecId(), false);
					}
				}
			}
			if (orgId != null) {
				Map params = new HashMap();
				params.put("treeType", treeType);
				params.put("orgId", orgId);
				params.put("sortType",
						OrganizationConstant.ATTR_SPEC_SORT_TYPE_TREE);

				List<OrgAttrSpec> treeOrgAttrlist = treeOrgExtendAttrManager
						.getOrgAttrSpecListByParams(params);
				for (OrgAttrSpec orgAttrSpec : treeOrgAttrlist) {
					if (orgAttrSpec != null
							&& orgAttrSpec.getOrgAttrSpecId() != null) {
						this.setDisable(orgAttrSpec.getOrgAttrSpecId(), false);
					}
				}
			}
		}
	}

	/**
	 * 设置属性不可修改
	 * 
	 * @param orgAttrCd
	 */
	public void setPartDisable(List<OrgAttrSpecSort> orgAttrSpecSortList,
			boolean disable) {
		if (orgAttrSpecSortList != null && orgAttrSpecSortList.size() > 0) {
			for (OrgAttrSpecSort orgAttrSpecSort : orgAttrSpecSortList) {
				List<OrgAttrSpec> orgAttrSpecList = orgAttrSpecSort
						.getOrgAttrSpecList();
				if (orgAttrSpecList != null && orgAttrSpecList.size() > 0) {
					for (OrgAttrSpec orgAttrSpec : orgAttrSpecList) {
						Component comp = this.getRadiogroupBody().getFellow(
								preId + orgAttrSpec.getOrgAttrSpecId());
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
	 * @param orgAttrCd
	 */
	public void setAllDisable(boolean disable) {
		if (this.orgAttrSpecSortList != null
				&& this.orgAttrSpecSortList.size() > 0) {
			for (OrgAttrSpecSort orgAttrSpecSort : this.orgAttrSpecSortList) {
				List<OrgAttrSpec> orgAttrSpecList = orgAttrSpecSort
						.getOrgAttrSpecList();
				if (orgAttrSpecList != null && orgAttrSpecList.size() > 0) {
					for (OrgAttrSpec orgAttrSpec : orgAttrSpecList) {
						Component comp = this.getRadiogroupBody().getFellow(
								preId + orgAttrSpec.getOrgAttrSpecId());
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
	public void setVisible(Long orgAttrSpecSortId, boolean visible) {
		Component comp = this.getRadiogroupBody().getFellow(
				groupPreId + orgAttrSpecSortId);
		if (comp != null) {
			if (comp instanceof Groupbox) {
				((Groupbox) comp).setVisible(visible);
			}
		}
	}
}