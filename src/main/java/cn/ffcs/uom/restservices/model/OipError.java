package cn.ffcs.uom.restservices.model;

import lombok.Getter;
import lombok.Setter;

public class OipError {

	@Setter
	@Getter
	private String errorCode;

	@Setter
	@Getter
	private String errorDesc;

}
