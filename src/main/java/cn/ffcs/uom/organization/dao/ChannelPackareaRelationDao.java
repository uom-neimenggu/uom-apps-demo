package cn.ffcs.uom.organization.dao;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.vo.ChannelPackareaRelationVo;

public interface ChannelPackareaRelationDao {
    public PageInfo queryPageInfoByChannelPackareaRelationVo(ChannelPackareaRelationVo channelPackareaRelationVo,
        int currentPage, int pageSize);

	public List<Map<String, Object>> queryListByChannelPackareaRelationVo(
			ChannelPackareaRelationVo channelPackareaRelationVo);
}
