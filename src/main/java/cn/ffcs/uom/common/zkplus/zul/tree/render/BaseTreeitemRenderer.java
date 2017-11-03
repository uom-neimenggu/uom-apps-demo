package cn.ffcs.uom.common.zkplus.zul.tree.render;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.ZKRebuildUuid;
import cn.ffcs.uom.common.zkplus.util.ComposerUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNode;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * BaseTreeitemRenderer.
 * 
 * @author chenmh
 * @version Revision 1.0.0
 * 
 */
public class BaseTreeitemRenderer implements TreeitemRenderer {
	/**
	 * 列绑定值名称.
	 */
	public static final String PROP_NAME = "pn";
	/**
	 * 列绑定值名称,特殊列label.
	 */
	public static final String PROP_LABEL = "label";

	Component rootComp; //

	/**
	 * @param item
	 *            Treeitem
	 * @param data
	 *            Object
	 * @see org.zkoss.zul.TreeitemRenderer#render(org.zkoss.zul.Treeitem,
	 *      java.lang.Object)
	 * @throw Exception Exception
	 */
	public void render(final Treeitem item, final Object data) throws Exception {

		final TreeNode ite = (TreeNode) data;
		// item.setLabel(ite.getLable());
		item.setValue(ite);
		item.setOpen(false);
		if (rootComp == null) {
			rootComp = item.getParent().getParent();
		}
		// 压力测试
		ZKRebuildUuid.rebuildUuidById(item, "treeItem_" + rootComp.getId()
				+ "_z1" + ite.getTestId());

		// Construct treecells
		final Treecell tcNamn = new Treecell(ite.getLable());
		Treerow tr = null;

		if (item.getTreerow() == null) {
			tr = new Treerow();
			tr.setParent(item);
		} else {
			tr = item.getTreerow();
			tr.getChildren().clear();
		}
		if (!StrUtil.isEmpty(ite.getHint())) { // 提示信息
			tr.setTooltiptext(ite.getHint());
		}

		// 压力测试
		ZKRebuildUuid.rebuildUuidById(tr, "treeRow_" + rootComp.getId() + "_z2"
				+ ite.getTestId());

		// Attach treecells to treerow
		tcNamn.setAttribute(BaseTreeitemRenderer.PROP_NAME,
				BaseTreeitemRenderer.PROP_LABEL);
		tcNamn.setParent(tr);
		tcNamn.setStyle("white-space:nowrap;");
		// 压力测试
		ZKRebuildUuid.rebuildUuidById(tcNamn, "treeCell_" + rootComp.getId()
				+ "_z3" + ite.getTestId());
		final String icon = null;

		/*
		 * if (data instanceof CountryNode) icon = "country_16x16.png"; if (data
		 * instanceof CityNode) icon = "city_16x16.png"; if (data instanceof
		 * StreetNode) icon = "street_16x16.png";
		 */
		if (icon != null) {
			item.setImage(icon);
		}

		// Attach event to Treeitem
		item.addEventListener("onClick", new EventListener() {
			public void onEvent(final Event e) {
				// 朱林涛-去除当父级节点展开且选中时，其子节点也都会被全部选中 2016-05-11
				// BaseTreeitemRenderer.this.checkSelection(e);
				// alert(((Treeitem)e.getTarget()).getLabel()+"");
			}
		});

		// Attach click event to Treeitem, send to tree
		item.addEventListener("onClick", new EventListener() {
			public void onEvent(final Event e) {
				final Tree tree = (Tree) ComposerUtil.getSupExt(item,
						Tree.class);
				if (tree != null) {
					Events.postEvent("onClickTreeItem", tree, item);
				}
			}
		});

		// 朱林涛-去除当父级节点展开且选中时，其子节点也都会被全部选中 2016-05-11
		/*
		 * item.addEventListener("onOpen", new EventListener() { public void
		 * onEvent(final Event e) {
		 * BaseTreeitemRenderer.this.synSelectionToChild(e); } });
		 */

		item.addEventListener("onOpen", new EventListener() {
			public void onEvent(final Event e) {
				BaseTreeitemRenderer.this
						.setStyleToOrganizationRelationTreeLable(e);
			}
		});
		/**
		 * 电信树初始化
		 */
		telcomRegionTreeInit(item, ite);
		/**
		 * 岗位树初始化
		 */
		positionTreeInit(item, ite);
	}

