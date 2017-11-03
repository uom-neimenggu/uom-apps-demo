package cn.ffcs.uom.common.treechooser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import cn.ffcs.uom.common.treechooser.component.ICheckHasChildrenCallback;
import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 
 * @author zengzh
 * @date 2013-06-05
 **/
@SuppressWarnings("serial")
public class Node extends AttrValue implements TreeNodeEntity, Serializable {

	//
	private boolean isGetRoot = false;// 设置为true则表示用来获取根节点
	private int level;// 该节点所在层级
	private boolean end;// 是否末端节点

	private TreeChooserBandbox component;// 所在组件

	public TreeChooserBandbox getComponent() {
		return component;
	}

	public void setComponent(TreeChooserBandbox component) {
		this.component = component;
	}

	public void setGetRoot(boolean isGetRoot) {
		this.isGetRoot = isGetRoot;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		if (this.isEnd())
			// 末端节点
			return new ArrayList<TreeNodeEntity>();

		List<Integer> pattern = getSplitedPattern();
		List<Node> nodes = component.getAllNodes();
		Pair<Pattern, Pattern> pair = getChildrenPattern();
		Pattern p = pair.getLeft();
		Pattern p2 = pair.getRight();

		ArrayList<TreeNodeEntity> result = new ArrayList<TreeNodeEntity>();
		for (Node node : nodes) {
			if (this.getAttrValue().equals(node.getAttrValue()))
				continue;
			Matcher m = p.matcher(node.getAttrValue());
			if (m.find()) {
				result.add(node);
				continue;
			}
			Matcher m2 = p2.matcher(node.getAttrValue());
			if (m2.find()) {
				result.add(node);
			}
		}

		// 拷贝参数信息
		for (TreeNodeEntity node : result) {
			final Node node2 = (Node) node;
			node2.setLevel(this.level + 1);
			node2.setComponent(this.getComponent());
			if (node2.level + 1 >= pattern.size()) {
				node2.setEnd(true);
			} else if (!component.isHasChildren(node2.getAttrValue(),
					new ICheckHasChildrenCallback() {
						@Override
						public boolean checkHasChildren() {
							return node2.isHaveChildren();
						}
					})) {
				node2.setEnd(true);
			} else {
				node2.setEnd(false);
			}
		}

		return result;
	}

	private Pair<Pattern, Pattern> getChildrenPattern() {
		// 取出节点
		List<Integer> pattern = getSplitedPattern();
		String head = getHead(pattern);

		int headLen = head.length();
		int childLen = pattern.get(level + 1);
		int len = getTotalLen(pattern);

		// 匹配出子节点（头部（根+自身）固定，孩子可变，后面为填充字符或空）

		String regex = "^" + Pattern.quote(String.valueOf(head)) + ".{"
				+ (childLen) + "}"
				+ Pattern.quote(String.valueOf(component.getFill())) + "{"
				+ (len - headLen - childLen) + "}$";
		String regex2 = "^" + Pattern.quote(String.valueOf(head)) + ".{"
				+ (childLen) + "}$";
		Pattern p = Pattern.compile(regex);
		Pattern p2 = Pattern.compile(regex2);
		return Pair.of(p, p2);
	}

	public boolean isHaveChildren() {
		List<Node> nodes = component.getAllNodes();
		Pair<Pattern, Pattern> pair = getChildrenPattern();
		Pattern p = pair.getLeft();
		Pattern p2 = pair.getRight();

		for (Node node : nodes) {
			if (this.getAttrValue().equals(node.getAttrValue()))
				continue;
			Matcher m = p.matcher(node.getAttrValue());
			if (m.find()) {
				return true;
			}
			Matcher m2 = p2.matcher(node.getAttrValue());
			if (m2.find()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		// 取出所有节点
		List<Node> allNodes = component.getAllNodes();
		List<Integer> pattern = getSplitedPattern();
		int rootLen = pattern.get(0);
		int len = getTotalLen(pattern);

		// 匹配出根节点（根固定，后面为填充字符或空）
		ArrayList<TreeNodeEntity> result = new ArrayList<TreeNodeEntity>();
		String regex = "^.{" + rootLen + "}"
				+ Pattern.quote(String.valueOf(component.getFill())) + "{"
				+ (len - rootLen) + "}$";
		String regex2 = "^.{" + rootLen + "}$";
		Pattern p = Pattern.compile(regex);
		Pattern p2 = Pattern.compile(regex2);

		for (Node node : allNodes) {
			Matcher m = p.matcher(node.getAttrValue());
			if (m.find()) {
				result.add(node);
				continue;
			}
			Matcher m2 = p2.matcher(node.getAttrValue());
			if (m2.find()) {
				result.add(node);
			}
		}

		// 拷贝参数信息
		for (TreeNodeEntity node : result) {
			final Node node2 = (Node) node;
			node2.setLevel(0);
			node2.setComponent(this.getComponent());
			if (node2.level + 1 >= pattern.size()) {
				node2.setEnd(true);
			} else if (!component.isHasChildren(node2.getAttrValue(),
					new ICheckHasChildrenCallback() {
						@Override
						public boolean checkHasChildren() {
							return node2.isHaveChildren();
						}
					})) {
				node2.setEnd(true);
			} else {
				node2.setEnd(false);
			}
		}

		return result;
	}

	@Override
	public String getLabel() {
		return this.getAttrValueName();
	}

	@Override
	public boolean isGetRoot() {
		return isGetRoot;
	}

	/**
	 * 层次格式串转换成List<Integer>
	 * 
	 * @author 曾臻
	 * @date 2013-6-6
	 * @return
	 */
	private List<Integer> getSplitedPattern() {
		String[] split = component.getPattern().split(",");
		List<Integer> list = new ArrayList<Integer>(split.length);
		for (String s : split) {
			int n = Integer.valueOf(s);
			list.add(n);
		}
		return list;
	}

	private int getTotalLen(List<Integer> pattern) {
		int result = 0;
		for (int n : pattern) {
			result += n;
		}
		return result;
	}

	/**
	 * 获取本节点编码的头部串（后面通常是0或空）
	 * 
	 * @author 曾臻
	 * @date 2013-6-6
	 * @return
	 */
	private String getHead(List<Integer> pattern) {
		int headLen = 0;
		for (int i = 0; i <= level; i++) {
			headLen += pattern.get(i);
		}
		String result = this.getAttrValue().substring(0, headLen);
		return result;
	}

}
