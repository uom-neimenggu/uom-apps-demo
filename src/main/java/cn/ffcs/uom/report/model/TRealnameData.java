package cn.ffcs.uom.report.model;

// Generated 2017-6-14 9:21:17 by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class TRealnameData implements Serializable {
	private static final long serialVersionUID = 247293302630068115L;
	
	@Getter
	@Setter
	private BigDecimal id;
	
	@Getter
	@Setter
	private String hrname;
	
	@Getter
	@Setter
	private BigDecimal c1;
	
	@Getter
	@Setter
	private BigDecimal c2;
	
	@Getter
	@Setter
	private BigDecimal c3;
	
	@Getter
	@Setter
	private BigDecimal c4;
	
	@Getter
	@Setter
	private String createDate;
}
