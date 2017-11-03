package cn.ffcs.uom.grid.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 网格请求报文 .
 */
public class RootRequest {
	@Getter
	@Setter
	private TcpContRequest tcpCont;
	@Getter
	@Setter
	private SvcContRequest svcCont;
}