	/**
	 * updateRender.
	 * 
	 * @param item
	 *            Treeitem
	 * @param data
	 *            Object
	 */
	public static void updateRender(final Treeitem item, final Object data) {
		final TreeNode ite = (TreeNode) data;
		item.setValue(ite);
		item.setOpen(false);
		// row
		final Treerow row = item.getTreerow();
		if (row == null) {
			return; // 没有row无需刷新
		}
		// cells
		final List<Treecell> tclist = row.getChildren();
		for (final Treecell tc : tclist) {
			final Object oProp = tc
					.getAttribute(BaseTreeitemRenderer.PROP_NAME); // 属性名称
			if (oProp == null
					|| !BaseTreeitemRenderer.PROP_LABEL.equals(oProp + "")) {
				continue;
			}
			// update
			tc.setLabel(ite.getLable());
		}
	}

	/**
	 * 设置父子节点选中状态.
	 * 
	 * @param e
	 *            事件
	 */
	public void checkSelection(final Event e) {
		final Treeitem item = (Treeitem) e.getTarget();
		if (this.isCheckmark(item) && this.isMultipleTree(item)) {
			this.checkChild(item);
			// 朱林涛-去除子节点全部选中时，其父节点也会被选中 2016-05-11
			// this.checkParent(item);
		}

	}

	/**
	 * @param e
	 *            事件
	 */
	public void synSelectionToChild(final Event e) {
		final Treeitem item = (Treeitem) e.getTarget();
		if (this.isCheckmark(item) && this.isMultipleTree(item)
				&& item.isSelected()) {
			this.checkChild(item);

		}

	}

	/**
	 * 选中子节点
	 * 
	 * @param item
	 *            子节点
	 */
	public void checkChild(final Treeitem item) {
		// System.out.println("########"+item.getLabel()+":"+item.isSelected());
		if (item.getTreechildren() == null) {
			return;
		} else {
			final Treechildren children = item.getTreechildren();
			for (final Object obj : children.getItems()) {
				final Treeitem childItem = (Treeitem) obj;
				childItem.setSelected(item.isSelected());
				// System.out.println("  --"+childItem.getLabel()+":"+childItem.isSelected());
				this.checkChild(childItem);
			}
		}

	}

	/**
	 * 选中父节点
	 * 
	 * @param item
	 *            节点
	 */
	public void checkParent(final Treeitem item) {
		if (item.getParent().getParent() instanceof Tree) {
			return;
		} else {
			final Treeitem parentItem = (Treeitem) item.getParent().getParent();
			// System.out.println("########"+parentItem.getLabel()+":"+parentItem.isSelected());
			// if (!item.isSelected())
			// {
			final Treechildren children = parentItem.getTreechildren();
			boolean isCheck = true;
			for (final Object obj : children.getItems()) {
				final Treeitem childItem = (Treeitem) obj;
				// System.out.println("  --"+childItem.getLabel()+":"+childItem.isSelected());
				if (!childItem.isSelected()) {
					isCheck = false;
					break;
				}
			}
			parentItem.setSelected(isCheck);
			// }
			/*
			 * else { parentItem.setSelected(true); }
			 */
			this.checkParent(parentItem);
		}
	}

