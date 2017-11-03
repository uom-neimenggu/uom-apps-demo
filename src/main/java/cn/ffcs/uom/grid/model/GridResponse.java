package cn.ffcs.uom.grid.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 网格响应报文 .
 */
public class GridResponse {
	@Getter
	@Setter
	private String latnId;
	@Getter
	@Setter
	private Long uomOrgId;
	@Getter
	@Setter
	private String uomUuid;
	// 网格编码
	@Getter
	@Setter
	private String gridId;
	// 操作类型 1新增组织 2添加组织关系 3修改组织 4删除组织关系 5删除组织
	@Getter
	@Setter
	private String optType;
	// 1表示容许修改或注销 0表示不容许修改或注销
    @Getter
    @Setter
    private String resultId;
	// 结果编码
	@Getter
	@Setter
	private String resultCode;
	// 结果说明
	@Getter
	@Setter
	private String resultDesc;
}
