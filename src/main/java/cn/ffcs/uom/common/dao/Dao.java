package cn.ffcs.uom.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;

/**
 * @版权：福富软件 版权所有 (c) 2007
 * @文件：com.ffcs.crm.common.dao.Dao.java
 * @所含类：Dao
 * @author: wuq
 * @version: V1.0
 * @see:
 * @创建日期：2007-9-13
 * @功能说明：
 * @修改记录： =============================================================<br>
 *        日期:2007-9-13 wuq 创建
 *        =============================================================<br>
 */
@SuppressWarnings("unchecked")
public interface Dao {
    /**
     * @param clazz
     *            参数类
     * @param obj
     *            参数对象
     * @return List
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-13 chenjun 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    List getObjects(Class clazz, Object obj);
    
    /**
     * @param clazz
     *            参数类
     * @param id
     *            参数
     * @return Object
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-13 chenjun 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    Object getObject(Class clazz, Serializable id);
    
    /**
     * @param o
     *            参数类
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-13 chenjun 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    void saveObject(Object o);
    
    /**
     * @param o
     *            参数类
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-13 chenjun 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    void addObject(Object o);
    
    /**
     * @param o
     *            参数类
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-13 chenjun 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    void merge(Object o);
    
    /**
     * @param o
     *            参数类
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-3-28 chenjun 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    
    void updateObject(Object o);
    
    /**
     * Generic method to delete an object based on class and id.
     * 
     * @param clazz
     *            model class to lookup
     * @param id
     *            the identifier (primary key) of the class
     */
    void removeObject(Class clazz, Serializable id);
    
    /**
     * @param string
     *            查询语句
     * @return List
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-3-28 chenjun 创建方法，并实现其功能 根据HQL语句查询
     *        ==============================================================<br>
     */
    
    List findByHql(String string);
    
    /**
     * @param sql
     *            查询语句
     * @param params
     *            查询参数
     * @param currentPage
     *            当前页
     * @param perPageNum
     *            每页记录数
     * @return PageInfo
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-9-13 chenjun 创建方法，并实现其功能 根据SQL语句分页查找PO对象数据
     *        ==============================================================<br>
     */
    PageInfo findPageInfoByHQLAndParams(final String sql, final List params,
        final int currentPage, final int perPageNum);
    
    /**
     * @param sql
     *            查询语句
     * @param currentPage
     *            当前页
     * @param perPageNum
     *            每页记录数
     * @return PageInfo
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-4-4 chenjun 创建方法，并实现其功能 分页查询
     *        ==============================================================<br>
     */
    PageInfo findPageInfoByHQL(final String sql, final int currentPage,
        final int perPageNum);
    
    /**
     * @param entities
     *            数据集记录
     * @author: liujr
     * @修改记录： ==============================================================<br>
     *        日期: 2007-4-11 liujr 创建方法，并实现其功能 从数据库删除指定数据集的记录
     *        ==============================================================<br>
     */
    void deleteAll(Collection entities);
    
    /**
     * @param sql
     *            查询语句
     * @return List
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-6-8 chenjun 创建方法，并实现其功能 根据JDBCSQL语句查询数据
     *        ==============================================================<br>
     */
    
    List findListByJDBCSQL(final String sql);
    
    /**
     * 获取单一值.
     * 
     * @param sql sql语句
     * @return SingleValue
     * @author: zfz
     * @修改记录： ==============================================================<br>
     *        日期:2008-6-12 zfz 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    String getSingleValueByJDBCSQL(final String sql);
    
    /**
     * 获取单一值。
     * 
     * @param sql
     *            sql语句
     * @param params
     *            参数列表
     * @return 单一值
     * @author: wuq
     * @修改记录： ==============================================================<br>
     *        日期:2009-1-5 wuq 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    String getSingleValueByJDBCAndParamsSQL(final String sql, final List params);
    
    /**
     * 获取单一值.
     * 
     * @param hql
     *            hql语句
     * @return 单一值
     */
    String getSingleValueByHql(final String hql);
    
    /**
     * @param sql
     *            查询语句
     * @param params
     *            查询参数
     * @param currentPage
     *            当前页
     * @param perPageNum
     *            每页记录数
     * @param isGroupby
     *            是否分组
     * @return PageInfo
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-6-8 chenjun 创建方法，并实现其功能 根据JDBCSQL语句查询数据
     *        ==============================================================<br>
     */
    PageInfo findPageInfoByJDBCSQLAndParams(final String sql,
        final List params, final int currentPage, final int perPageNum,
        final boolean isGroupby);
    
    /**
     * @param sql
     *            查询语句
     * @param currentPage
     *            当前页
     * @param perPageNum
     *            每页记录数
     * @param isGroupby
     *            是否分组
     * @return PageInfo
     * @author: chenjun
     * @修改记录： ==============================================================<br>
     *        日期:2007-6-8 chenjun 创建方法，并实现其功能 根据JDBCSQL语句查询数据
     *        ==============================================================<br>
     */
    PageInfo findPageInfoByJDBCSQL(final String sql, final int currentPage,
        final int perPageNum, final boolean isGroupby);
    
    /**
     * @param seqName
     *            参数
     * @return String
     * @author: panchh
     * @修改记录： ==============================================================<br>
     *        日期:2007-6-21 panchh 创建方法，并实现其功能 取SEQ值
     *        ==============================================================<br>
     */
    String getSeqNextval(String seqName);
    
    /**
     * @param sql
     *            HQL语句
     * @param params
     *            参数
     * @return List
     * @author: liujr
     * @修改记录： ==============================================================<br>
     *        日期:2007-10-29 liujr 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    List findListByHQLAndParams(final String sql, final List params);
    
    /**
     * @param sql
     *            jdbcsql
     * @param params
     *            参数
     * @return List
     * @author: liujr
     * @修改记录： ==============================================================<br>
     *        日期:2007-10-29 liujr 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    List findListByJDBCSQLAndParams(final String sql, final List params);
    
    /**
     * 获取字段comment.
     * 
     * @param owner
     *            表空间所有者
     * @param tableName
     *            表名
     * @param colName
     *            列名
     * @return 字段中文名
     * @throws ManagerException
     *             ManagerException
     * @author: panchh
     * @修改记录： ==============================================================<br>
     *        日期:Dec 3, 2007 panchh 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    String getColumnChineseName(String owner, String tableName, String colName);
    
    /**
     * 获取单一值HQL,绑定变量.
     * 
     * @param hql hql语句
     * @param params 参数
     * @return SingleValue
     * @author: yejb
     * @修改记录： ==============================================================<br>
     *        日期:2008-12-31 yejb 创建方法，并实现其功能
     *        ==============================================================<br>
     */
    String getSingleValueByHqlAndParams(String hql, List params);
}