	/**
	 * 判断树的选择类型 true checkbox false radio
	 * 
	 * @param item
	 *            Treeitem
	 * @return boolean
	 */
	public boolean isMultipleTree(final Treeitem item) {
		final Object parentObj = item.getParent().getParent();
		if (parentObj instanceof Tree) {
			return ((Tree) parentObj).isMultiple();
		} else if (parentObj instanceof Treeitem) {
			return this.isMultipleTree((Treeitem) parentObj);
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为选择树 true checkbox false radio.
	 * 
	 * @param item
	 *            Treeitem
	 * @return boolean
	 */
	public boolean isCheckmark(final Treeitem item) {
		if (item == null || item.getParent() == null
				|| item.getParent().getParent() == null) {
			return false;
		}
		final Object parentObj = item.getParent().getParent();
		if (parentObj instanceof Tree) {
			return ((Tree) parentObj).isCheckmark();
		} else if (parentObj instanceof Treeitem) {
			return this.isCheckmark((Treeitem) parentObj);
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 方法功能: 给树已经选中的节点item增加一个下级item
	 * 
	 * @param Tree
	 *            tree Object data
	 * @throws
	 * @author: huang zhong kui
	 * @修改记录： ==============================================================<br>
	 *        日期:2010-12-24 huang zhong kui 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void onTreeAddItemAndSelected(final Tree tree,
			final Object data) {
		if (tree != null && data != null) {
			final Treeitem treeitem = new Treeitem();
			final TreeNodeEntity ite = (TreeNodeEntity) data;
			final TreeNodeImpl tni = new TreeNodeImpl(ite);
			treeitem.setValue(tni);
			final Treecell tcNamn = new Treecell(tni.getLable());
			Treerow tr = null;
			if (treeitem.getTreerow() == null) {
				tr = new Treerow();
				tr.setParent(treeitem);
			} else {
				tr = treeitem.getTreerow();
			}
			tcNamn.setAttribute(BaseTreeitemRenderer.PROP_NAME,
					BaseTreeitemRenderer.PROP_LABEL);
			tcNamn.setParent(tr);
			Treechildren trc = null;
			if (tree.getSelectedItem() != null) {
				tree.getSelectedItem().setOpen(true);
				if (tree.getSelectedItem().getTreechildren() == null) {
					trc = new Treechildren();
					trc.setParent(tree.getSelectedItem());
				} else {
					trc = tree.getSelectedItem().getTreechildren();
				}
				// 重新更新树的实体数据
				final TreeNodeImpl entity = (TreeNodeImpl) tree
						.getSelectedItem().getValue();
				if (entity.getChildren() != null) {
					entity.getChildren().add(tni);
					tree.getSelectedItem().setValue(entity);
				}
			} else {
				if (tree.getTreechildren() == null) {
					trc = new Treechildren();
					trc.setParent(tree);
				} else {
					trc = tree.getTreechildren();
				}
			}
			treeitem.setParent(trc);
			treeitem.setSelected(true);
		}
	}

	/**
	 * 
	 * 方法功能: 删除树已经选中的节点.
	 * 
	 * @param tree
	 *            tree
	 * @throws
	 * @author: huang zhong kui
	 * @修改记录： ==============================================================<br>
	 *        日期:2010-12-24 huang zhong kui 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void onTreeRemoveSelectedItem(final Tree tree) {
		if (tree != null) {
			if (tree.getSelectedItem() != null) {
				if (tree.getSelectedItem().getParentItem() != null) {
					// 重新更新树的实体数据
					final TreeNodeImpl entity = (TreeNodeImpl) (tree
							.getSelectedItem().getParentItem().getValue());
					if (entity.getChildren() != null) {
						final TreeNodeImpl ChildrenEntity = (TreeNodeImpl) (tree
								.getSelectedItem().getValue());
						entity.getChildren().remove(ChildrenEntity);
						tree.getSelectedItem().getParentItem().setValue(entity);
					}
				}
				tree.getSelectedItem().setParent(null);
			}
		}
	}

	/**
	 * 设置组织树样式
	 * 
	 * @param e
	 */
	public void setStyleToOrganizationRelationTreeLable(final Event e) {
		final Treeitem item = (Treeitem) e.getTarget();
		if (item != null) {
			if ((Object) ((TreeNodeImpl) item.getValue()).getEntity() instanceof OrganizationRelation) {
				Treechildren treechildren = item.getTreechildren();
				if (treechildren != null) {
					List<Treeitem> childItemList = treechildren.getChildren();
					if (childItemList != null) {
						for (Treeitem ti : childItemList) {
							Object object = ((TreeNodeImpl) ti.getValue())
									.getEntity();
							OrganizationRelation organizationRelation = null;
							if (object != null
									&& object instanceof OrganizationRelation) {
								organizationRelation = (OrganizationRelation) object;
							}
							if (organizationRelation != null) {
								if (organizationRelation.isCompany()) {
									List list = ti.getTreerow().getChildren();
									Treecell cell = null;
									if (list != null) {
										cell = (Treecell) list.get(0);
									}
									if (cell != null) {
										cell.setStyle("color:blue");
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 电信管理区域初始化：打开安徽
	 * 
	 * @param item
	 * @param treeNode
	 */
	private void telcomRegionTreeInit(Treeitem item, TreeNode treeNode) {
		if (treeNode instanceof TreeNodeImpl) {
			TreeNodeImpl tni = (TreeNodeImpl) treeNode;
			if (tni.getEntity() instanceof TelcomRegion) {
				TelcomRegion telcomRegion = (TelcomRegion) tni.getEntity();
				/**
				 * 中国要先打开才能打开下级的安徽
				 */
				if ("8100000".equals(telcomRegion.getRegionCode())) {
					item.setOpen(true);
				}
				/**
				 * 安徽省
				 */
				if ("8340000".equals(telcomRegion.getRegionCode())) {
					item.setOpen(true);
				}
			}
		}
	}

	/**
	 * 岗位树初始化
	 * 
	 * @param item
	 * @param treeNode
	 */
	private void positionTreeInit(Treeitem item, TreeNode treeNode) {
		if (treeNode instanceof TreeNodeImpl) {
			TreeNodeImpl tni = (TreeNodeImpl) treeNode;
			if (tni.getEntity() instanceof Position) {
				Position position = (Position) tni.getEntity();
				/**
				 * 岗位的都默认打开
				 */
				item.setOpen(true);
				Tree tree = item.getTree();
				/**
				 * (可多选的时候，其实就是positionTreeBandbox)非末级节点，不可选;岗位页面还是要非末级可选
				 */
				if (treeNode.getChildCount() > 0 && tree != null
						&& tree.isMultiple()) {
					item.setCheckable(false);
				}
			}
		}
	}
}
