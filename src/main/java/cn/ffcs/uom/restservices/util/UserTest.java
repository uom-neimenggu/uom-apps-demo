package cn.ffcs.uom.restservices.util;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonRootName("user")
public class UserTest {

	@Setter
	@Getter
	@JsonIgnore
	private Long userId;

	@Setter
	@Getter
	@JsonProperty("USER_NAME")
	private String userName;

	@Setter
	@Getter
	private Long age;

	@Setter
	@Getter
	@JsonIgnore
	private String sex;

	@Setter
	@Getter
	private String userRole;

	@Setter
	@Getter
	@JsonIgnore
	private String userCode;

}
