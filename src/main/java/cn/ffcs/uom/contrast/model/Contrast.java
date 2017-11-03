package cn.ffcs.uom.contrast.model;

import java.io.Serializable;
import java.util.Date;

import cn.ffcs.uom.common.model.UomEntity;

import lombok.Getter;
import lombok.Setter;

public class Contrast extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long uomStaffId;

	@Getter
	@Setter
	private Long ossStaffId;

	@Getter
	@Setter
	private Long isType;

	@Getter
	@Setter
	private String uuId;

	@Getter
	@Setter
	private Long staffFixId;

	@Getter
	@Setter
	private String uomNbr;

	@Getter
	@Setter
	private String ossNbr;

	@Getter
	@Setter
	private String uomAccount;

	@Getter
	@Setter
	private String ossAccount;

	@Getter
	@Setter
	private Long flag;

	@Getter
	@Setter
	private String ossName;

	@Getter
	@Setter
	private String ossCertNumber;

	@Getter
	@Setter
	private String remark;

}
