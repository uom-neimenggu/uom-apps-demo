package cn.ffcs.uom.dataPermission.vo;

import cn.ffcs.raptornuke.portal.model.Role;
import lombok.Getter;
import lombok.Setter;

public class RoleAdapter {
	@Getter
	@Setter
	private String title;
	
	@Getter
	@Setter
	private String description;
	
	@Getter
	@Setter
	private Boolean isSystemRole;
	
	@Getter
	@Setter
	private Role role;
}
