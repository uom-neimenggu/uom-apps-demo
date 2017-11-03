package cn.ffcs.uom.contrast.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class StaffContrastCrm implements Serializable {
	@Getter
	@Setter
	private Long empeeId;
	@Getter
	@Setter
	private String empeeAcct;
	@Getter
	@Setter
	private String certName;
	@Getter
	@Setter
	private Long staffId;
	@Getter
	@Setter
	private String staffAccount;
	@Getter
	@Setter
	private String staffName;
	@Getter
	@Setter
	private String orgFullName;
}
