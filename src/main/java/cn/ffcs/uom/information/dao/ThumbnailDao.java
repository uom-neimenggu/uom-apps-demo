/**
 * 
 */
package cn.ffcs.uom.information.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import cn.ffcs.uom.information.vo.ThumbnailVo;

/**
 * @author 曾臻
 * @date 2012-1-15
 *
 */
public interface ThumbnailDao {
	public long addThumbnail(ThumbnailVo a,InputStream is);
	public void loadThumbnailContent(long id,OutputStream os);
	public ThumbnailVo loadThumbnail(long id);
	public void deleteThumbnails(List<Long> ids);
}
