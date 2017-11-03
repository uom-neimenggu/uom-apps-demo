package cn.ffcs.uom.common.util;

import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.zkplus.ZKRebuildUuid;

/**
 * Combobox组件.
 * 
 * @author zfz
 * @version Revision 1.0.0
 */
public final class ComboboxUtils {

	/**
	 * .
	 */
	private ComboboxUtils() {

	}

	/**
	 * 绑定combobox.
	 * 
	 * @param combobox
	 *            Combobox
	 * @param codes
	 *            List<NodeVo>
	 * @param empty
	 *            boolean
	 * @param all
	 *            boolean
	 */
	public static void rendererByCode(Combobox combobox, List<NodeVo> codes,
			boolean empty, boolean all) {
		if (combobox == null || codes == null) {
			return; // throw new IllegalArgumentException("传入的组件或者是值编码不能为空!");
		}
		if (empty) {
			new Comboitem().setParent(combobox);
		}
		if (all) {
			new Comboitem("").setParent(combobox);
		}
		Comboitem defaultItem = null;
		if (combobox.getChildren() != null && combobox.getChildren().size() > 0) {
			combobox.getChildren().clear();
		}
		for (NodeVo code : codes) {
			Comboitem item = new Comboitem(code.getName());

			// 压力测试
			ZKRebuildUuid.rebuildUuidById(item, "ci_" + combobox.getId()
					+ code.getId());

			item.setValue(code.getId());
			// 2012-03-31 刘壮飞 增加取值提示
			item.setTooltiptext(code.getId());
			item.setParent(combobox);
			if (code.isDefault()) {
				defaultItem = item;
			}
		}
		if (defaultItem != null) {
			combobox.setSelectedItem(defaultItem);
		}
	}

	/**
	 * .
	 * 
	 * @param combobox
	 *            Combobox
	 * @param nodeVos
	 *            List<NodeVo>
	 */
	public static void rendererForQuery(Combobox combobox, List<NodeVo> nodeVos) {
		rendererByCode(combobox, nodeVos, false, true);
	}

	/**
	 * .
	 * 
	 * @param combobox
	 *            Combobox
	 * @param nodeVos
	 *            List<NodeVo>
	 */
	public static void rendererForEdit(Combobox combobox, List<NodeVo> nodeVos) {
		rendererByCode(combobox, nodeVos, true, false);
	}

	/**
	 * .
	 * 
	 * @param combobox
	 *            Combobox
	 * @param value
	 *            String
	 */
	public static void setSelected(Combobox combobox, String value) {
		if (combobox == null) {
			return;
		}
		if (StrUtil.isEmpty(value)) {
			return;
		}
		int itemCount = combobox.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			Comboitem item = combobox.getItemAtIndex(i);
			if (item != null && value.equals(item.getValue())) {
				combobox.setSelectedItem(item);
				combobox.setValue(item.getLabel());
				break;
			}
		}
	}

	/**
	 * 隐藏某一选项 .
	 * 
	 * @param combobox
	 *            Combobox
	 * @param value
	 *            String
	 * @author g.huangwch 2011-12-9 g.huangwch
	 */
	public static void hidenComboitem(Combobox combobox, String value) {
		if (combobox == null) {
			return;
		}
		if (StrUtil.isEmpty(value)) {
			return;
		}
		int itemCount = combobox.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			Comboitem item = combobox.getItemAtIndex(i);
			if (item != null && value.equals(item.getValue())) {
				item.setVisible(false);
				break;
			}
		}
	}
}
