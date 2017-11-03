package cn.ffcs.uom.systemconfig.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

public interface IdentityCardConfigDao extends BaseDao {
	/**
	 * 分页取类信息
	 * 
	 * @param identityCardConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByIdentityCardConfig(
			IdentityCardConfig identityCardConfig, int currentPage, int pageSize);

	/**
	 * 保存记录
	 * 
	 * @param identityCardConfig
	 */
	public void saveIdentityCardConfig(IdentityCardConfig identityCardConfig);

	/**
	 * 更新记录
	 * 
	 * @param identityCardConfig
	 */
	public void updateIdentityCardConfig(IdentityCardConfig identityCardConfig);

	/**
	 * 删除记录
	 * 
	 * @param identityCardConfig
	 */
	public void removeIdentityCardConfig(IdentityCardConfig identityCardConfig);

	public List<NodeVo> getValuesList();

	public List<NodeVo> getValuesList(Long identityCardId);

	public IdentityCardConfig getIdentityCardConfig(Long identityCardId);

	public List getIdentityCardTempList(IdentityCardConfig identityCardConfig,
			Long identityCardCount);

	/**
	 * 根据身份证类型前缀获取身份证类型ID.
	 * 
	 * @param cardPrefix
	 * @return
	 */
	public Long getCardConfigIdByCardPrefix(String cardPrefix);

}
