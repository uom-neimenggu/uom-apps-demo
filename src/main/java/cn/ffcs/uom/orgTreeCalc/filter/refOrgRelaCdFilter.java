package cn.ffcs.uom.orgTreeCalc.filter;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.StaffOrganization;

public class refOrgRelaCdFilter extends BaseStaffOrgFilter {
	private List<String> relaCds;
	private Logger logger = Logger.getLogger(this.getClass());

	public refOrgRelaCdFilter(List<String> relaCds) {
		this.relaCds = relaCds;
		logger.debug("refOrgRelaCdFilter : relaCds:" + relaCds.toString());
	}

	@Override
	public boolean validate(StaffOrganization entity, Object... args) {
		boolean rtn = false;
		if(args.length > 0) {
			if(relaCds.contains(StrUtil.strnull(args[0]))) {
				rtn = true;
			}
		}
		return rtn && super.validate(entity, args);
	}
}
