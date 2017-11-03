package cn.ffcs.uom.common.util;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import cn.ffcs.uom.common.vo.NodeVo;

/**
 * .
 * 
 * @author zfz
 * @version Revision 1.0.0
 */
public final class ListboxUtils {

	/**
	 * .
	 */
	private ListboxUtils() {

	}

	/**
	 * 方法功能: 按照传入条件，控件对象，似乎否空，是否全部来进行下拉框的取值设置.
	 * 
	 * @param
	 * @return
	 * @param listbox
	 *            Listbox
	 * @param codes
	 *            List<NodeVo>
	 * @param empty
	 *            boolean
	 * @param all
	 *            boolean
	 * @author: Liuzhuangfei
	 * @修改记录： ==============================================================<br>
	 *        日期:2010-12-22 Liuzhuangfei 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void rendererByCode(Listbox listbox, List<NodeVo> codes,
			boolean empty, boolean all) {
		if (listbox != null && listbox.getItems() != null
				&& listbox.getItems().size() > 0) {
			listbox.getItems().clear();
		}
		if (listbox == null || codes == null) {
			return; // throw new IllegalArgumentException("传入的组件或者是值编码不能为空!");
		}
		if (empty) {
			new Listitem().setParent(listbox);
		}
		if (all) {
			new Listitem("全部").setParent(listbox);
		}
		for (NodeVo code : codes) {
			Listitem item = new Listitem(code.getName());
			// 2012-03-31 刘壮飞 增加取值提示
			item.setTooltiptext(code.getId());
			item.setValue(code.getId());
			item.setParent(listbox);
			if (code.isDefault()) {
				item.setSelected(true);
			}
		}
		if (listbox.getItems().size() != 0 && listbox.getSelectedItem() == null) {
			listbox.setSelectedIndex(0);
		}
	}
	
	/**
	 * 方法功能: 按照传入条件，控件对象，似乎否空，是否全部来进行下拉框的取值设置. 不默认选中空行
	 * @param listbox
	 * @param codes
	 * @param empty
	 * @param all
	 */
	public static void rendererByCodeNoSelected(Listbox listbox, List<NodeVo> codes,
			boolean empty, boolean all) {
		if (listbox != null && listbox.getItems() != null
				&& listbox.getItems().size() > 0) {
			listbox.getItems().clear();
		}
		if (listbox == null || codes == null) {
			return; // throw new IllegalArgumentException("传入的组件或者是值编码不能为空!");
		}
		if (empty) {
			new Listitem().setParent(listbox);
		}
		if (all) {
			new Listitem("全部").setParent(listbox);
		}
		for (NodeVo code : codes) {
			Listitem item = new Listitem(code.getName());
			// 2012-03-31 刘壮飞 增加取值提示
			item.setTooltiptext(code.getId());
			item.setValue(code.getId());
			item.setParent(listbox);
			if (code.isDefault()) {
				item.setSelected(true);
			}
		}
	}

	/**
	 * renderSetIdByIdx.
	 * 
	 * @param listbox
	 *            Listbox
	 * @author wuyx 2011-5-16 wuyx
	 */
	public static void renderSetIdByIdx(Listbox listbox) {
		if (listbox != null) {
			String listboxId = listbox.getId() == null ? "" : listbox.getId()
					+ "";
			if (listbox.getChildren() != null) {
				List subCompList = listbox.getChildren();
				int sum = subCompList.size();
				for (int i = 0; i < sum; i++) {
					Component subComp = (Component) subCompList.get(i);
					if (subComp instanceof Listitem) {
						subComp.setId(listboxId + i);
					}
				}
			}
		}
	}

	/**
	 * .
	 * 
	 * @param listbox
	 *            Listbox
	 * @param nodeVos
	 *            List<NodeVo>
	 */
	public static void rendererForQuery(Listbox listbox, List<NodeVo> nodeVos) {
		rendererByCode(listbox, nodeVos, false, true);
	}

	/**
	 * .
	 * 
	 * @param listbox
	 *            Listbox
	 * @param nodeVos
	 *            List<NodeVo>
	 */
	public static void rendererForEdit(Listbox listbox, List<NodeVo> nodeVos) {
		rendererByCode(listbox, nodeVos, true, false);
	}
	
	public static void rendererForEditNoSelected(Listbox listbox, List<NodeVo> nodeVos) {
		rendererByCodeNoSelected(listbox, nodeVos, true, false);
	}

	/**
	 * .
	 * 
	 * @param listbox
	 *            Listbox
	 * @param nodeVos
	 *            List<NodeVo>
	 */
	public static void rendererForNotNull(Listbox listbox, List<NodeVo> nodeVos) {
		rendererByCode(listbox, nodeVos, false, false);
	}

	/**
	 * 
	 * 方法功能: .
	 * 
	 * @param listbox
	 * @author: chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-5-28 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void clearListbox(Listbox listbox) {
		if (listbox == null || listbox.getItemCount() <= 0) {
			return;
		}
		int len = listbox.getItemCount();
		for (int i = len - 1; i >= 0; i--) {
			listbox.removeItemAt(i);
		}
		ListModel listModel = null;
		listbox.setModel(listModel);

	}

	/**
	 * .
	 * 
	 * @param listbox
	 *            Listbox
	 * @param value
	 *            String
	 */
	public static void selectByCodeValue(Listbox listbox, String value) {
		if (listbox == null) {
			return;
			// throw new IllegalArgumentException("listbox must not null!");
		}
		for (Object object : listbox.getItems()) {
			Listitem item = (Listitem) object;
			String itemValue = (String) item.getValue();
			if (!StrUtil.isEmpty(itemValue)
					&& itemValue.toString().equals(value)) {
				item.setSelected(true);
				return;
			} else if (value == null || "".equals(value)) {
				item.setSelected(true);
				return;
			}
		}
	}

	/**
	 * 获取下拉框值.
	 * 
	 * @param listbox
	 *            Listbox
	 * @return Object
	 * @author wuyx 2011-4-27 wuyx
	 */
	public static Object getSelectedValue(final Listbox listbox) {
		if (listbox != null && listbox.getSelectedItem() != null) {
			return listbox.getSelectedItem().getValue();
		}
		return null;
	}

	/**
	 * 
	 * 方法功能: 取消选中.
	 * 
	 * @param listbox
	 * @author: chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-10-9 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void disSelect(final Listbox listbox) {
		int count = listbox.getSelectedCount();
		if (count < 1) {
			return;
		}
		for (int i = 0; i < count; i++) {
			listbox.getSelectedItem().setSelected(false);
		}
	}

	/**
	 * 控制下拉列表框显示选项
	 * 
	 * @param listbox
	 *            Listbox
	 * @param value
	 *            valueList
	 */
	public static void isVisibleByCodeValue(Listbox listbox,
			List<String> valueList) {
		if (listbox == null) {
			return;
		}
		for (Object object : listbox.getItems()) {
			Listitem item = (Listitem) object;
			String itemValue = (String) item.getValue();
			if (!StrUtil.isEmpty(itemValue)) {
				for (String value : valueList) {
					if (itemValue.toString().equals(value)) {
						item.setVisible(true);
						break;
					} else {
						item.setVisible(false);
					}
				}
			}
		}
	}
}
