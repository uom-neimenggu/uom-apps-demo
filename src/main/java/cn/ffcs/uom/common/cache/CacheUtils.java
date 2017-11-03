package cn.ffcs.uom.common.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单对象缓存工具.
 */
public class CacheUtils {
  
	private static CacheUtils instance = new CacheUtils();
    
    /**
     * 
     * 构造函数.
     *
     */
    protected CacheUtils() {
    }
    
    public static CacheUtils getInstance() {
        return CacheUtils.instance;
    }
    
    private final Map<String,Object>          buffer       = new HashMap<String,Object>();
    
	/**
	 * 清空缓存.
	 * 
	 * @author fanggq
	 */
	public int clean() {
		int size = this.size();
		this.buffer.clear();
		return size;
	}
    
	/**
	 * 是否存在缓存
	 * @param key
	 * @return
	 */
    public boolean has(String key){
    	return this.buffer.containsKey(key);
    }
    
    /**
     * 返回缓存数量.
     * @return size of buffer
     */
    public int size() {
        return this.buffer.size();
    }
    
    /**
     * 把对象保存到缓存中
     * @param key
     * @param object 要缓存的对象
     */
    public void put(final String key,
        final Object object) {
        this.buffer.put(key, object);
    }
    
    /**
     * 返回指定key指定有对象
     * 
     * @param key key
     * @return 
     */
    public Object get(final Object key) {
        final Object result = this.buffer.get(key);
        return result;
    }
    
    /**
     * 清除缓存对象
     * @param key 缓存对象 key
     */
    public void remove(final String key) {
        // 清除
        this.buffer.remove(key);
    }
    
}
