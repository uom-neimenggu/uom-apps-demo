package cn.ffcs.uom.mail.model;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GroupMailRootInParam {

	@Setter
	@Getter
	private String sysId;

	@Setter
	@Getter
	private String appSignature;

}
