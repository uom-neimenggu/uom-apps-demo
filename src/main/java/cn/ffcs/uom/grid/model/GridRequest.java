package cn.ffcs.uom.grid.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 网格请求报文 .
 */
public class GridRequest {
	@Getter
	@Setter
	private String latnId;
	@Getter
	@Setter
	private Long uomOrgId;
	@Getter
	@Setter
	private String uomUuid;
	@Getter
	@Setter
	private Long uomRelaOrgId;
	@Getter
	@Setter
	private String uomRelaUuid;
	// 网格编码
	@Getter
	@Setter
	private String gridId;
	//操作类型 1新增组织 2添加组织关系 3修改组织 4删除组织关系 5删除组织
	@Getter
	@Setter
	private String optType;
	// 网格类型
	@Getter
	@Setter
	private String gridType;
	// 网格小类
	@Getter
	@Setter
	private String gridSubType;
	// 网格客户群
	@Getter
	@Setter
	private String mktChannelId;
	@Getter
	@Setter
	private String gridTypeNew;
	@Getter
	@Setter
	private String gridSubTypeNew;
	@Getter
	@Setter
	private String mktChannelIdNew;
}
