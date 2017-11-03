package cn.ffcs.uom.restservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.manager.CascadeRelationManager;
import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.politicallocation.constants.PoliticalLocationConstants;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.restservices.dao.ChannelInfoDao;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.AttrItemInParam;
import cn.ffcs.uom.restservices.model.ChannelAttrInParam;
import cn.ffcs.uom.restservices.model.ChannelInfoInParam;
import cn.ffcs.uom.restservices.model.ContractRootInParam;
import cn.ffcs.uom.restservices.model.ContractRootOutParam;
import cn.ffcs.uom.restservices.model.GrpBusiStore;
import cn.ffcs.uom.restservices.model.GrpBusiStoreAttr;
import cn.ffcs.uom.restservices.model.GrpChannel;
import cn.ffcs.uom.restservices.model.GrpChannelAttr;
import cn.ffcs.uom.restservices.model.GrpChannelBusiStoreRela;
import cn.ffcs.uom.restservices.model.GrpChannelCustomAttr;
import cn.ffcs.uom.restservices.model.GrpChannelOperatorsRela;
import cn.ffcs.uom.restservices.model.GrpChannelRela;
import cn.ffcs.uom.restservices.model.GrpOperators;
import cn.ffcs.uom.restservices.model.GrpOperatorsAttr;
import cn.ffcs.uom.restservices.model.GrpOperatorsCustomAttr;
import cn.ffcs.uom.restservices.model.GrpStaff;
import cn.ffcs.uom.restservices.model.GrpStaffAttr;
import cn.ffcs.uom.restservices.model.GrpStaffChannelRela;
import cn.ffcs.uom.restservices.model.GrpStaffCustomAttr;
import cn.ffcs.uom.restservices.model.OperatorsAttrInParam;
import cn.ffcs.uom.restservices.model.SvcContInParam;
import cn.ffcs.uom.restservices.model.UomModelStorageOutParam;
import cn.ffcs.uom.restservices.vo.ChannelConfigVo;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;
import cn.ffcs.uom.webservices.constants.WsConstants;

@Service("channelInfoManager")
@Scope("prototype")
public class ChannelInfoManagerImpl implements ChannelInfoManager {

	@Resource(name = "cascadeRelationManager")
	private CascadeRelationManager cascadeRelationManager;

	@Resource(name = "partyManager")
	private PartyManager partyManager;

	@Resource(name = "organizationManager")
	private OrganizationManager organizationManager;

	@Resource(name = "organizationRelationManager")
	private OrganizationRelationManager organizationRelationManager;

	@Resource(name = "organizationExtendAttrManager")
	private OrganizationExtendAttrManager organizationExtendAttrManager;

	@Resource(name = "channelInfoDao")
	private ChannelInfoDao channelInfoDao;

