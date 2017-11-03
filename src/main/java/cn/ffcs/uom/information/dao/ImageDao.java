/**
 * 
 */
package cn.ffcs.uom.information.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import cn.ffcs.uom.information.vo.ImageVo;

/**
 * @author 曾臻
 * @date 2012-1-14
 *
 */
public interface ImageDao {
	public long addImage(ImageVo a,InputStream is);
	public void loadImageContent(long id,OutputStream os);
	public ImageVo loadImage(long id);
	public void deleteImages(List<Long> ids);
}
