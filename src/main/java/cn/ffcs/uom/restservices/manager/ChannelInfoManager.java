package cn.ffcs.uom.restservices.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.restservices.model.ContractRootInParam;
import cn.ffcs.uom.restservices.model.ContractRootOutParam;
import cn.ffcs.uom.restservices.model.GrpChannel;
import cn.ffcs.uom.restservices.model.GrpChannelAttr;
import cn.ffcs.uom.restservices.model.GrpChannelOperatorsRela;
import cn.ffcs.uom.restservices.model.GrpChannelRela;
import cn.ffcs.uom.restservices.model.GrpOperators;
import cn.ffcs.uom.restservices.model.GrpOperatorsAttr;
import cn.ffcs.uom.restservices.model.GrpStaff;
import cn.ffcs.uom.restservices.model.GrpStaffAttr;
import cn.ffcs.uom.restservices.model.GrpStaffChannelRela;
import cn.ffcs.uom.restservices.model.UomModelStorageOutParam;
import cn.ffcs.uom.restservices.vo.ChannelConfigVo;

public interface ChannelInfoManager {

	/**
	 * 判断是否是生效状态
	 */
	public boolean isActive(String status);

	/**
	 * 组装报文实体
	 * 
	 * @param operators
	 * @param channel
	 * @return
	 */
	public ContractRootInParam contractRootInParamPacking(
			GrpOperators operators, GrpChannel channel);

	/**
	 * 集团模型入库
	 * 
	 * @param rootIn
	 * @param rootOutParam
	 * @return
	 */
	public ContractRootOutParam saveGroupModelStorage(
			ContractRootInParam rootIn, ContractRootOutParam rootOutParam);

	/**
	 * 主数据模型入库
	 * 
	 * @param rootIn
	 * @param rootOutParam
	 * @return
	 */
	public UomModelStorageOutParam saveUomModelStorage(
			ContractRootInParam rootIn, ContractRootOutParam rootOutParam);

	/**
	 * 主数据模型数据验证
	 * 
	 * @param rootIn
	 * @param rootOutParam
	 * @return
	 */
	public UomModelStorageOutParam uomModelValid(ContractRootInParam rootIn,
			ContractRootOutParam rootOutParam);

	public GrpOperators queryGrpOperatorsByOperatorsNbr(String operatorsNbr);

	public List<GrpOperators> queryGrpOperatorsList(GrpOperators grpOperators);

	public PageInfo queryPageInfoByGrpOperators(GrpOperators grpOperators,
			int currentPage, int pageSize);

	public GrpChannel queryGrpChannelByChannelNbr(String channelNbr);

	public List<GrpChannel> queryGrpChannelList(GrpChannel grpChannel);

	public PageInfo queryPageInfoByGrpChannel(GrpChannel grpChannel,
			int currentPage, int pageSize);

	public GrpStaff queryGrpStaffBySalesCode(String salesCode);

	public List<GrpStaff> queryGrpStaffList(GrpStaff grpStaff);

	public PageInfo queryPageInfoByGrpStaff(GrpStaff grpStaff, int currentPage,
			int pageSize);

	public GrpOperatorsAttr queryGrpOperatorsAttr(
			GrpOperatorsAttr grpOperatorsAttr);

	public List<GrpOperatorsAttr> queryGrpOperatorsAttrList(
			GrpOperatorsAttr grpOperatorsAttr);

	public GrpChannelAttr queryGrpChannelAttr(GrpChannelAttr grpChannelAttr);

	public List<GrpChannelAttr> queryGrpChannelAttrList(
			GrpChannelAttr grpChannelAttr);

	public GrpStaffAttr queryGrpStaffAttr(GrpStaffAttr grpStaffAttr);

	public List<GrpStaffAttr> queryGrpStaffAttrList(GrpStaffAttr grpStaffAttr);

	public GrpChannelOperatorsRela queryGrpChannelOperatorsRela(
			GrpChannelOperatorsRela grpChannelOperatorsRela);

	public List<GrpChannelOperatorsRela> queryGrpChannelOperatorsRelaList(
			GrpChannelOperatorsRela grpChannelOperatorsRela);

	public PageInfo queryPageInfoByGrpChannelOperatorsRela(
			GrpChannelOperatorsRela grpChannelOperatorsRela, int currentPage,
			int pageSize);

	public GrpChannelRela queryGrpChannelRela(GrpChannelRela grpChannelRela);

	public List<GrpChannelRela> queryGrpChannelRelaList(
			GrpChannelRela grpChannelRela);

	public PageInfo queryPageInfoByGrpChannelRela(
			GrpChannelRela grpChannelRela, int currentPage, int pageSize);

	public GrpStaffChannelRela queryGrpStaffChannelRela(
			GrpStaffChannelRela grpStaffChannelRela);

	public List<GrpStaffChannelRela> queryGrpStaffChannelRelaList(
			GrpStaffChannelRela grpStaffChannelRela);

	public PageInfo queryPageInfoByGrpStaffChannelRela(
			GrpStaffChannelRela grpStaffChannelRela, int currentPage,
			int pageSize);
	
	public List<ChannelConfigVo> queryChannelConfig(List<String> classNames, List<String> params);

}
