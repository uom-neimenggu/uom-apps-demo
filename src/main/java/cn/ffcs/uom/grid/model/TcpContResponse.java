package cn.ffcs.uom.grid.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 网格响应报文 .
 */
public class TcpContResponse {
	@Getter
	@Setter
	private String systemCode;
	@Getter
	@Setter
	private String transactionID;
	@Getter
	@Setter
	private String reqTime;
	@Getter
	@Setter
	private String rspTime;
	@Getter
	@Setter
	private String action;
	@Getter
	@Setter
	private String busCode;
	@Getter
	@Setter
	private String busDesc;
}
