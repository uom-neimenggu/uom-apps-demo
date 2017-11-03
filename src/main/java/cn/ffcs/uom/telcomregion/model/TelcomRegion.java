package cn.ffcs.uom.telcomregion.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;

/**
 * 电信管理区域实体.
 * 
 * @author
 * 
 **/
public class TelcomRegion extends UomEntity implements TreeNodeEntity,
		Serializable {
	/**
	 * 电信管理区域标识
	 */
	public Long getTelcomRegionId() {
		return super.getId();
	}

	public void setTelcomRegionId(Long telcomRegionId) {
		super.setId(telcomRegionId);
	}

	/**
	 * 电信区域编码.
	 **/
	@Getter
	@Setter
	private String regionCode;
	/**
	 * 电信区域名称.
	 **/
	@Getter
	@Setter
	private String regionName;
	/**
	 * 电信区域描述.
	 **/
	@Getter
	@Setter
	private String regionDesc;
	/**
	 * 电信区域类型.
	 **/
	@Getter
	@Setter
	private String regionType;
	/**
	 * 电信区域简拼.
	 **/
	@Getter
	@Setter
	private String regionAbbr;

	/**
	 * 上级电信区域标识.
	 **/
	@Getter
	@Setter
	private Long upRegionId;
	/**
	 * 区域组织编码前缀
	 */
	@Getter
	@Setter
	private String preOrgCode;
	/**
	 * 区号
	 */
	@Getter
	@Setter
	private String areaCode;
	/**
	 * 是否根节点
	 */
	@Setter
	private Boolean isRoot = false;
	/**
	 * 根节点
	 */
	@Setter
	private Long rootUpRegionId = 0L;

	/**
	 * 是否是配置界面(忽略数据权时都可使用)
	 */
	@Setter
	@Getter
	private Boolean isConfigPage;

	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		String sql = "SELECT * FROM TELCOM_REGION T WHERE T.STATUS_CD=? AND T.UP_REGION_ID=?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getTelcomRegionId());
		return (ArrayList<TreeNodeEntity>) DefaultDaoFactory.getDefaultDao()
				.jdbcFindList(sql, params, TelcomRegion.class);
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.regionName)) {
			return this.regionName;
		}
		return "";
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		/**
		 * 配置界面
		 */
		if (this.getIsConfigPage() != null && this.getIsConfigPage()) {
			String sql = "SELECT * FROM TELCOM_REGION T WHERE T.STATUS_CD=? AND T.UP_REGION_ID=?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.rootUpRegionId);
			return (ArrayList<TreeNodeEntity>) DefaultDaoFactory
					.getDefaultDao().jdbcFindList(sql, params,
							TelcomRegion.class);
		} else {
			if (PlatformUtil.getCurrentUser() != null) {
				Long telecomRegionId = null;
				/**
				 * 数据权限：电信管理区域
				 */
				if (PlatformUtil.isAdmin()) {
					/**
					 * 如果是admin
					 */
					telecomRegionId = TelecomRegionConstants.ROOT_TELECOM_REGION_ID;
				} else {
					telecomRegionId = PermissionUtil
							.getPermissionTelcomRegionId(PlatformUtil
									.getCurrentUser().getRoleIds());
				}
				if (telecomRegionId != null) {
					String sql = "SELECT * FROM TELCOM_REGION T WHERE T.STATUS_CD=? AND T.TELCOM_REGION_ID=?";
					List params = new ArrayList();
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(telecomRegionId);
					return (ArrayList<TreeNodeEntity>) DefaultDaoFactory
							.getDefaultDao().jdbcFindList(sql, params,
									TelcomRegion.class);
				}
			}
			return null;
		}
	}

	@Override
	public boolean isGetRoot() {
		return this.isRoot;
	}

}
