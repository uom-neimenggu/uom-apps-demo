package cn.ffcs.uom.common.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于比较数据结构相同，找出属性值不同的属性，并保存下来。
 * 
 * @author zhulintao
 * 
 */
public class DiverseAttribute implements Serializable {

	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String tableName;
	@Getter
	@Setter
	private String className;
	@Getter
	@Setter
	private String attrName;
	@Getter
	@Setter
	private String attrNewVaule;
	@Getter
	@Setter
	private String attrOldVaule;
	@Getter
	@Setter
	private boolean difference;

}
