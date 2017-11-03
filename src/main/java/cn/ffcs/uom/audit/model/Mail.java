package cn.ffcs.uom.audit.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Mail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String from;
	@Getter
	@Setter
	private String displayName;
	@Getter
	@Setter
	private String to;
	@Getter
	@Setter
	private String subject;
	@Getter
	@Setter
	private String content;
}