	/**
	 * 判断是否是生效状态
	 */
	@Override
	public boolean isActive(String status) {

		if (!StrUtil.isEmpty(status)) {
			if (BaseUnitConstants.ENTT_STATE_INACTIVE.equals(status)
					|| BaseUnitConstants.ENTT_STATE_UNACTIVE.equals(status)
					|| BaseUnitConstants.ENTT_STATE_FILE.equals(status)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 组装报文实体
	 * 
	 * @param operators
	 * @param channel
	 * @return
	 */
	@Override
	public ContractRootInParam contractRootInParamPacking(
			GrpOperators operators, GrpChannel channel) {

		ContractRootInParam rootIn = new ContractRootInParam();
		SvcContInParam svcCont = new SvcContInParam();
		ChannelInfoInParam channelInfo = new ChannelInfoInParam();

		if (operators != null && !StrUtil.isEmpty(operators.getOperatorsNbr())) {

			OperatorsAttrInParam operatorsAttr = new OperatorsAttrInParam();
			List<AttrItemInParam> attrItems = new ArrayList<AttrItemInParam>();

			GrpOperatorsAttr grpOperatorsAttr = new GrpOperatorsAttr();
			grpOperatorsAttr.setOperatorsNbr(operators.getOperatorsNbr());

			List<GrpOperatorsAttr> grpOperatorsAttrList = this
					.queryGrpOperatorsAttrList(grpOperatorsAttr);

			if (grpOperatorsAttrList != null && grpOperatorsAttrList.size() > 0) {
				for (GrpOperatorsAttr grpOperatorsAttrDb : grpOperatorsAttrList) {
					AttrItemInParam attrItem = new AttrItemInParam();
					BeanUtils.copyProperties(attrItem, grpOperatorsAttrDb);
					attrItems.add(attrItem);
				}
			}

			operatorsAttr.setOperatorsNbr(operators.getOperatorsNbr());
			operatorsAttr.setAttrItems(attrItems);

			channelInfo.setOperators(operators);
			channelInfo.setOperatorsAttr(operatorsAttr);

		}

		if (channel != null && !StrUtil.isEmpty(channel.getChannelNbr())) {

			ChannelAttrInParam channelAttr = new ChannelAttrInParam();
			List<AttrItemInParam> attrItems = new ArrayList<AttrItemInParam>();
			List<GrpChannelRela> channelRelas = new ArrayList<GrpChannelRela>();
			List<GrpChannelOperatorsRela> channelOperatorsRelas = new ArrayList<GrpChannelOperatorsRela>();

			GrpChannelAttr grpChannelAttr = new GrpChannelAttr();
			grpChannelAttr.setChannelNbr(channel.getChannelNbr());

			List<GrpChannelAttr> grpChannelAttrList = this
					.queryGrpChannelAttrList(grpChannelAttr);

			if (grpChannelAttrList != null && grpChannelAttrList.size() > 0) {
				for (GrpChannelAttr grpChannelAttrDb : grpChannelAttrList) {
					AttrItemInParam attrItem = new AttrItemInParam();
					BeanUtils.copyProperties(attrItem, grpChannelAttrDb);
					attrItems.add(attrItem);
				}
			}

			channelAttr.setChannelNbr(channel.getChannelNbr());
			channelAttr.setAttrItems(attrItems);

			GrpChannelRela grpChannelRela = new GrpChannelRela();
			grpChannelRela.setChannelNbr(channel.getChannelNbr());
			channelRelas = this.queryGrpChannelRelaList(grpChannelRela);

			GrpChannelOperatorsRela grpChannelOperatorsRela = new GrpChannelOperatorsRela();
			grpChannelOperatorsRela.setChannelNbr(channel.getChannelNbr());
			channelOperatorsRelas = this
					.queryGrpChannelOperatorsRelaList(grpChannelOperatorsRela);

			channelInfo.setChannel(channel);
			channelInfo.setChannelAttr(channelAttr);
			channelInfo.setChannelRelas(channelRelas);
			channelInfo.setChannelOperatorsRelas(channelOperatorsRelas);
		}

		svcCont.setChannelInfo(channelInfo);
		rootIn.setSvcCont(svcCont);

		return rootIn;

	}

	/**
	 * 集团模型入库
	 * 
	 * @param rootIn
	 * @param rootOutParam
	 * @return
	 */
	@Override
	public ContractRootOutParam saveGroupModelStorage(
			ContractRootInParam rootIn, ContractRootOutParam rootOutParam) {

		// 经营主体入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getOperators())) {

			GrpOperators grpOperators = channelInfoDao
					.queryGrpOperatorsByOperatorsNbr(rootIn.getSvcCont()
							.getChannelInfo().getOperators().getOperatorsNbr());
			if (grpOperators != null
					&& grpOperators.getGrpOperatorsId() != null) {

				rootIn.getSvcCont().getChannelInfo().getOperators()
						.setGrpOperatorsId(grpOperators.getGrpOperatorsId());

				// if
				// (BaseUnitConstants.ENTT_STATE_DEL.equals(rootIn.getSvcCont()
				// .getChannelInfo().getOperators().getAction())
				// || !this.isActive(rootIn.getSvcCont().getChannelInfo()
				// .getOperators().getStatusCd())) {
				//
				// rootIn.getSvcCont().getChannelInfo().getOperators()
				// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);

				// 经营主体删除时，其相应的扩展属性也做删除处理
				// GrpOperatorsAttr queryGrpOperatorsAttr = new
				// GrpOperatorsAttr();
				// queryGrpOperatorsAttr.setOperatorsNbr(rootIn.getSvcCont()
				// .getChannelInfo().getOperators().getOperatorsNbr());
				// List<GrpOperatorsAttr> grpOperatorsAttrList =
				// channelInfoDao
				// .queryGrpOperatorsAttrList(queryGrpOperatorsAttr);
				//
				// for (GrpOperatorsAttr grpOperatorsAttr :
				// grpOperatorsAttrList) {
				// grpOperatorsAttr.removeOnly();
				// }

				// } else {
				// rootIn.getSvcCont().getChannelInfo().getOperators()
				// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
				// }

				rootIn.getSvcCont().getChannelInfo().getOperators()
						.updateOnly();
			} else {
				rootIn.getSvcCont().getChannelInfo().getOperators().addOnly();
			}

		}

		// 经营主体扩展属性入库
		// if (!(!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
		// .getOperators()) && (BaseUnitConstants.ENTT_STATE_DEL
		// .equals(rootIn.getSvcCont().getChannelInfo().getOperators()
		// .getAction()) || !this.isActive(rootIn.getSvcCont()
		// .getChannelInfo().getOperators().getStatusCd())))) {

		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getOperatorsAttr())
				&& !StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
						.getOperatorsAttr().getAttrItems())
				&& rootIn.getSvcCont().getChannelInfo().getOperatorsAttr()
						.getAttrItems().size() > 0) {

			for (AttrItemInParam attrItem : rootIn.getSvcCont()
					.getChannelInfo().getOperatorsAttr().getAttrItems()) {
				GrpOperatorsAttr queryGrpOperatorsAttr = new GrpOperatorsAttr();
				BeanUtils.copyProperties(queryGrpOperatorsAttr, attrItem);
				queryGrpOperatorsAttr.setOperatorsNbr(rootIn.getSvcCont()
						.getChannelInfo().getOperatorsAttr().getOperatorsNbr());
				GrpOperatorsAttr grpOperatorsAttr = channelInfoDao
						.queryGrpOperatorsAttr(queryGrpOperatorsAttr);

				if (grpOperatorsAttr != null
						&& grpOperatorsAttr.getGrpOperatorsAttrId() != null) {
					queryGrpOperatorsAttr
							.setGrpOperatorsAttrId(grpOperatorsAttr
									.getGrpOperatorsAttrId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(queryGrpOperatorsAttr.getAction())) {
					// queryGrpOperatorsAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// queryGrpOperatorsAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					queryGrpOperatorsAttr.updateOnly();
				} else {
					queryGrpOperatorsAttr.addOnly();
				}

			}
		}
		// }

		// 经营主体自定义扩展属性入库
		// if (!(!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
		// .getOperators()) && (BaseUnitConstants.ENTT_STATE_DEL
		// .equals(rootIn.getSvcCont().getChannelInfo().getOperators()
		// .getAction()) || !this.isActive(rootIn.getSvcCont()
		// .getChannelInfo().getOperators().getStatusCd())))) {

		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getOperatorsCustomAttr())
				&& !StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
						.getOperatorsCustomAttr().getAttrItems())
				&& rootIn.getSvcCont().getChannelInfo()
						.getOperatorsCustomAttr().getAttrItems().size() > 0) {

			for (AttrItemInParam attrItem : rootIn.getSvcCont()
					.getChannelInfo().getOperatorsCustomAttr().getAttrItems()) {
				GrpOperatorsCustomAttr queryGrpOperatorsCustomAttr = new GrpOperatorsCustomAttr();
				BeanUtils.copyProperties(queryGrpOperatorsCustomAttr, attrItem);
				queryGrpOperatorsCustomAttr.setOperatorsNbr(rootIn.getSvcCont()
						.getChannelInfo().getOperatorsCustomAttr()
						.getOperatorsNbr());
				GrpOperatorsCustomAttr grpOperatorsCustomAttr = channelInfoDao
						.queryGrpOperatorsCustomAttr(queryGrpOperatorsCustomAttr);

				if (grpOperatorsCustomAttr != null
						&& grpOperatorsCustomAttr.getGrpOperatorsCustomAttrId() != null) {
					queryGrpOperatorsCustomAttr
							.setGrpOperatorsCustomAttrId(grpOperatorsCustomAttr
									.getGrpOperatorsCustomAttrId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(queryGrpOperatorsCustomAttr.getAction())) {
					// queryGrpOperatorsCustomAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// queryGrpOperatorsCustomAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					queryGrpOperatorsCustomAttr.updateOnly();
				} else {
					queryGrpOperatorsCustomAttr.addOnly();
				}

			}
		}
		// }

		// 经营场所入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getBusiStore())) {

			GrpBusiStore grpBusiStore = channelInfoDao
					.queryGrpBusiStoreByBusiStoreNbr(rootIn.getSvcCont()
							.getChannelInfo().getBusiStore().getBusiStoreNbr());

			if (grpBusiStore != null
					&& grpBusiStore.getGrpBusiStoreId() != null) {

				rootIn.getSvcCont().getChannelInfo().getBusiStore()
						.setGrpBusiStoreId(grpBusiStore.getGrpBusiStoreId());

				// if
				// (BaseUnitConstants.ENTT_STATE_DEL.equals(rootIn.getSvcCont()
				// .getChannelInfo().getBusiStore().getAction())
				// || !this.isActive(rootIn.getSvcCont().getChannelInfo()
				// .getBusiStore().getStatusCd())) {
				//
				// rootIn.getSvcCont().getChannelInfo().getBusiStore()
				// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);

				// 经营场所删除时，其相应的扩展属性也做删除处理
				// GrpBusiStoreAttr queryGrpBusiStoreAttr = new
				// GrpBusiStoreAttr();
				// queryGrpBusiStoreAttr.setBusiStoreNbr(rootIn.getSvcCont()
				// .getChannelInfo().getBusiStore().getBusiStoreNbr());
				// List<GrpBusiStoreAttr> grpBusiStoreAttrAttrList =
				// channelInfoDao
				// .queryGrpBusiStoreAttrList(queryGrpBusiStoreAttr);
				//
				// for (GrpBusiStoreAttr grpBusiStoreAttr :
				// grpBusiStoreAttrAttrList) {
				// grpBusiStoreAttr.removeOnly();
				// }

				// } else {
				// rootIn.getSvcCont().getChannelInfo().getBusiStore()
				// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
				// }

				rootIn.getSvcCont().getChannelInfo().getBusiStore()
						.updateOnly();
			} else {
				rootIn.getSvcCont().getChannelInfo().getBusiStore().addOnly();
			}

		}

		// 经营场所扩展属性入库
		// if (!(!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
		// .getBusiStore()) && (BaseUnitConstants.ENTT_STATE_DEL
		// .equals(rootIn.getSvcCont().getChannelInfo().getBusiStore()
		// .getAction()) || !this.isActive(rootIn.getSvcCont()
		// .getChannelInfo().getBusiStore().getStatusCd())))) {

		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getBusiStoreAttr())
				&& !StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
						.getBusiStoreAttr().getAttrItems())
				&& rootIn.getSvcCont().getChannelInfo().getBusiStoreAttr()
						.getAttrItems().size() > 0) {

			for (AttrItemInParam attrItem : rootIn.getSvcCont()
					.getChannelInfo().getBusiStoreAttr().getAttrItems()) {
				GrpBusiStoreAttr queryGrpBusiStoreAttr = new GrpBusiStoreAttr();
				BeanUtils.copyProperties(queryGrpBusiStoreAttr, attrItem);
				queryGrpBusiStoreAttr.setBusiStoreNbr(rootIn.getSvcCont()
						.getChannelInfo().getBusiStoreAttr().getBusiStoreNbr());
				GrpBusiStoreAttr grpBusiStoreAttr = channelInfoDao
						.queryGrpBusiStoreAttr(queryGrpBusiStoreAttr);

				if (grpBusiStoreAttr != null
						&& grpBusiStoreAttr.getGrpBusiStoreAttrId() != null) {
					queryGrpBusiStoreAttr
							.setGrpBusiStoreAttrId(grpBusiStoreAttr
									.getGrpBusiStoreAttrId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(queryGrpBusiStoreAttr.getAction())) {
					// queryGrpBusiStoreAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// queryGrpBusiStoreAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					queryGrpBusiStoreAttr.updateOnly();
				} else {
					queryGrpBusiStoreAttr.addOnly();
				}

			}
		}
		// }

		// 渠道单元入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannel())) {

			GrpChannel grpChannel = channelInfoDao
					.queryGrpChannelByChannelNbr(rootIn.getSvcCont()
							.getChannelInfo().getChannel().getChannelNbr());

			if (grpChannel != null && grpChannel.getGrpChannelId() != null) {

				rootIn.getSvcCont().getChannelInfo().getChannel()
						.setGrpChannelId(grpChannel.getGrpChannelId());

				// if
				// (BaseUnitConstants.ENTT_STATE_DEL.equals(rootIn.getSvcCont()
				// .getChannelInfo().getChannel().getAction())
				// || !this.isActive(rootIn.getSvcCont().getChannelInfo()
				// .getChannel().getStatusCd())) {
				//
				// rootIn.getSvcCont().getChannelInfo().getChannel()
				// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);

				// 渠道单元删除时，其相应的扩展属性也做删除处理
				// GrpChannelAttr queryGrpChannelAttr = new
				// GrpChannelAttr();
				// queryGrpChannelAttr.setChannelNbr(rootIn.getSvcCont()
				// .getChannelInfo().getChannel().getChannelNbr());
				// List<GrpChannelAttr> grpChannelAttrList = channelInfoDao
				// .queryGrpChannelAttrList(queryGrpChannelAttr);
				//
				// for (GrpChannelAttr grpChannelAttr : grpChannelAttrList)
				// {
				// grpChannelAttr.removeOnly();
				// }

				// } else {
				// rootIn.getSvcCont().getChannelInfo().getChannel()
				// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
				// }

				rootIn.getSvcCont().getChannelInfo().getChannel().updateOnly();
			} else {
				rootIn.getSvcCont().getChannelInfo().getChannel().addOnly();
			}

		}

		// 渠道单元扩展属性入库
		// if (!(!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
		// .getChannel()) && (BaseUnitConstants.ENTT_STATE_DEL
		// .equals(rootIn.getSvcCont().getChannelInfo().getChannel()
		// .getAction()) || !this.isActive(rootIn.getSvcCont()
		// .getChannelInfo().getChannel().getStatusCd())))) {

		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannelAttr())
				&& !StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
						.getChannelAttr().getAttrItems())
				&& rootIn.getSvcCont().getChannelInfo().getChannelAttr()
						.getAttrItems().size() > 0) {

			for (AttrItemInParam attrItem : rootIn.getSvcCont()
					.getChannelInfo().getChannelAttr().getAttrItems()) {
				GrpChannelAttr queryGrpChannelAttr = new GrpChannelAttr();
				BeanUtils.copyProperties(queryGrpChannelAttr, attrItem);
				queryGrpChannelAttr.setChannelNbr(rootIn.getSvcCont()
						.getChannelInfo().getChannelAttr().getChannelNbr());
				GrpChannelAttr grpChannelAttr = channelInfoDao
						.queryGrpChannelAttr(queryGrpChannelAttr);

				if (grpChannelAttr != null
						&& grpChannelAttr.getGrpChannelAttrId() != null) {
					queryGrpChannelAttr.setGrpChannelAttrId(grpChannelAttr
							.getGrpChannelAttrId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(queryGrpChannelAttr.getAction())) {
					// queryGrpChannelAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// queryGrpChannelAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					queryGrpChannelAttr.updateOnly();
				} else {
					queryGrpChannelAttr.addOnly();
				}

			}

		}
		// }

		// 渠道单元自定义扩展属性入库
		// if (!(!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
		// .getChannel()) && (BaseUnitConstants.ENTT_STATE_DEL
		// .equals(rootIn.getSvcCont().getChannelInfo().getChannel()
		// .getAction()) || !this.isActive(rootIn.getSvcCont()
		// .getChannelInfo().getChannel().getStatusCd())))) {

		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannelCustomAttr())
				&& !StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
						.getChannelCustomAttr().getAttrItems())
				&& rootIn.getSvcCont().getChannelInfo().getChannelCustomAttr()
						.getAttrItems().size() > 0) {

			for (AttrItemInParam attrItem : rootIn.getSvcCont()
					.getChannelInfo().getChannelCustomAttr().getAttrItems()) {
				GrpChannelCustomAttr queryGrpChannelCustomAttr = new GrpChannelCustomAttr();
				BeanUtils.copyProperties(queryGrpChannelCustomAttr, attrItem);
				queryGrpChannelCustomAttr.setChannelNbr(rootIn.getSvcCont()
						.getChannelInfo().getChannelCustomAttr()
						.getChannelNbr());
				GrpChannelCustomAttr grpChannelCustomAttr = channelInfoDao
						.queryGrpChannelCustomAttr(queryGrpChannelCustomAttr);

				if (grpChannelCustomAttr != null
						&& grpChannelCustomAttr.getGrpChannelCustomAttrId() != null) {
					queryGrpChannelCustomAttr
							.setGrpChannelCustomAttrId(grpChannelCustomAttr
									.getGrpChannelCustomAttrId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(queryGrpChannelCustomAttr.getAction())) {
					// queryGrpChannelCustomAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// queryGrpChannelCustomAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					queryGrpChannelCustomAttr.updateOnly();
				} else {
					queryGrpChannelCustomAttr.addOnly();
				}

			}

		}
		// }

		// 员工入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getStaff())) {

			GrpStaff grpStaff = channelInfoDao.queryGrpStaffBySalesCode(rootIn
					.getSvcCont().getChannelInfo().getStaff().getSalesCode());
			if (grpStaff != null && grpStaff.getGrpStaffId() != null) {

				rootIn.getSvcCont().getChannelInfo().getStaff()
						.setGrpStaffId(grpStaff.getGrpStaffId());

				// if
				// (BaseUnitConstants.ENTT_STATE_DEL.equals(rootIn.getSvcCont()
				// .getChannelInfo().getStaff().getAction())
				// || !this.isActive(rootIn.getSvcCont().getChannelInfo()
				// .getStaff().getStatusCd())) {
				//
				// rootIn.getSvcCont().getChannelInfo().getStaff()
				// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);

				// 销售员删除时，其相应的扩展属性也做删除处理
				// GrpStaffAttr queryGrpStaffAttr = new GrpStaffAttr();
				// queryGrpStaffAttr.setSalesCode(rootIn.getSvcCont()
				// .getChannelInfo().getStaff().getSalesCode());
				// List<GrpStaffAttr> grpStaffAttrList = channelInfoDao
				// .queryGrpStaffAttrList(queryGrpStaffAttr);
				//
				// for (GrpStaffAttr grpStaffAttr : grpStaffAttrList) {
				// grpStaffAttr.removeOnly();
				// }

				// } else {
				// rootIn.getSvcCont().getChannelInfo().getStaff()
				// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
				// }

				rootIn.getSvcCont().getChannelInfo().getStaff().updateOnly();
			} else {
				rootIn.getSvcCont().getChannelInfo().getStaff().addOnly();
			}

		}

		// 员工扩展属性入库
		// if (!(!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
		// .getStaff()) && (BaseUnitConstants.ENTT_STATE_DEL.equals(rootIn
		// .getSvcCont().getChannelInfo().getStaff().getAction()) || !this
		// .isActive(rootIn.getSvcCont().getChannelInfo().getStaff()
		// .getStatusCd())))) {

		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getStaffAttr())
				&& !StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
						.getStaffAttr().getAttrItems())
				&& rootIn.getSvcCont().getChannelInfo().getStaffAttr()
						.getAttrItems().size() > 0) {

			for (AttrItemInParam attrItem : rootIn.getSvcCont()
					.getChannelInfo().getStaffAttr().getAttrItems()) {
				GrpStaffAttr queryGrpStaffAttr = new GrpStaffAttr();
				BeanUtils.copyProperties(queryGrpStaffAttr, attrItem);
				queryGrpStaffAttr.setSalesCode(rootIn.getSvcCont()
						.getChannelInfo().getStaffAttr().getSalesCode());
				GrpStaffAttr grpStaffAttr = channelInfoDao
						.queryGrpStaffAttr(queryGrpStaffAttr);

				if (grpStaffAttr != null
						&& grpStaffAttr.getGrpStaffAttrId() != null) {
					queryGrpStaffAttr.setGrpStaffAttrId(grpStaffAttr
							.getGrpStaffAttrId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(queryGrpStaffAttr.getAction())) {
					// queryGrpStaffAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// queryGrpStaffAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					queryGrpStaffAttr.updateOnly();
				} else {
					queryGrpStaffAttr.addOnly();
				}

			}
		}
		// }

		// 员工自定义扩展属性入库
		// if (!(!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
		// .getStaff()) && (BaseUnitConstants.ENTT_STATE_DEL.equals(rootIn
		// .getSvcCont().getChannelInfo().getStaff().getAction()) || !this
		// .isActive(rootIn.getSvcCont().getChannelInfo().getStaff()
		// .getStatusCd())))) {

		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getStaffCustomAttr())
				&& !StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
						.getStaffCustomAttr().getAttrItems())
				&& rootIn.getSvcCont().getChannelInfo().getStaffCustomAttr()
						.getAttrItems().size() > 0) {

			for (AttrItemInParam attrItem : rootIn.getSvcCont()
					.getChannelInfo().getStaffCustomAttr().getAttrItems()) {
				GrpStaffCustomAttr queryGrpStaffCustomAttr = new GrpStaffCustomAttr();
				BeanUtils.copyProperties(queryGrpStaffCustomAttr, attrItem);
				queryGrpStaffCustomAttr.setSalesCode(rootIn.getSvcCont()
						.getChannelInfo().getStaffCustomAttr().getSalesCode());
				GrpStaffCustomAttr grpStaffCustomAttr = channelInfoDao
						.queryGrpStaffCustomAttr(queryGrpStaffCustomAttr);

				if (grpStaffCustomAttr != null
						&& grpStaffCustomAttr.getGrpStaffCustomAttrId() != null) {
					queryGrpStaffCustomAttr
							.setGrpStaffCustomAttrId(grpStaffCustomAttr
									.getGrpStaffCustomAttrId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(queryGrpStaffCustomAttr.getAction())) {
					// queryGrpStaffCustomAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// queryGrpStaffCustomAttr
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					queryGrpStaffCustomAttr.updateOnly();
				} else {
					queryGrpStaffCustomAttr.addOnly();
				}

			}
		}
		// }

		// 渠道单元关联经营主体入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannelOperatorsRelas())
				&& rootIn.getSvcCont().getChannelInfo()
						.getChannelOperatorsRelas().size() > 0) {

			for (GrpChannelOperatorsRela grpChannelOperatorsRela : rootIn
					.getSvcCont().getChannelInfo().getChannelOperatorsRelas()) {

				GrpChannelOperatorsRela queryGrpChannelOperatorsRela = channelInfoDao
						.queryGrpChannelOperatorsRela(grpChannelOperatorsRela);

				if (queryGrpChannelOperatorsRela != null
						&& queryGrpChannelOperatorsRela
								.getGrpChannelOperatorsRelaId() != null) {

					grpChannelOperatorsRela
							.setGrpChannelOperatorsRelaId(queryGrpChannelOperatorsRela
									.getGrpChannelOperatorsRelaId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(grpChannelOperatorsRela.getAction())) {
					// grpChannelOperatorsRela
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// grpChannelOperatorsRela
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					grpChannelOperatorsRela.updateOnly();
				} else {
					grpChannelOperatorsRela.addOnly();
				}

			}

		}

		// 渠道单元关联经营场所入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannelBusiStoreRelas())
				&& rootIn.getSvcCont().getChannelInfo()
						.getChannelBusiStoreRelas().size() > 0) {

			for (GrpChannelBusiStoreRela grpChannelBusiStoreRela : rootIn
					.getSvcCont().getChannelInfo().getChannelBusiStoreRelas()) {

				GrpChannelBusiStoreRela queryGrpChannelBusiStoreRela = channelInfoDao
						.queryGrpChannelBusiStoreRela(grpChannelBusiStoreRela);

				if (queryGrpChannelBusiStoreRela != null
						&& queryGrpChannelBusiStoreRela
								.getGrpChannelBusiStoreRelaId() != null) {

					grpChannelBusiStoreRela
							.setGrpChannelBusiStoreRelaId(queryGrpChannelBusiStoreRela
									.getGrpChannelBusiStoreRelaId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(grpChannelBusiStoreRela.getAction())) {
					// grpChannelBusiStoreRela
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// grpChannelBusiStoreRela
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					grpChannelBusiStoreRela.updateOnly();
				} else {
					grpChannelBusiStoreRela.addOnly();
				}

			}

		}

		// 店中商关系入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannelRelas())
				&& rootIn.getSvcCont().getChannelInfo().getChannelRelas()
						.size() > 0) {

			for (GrpChannelRela grpChannelRela : rootIn.getSvcCont()
					.getChannelInfo().getChannelRelas()) {

				GrpChannelRela queryGrpChannelRela = channelInfoDao
						.queryGrpChannelRela(grpChannelRela);

				if (queryGrpChannelRela != null
						&& queryGrpChannelRela.getGrpChannelRelaId() != null) {
					grpChannelRela.setGrpChannelRelaId(queryGrpChannelRela
							.getGrpChannelRelaId());
					// if
					// (BaseUnitConstants.ENTT_STATE_DEL.equals(grpChannelRela
					// .getAction())) {
					// grpChannelRela
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// grpChannelRela
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					grpChannelRela.updateOnly();
				} else {
					grpChannelRela.addOnly();
				}

			}

		}

		// 员工渠道单元关系入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getStaffChannelRelas())
				&& rootIn.getSvcCont().getChannelInfo().getStaffChannelRelas()
						.size() > 0) {

			for (GrpStaffChannelRela grpStaffChannelRela : rootIn.getSvcCont()
					.getChannelInfo().getStaffChannelRelas()) {

				GrpStaffChannelRela queryGrpStaffChannelRela = channelInfoDao
						.queryGrpStaffChannelRela(grpStaffChannelRela);

				if (queryGrpStaffChannelRela != null
						&& queryGrpStaffChannelRela.getGrpStaffChannelRelaId() != null) {
					grpStaffChannelRela
							.setGrpStaffChannelRelaId(queryGrpStaffChannelRela
									.getGrpStaffChannelRelaId());
					// if (BaseUnitConstants.ENTT_STATE_DEL
					// .equals(grpStaffChannelRela.getAction())) {
					// grpStaffChannelRela
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
					// } else {
					// grpStaffChannelRela
					// .setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
					// }
					grpStaffChannelRela.updateOnly();
				} else {
					grpStaffChannelRela.addOnly();
				}

			}

		}

		rootOutParam.getTcpCont().getResponse()
				.setRspType(WsConstants.CHANNEL_RSP_TYPE_SUCCESS);

		return rootOutParam;
	}

	/**
	 * 主数据模型入库
	 * 
	 * @param rootIn
	 * @param rootOutParam
	 * @return
	 */
	@Override
	public UomModelStorageOutParam saveUomModelStorage(
			ContractRootInParam rootIn, ContractRootOutParam rootOutParam) {

		Organization organization = null;
		Organization addOrgOperators = null;
		Organization addOrgChannel = null;

		UomModelStorageOutParam uomModelStorageOutParam = new UomModelStorageOutParam();
		uomModelStorageOutParam.setRootIn(rootIn);
		uomModelStorageOutParam.setRootOutParam(rootOutParam);

		CascadeRelation queryOrgType = new CascadeRelation();
		queryOrgType.setRelaCd(CascadeRelationConstants.RELA_CD_4);

		CascadeRelation queryLocationCode = new CascadeRelation();
		queryLocationCode.setRelaCd(CascadeRelationConstants.RELA_CD_5);

		OrganizationExtendAttr queryOrganizationExtendAttr = new OrganizationExtendAttr();
		queryOrganizationExtendAttr
				.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);

		// 经营主体入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getOperators())) {

			GrpOperators operators = rootIn.getSvcCont().getChannelInfo()
					.getOperators();

			queryOrganizationExtendAttr.setOrgAttrValue(operators
					.getOperatorsNbr());

			// 查询该经营主体在主数据中是否存在
			OrganizationExtendAttr organizationExtendAttr = organizationExtendAttrManager
					.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

			OrganizationExtendAttr organizationExtendAttrStatusCd1100 = organizationExtendAttrManager
					.queryOrganizationExtendAttrStatusCd1100(queryOrganizationExtendAttr);

			if (organizationExtendAttr != null
					&& organizationExtendAttr.getOrgId() != null) {// 存在做更新和删除操作

//				organization = new Organization();
				organization = organizationManager
						.getById(organizationExtendAttr.getOrgId());
				rootIn.getSvcCont().getChannelInfo().getOperators()
						.setProOrgId(organizationExtendAttr.getOrgId());

				if (organization != null && organization.getOrgId() != null) {

					if (BaseUnitConstants.ENTT_STATE_DEL.equals(operators
							.getAction())
							|| !this.isActive(operators.getStatusCd())) {

						/**
						 * 使重新查库
						 */
						// organization.setSubOrganizationList(null);
						// List<Organization> subOrgList = organization
						// .getSubOrganizationList();
						// if (subOrgList != null && subOrgList.size() > 0) {
						// rootOutParam
						// .getTcpCont()
						// .getResponse()
						// .setRspType(
						// WsConstants.CHANNEL_RSP_TYPE_FAILED);
						// rootOutParam.getTcpCont().getResponse()
						// .setRspDesc("存在关联的组织,不能删除");
						// return uomModelStorageOutParam;
						// }

						/**
						 * 使重新查库
						 */
						// organization.setPositionList(null);
						// List<Position> positionList = organization
						// .getPositionList();
						// if (positionList != null && positionList.size() > 0)
						// {
						// rootOutParam
						// .getTcpCont()
						// .getResponse()
						// .setRspType(
						// WsConstants.CHANNEL_RSP_TYPE_FAILED);
						// rootOutParam.getTcpCont().getResponse()
						// .setRspDesc("存在关联的岗位,不能删除");
						// return uomModelStorageOutParam;
						//
						// }

						/**
						 * 使重新查库
						 */
						// organization.setStaffList(null);
						// List<Staff> staffList = organization.getStaffList();
						// if (staffList != null && staffList.size() > 0) {
						// rootOutParam
						// .getTcpCont()
						// .getResponse()
						// .setRspType(
						// WsConstants.CHANNEL_RSP_TYPE_FAILED);
						// rootOutParam.getTcpCont().getResponse()
						// .setRspDesc("存在员工,不能删除");
						// return uomModelStorageOutParam;
						//
						// }

						if (organization.getPartyId() != null) {
							Party party = organization.getParty();
							if (party != null) {
								partyManager.delParty(party);
							}
						}

						organizationManager.removeOrganization(organization);

					} else {

						// 修改实例对象
						organization = new Organization();
						organization = organizationManager
								.getById(organizationExtendAttr.getOrgId());

						if (organization != null
								&& organization.getOrgId() != null) {

							List<OrganizationRelation> organizationRelationList = organization
									.getOrganizationRelationList();

							for (OrganizationRelation organizationRelation : organizationRelationList) {
								if (organizationRelation.getRelaCd().equals(
										OrganizationConstant.RELA_CD_EXTER)) {
									// 设置父组织ID
									if (!StrUtil.isEmpty(operators
											.getParentOperNbr())) {
										OrganizationExtendAttr queryOrgExtendAttr = new OrganizationExtendAttr();
										queryOrgExtendAttr
												.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
										queryOrgExtendAttr
												.setOrgAttrValue(operators
														.getParentOperNbr());
										organizationExtendAttr = organizationExtendAttrManager
												.queryOrganizationExtendAttr(queryOrgExtendAttr);

										if (organizationExtendAttr != null
												&& organizationExtendAttr
														.getOrgId() != null
												&& organizationExtendAttr
														.getOrgId()
														.equals(organizationRelation
																.getRelaOrgId())) {
											continue;
										} else {
											OrganizationRelation newOrganizationRelation = new OrganizationRelation();
											newOrganizationRelation
													.setOrgId(organizationRelation
															.getOrgId());
											newOrganizationRelation
													.setRelaOrgId(organizationExtendAttr
															.getOrgId());
											newOrganizationRelation
													.setRelaCd(OrganizationConstant.RELA_CD_EXTER);
											organizationRelation.remove();
											newOrganizationRelation.add();
										}
									} else if (!OrganizationConstant.ROOT_AGENT_ORG_ID
											.equals(organizationRelation
													.getRelaOrgId())) {
										OrganizationRelation newOrganizationRelation = new OrganizationRelation();
										newOrganizationRelation
												.setOrgId(organizationRelation
														.getOrgId());
										newOrganizationRelation
												.setRelaOrgId(OrganizationConstant.ROOT_AGENT_ORG_ID);
										newOrganizationRelation
												.setRelaCd(OrganizationConstant.RELA_CD_EXTER);
										organizationRelation.remove();
										newOrganizationRelation.add();
									}
								}
							}

							OrgContactInfo organizationContactInfo = organization
									.getOrganizationContactInfo();

							if (organization.getPartyId() != null) {
								Party party = organization.getParty();
								Long partyId = organization.getPartyId();
								if (party != null) {
									PartyOrganization partyOrganization = partyManager
											.getPartyOrg(partyId);
									PartyCertification partyCertification = partyManager
											.getDefaultPartyCertification(partyId);
									PartyContactInfo partyContactInfo = partyManager
											.getDefaultPartyContactInfo(partyId);

									// 参与人信息设置
									party.setPartyName(operators
											.getOperatorsName());

									// 组织参与人信息设置
									if (partyOrganization != null) {
										partyOrganization
												.setOrgContent(operators
														.getOperatorsName());
										partyOrganization
												.setPrincipal(operators
														.getLegalRepr());

										partyOrganization.update();
									}

									// 设置参与人联系信息
									if (partyContactInfo != null) {
										partyContactInfo
												.setMobilePhone(operators
														.getTelephone());
										partyContactInfo.setEmail(operators
												.getEmail());
										if (!StrUtil.isEmpty(operators
												.getTelephone())) {
											partyContactInfo
													.setInnerEmail(operators
															.getTelephone()
															+ "@anhuitelecom.com");
										}
										partyContactInfo.update();
									}

									// 设置参与人证件信息
									if (partyCertification != null) {
										if (!StrUtil.isEmpty(operators
												.getCertType())) {
											// if
											// (PartyConstant.DEFAULT_CERT_TYPE_IDENTITY_CARD
											// .equals(operators
											// .getCertType())) {
											partyCertification
													.setCertType(operators
															.getCertType());
											partyCertification
													.setCertNumber(operators
															.getCertNbr());
											partyCertification.update();
											// } else {
											// partyCertification = new
											// PartyCertification();
											// partyCertification
											// .setPartyId(partyId);
											// partyCertification
											// .setCertType(operators
											// .getCertType());
											//
											// List<PartyCertification>
											// partyCertificationList =
											// partyManager
											// .getPartyCertificationList(partyCertification);
											// if (partyCertificationList !=
											// null
											// && partyCertificationList
											// .size() > 0) {
											// partyCertification =
											// partyCertificationList
											// .get(0);
											// partyCertification
											// .setCertNumber(operators
											// .getCertNbr());
											// partyCertification.update();
											// } else {
											// partyCertification
											// .setCertNumber(operators
											// .getCertNbr());
											// partyCertification.add();
											// }
											// }
										}

									}

									party.update();

								}
							}

							// 设置电信管理区域和行政管理区域
							if (!StrUtil.isEmpty(operators.getCommonRegionId())) {

								queryLocationCode.setCascadeValue(operators
										.getCommonRegionId());

								queryLocationCode = cascadeRelationManager
										.queryCascadeRelation(queryLocationCode);

								TelcomRegion telcomRegion = organization
										.getTelcomRegion(operators
												.getCommonRegionId());

								if (telcomRegion != null
										&& telcomRegion.getTelcomRegionId() != null
										&& !(telcomRegion.getTelcomRegionId()
												.equals(organization
														.getTelcomRegionId()))) {
									organization.setTelcomRegionId(telcomRegion
											.getTelcomRegionId());
								}

								if (queryLocationCode != null
										&& !StrUtil.isEmpty(queryLocationCode
												.getRelaCascadeValue())) {

									PoliticalLocation politicalLocation = organization
											.getPoliticalLocation(queryLocationCode
													.getRelaCascadeValue());

									if (politicalLocation != null
											&& politicalLocation
													.getLocationId() != null
											&& !(politicalLocation
													.getLocationId()
													.equals(organization
															.getLocationId()))) {
										organization
												.setLocationId(politicalLocation
														.getLocationId());
									}

								}

							}

							// 组织信息设置
							organization.setOrgName(operators
									.getOperatorsName());
							organization.setOrgShortName(operators
									.getOperatorsSname());

							// 组织联系信息设置
							organizationContactInfo.setPhone1(operators
									.getTelephone());
							organizationContactInfo.setEmail1(operators
									.getEmail());
							organizationContactInfo.setAddress(operators
									.getAdderss());

							organization.update();
							organizationContactInfo.update();

						}

					}
				}

			} else if (organizationExtendAttrStatusCd1100 != null
					&& organizationExtendAttrStatusCd1100.getOrgId() != null) {// 存在做数据恢复操作
				List<Organization> organizationList = new ArrayList<Organization>();
				Organization organizationStatusCd1100 = organizationManager
						.getByIdStatusCd1100(organizationExtendAttrStatusCd1100
								.getOrgId());
				organizationList.add(organizationStatusCd1100);
				organizationManager.updateOrganizationList(organizationList);
			} else {// 不存在做新增操作

				// 新增实例对象
				organization = new Organization();
				OrgType orgType = new OrgType();
				OrganizationRelation organizationRelation = new OrganizationRelation();
				OrgContactInfo organizationContactInfo = new OrgContactInfo();
				List<OrganizationExtendAttr> organizationExtendAttrList = new ArrayList<OrganizationExtendAttr>();
				List<OrgType> addOrgTypeList = new ArrayList<OrgType>();
				Party party = new Party();
				PartyRole partyRole = new PartyRole();
				PartyOrganization partyOrganization = new PartyOrganization();
				PartyCertification partyCertification = new PartyCertification();
				PartyContactInfo partyContactInfo = new PartyContactInfo();
				Individual individual = new Individual();

				// 设置组织默认值
				organization
						.setTelcomRegionId(TelecomRegionConstants.AH_TELECOM_REGION_ID);
				organization
						.setLocationId(PoliticalLocationConstants.AH_POLITICAL_LOCATION_ID);
				organization.setOrgType(OrganizationConstant.ORG_TYPE_W);
				organization.setExistType(OrganizationConstant.ORG_EXIST_TYPE);
				organization
						.setOrgPriority(OrganizationConstant.DEFAULT_ORG_PRIORITY);

				// 设置组织类型
				orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_AGENT);
				addOrgTypeList.add(orgType);

				// 设置扩展属性-集团4G门户编码
				organizationExtendAttrList.add(queryOrganizationExtendAttr);

				// 设置组织参与人角色类型
				partyRole.setRoleType(OrganizationConstant.ROLE_TYPE_AGENT);

				// 设置参与人联系信息
				partyContactInfo
						.setHeadFlag(PartyConstant.ATTR_VALUE_HEADFLAG_YES);
				partyContactInfo
						.setContactType(PartyConstant.ATTR_VALUE_CONTACTTYPE_BUSINESS);

				// 设置参与人证件信息
				partyCertification.setCertSort(PartyConstant.DEFAULT_CERT_SORT);

				// 设置组织关系
				organizationRelation
						.setRelaCd(OrganizationConstant.RELA_CD_EXTER);
				organizationRelation
						.setRelaOrgId(OrganizationConstant.ROOT_AGENT_ORG_ID);

				// 设置父组织ID
				if (!StrUtil.isEmpty(operators.getParentOperNbr())) {
					OrganizationExtendAttr queryOrgExtendAttr = new OrganizationExtendAttr();
					queryOrgExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
					queryOrgExtendAttr.setOrgAttrValue(operators
							.getParentOperNbr());
					organizationExtendAttr = organizationExtendAttrManager
							.queryOrganizationExtendAttr(queryOrgExtendAttr);

					if (organizationExtendAttr != null
							&& organizationExtendAttr.getOrgId() != null) {
						organizationRelation
								.setRelaOrgId(organizationExtendAttr.getOrgId());
					}
				}

				// 设置电信管理区域和行政管理区域
				if (!StrUtil.isEmpty(operators.getCommonRegionId())) {

					queryLocationCode.setCascadeValue(operators
							.getCommonRegionId());

					queryLocationCode = cascadeRelationManager
							.queryCascadeRelation(queryLocationCode);

					TelcomRegion telcomRegion = organization
							.getTelcomRegion(operators.getCommonRegionId());

					if (telcomRegion != null
							&& telcomRegion.getTelcomRegionId() != null) {
						organization.setTelcomRegionId(telcomRegion
								.getTelcomRegionId());
					}

					if (queryLocationCode != null
							&& !StrUtil.isEmpty(queryLocationCode
									.getRelaCascadeValue())) {

						PoliticalLocation politicalLocation = organization
								.getPoliticalLocation(queryLocationCode
										.getRelaCascadeValue());

						if (politicalLocation != null
								&& politicalLocation.getLocationId() != null) {
							organization.setLocationId(politicalLocation
									.getLocationId());
						}

					}

				}

				// 组织信息设置
				organization.setOrgName(operators.getOperatorsName());
				organization.setOrgShortName(operators.getOperatorsSname());

				// 组织联系信息设置
				organizationContactInfo.setPhone1(operators.getTelephone());
				organizationContactInfo.setEmail1(operators.getEmail());
				organizationContactInfo.setAddress(operators.getAdderss());

				// 参与人信息设置
				party.setPartyName(operators.getOperatorsName());
				party.setPartyType(PartyConstant.ATTR_VALUE_PARTYTYPE_ORGANIZATION);

				// 组织参与人信息设置
				partyOrganization.setOrgContent(operators.getOperatorsName());
				partyOrganization.setPrincipal(operators.getLegalRepr());
				partyOrganization.setOrgType(OrganizationConstant.ORG_TYPE_W);

				// 设置参与人联系信息
				partyContactInfo.setMobilePhone(operators.getTelephone());
				partyContactInfo.setEmail(operators.getEmail());
				if (!StrUtil.isEmpty(operators.getTelephone())) {
					partyContactInfo.setInnerEmail(operators.getTelephone()
							+ "@anhuitelecom.com");
				}

				// 设置参与人证件信息
				if (!StrUtil.isEmpty(operators.getCertType())) {
					if (PartyConstant.DEFAULT_CERT_TYPE_IDENTITY_CARD
							.equals(operators.getCertType())) {
						partyCertification
								.setCertType(PartyConstant.DEFAULT_CERT_TYPE_IDENTITY_CARD);
						partyCertification.setIdentityCardId(1L);
					} else {
						partyCertification.setCertType(operators.getCertType());
					}
				}

				partyCertification.setCertNumber(operators.getCertNbr());

				// 组织相关信息保存
				party.setIndividual(individual);
				party.setPartyRole(partyRole);
				party.setPartyOrganization(partyOrganization);
				party.setPartyCertification(partyCertification);
				party.setPartyContactInfo(partyContactInfo);
				organization.setAgentAddParty(party);
				organization
						.setOrganizationContactInfo(organizationContactInfo);
				organization
						.setOrganizationExtendAttrList(organizationExtendAttrList);
				organization.setAddOrgTypeList(addOrgTypeList);

				// 新增组织入库
				organizationManager.addOrganization(organization);
				organizationRelation.setOrgId(organization.getOrgId());
				organizationRelation.add();
				addOrgOperators = organization;
				rootIn.getSvcCont().getChannelInfo().getOperators()
						.setProOrgId(organization.getOrgId());
			}

		}

		// 渠道单元入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannel())) {

			GrpChannel channel = rootIn.getSvcCont().getChannelInfo()
					.getChannel();

			queryOrganizationExtendAttr
					.setOrgAttrValue(channel.getChannelNbr());

			// 查询该渠道单元在主数据中是否存在
			OrganizationExtendAttr organizationExtendAttr = organizationExtendAttrManager
					.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

			OrganizationExtendAttr organizationExtendAttrStatusCd1100 = organizationExtendAttrManager
					.queryOrganizationExtendAttrStatusCd1100(queryOrganizationExtendAttr);

			if (organizationExtendAttr != null
					&& organizationExtendAttr.getOrgId() != null) {// 存在做更新和删除操作

				organization = new Organization();
				organization = organizationManager
						.getById(organizationExtendAttr.getOrgId());
				rootIn.getSvcCont().getChannelInfo().getChannel()
						.setProOrgId(organizationExtendAttr.getOrgId());

				if (organization != null && organization.getOrgId() != null) {

					if (BaseUnitConstants.ENTT_STATE_DEL.equals(channel
							.getAction())
							|| !this.isActive(channel.getStatusCd())) {

						/**
						 * 使重新查库
						 */
						// organization.setSubOrganizationList(null);
						// List<Organization> subOrgList = organization
						// .getSubOrganizationList();
						// if (subOrgList != null && subOrgList.size() > 0) {
						// rootOutParam
						// .getTcpCont()
						// .getResponse()
						// .setRspType(
						// WsConstants.CHANNEL_RSP_TYPE_FAILED);
						// rootOutParam.getTcpCont().getResponse()
						// .setRspDesc("存在关联的组织,不能删除");
						// return uomModelStorageOutParam;
						// }

						/**
						 * 使重新查库
						 */
						// organization.setPositionList(null);
						// List<Position> positionList = organization
						// .getPositionList();
						// if (positionList != null && positionList.size() > 0)
						// {
						// rootOutParam
						// .getTcpCont()
						// .getResponse()
						// .setRspType(
						// WsConstants.CHANNEL_RSP_TYPE_FAILED);
						// rootOutParam.getTcpCont().getResponse()
						// .setRspDesc("存在关联的岗位,不能删除");
						// return uomModelStorageOutParam;
						//
						// }

						/**
						 * 使重新查库
						 */
						// organization.setStaffList(null);
						// List<Staff> staffList = organization.getStaffList();
						// if (staffList != null && staffList.size() > 0) {
						// rootOutParam
						// .getTcpCont()
						// .getResponse()
						// .setRspType(
						// WsConstants.CHANNEL_RSP_TYPE_FAILED);
						// rootOutParam.getTcpCont().getResponse()
						// .setRspDesc("存在员工,不能删除");
						// return uomModelStorageOutParam;
						//
						// }

						organizationManager.removeOrganization(organization);

					} else {

						// 修改实例对象
						organization = new Organization();
						organization = organizationManager
								.getById(organizationExtendAttr.getOrgId());

						if (organization != null
								&& organization.getOrgId() != null) {

							List<OrgType> orgTypeList = organization
									.getOrgTypeList();

							OrgType newOrgType = new OrgType();
							newOrgType.setOrgId(organizationExtendAttr
									.getOrgId());
							queryOrgType.setCascadeValue(channel
									.getChannelTypeCd());

							queryOrgType = cascadeRelationManager
									.queryCascadeRelation(queryOrgType);

							if (queryOrgType != null
									&& !StrUtil.isEmpty(queryOrgType
											.getRelaCascadeValue())) {

								newOrgType.setOrgTypeCd(queryOrgType
										.getRelaCascadeValue());

								for (OrgType orgType : orgTypeList) {
									if (!orgType.getOrgTypeCd().equals(
											newOrgType.getOrgTypeCd())) {
										if (!StrUtil.isEmpty(channel
												.getChannelTypeCd())) {
											orgType.remove();
											newOrgType.add();
										}
									}
								}
							}

							// 设置电信管理区域和行政管理区域
							if (!StrUtil.isEmpty(channel.getCommonRegionId())) {

								queryLocationCode.setCascadeValue(channel
										.getCommonRegionId());

								queryLocationCode = cascadeRelationManager
										.queryCascadeRelation(queryLocationCode);

								TelcomRegion telcomRegion = organization
										.getTelcomRegion(channel
												.getCommonRegionId());

								if (telcomRegion != null
										&& telcomRegion.getTelcomRegionId() != null
										&& !(telcomRegion.getTelcomRegionId()
												.equals(organization
														.getTelcomRegionId()))) {
									organization.setTelcomRegionId(telcomRegion
											.getTelcomRegionId());
								}

								if (queryLocationCode != null
										&& !StrUtil.isEmpty(queryLocationCode
												.getRelaCascadeValue())) {

									PoliticalLocation politicalLocation = organization
											.getPoliticalLocation(queryLocationCode
													.getRelaCascadeValue());

									if (politicalLocation != null
											&& politicalLocation
													.getLocationId() != null
											&& !(politicalLocation
													.getLocationId()
													.equals(organization
															.getLocationId()))) {
										organization
												.setLocationId(politicalLocation
														.getLocationId());
									}

								}

							}

							// 组织信息设置
							organization.setOrgName(channel.getChannelName());
							organization.update();

						}

					}
				}

			} else if (organizationExtendAttrStatusCd1100 != null
					&& organizationExtendAttrStatusCd1100.getOrgId() != null) {// 存在做数据恢复操作
				List<Organization> organizationList = new ArrayList<Organization>();
				Organization organizationStatusCd1100 = organizationManager
						.getByIdStatusCd1100(organizationExtendAttrStatusCd1100
								.getOrgId());
				organizationList.add(organizationStatusCd1100);
				organizationManager.updateOrganizationList(organizationList);
			} else {// 不存在做新增操作

				// 新增实例对象
				organization = new Organization();
				OrgType orgType = null;
				OrganizationRelation organizationRelation = new OrganizationRelation();
				OrgContactInfo organizationContactInfo = new OrgContactInfo();
				List<OrganizationExtendAttr> organizationExtendAttrList = new ArrayList<OrganizationExtendAttr>();
				List<OrgType> addOrgTypeList = new ArrayList<OrgType>();

				// 设置组织默认值
				organization
						.setTelcomRegionId(TelecomRegionConstants.AH_TELECOM_REGION_ID);
				organization
						.setLocationId(PoliticalLocationConstants.AH_POLITICAL_LOCATION_ID);
				organization.setOrgType(OrganizationConstant.ORG_TYPE_W);
				organization.setExistType(OrganizationConstant.ORG_EXIST_TYPE);
				organization
						.setOrgPriority(OrganizationConstant.DEFAULT_ORG_PRIORITY);

				// 设置组织类型默认值
				// orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202020000);

				// 设置扩展属性-集团4G门户编码
				organizationExtendAttrList.add(queryOrganizationExtendAttr);

				// 设置组织关系
				organizationRelation
						.setRelaCd(OrganizationConstant.RELA_CD_EXTER);

				// 设置电信管理区域和行政管理区域
				if (!StrUtil.isEmpty(channel.getCommonRegionId())) {

					queryLocationCode.setCascadeValue(channel
							.getCommonRegionId());

					queryLocationCode = cascadeRelationManager
							.queryCascadeRelation(queryLocationCode);

					TelcomRegion telcomRegion = organization
							.getTelcomRegion(channel.getCommonRegionId());

					if (telcomRegion != null
							&& telcomRegion.getTelcomRegionId() != null) {
						organization.setTelcomRegionId(telcomRegion
								.getTelcomRegionId());
					}

					if (queryLocationCode != null
							&& !StrUtil.isEmpty(queryLocationCode
									.getRelaCascadeValue())) {

						PoliticalLocation politicalLocation = organization
								.getPoliticalLocation(queryLocationCode
										.getRelaCascadeValue());

						if (politicalLocation != null
								&& politicalLocation.getLocationId() != null) {
							organization.setLocationId(politicalLocation
									.getLocationId());
						}

					}

				}

				// 组织信息设置
				organization.setOrgName(channel.getChannelName());
				organization.setOrgShortName(channel.getChannelName());

				// 组织联系信息设置
				ChannelAttrInParam channelAttr = rootIn.getSvcCont()
						.getChannelInfo().getChannelAttr();
				if (channelAttr != null && channelAttr.getAttrItems() != null) {

					if (channelAttr.getAttrItems().size() > 0) {
						for (AttrItemInParam attrItem : channelAttr
								.getAttrItems()) {

							if (attrItem.getAttrId().equals(
									OrganizationConstant.GRP_ORG_ADDRESS)) {
								organizationContactInfo.setAddress(attrItem
										.getAttrValue());
							}

						}
					}

				}

				// 组织类型信息设置
				if (!StrUtil.isEmpty(channel.getChannelTypeCd())) {

					queryOrgType.setCascadeValue(channel.getChannelTypeCd());

					queryOrgType = cascadeRelationManager
							.queryCascadeRelation(queryOrgType);

					if (queryOrgType != null
							&& !StrUtil.isEmpty(queryOrgType
									.getRelaCascadeValue())) {
						orgType = new OrgType();
						orgType.setOrgTypeCd(queryOrgType.getRelaCascadeValue());
					}

				}

				if (orgType != null) {
					addOrgTypeList.add(orgType);
				}

				organization
						.setOrganizationContactInfo(organizationContactInfo);
				organization
						.setOrganizationExtendAttrList(organizationExtendAttrList);
				organization.setAddOrgTypeList(addOrgTypeList);

				// 新增组织入库
				organizationManager.addOrganization(organization);
				addOrgChannel = organization;
				rootIn.getSvcCont().getChannelInfo().getChannel()
						.setProOrgId(organization.getOrgId());
			}

		}

		// 渠道单元关联经营主体入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannelOperatorsRelas())
				&& rootIn.getSvcCont().getChannelInfo()
						.getChannelOperatorsRelas().size() > 0) {

			List<GrpChannelOperatorsRela> channelOperatorsRelas = rootIn
					.getSvcCont().getChannelInfo().getChannelOperatorsRelas();

			for (GrpChannelOperatorsRela grpChannelOperatorsRela : channelOperatorsRelas) {

				if (!StrUtil.isEmpty(grpChannelOperatorsRela.getOperatorsNbr())
						&& !StrUtil.isEmpty(grpChannelOperatorsRela
								.getChannelNbr())) {

					OrganizationExtendAttr operatorsOrgExtendAttr = null;
					OrganizationExtendAttr channelOrgExtendAttr = null;

					// 查询该经营主体在主数据中是否存在
					OrganizationExtendAttr queryOrgExtendAttr = new OrganizationExtendAttr();
					queryOrgExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
					queryOrgExtendAttr.setOrgAttrValue(grpChannelOperatorsRela
							.getOperatorsNbr());

					if (addOrgOperators != null
							&& addOrgOperators.getOrgId() != null) {
						operatorsOrgExtendAttr = new OrganizationExtendAttr();
						operatorsOrgExtendAttr.setOrgId(addOrgOperators
								.getOrgId());
					} else {
						operatorsOrgExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(queryOrgExtendAttr);
					}

					// 查询该渠道单元在主数据中是否存在
					queryOrgExtendAttr = new OrganizationExtendAttr();
					queryOrgExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
					queryOrgExtendAttr.setOrgAttrValue(grpChannelOperatorsRela
							.getChannelNbr());

					if (addOrgChannel != null
							&& addOrgChannel.getOrgId() != null) {
						channelOrgExtendAttr = new OrganizationExtendAttr();
						channelOrgExtendAttr.setOrgId(addOrgChannel.getOrgId());
					} else {
						channelOrgExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(queryOrgExtendAttr);
					}

					// 设置组织关系
					OrganizationRelation organizationRelation = new OrganizationRelation();
					List<OrganizationRelation> organizationRelationList = null;

					organizationRelation
							.setRelaCd(OrganizationConstant.RELA_CD_EXTER);

					if (operatorsOrgExtendAttr != null
							&& operatorsOrgExtendAttr.getOrgId() != null
							&& channelOrgExtendAttr != null
							&& channelOrgExtendAttr.getOrgId() != null) {
						organizationRelation.setOrgId(channelOrgExtendAttr
								.getOrgId());
						organizationRelation
								.setRelaOrgId(operatorsOrgExtendAttr.getOrgId());
						organizationRelationList = organizationRelationManager
								.queryOrganizationRelationList(organizationRelation);

						if (BaseUnitConstants.ENTT_STATE_DEL
								.equals(grpChannelOperatorsRela.getAction())) {

							if (organizationRelationList != null
									&& organizationRelationList.size() > 0) {
								for (OrganizationRelation orgRelation : organizationRelationList) {
									orgRelation.remove();
								}
							}

						} else {

							if (organizationRelationList == null
									|| organizationRelationList.size() <= 0) {

								organizationRelation.setRelaOrgId(null);
								organizationRelationList = organizationRelationManager
										.queryOrganizationRelationList(organizationRelation);

								if (organizationRelationList != null
										&& organizationRelationList.size() > 0) {
									for (OrganizationRelation orgRelation : organizationRelationList) {
										orgRelation.remove();
									}
								}

								organizationRelation
										.setRelaOrgId(operatorsOrgExtendAttr
												.getOrgId());
								organizationRelation.add();
							}

						}
					}
				}

			}

		}

		// 店中商关系入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannelRelas())
				&& rootIn.getSvcCont().getChannelInfo().getChannelRelas()
						.size() > 0) {

			List<GrpChannelRela> channelRelas = rootIn.getSvcCont()
					.getChannelInfo().getChannelRelas();

			for (GrpChannelRela grpChannelRela : channelRelas) {

				if (!StrUtil.isEmpty(grpChannelRela.getChannelNbr())
						&& !StrUtil.isEmpty(grpChannelRela.getRelaChannelNbr())) {

					OrganizationExtendAttr channelOrgExtendAttr = null;

					// 查询该店中商在主数据中是否存在
					OrganizationExtendAttr queryOrgExtendAttr = new OrganizationExtendAttr();
					queryOrgExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
					queryOrgExtendAttr.setOrgAttrValue(grpChannelRela
							.getChannelNbr());

					if (addOrgChannel != null
							&& addOrgChannel.getOrgId() != null) {
						channelOrgExtendAttr = new OrganizationExtendAttr();
						channelOrgExtendAttr.setOrgId(addOrgChannel.getOrgId());
					} else {
						channelOrgExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(queryOrgExtendAttr);
					}

					// 查询该渠道单元在主数据中是否存在
					queryOrgExtendAttr = new OrganizationExtendAttr();
					queryOrgExtendAttr
							.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);
					queryOrgExtendAttr.setOrgAttrValue(grpChannelRela
							.getRelaChannelNbr());
					OrganizationExtendAttr relaChannelOrgExtendAttr = organizationExtendAttrManager
							.queryOrganizationExtendAttr(queryOrgExtendAttr);

					// 设置组织关系
					OrganizationRelation organizationRelation = new OrganizationRelation();
					List<OrganizationRelation> organizationRelationList = null;

					organizationRelation
							.setRelaCd(OrganizationConstant.RELA_CD_JZ);

					if (channelOrgExtendAttr != null
							&& channelOrgExtendAttr.getOrgId() != null
							&& relaChannelOrgExtendAttr != null
							&& relaChannelOrgExtendAttr.getOrgId() != null) {

						organizationRelation.setOrgId(channelOrgExtendAttr
								.getOrgId());
						organizationRelation
								.setRelaOrgId(relaChannelOrgExtendAttr
										.getOrgId());

						organizationRelationList = organizationRelationManager
								.queryOrganizationRelationList(organizationRelation);

						if (BaseUnitConstants.ENTT_STATE_DEL
								.equals(grpChannelRela.getAction())) {

							if (organizationRelationList != null
									&& organizationRelationList.size() > 0) {
								for (OrganizationRelation orgRelation : organizationRelationList) {
									orgRelation.remove();
								}
							}

						} else {

							if (organizationRelationList == null
									|| organizationRelationList.size() <= 0) {

								organizationRelation.setRelaOrgId(null);
								organizationRelationList = organizationRelationManager
										.queryOrganizationRelationList(organizationRelation);

								if (organizationRelationList != null
										&& organizationRelationList.size() > 0) {
									for (OrganizationRelation orgRelation : organizationRelationList) {
										orgRelation.remove();
									}
								}

								organizationRelation
										.setRelaOrgId(relaChannelOrgExtendAttr
												.getOrgId());
								organizationRelation.add();
							}

						}
					}
				}

			}

		}

		rootOutParam.getTcpCont().getResponse()
				.setRspType(WsConstants.CHANNEL_RSP_TYPE_SUCCESS);

		return uomModelStorageOutParam;

	}

	/**
	 * 主数据模型数据验证
	 * 
	 * @param rootIn
	 * @param rootOutParam
	 * @return
	 */
	@Override
	public UomModelStorageOutParam uomModelValid(ContractRootInParam rootIn,
			ContractRootOutParam rootOutParam) {

		Organization organization = null;

		UomModelStorageOutParam uomModelStorageOutParam = new UomModelStorageOutParam();
		uomModelStorageOutParam.setRootIn(rootIn);
		uomModelStorageOutParam.setRootOutParam(rootOutParam);

		OrganizationExtendAttr queryOrganizationExtendAttr = new OrganizationExtendAttr();
		queryOrganizationExtendAttr
				.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_18);

		// 经营主体入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getOperators())) {

			GrpOperators operators = rootIn.getSvcCont().getChannelInfo()
					.getOperators();

			queryOrganizationExtendAttr.setOrgAttrValue(operators
					.getOperatorsNbr());

			// 查询该经营主体在主数据中是否存在
			OrganizationExtendAttr organizationExtendAttr = organizationExtendAttrManager
					.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

			if (organizationExtendAttr != null
					&& organizationExtendAttr.getOrgId() != null) {// 存在做更新和删除校验

				organization = new Organization();
				organization = organizationManager
						.getById(organizationExtendAttr.getOrgId());
				rootIn.getSvcCont().getChannelInfo().getOperators()
						.setProOrgId(organizationExtendAttr.getOrgId());

				if (organization != null && organization.getOrgId() != null) {

					if (BaseUnitConstants.ENTT_STATE_DEL.equals(operators
							.getAction())
							|| !this.isActive(operators.getStatusCd())) {

						/**
						 * 使重新查库
						 */
						organization.setSubOrganizationList(null);
						List<Organization> subOrgList = organization
								.getSubOrganizationList();
						if (subOrgList != null && subOrgList.size() > 0) {
							rootOutParam
									.getTcpCont()
									.getResponse()
									.setRspType(
											WsConstants.CHANNEL_RSP_TYPE_FAILED);
							rootOutParam.getTcpCont().getResponse()
									.setRspDesc("存在关联的组织,不能删除");
							return uomModelStorageOutParam;
						}

						/**
						 * 使重新查库
						 */
						organization.setPositionList(null);
						List<Position> positionList = organization
								.getPositionList();
						if (positionList != null && positionList.size() > 0) {
							rootOutParam
									.getTcpCont()
									.getResponse()
									.setRspType(
											WsConstants.CHANNEL_RSP_TYPE_FAILED);
							rootOutParam.getTcpCont().getResponse()
									.setRspDesc("存在关联的岗位,不能删除");
							return uomModelStorageOutParam;

						}

						/**
						 * 使重新查库
						 */
						organization.setStaffList(null);
						List<Staff> staffList = organization.getStaffList();
						if (staffList != null && staffList.size() > 0) {
							rootOutParam
									.getTcpCont()
									.getResponse()
									.setRspType(
											WsConstants.CHANNEL_RSP_TYPE_FAILED);
							rootOutParam.getTcpCont().getResponse()
									.setRspDesc("存在员工,不能删除");
							return uomModelStorageOutParam;
						}

					} else {
						if (!StrUtil.isEmpty(operators.getCertType())) {
							if (PartyConstant.DEFAULT_CERT_TYPE_IDENTITY_CARD
									.equals(operators.getCertType())) {
								if (!IdcardValidator
										.isValidatedAllIdcard(operators
												.getCertNbr())) {
									rootOutParam
											.getTcpCont()
											.getResponse()
											.setRspType(
													WsConstants.CHANNEL_RSP_TYPE_FAILED);
									rootOutParam.getTcpCont().getResponse()
											.setRspDesc("身份证格式不正确，请填写真实身份证信息!");
									return uomModelStorageOutParam;
								}
							}
						}
					}
				}

			} else {// 不存在做新增校验
				// 参与人证件信息
				if (!StrUtil.isEmpty(operators.getCertType())) {
					if (PartyConstant.DEFAULT_CERT_TYPE_IDENTITY_CARD
							.equals(operators.getCertType())) {
						if (!IdcardValidator.isValidatedAllIdcard(operators
								.getCertNbr())) {
							rootOutParam
									.getTcpCont()
									.getResponse()
									.setRspType(
											WsConstants.CHANNEL_RSP_TYPE_FAILED);
							rootOutParam.getTcpCont().getResponse()
									.setRspDesc("身份证格式不正确，请填写真实身份证信息!");
							return uomModelStorageOutParam;
						}
					}
				}
			}

		}

		// 渠道单元入库
		if (!StrUtil.isNullOrEmpty(rootIn.getSvcCont().getChannelInfo()
				.getChannel())) {

			GrpChannel channel = rootIn.getSvcCont().getChannelInfo()
					.getChannel();

			queryOrganizationExtendAttr
					.setOrgAttrValue(channel.getChannelNbr());

			// 查询该渠道单元在主数据中是否存在
			OrganizationExtendAttr organizationExtendAttr = organizationExtendAttrManager
					.queryOrganizationExtendAttr(queryOrganizationExtendAttr);

			if (organizationExtendAttr != null
					&& organizationExtendAttr.getOrgId() != null) {// 存在做删除校验

				organization = new Organization();
				organization = organizationManager
						.getById(organizationExtendAttr.getOrgId());
				rootIn.getSvcCont().getChannelInfo().getChannel()
						.setProOrgId(organizationExtendAttr.getOrgId());

				if (organization != null && organization.getOrgId() != null) {

					if (BaseUnitConstants.ENTT_STATE_DEL.equals(channel
							.getAction())
							|| !this.isActive(channel.getStatusCd())) {

						/**
						 * 使重新查库
						 */
						organization.setSubOrganizationList(null);
						List<Organization> subOrgList = organization
								.getSubOrganizationList();
						if (subOrgList != null && subOrgList.size() > 0) {
							rootOutParam
									.getTcpCont()
									.getResponse()
									.setRspType(
											WsConstants.CHANNEL_RSP_TYPE_FAILED);
							rootOutParam.getTcpCont().getResponse()
									.setRspDesc("存在关联的组织,不能删除");
							return uomModelStorageOutParam;
						}

						/**
						 * 使重新查库
						 */
						organization.setPositionList(null);
						List<Position> positionList = organization
								.getPositionList();
						if (positionList != null && positionList.size() > 0) {
							rootOutParam
									.getTcpCont()
									.getResponse()
									.setRspType(
											WsConstants.CHANNEL_RSP_TYPE_FAILED);
							rootOutParam.getTcpCont().getResponse()
									.setRspDesc("存在关联的岗位,不能删除");
							return uomModelStorageOutParam;

						}

						/**
						 * 使重新查库
						 */
						organization.setStaffList(null);
						List<Staff> staffList = organization.getStaffList();
						if (staffList != null && staffList.size() > 0) {
							rootOutParam
									.getTcpCont()
									.getResponse()
									.setRspType(
											WsConstants.CHANNEL_RSP_TYPE_FAILED);
							rootOutParam.getTcpCont().getResponse()
									.setRspDesc("存在员工,不能删除");
							return uomModelStorageOutParam;

						}

					}
				}

			}

		}

		rootOutParam.getTcpCont().getResponse()
				.setRspType(WsConstants.CHANNEL_RSP_TYPE_SUCCESS);

		return uomModelStorageOutParam;

	}

	@Override
	public GrpOperators queryGrpOperatorsByOperatorsNbr(String operatorsNbr) {
		return channelInfoDao.queryGrpOperatorsByOperatorsNbr(operatorsNbr);
	}

	@Override
	public List<GrpOperators> queryGrpOperatorsList(GrpOperators grpOperators) {
		return channelInfoDao.queryGrpOperatorsList(grpOperators);
	}

	@Override
	public GrpChannel queryGrpChannelByChannelNbr(String channelNbr) {
		return channelInfoDao.queryGrpChannelByChannelNbr(channelNbr);
	}

	@Override
	public List<GrpChannel> queryGrpChannelList(GrpChannel grpChannel) {
		return channelInfoDao.queryGrpChannelList(grpChannel);
	}

	@Override
	public GrpStaff queryGrpStaffBySalesCode(String salesCode) {
		return channelInfoDao.queryGrpStaffBySalesCode(salesCode);
	}

	@Override
	public List<GrpStaff> queryGrpStaffList(GrpStaff grpStaff) {
		return channelInfoDao.queryGrpStaffList(grpStaff);
	}

	@Override
	public GrpOperatorsAttr queryGrpOperatorsAttr(
			GrpOperatorsAttr grpOperatorsAttr) {
		return channelInfoDao.queryGrpOperatorsAttr(grpOperatorsAttr);
	}

	@Override
	public List<GrpOperatorsAttr> queryGrpOperatorsAttrList(
			GrpOperatorsAttr grpOperatorsAttr) {
		return channelInfoDao.queryGrpOperatorsAttrList(grpOperatorsAttr);
	}

	@Override
	public GrpChannelAttr queryGrpChannelAttr(GrpChannelAttr grpChannelAttr) {
		return channelInfoDao.queryGrpChannelAttr(grpChannelAttr);
	}

	@Override
	public List<GrpChannelAttr> queryGrpChannelAttrList(
			GrpChannelAttr grpChannelAttr) {
		return channelInfoDao.queryGrpChannelAttrList(grpChannelAttr);
	}

	@Override
	public GrpStaffAttr queryGrpStaffAttr(GrpStaffAttr grpStaffAttr) {
		return channelInfoDao.queryGrpStaffAttr(grpStaffAttr);
	}

	@Override
	public List<GrpStaffAttr> queryGrpStaffAttrList(GrpStaffAttr grpStaffAttr) {
		return channelInfoDao.queryGrpStaffAttrList(grpStaffAttr);
	}

	@Override
	public GrpChannelOperatorsRela queryGrpChannelOperatorsRela(
			GrpChannelOperatorsRela grpChannelOperatorsRela) {
		return channelInfoDao
				.queryGrpChannelOperatorsRela(grpChannelOperatorsRela);
	}

	@Override
	public List<GrpChannelOperatorsRela> queryGrpChannelOperatorsRelaList(
			GrpChannelOperatorsRela grpChannelOperatorsRela) {
		return channelInfoDao
				.queryGrpChannelOperatorsRelaList(grpChannelOperatorsRela);
	}

	@Override
	public GrpChannelRela queryGrpChannelRela(GrpChannelRela grpChannelRela) {
		return channelInfoDao.queryGrpChannelRela(grpChannelRela);
	}

	@Override
	public List<GrpChannelRela> queryGrpChannelRelaList(
			GrpChannelRela grpChannelRela) {
		return channelInfoDao.queryGrpChannelRelaList(grpChannelRela);
	}

	@Override
	public GrpStaffChannelRela queryGrpStaffChannelRela(
			GrpStaffChannelRela grpStaffChannelRela) {
		return channelInfoDao.queryGrpStaffChannelRela(grpStaffChannelRela);
	}

	@Override
	public List<GrpStaffChannelRela> queryGrpStaffChannelRelaList(
			GrpStaffChannelRela grpStaffChannelRela) {
		return channelInfoDao.queryGrpStaffChannelRelaList(grpStaffChannelRela);
	}

	@Override
	public PageInfo queryPageInfoByGrpOperators(GrpOperators grpOperators,
			int currentPage, int pageSize) {
		return channelInfoDao.queryPageInfoByGrpOperators(grpOperators,
				currentPage, pageSize);
	}

	@Override
	public PageInfo queryPageInfoByGrpChannel(GrpChannel grpChannel,
			int currentPage, int pageSize) {
		return channelInfoDao.queryPageInfoByGrpChannel(grpChannel,
				currentPage, pageSize);
	}

	@Override
	public PageInfo queryPageInfoByGrpStaff(GrpStaff grpStaff, int currentPage,
			int pageSize) {
		return channelInfoDao.queryPageInfoByGrpStaff(grpStaff, currentPage,
				pageSize);
	}

	@Override
	public PageInfo queryPageInfoByGrpChannelOperatorsRela(
			GrpChannelOperatorsRela grpChannelOperatorsRela, int currentPage,
			int pageSize) {
		return channelInfoDao.queryPageInfoByGrpChannelOperatorsRela(
				grpChannelOperatorsRela, currentPage, pageSize);
	}

	@Override
	public PageInfo queryPageInfoByGrpChannelRela(
			GrpChannelRela grpChannelRela, int currentPage, int pageSize) {
		return channelInfoDao.queryPageInfoByGrpChannelRela(grpChannelRela,
				currentPage, pageSize);
	}

	@Override
	public PageInfo queryPageInfoByGrpStaffChannelRela(
			GrpStaffChannelRela grpStaffChannelRela, int currentPage,
			int pageSize) {
		return channelInfoDao.queryPageInfoByGrpStaffChannelRela(
				grpStaffChannelRela, currentPage, pageSize);
	}

    @Override
    public List<ChannelConfigVo> queryChannelConfig(List<String> classNames, List<String> params) {
        if(classNames == null)
            return null;
        
        if(params == null)
            return null;
        
        List sqlParams = new ArrayList();
        StringBuffer sb = new StringBuffer(" SELECT a.java_code as javaCode, WMSYS.WM_CONCAT(B.JAVA_CODE ||'-'|| c.attr_value) as attrValues "
            + "FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C WHERE 1 = 1 AND C.STATUS_CD = 1000 AND C.ATTR_ID = B.ATTR_ID "
            + "AND A.CLASS_ID = B.CLASS_ID ");
        if(classNames.size() > 0)
        {
            //有类型
            sb.append(" AND A.JAVA_CODE in ( '999999' ");
            //添加需要查询的配置表类型
            for(int i = 0; i < classNames.size(); ++i)
            {
                sb.append(", ? ");
                String tempName = classNames.get(i);
                sqlParams.add(tempName);
            }
            sb.append(" ) ");
        }
        
        if(params.size() > 0)
        {
            //有类型
            sb.append(" AND B.JAVA_CODE in ( '999999' ");
            //添加需要查询的配置表类型
            for(int i = 0; i < params.size(); ++i)
            {
                sb.append(", ? ");
                String tempName = params.get(i);
                sqlParams.add(tempName);
            }
            sb.append(" ) ");
        }
        
        
        sb.append("group by a.java_code");
        return channelInfoDao.getJdbcTemplate().query(sb.toString(), sqlParams.toArray(), new BeanPropertyRowMapper(ChannelConfigVo.class));
    }
}
