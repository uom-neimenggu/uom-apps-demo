package cn.ffcs.uom.restservices.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
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

public interface ChannelInfoDao extends BaseDao {
	
	/**
	 * 查询dual
	 * 语句为select 1 from dual;
	 * @param str
	 * @return
	 */
	public String selectDual1(String str);
	
	public GrpOperators queryGrpOperatorsByOperatorsNbr(String operatorsNbr);

	/**
	 * 根据经营场所编码查询经营场所
	 * 
	 * @param busiStoreNbr
	 */
	public GrpBusiStore queryGrpBusiStoreByBusiStoreNbr(String busiStoreNbr);

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

	public GrpOperatorsCustomAttr queryGrpOperatorsCustomAttr(
			GrpOperatorsCustomAttr grpOperatorsCustomAttr);

	public List<GrpOperatorsCustomAttr> queryGrpOperatorsCustomAttrList(
			GrpOperatorsCustomAttr grpOperatorsCustomAttr);

	public GrpBusiStoreAttr queryGrpBusiStoreAttr(
			GrpBusiStoreAttr grpBusiStoreAttr);

	/**
	 * 查询经营场所扩展属性信息
	 * 
	 * @param grpBusiStoreAttr
	 * @return
	 */
	public List<GrpBusiStoreAttr> queryGrpBusiStoreAttrList(
			GrpBusiStoreAttr grpBusiStoreAttr);

	public GrpChannelAttr queryGrpChannelAttr(GrpChannelAttr grpChannelAttr);

	public List<GrpChannelAttr> queryGrpChannelAttrList(
			GrpChannelAttr grpChannelAttr);

	public GrpChannelCustomAttr queryGrpChannelCustomAttr(
			GrpChannelCustomAttr grpChannelCustomAttr);

	public List<GrpChannelCustomAttr> queryGrpChannelCustomAttrList(
			GrpChannelCustomAttr grpChannelCustomAttr);

	public GrpStaffAttr queryGrpStaffAttr(GrpStaffAttr grpStaffAttr);

	public List<GrpStaffAttr> queryGrpStaffAttrList(GrpStaffAttr grpStaffAttr);

	public GrpStaffCustomAttr queryGrpStaffCustomAttr(
			GrpStaffCustomAttr grpStaffCustomAttr);

	public List<GrpStaffCustomAttr> queryGrpStaffCustomAttrList(
			GrpStaffCustomAttr grpStaffCustomAttr);

	public GrpChannelOperatorsRela queryGrpChannelOperatorsRela(
			GrpChannelOperatorsRela grpChannelOperatorsRela);

	public GrpChannelBusiStoreRela queryGrpChannelBusiStoreRela(
			GrpChannelBusiStoreRela grpChannelBusiStoreRela);

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

}
