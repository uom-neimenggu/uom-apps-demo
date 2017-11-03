package cn.ffcs.uom.organization.util;

import java.text.DecimalFormat;
import java.util.List;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;

public class OrgFullCodeUtil {
	/**
	 * 组织集体1位编码格式.
	 */
	private static final DecimalFormat ORG_GROUP_CODE_1 = new DecimalFormat("0");
	/**
	 * 组织集体2位编码格式.
	 */
	private static final DecimalFormat ORG_GROUP_CODE_2 = new DecimalFormat(
			"00");
	/**
	 * 组织集体3位编码格式.
	 */
	private static final DecimalFormat ORG_GROUP_CODE_3 = new DecimalFormat(
			"000");
	/**
	 * 组织集体全编码前尾数位数.
	 */
	private static final int ORG_GROUP_PREFIX_LENGTH = 6;
	/**
	 * 组织集体全编码后尾数位数.
	 */
	private static final int ORG_GROUP_SUBFIX_LENGTH = 15;

	/**
	 * 
	 * 
	 * 功能描述：取得组织编码的集团编码规则编码信息.
	 * 
	 * 组织机构层级编码由八段构成，与各省的组织结构相对应，举例如下： ## ## ## ### ### ### [### [###]]
	 * 前六段编码为当前使用，后两段编码为扩展编码，当省公司有需求时，可以使用扩展码对省公司部门信息进行扩展。
	 * 前三段编码为公司编码，采用两位编码，分别对应省
	 * （自治区、直辖市）公司编码、地（市）公司编码及县（市）公司编码，第四段至第六段编码为部门编码，采用三位编码
	 * ，分别对应上述各级公司的一级部门编码、二级部门编码和三级部门编码。 如加上各级公司的直属公司，上述编码详细描述如下：
	 * 第一段编码为集团总部、省（自治区、直辖市）公司编码； 第二段编码为集团公司本部直属单位，省（自治区、直辖市）直属公司、地（市）公司编码；
	 * 第三段编码为地（市）直属公司、县（市）公司编码；
	 * 第四段编码为集团公司本部、省（自治区、直辖市）公司、地（市）公司、县（市）公司的一级部门编码，以及集团公司本部直属单位
	 * 、省（自治区、直辖市）直属公司、地（市）直属公司的一级部门编码。县（市）公司的直属公司编码也存储在第四段编码上。为区分出该机构为公司还是部门，
	 * 第四段三位编码的第一位以英文C和数字来进行标识，C代表该机构为公司，数字代表为部门。
	 * 第五段编码为集团公司本部、省（自治区、直辖市）公司、地（市）公司及县
	 * （市）公司的二级部门编码，以及集团公司本部直属单位、省（自治区、直辖市）直属公司
	 * 、地（市）直属公司的二级部门编码。县（市）公司直属公司的一级部门编码也存储在第五段编码上
	 * 。为区分出该机构为公司还是部门，第五段三位编码的第一位以英文C和数字来进行标识，C代表该机构为公司，数字代表为部门。
	 * 第六段编码为省（自治区、直辖市
	 * ）公司、地（市）公司及县（市）公司的更小组织单元编码，以及集团公司本部直属单位、省（自治区、直辖市）直属公司、地（市）
	 * 直属公司的更小组织单元编码。县
	 * （市）公司直属公司的二级部门编码也存储在第六段编码上。为区分出该机构为公司还是部门，第六段三位编码的第一位以英文C和数字来进行标识
	 * ，C代表该机构为公司，数字代表为部门。
	 * 
	 * 第一段编码由集团公司统一制定，见“省公司编码表”，第二段及以后编码由省公司自行编码。组织机构编码整体必须唯一，不能重复。
	 * 
	 * 安徽省为11
	 * 
	 * @param parentOrg
	 *            上级组织
	 * @param org
	 *            当前级组织
	 * 
	 * @return String[0]-集团编码层次、String[1]-集团编码中的本级编码、String[2]-集团编码全码
	 * 
	 * 
	 */
	public static synchronized String[] getGroupOrgCodeInfo(
			Organization parentOrg, Organization org) throws Exception {
		String[] info = new String[] { "", "", "" };
		// 是否是单位
		boolean isCompany = org.isCompany();
		// 是否是部门
		boolean isDepartment = org.isDepartment();
		// 是否是团队(团队当部门处理)
		boolean isTeam = org.isTeam();
		// 不是部门或者单位无需编码
		if (!isCompany && !isDepartment && !isTeam) {
			return info;
		}
		// 1:第几段编码:父类直接加1(对应组织级别)
		if (parentOrg.getOrgGroupCodeLevel() != null) {
			info[0] = "" + (new Integer(parentOrg.getOrgGroupCodeLevel()) + 1);
		} else {
			throw new Exception("上级组织级别为空");
		}
		String seqNO = parentOrg.getMaxSubOrgGroupCodeSalevelCode();
		seqNO = seqNO.replaceAll("C", "");
		// 2:单位和公司的本级编码
		if (isCompany) {

			info[1] = ORG_GROUP_CODE_2.format(Long.valueOf(seqNO));

			if (new Integer(info[0]) > 3) {// 如果在第三层之后出现公司类型的组织，则在原来两位数的基础上，在其前面加字母C.
				info[1] = "C" + info[1];
			}

		} else if (isDepartment || isTeam) {
			info[1] = ORG_GROUP_CODE_3.format(Long.valueOf(seqNO));
		}
		// 3-1:拼凑全编码
		StringBuilder fullCode = new StringBuilder(ORG_GROUP_SUBFIX_LENGTH);
		List<Organization> parentList = parentOrg.getTreeParentOrgList();
		parentList.add(parentOrg);
		if (parentList != null) {
			for (int i = 0; i < parentList.size(); i++) {
				if (parentList.get(i) != null
						&& (OrganizationConstant.ROOT_TREE_ORG_ID
								.equals(parentList.get(i).getOrgId())
								|| OrganizationConstant.ROOT_EDW_ORG_ID
										.equals(parentList.get(i).getOrgId()) || OrganizationConstant.ROOT_MARKETING_ORG_ID
									.equals(parentList.get(i).getOrgId())|| OrganizationConstant.ROOT_COST_ORG_ID
									.equals(parentList.get(i).getOrgId()))) {
					continue;
				}
				if (parentList.get(i) != null
						&& StrUtil.isEmpty(parentList.get(i)
								.getOrgGroupCodeSalevelCode())) {
					throw new Exception("上级树组织:" + parentList.get(i).getOrgId()
							+ "集团简码出现空");
				}

				Organization departmentOrTeam = parentList.get(i);
				// 是否是部门
				boolean isParentDepartment = departmentOrTeam.isDepartment();
				// 是否是团队(团队当部门处理)
				boolean isParentTeam = departmentOrTeam.isTeam();

				if (isParentDepartment || isParentTeam) {// 解决父组织中存在组织是部门或团队，且本级组织也是部门或团队时，生成集团编码错误的BUG
					if (fullCode.length() < 6) {

						if (info[1].length() < 3) {// 解决上组织是部门或团队且本级组织层级小于或等于3时，组织类型为公司是的简码处理
							info[1] = "C" + info[1];
						}

						for (int j = fullCode.length(); j < ORG_GROUP_PREFIX_LENGTH; j++) {
							fullCode.append("0");
						}

					}
				}

				fullCode.append(parentList.get(i).getOrgGroupCodeSalevelCode());

			}
		}
		// 3-2:再加上本级编码组合[公司只用前3段-6位]
		if (isCompany) {
			fullCode.append(info[1]);
		}
		// 3-3:判断前3段是否已满6位(000000)
		int length = fullCode.length();
		if (length < 6) {
			for (int i = 0; i < ORG_GROUP_PREFIX_LENGTH - length; i++) {
				fullCode.append("0");
			}
		}
		// 补齐后尾数位[部门只用后3段-7位以后]
		if (isDepartment || isTeam) {
			fullCode.append(info[1]);
		}
		length = fullCode.length();
		if (length < ORG_GROUP_SUBFIX_LENGTH) {
			for (int i = 0; i < ORG_GROUP_SUBFIX_LENGTH - length; i++) {
				fullCode.append("0");
			}
		}
		// 4:集体全编码
		info[2] = fullCode.toString();
		return info;
	}
}
