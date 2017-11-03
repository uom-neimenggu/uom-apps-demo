package cn.ffcs.uac.staff.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class DemoStaff  implements Serializable {

	private static final long serialVersionUID = 6888026211320801819L;
	@Getter
	@Setter
	private String staffId;
	@Getter
	@Setter
	private String cretNumber;
	@Getter
	@Setter
	private String phoneNumber;
	@Getter
	@Setter
	private String staffName;
	@Getter
	@Setter
	private String genter;
	@Getter
	@Setter
	private String staffCode;
	@Getter
	@Setter
	private String email;
	@Getter
	@Setter
	private String status;
	@Getter
	@Setter
	private String type;
	
	
	public DemoStaff() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return 
	 */
	public static DemoStaff newInstance() {
		return new DemoStaff();
	}
	
}
