package cn.ffcs.uom.organization.manager;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.organization.vo.ChannelPackareaRelationVo;

public interface ChannelPackareaRelationManager {

	public PageInfo queryPageInfoByChannelPackareaRelationVo(
			ChannelPackareaRelationVo channelPackareaRelationVo,
			int currentPage, int pageSize);

	public void removeUomGroupOrgTran(
			ChannelPackareaRelationVo channelPackareaRelationVo);

	/**
	 * 根据id号，查询包区网点关系数据 .
	 * 
	 * @param id
	 * @return
	 * @author xiaof 2017年1月4日 xiaof
	 */
	public UomGroupOrgTran queryUomGroupOrgTranById(Long id);

	public List<Map<String, Object>> queryListByChannelPackareaRelationVo(
			ChannelPackareaRelationVo channelPackareaRelationVo);

}
