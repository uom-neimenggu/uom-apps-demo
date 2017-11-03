package cn.ffcs.uom.common.zkplus.zul.tree.node.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import lombok.Setter;

import org.apache.commons.beanutils.PropertyUtils;

import cn.ffcs.raptornuke.plugin.common.exception.RtManagerException;
import cn.ffcs.uom.common.zkplus.ZKRebuildUuid;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNode;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntityCtrl;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntityHint;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntityId;

/**
 * TreeNodeImpl.
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author wuyx
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2011-6-7
 * @功能说明：
 * 
 * @param <T>
 */
public class TreeNodeImpl<T extends TreeNodeEntity> implements TreeNode {
    
    /**
     * entity.
     */
    protected T                   entity   = null;
    
    /**
     * children.
     */
    protected ArrayList<TreeNode> children = null;
    /**
     * 
     */
    @Setter
    protected Boolean             isOpen   = false;
    
    /**
     * TreeNodeImpl构造函数.
     * @param entity T
     */
    public TreeNodeImpl(final T entity) {
        this.entity = entity;
        if (entity instanceof TreeNodeEntityCtrl) {
            this.setIsOpen(((TreeNodeEntityCtrl) entity).isOpen());
        }
    }
    
    /**
     * readChildren.
     */
    @SuppressWarnings("unchecked")
    public void readChildren() {
        if (this.entity == null) {
            this.children = new ArrayList<TreeNode>();
        } else {
            this.children = new ArrayList<TreeNode>();
            ArrayList<T> entityList = null;
            if (this.entity.isGetRoot()) {
                entityList = (ArrayList<T>) this.entity.getRoot();
            } else {
                entityList = (ArrayList<T>) this.entity.getChildren();
            }
            final int sum = entityList == null ? 0
                : entityList.size();
            for (int i = 0; i < sum; i++) {
                final TreeNodeImpl tni = new TreeNodeImpl(entityList.get(i));
                this.children.add(tni);
            }
        }
    };
    
    public ArrayList<TreeNode> getChildren() {
        return this.children;
    }
    
    /**
     * getChild.
     * @param arg1 int
     * @return TreeNode
     */
    public TreeNode getChild(final int arg1) {
        TreeNode child = null;
        
        if (this.children == null) {
            this.readChildren();
        }
        
        if (this.children != null && (arg1 > -1 && arg1 < this.children.size())) {
            child = this.children.get(arg1);
        }
        
        return child;
    }
    
    /**
     * getChildCount.
     * @return int
     */
    public int getChildCount() {
        if (this.children == null) {
            this.readChildren();
        }
        
        if (this.children != null) {
            return this.children.size();
        }
        
        return 0;
    }
    
    public boolean isLeaf() {
//        return false;
        return (this.getChildCount() == 0);
    }
    
    public void setChildren(final ArrayList<TreeNode> children) {
        this.children = children;
    }
    
    public String getLable() {
        return this.entity.getLabel();
    }
    
    /**
     * entity.
     * @param lable String
     */
    public void setLable(final String lable) {
    }
    
    public T getEntity() {
        return this.entity;
    }
    
    public void setEntity(final T entity) {
        this.entity = entity;
    }
    
    /**
     * entity getAttr.
     * @param name String
     * @return Object
     */
    public Object getAttr(final String name) {
        try {
            return PropertyUtils.getProperty(this.entity, name);
        } catch (final IllegalAccessException e) {
            throw new RtManagerException("根据属性名称，读取属性值" + name, this.getClass(), "get",
                "IllegalAccessException");
        } catch (final InvocationTargetException e) {
            throw new RtManagerException("根据属性名称，读取属性值" + name, this.getClass(), "get",
                "InvocationTargetException");
        } catch (final NoSuchMethodException e) {
            throw new RtManagerException("根据属性名称，读取属性值" + name, this.getClass(), "get",
                "NoSuchMethodException");
        }
    }
    
    @Override
    public String getHint() {
        if (this.entity instanceof TreeNodeEntityHint) {
            return ((TreeNodeEntityHint) this.entity).getHint();
        }
        return null;
    }
    
    /**
     * 
     * {@inheritDoc}
     * @see com.ffcs.zkplus.zul.tree.node.TreeNode#getTestId()
     * @author zhoupc
     * 2011-5-9 zhoupc
     */
    public String getTestId() {
        if (ZKRebuildUuid.needReuild() && this.entity instanceof TreeNodeEntityId) {
            return ((TreeNodeEntityId) this.entity).getTestId();
        }
        return null;
    }
    
    public boolean isOpen() {
        return this.isOpen;
    }
}
