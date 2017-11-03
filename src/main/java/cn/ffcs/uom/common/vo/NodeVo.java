package cn.ffcs.uom.common.vo;


/**
 * 
 * 节点对象Vo.
 * 
 * 
 */
public class NodeVo {
	/**
	 * 标识.
	 */
	private String id;
	/**
	 * 名称.
	 */
	private String name;

	/**
	 * 是否默认选中
	 */
	private boolean isDefault = false;

	/**
	 * 描述.
	 */
	private String desc;

	public NodeVo() {

	}

	public NodeVo(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(final String desc) {
		this.desc = desc;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
