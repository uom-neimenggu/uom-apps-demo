/**
 * 
 */
package cn.ffcs.uom.information.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import cn.ffcs.uom.information.vo.AttachmentVo;

/**
 * @author 曾臻
 * @date 2012-1-6
 *
 */
public interface AttachmentDao {
	public long addAttachment(AttachmentVo a,InputStream is);
	public void loadAttachmentContent(long id,OutputStream os);
	public AttachmentVo loadAttachment(long id);
	public void deleteAttchments(List<Long> ids);
}
