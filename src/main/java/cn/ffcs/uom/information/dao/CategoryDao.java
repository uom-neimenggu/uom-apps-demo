/**
 * 
 */
package cn.ffcs.uom.information.dao;

import java.util.List;

import cn.ffcs.uom.information.vo.BulletinSettingVo;
import cn.ffcs.uom.information.vo.CategoryVo;

/**
 * @author 曾臻
 * @date 2012-11-20
 *
 */
public interface CategoryDao {
	public List<CategoryVo> queryCategoryList();
	public BulletinSettingVo loadBulletinSetting(String categoryCode);
}
