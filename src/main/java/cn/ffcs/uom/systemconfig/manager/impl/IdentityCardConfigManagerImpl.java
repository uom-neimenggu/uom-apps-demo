package cn.ffcs.uom.systemconfig.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.dao.IdentityCardConfigDao;
import cn.ffcs.uom.systemconfig.manager.IdentityCardConfigManager;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

@Service("identityCardConfigManager")
@Scope("prototype")
public class IdentityCardConfigManagerImpl implements IdentityCardConfigManager {
	@Resource
	private IdentityCardConfigDao identityCardConfigDao;

	@Override
	public PageInfo queryPageInfoByIdentityCardConfig(
			IdentityCardConfig identityCardConfig, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		return identityCardConfigDao.queryPageInfoByIdentityCardConfig(
				identityCardConfig, currentPage, pageSize);
	}

	@Override
	public void saveIdentityCardConfig(IdentityCardConfig identityCardConfig) {
		// TODO Auto-generated method stub
		identityCardConfigDao.saveIdentityCardConfig(identityCardConfig);
	}

	@Override
	public void updateIdentityCardConfig(IdentityCardConfig identityCardConfig) {
		// TODO Auto-generated method stub
		identityCardConfigDao.updateIdentityCardConfig(identityCardConfig);
	}

	@Override
	public void removeIdentityCardConfig(IdentityCardConfig identityCardConfig) {
		// TODO Auto-generated method stub
		identityCardConfigDao.removeIdentityCardConfig(identityCardConfig);

	}

	@Override
	public List<NodeVo> getValuesList() {
		// TODO Auto-generated method stub
		return identityCardConfigDao.getValuesList();
	}

	@Override
	public List<NodeVo> getValuesList(Long identityCardId) {
		// TODO Auto-generated method stub
		return identityCardConfigDao.getValuesList(identityCardId);
	}

	@Override
	public IdentityCardConfig getIdentityCardConfig(Long identityCardId) {
		// TODO Auto-generated method stub
		return identityCardConfigDao.getIdentityCardConfig(identityCardId);
	}

	@Override
	public List getIdentityCardTempList(IdentityCardConfig identityCardConfig,
			Long identityCardCount) {
		// TODO Auto-generated method stub
		return identityCardConfigDao.getIdentityCardTempList(
				identityCardConfig, identityCardCount);
	}

	/**
	 * ������֤����ǰ׺��ȡ���֤����ID.
	 * 
	 * @param cardPrefix
	 * @return
	 */
	@Override
	public Long getCardConfigIdByCardPrefix(String cardPrefix) {
		return identityCardConfigDao.getCardConfigIdByCardPrefix(cardPrefix);
	}
}
