package cn.ffcs.uom.systemconfig.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.identity.model.IdentityCardTemp;
import cn.ffcs.uom.systemconfig.dao.IdentityCardConfigDao;
import cn.ffcs.uom.systemconfig.model.IdentityCardConfig;

@Repository("identityCardConfigDao")
public class IdentityCardConfigDaoImpl extends BaseDaoImpl implements
		IdentityCardConfigDao {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public PageInfo queryPageInfoByIdentityCardConfig(
			IdentityCardConfig identityCardConfig, int currentPage, int pageSize) {

		StringBuffer hql = new StringBuffer(
				"FROM IdentityCardConfig WHERE statusCd = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (identityCardConfig != null) {

			if (!StrUtil.isEmpty(identityCardConfig.getIdentityCardName())) {
				hql.append(" AND identityCardName LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(identityCardConfig.getIdentityCardName()) + "%");
			}

			if (!StrUtil.isEmpty(identityCardConfig.getIdentityCardPrefix())) {
				hql.append(" AND identityCardPrefix LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(identityCardConfig.getIdentityCardPrefix())
						+ "%");
			}

		}

		hql.append(" ORDER BY identityCardId");

		return identityCardConfig.repository().findPageInfoByHQLAndParams(
				hql.toString(), params, currentPage, pageSize);

	}

	@Override
	public List<NodeVo> getValuesList() {

		List params = new ArrayList();
		String sql = "SELECT * FROM IDENTITY_CARD_CONFIG A WHERE A.STATUS_CD = ? AND A.IDENTITY_CARD_SWITCH = ? ORDER BY IDENTITY_CARD_ID ASC";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.SWITCH_OPEN);

		List<IdentityCardConfig> identityCardConfigList = IdentityCardConfig
				.repository().jdbcFindList(sql, params,
						IdentityCardConfig.class);
		List<NodeVo> retAttrValues = new ArrayList();
		if (identityCardConfigList != null) {
			for (IdentityCardConfig identityCardConfig : identityCardConfigList) {
				if (identityCardConfig != null) {
					NodeVo vo = new NodeVo();
					vo.setId(identityCardConfig.getIdentityCardId().toString());
					vo.setName(identityCardConfig.getIdentityCardName().trim());
					retAttrValues.add(vo);
				}
			}
		}
		return retAttrValues;
	}

	@Override
	public List<NodeVo> getValuesList(Long identityCardId) {

		List params = new ArrayList();
		String sql = "SELECT * FROM IDENTITY_CARD_CONFIG A WHERE A.STATUS_CD = ? AND IDENTITY_CARD_ID = ? AND A.IDENTITY_CARD_SWITCH = ? ORDER BY IDENTITY_CARD_ID ASC";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(identityCardId);
		params.add(BaseUnitConstants.SWITCH_OPEN);

		List<IdentityCardConfig> identityCardConfigList = IdentityCardConfig
				.repository().jdbcFindList(sql, params,
						IdentityCardConfig.class);
		List<NodeVo> retAttrValues = new ArrayList();
		if (identityCardConfigList != null) {
			for (IdentityCardConfig identityCardConfig : identityCardConfigList) {
				if (identityCardConfig != null) {
					NodeVo vo = new NodeVo();
					vo.setId(identityCardConfig.getIdentityCardId().toString());
					vo.setName(identityCardConfig.getIdentityCardName().trim());
					retAttrValues.add(vo);
				}
			}
		}
		return retAttrValues;
	}

	@Override
	public void saveIdentityCardConfig(IdentityCardConfig identityCardConfig) {
		identityCardConfig.addOnly();
	}

	@Override
	public void updateIdentityCardConfig(IdentityCardConfig identityCardConfig) {
		identityCardConfig.updateOnly();
	}

	@Override
	public void removeIdentityCardConfig(IdentityCardConfig identityCardConfig) {
		identityCardConfig.removeOnly();
	}

	@Override
	public IdentityCardConfig getIdentityCardConfig(Long identityCardId) {
		String hql = "from IdentityCardConfig s where s.identityCardId = ?";
		List<IdentityCardConfig> liIdentityCardConfig = getHibernateTemplate()
				.find(hql, new Object[] { identityCardId });
		if (null != liIdentityCardConfig && liIdentityCardConfig.size() > 0) {
			return liIdentityCardConfig.get(0);
		}
		return null;
	}

	@Override
	public List getIdentityCardTempList(IdentityCardConfig identityCardConfig,
			Long identityCardCount) {
		List identityCardTempList = new ArrayList();
		for (int i = 0; i < identityCardCount; i++) {
			IdentityCardTemp identityCardTemp = new IdentityCardTemp();
			identityCardTemp.setIdentityCardName(identityCardConfig
					.getIdentityCardName());
			identityCardTemp.setIdentityCardNumber(DefaultDaoFactory
					.getDefaultDao().genTransId(
							identityCardConfig.getIdentityCardPrefix(), 18,
							"SEQ_TEMP_IDENTITY_CARD_ID"));
			identityCardTemp.setCreateDate(new Date());
			identityCardTempList.add(identityCardTemp);
		}
		return identityCardTempList;
	}

	/**
	 * ������֤����ǰ׺��ȡ���֤����ID.
	 * 
	 * @param cardPrefix
	 * @return
	 */
	@Override
	public Long getCardConfigIdByCardPrefix(String cardPrefix) {
		String sql = "select identity_card_id from identity_card_config where identity_card_prefix  = ? and status_cd = ? and identity_card_switch = ?";
		return jdbcTemplate.queryForLong(sql, new Object[] { cardPrefix,
				BaseUnitConstants.ENTT_STATE_ACTIVE,
				BaseUnitConstants.SWITCH_OPEN });
	}
}
