package cn.ffcs.uom.grid.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 网格响应报文 .
 */
public class RootResponse {
	@Getter
	@Setter
	private TcpContResponse tcpCont;
	@Getter
	@Setter
	private SvcContResponse svcCont;
}
