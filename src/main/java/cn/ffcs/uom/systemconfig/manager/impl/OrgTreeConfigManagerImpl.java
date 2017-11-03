package cn.ffcs.uom.systemconfig.manager.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import oracle.jdbc.OracleTypes;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;
import cn.ffcs.uom.orgTreeCalc.manager.TreeBindingRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeBindingRule;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.systemconfig.manager.OrgTreeConfigManager;
import cn.ffcs.uom.systemconfig.model.OrgTreeConfig;

@Service("orgTreeConfigManager")
@Scope("prototype")
public class OrgTreeConfigManagerImpl extends BaseDaoImpl implements OrgTreeConfigManager {
    
    @Resource(name = "treeBindingRuleManager")
    private TreeBindingRuleManager treeBindingRuleManager;
    
    /**
     * 加载组织树
     * 
     * @param orgTree
     */
    @Override
    public void loadOrgTreeRootNode(final Listbox listbox) {
        super.getJdbcTemplate().execute(new ConnectionCallback<Object>() {
            @Override
            public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
                CallableStatement cstmt = null;
                cstmt = conn.prepareCall("{CALL PKG_OPERATELOG_PUBLISH.loadOrgTreeRootNode(?)}");
                cstmt.registerOutParameter(1, OracleTypes.CURSOR);
                cstmt.execute();
                ResultSet rs = (ResultSet) cstmt.getObject(1);
                List<NodeVo> retAttrValues = new ArrayList();
                while (rs.next()) {
                    NodeVo vo = new NodeVo();
                    vo.setId(rs.getString("org_id") + "&" + rs.getString("rela_cd"));
                    vo.setName(rs.getString("org_name"));
                    retAttrValues.add(vo);
                }
                ListboxUtils.rendererForEdit(listbox, retAttrValues);
                return null;
            }
        });
    }
    
    /**
     * 加载组织树
     * 
     * @param orgTree
     */
    @Override
    public void loadOrgTreeRootNode(final Tree orgTreeRootNode) {
        
        super.getJdbcTemplate().execute(new ConnectionCallback<Object>() {
            @Override
            public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
                CallableStatement cstmt = null;
                cstmt = conn.prepareCall("{CALL PKG_OPERATELOG_PUBLISH.loadOrgTreeRootNode(?)}");
                cstmt.registerOutParameter(1, OracleTypes.CURSOR);
                cstmt.execute();
                ResultSet rs = (ResultSet) cstmt.getObject(1);
                while (rs.next()) {
                    Treechildren treechildren = orgTreeRootNode.getTreechildren();
                    Treeitem titem = new Treeitem();
                    Treerow trow = new Treerow();
                    Treecell title = new Treecell(rs.getString("org_name"));
                    Treecell rela = new Treecell(rs.getString("rela_cd"));
                    title.setParent(trow);
                    rela.setParent(trow);
                    trow.setParent(titem);
                    MdsionOrgRelation orgRela = MdsionOrgRelation.newInstance();
                    orgRela.setTreeLabel(rs.getString("org_name"));
                    orgRela.setRelaCd(rs.getString("rela_cd"));
                    orgRela.setOrgId(Long.valueOf(rs.getString("org_id")));
                    TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(orgRela);
                    titem.setValue(treeNodeImpl);
                    titem.setParent(treechildren);
                }
                return null;
            }
        });
        
    }
    
    /**
     * 选中已打开的子节点
     * 
     * @param titem
     */
    private void selOpenNode(Treeitem titem, boolean isChecked) {
        if (titem.isOpen() && titem.getTreechildren() != null) {
            Collection<Treeitem> treeitems = titem.getTreechildren().getItems();
            for (Treeitem treeitem : treeitems) {
                Checkbox cb = (Checkbox) treeitem.getTreerow().getFirstChild().getFirstChild();
                cb.setChecked(isChecked);
                if (treeitem.isOpen())
                    selOpenNode(treeitem, isChecked);
            }
        }
    }
    
    /**
     * 加载专业多选树根节点
     * 
     * @param professionalTree
     * @param orgTreeId
     */
    @Override
    public void loadProfessionalCheckTreeRoot(Tree professionalTree, String orgTreeId,
        final List<AroleProfessionalTree> aroleProfessionalTreeList) {
        if (!StrUtil.isEmpty(orgTreeId)) {
            List<TreeBindingRule> treeBindingRuleList = treeBindingRuleManager
                .findTreeBindingRule(Long.valueOf(orgTreeId));
            if (treeBindingRuleList.size() > 0) {
                Treechildren treechildren = professionalTree.getTreechildren();
                for (final TreeBindingRule treeBindingRule : treeBindingRuleList) {
                    
                    OrganizationRelation orgRelaRoot = OrganizationRelation.newInstance();
                    orgRelaRoot.setOrgId(treeBindingRule.getRefTreeId());
                    orgRelaRoot.setRelaCd(treeBindingRule.getRefOrgRelaCd());
                    orgRelaRoot.setRelaOrgId(OrganizationConstant.ROOT_TREE_ORG_ID);
                    
                    final Treeitem titem = new Treeitem();
                    Treerow trow = new Treerow();
                    Treecell tcell = new Treecell("");
                    
                    final Checkbox checkbox = new Checkbox(treeBindingRule.getRefTreeName());
                    checkbox.setAttribute("professionalTreeId",
                        String.valueOf(treeBindingRule.getRefTreeId()));
                    checkbox.setId(UUID.randomUUID().toString());
                    checkbox.setValue(treeBindingRule.getRefTreeId() + "&"
                        + treeBindingRule.getRefOrgRelaCd());
                    for (AroleProfessionalTree aroleProfessionalTree : aroleProfessionalTreeList) {
                        if (treeBindingRule.getRefTreeId().toString()
                            .equals(aroleProfessionalTree.getOrgId().toString())
                            && treeBindingRule.getRefTreeId().toString()
                                .equals(aroleProfessionalTree.getProfessionalTreeId().toString())) {
                            checkbox.setChecked(true);
                            break;
                        }
                    }
                    tcell.appendChild(checkbox);
                    
                    tcell.setParent(trow);
                    trow.setParent(titem);
                    TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
                        orgRelaRoot);
                    titem.setValue(treeNodeImpl);
                    titem.setParent(treechildren);
                    
                    titem.addEventListener("onOpen", new EventListener() {
                        public void onEvent(final Event e) throws Exception {
                            Treechildren tc = titem.getTreechildren();
                            if (tc.getChildren().isEmpty()) {
                                loadDerivationTree(String.valueOf(treeBindingRule.getOrgTreeId()),
                                    String.valueOf(treeBindingRule.getRefTreeId()),
                                    treeBindingRule.getRefOrgRelaCd(), titem,
                                    aroleProfessionalTreeList,
                                    String.valueOf(treeBindingRule.getRefTreeId()));
                            }
                        }
                    });
                    
                    checkbox.addEventListener("onCheck", new EventListener() {
                        public void onEvent(final Event e) throws Exception {
                            selOpenNode(titem, checkbox.isChecked());
                        }
                    });
                    
                    new Treechildren().setParent(titem);
                    titem.setOpen(false);
                }
            } else {
                ZkUtil.showError("未绑定专业树", "提示信息");
            }
        } else {
            ZkUtil.showError("加载专业树失败", "提示信息");
        }
    }
    
    /**
     * 加载专业多选树根节点
     * 
     * @param professionalTree
     * @param orgTreeId
     */
    @Override
    public void loadProfessionalCheckTreeRootDw(Tree professionalTree, String orgTreeId,
        final List<AroleProfessionalTree> aroleProfessionalTreeList) {
        if (!StrUtil.isEmpty(orgTreeId)) {
            String sql = "select * from mdsion_org_tree t where t.status_cd = ? and t.mdsion_org_tree_id=? and t.isshow=1";
            List params = new ArrayList();
            params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
            params.add(orgTreeId);
            List<MdsionOrgTree> treeBindingRuleList = MdsionOrgTree.repository().jdbcFindList(sql,
                params, MdsionOrgTree.class);
            if (treeBindingRuleList.size() > 0) {
                Treechildren treechildren = professionalTree.getTreechildren();
                for (final MdsionOrgTree mdsionOrgTree : treeBindingRuleList) {
                    
                    OrganizationRelation orgRelaRoot = OrganizationRelation.newInstance();
                    orgRelaRoot.setOrgId(mdsionOrgTree.getOrgId());
                    orgRelaRoot.setRelaCd(mdsionOrgTree.getMdsionOrgRelTypeCd());
                    orgRelaRoot.setRelaOrgId(OrganizationConstant.ROOT_TREE_ORG_ID);
                    
                    final Treeitem titem = new Treeitem();
                    Treerow trow = new Treerow();
                    Treecell tcell = new Treecell("");
                    
                    final Checkbox checkbox = new Checkbox(mdsionOrgTree.getOrgTreeName());
                    checkbox.setAttribute("professionalTreeId",
                        String.valueOf(mdsionOrgTree.getOrgId()));
                    checkbox.setId(UUID.randomUUID().toString());
                    checkbox.setValue(mdsionOrgTree.getOrgId() + "&"
                        + mdsionOrgTree.getMdsionOrgRelTypeCd());
                    for (AroleProfessionalTree aroleProfessionalTree : aroleProfessionalTreeList) {
                        if (String.valueOf(mdsionOrgTree.getOrgId()).equals(
                            aroleProfessionalTree.getOrgId().toString())
                            && String.valueOf(mdsionOrgTree.getOrgId()).equals(
                                aroleProfessionalTree.getProfessionalTreeId().toString())) {
                            checkbox.setChecked(true);
                            break;
                        }
                    }
                    tcell.appendChild(checkbox);
                    
                    tcell.setParent(trow);
                    trow.setParent(titem);
                    TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
                        orgRelaRoot);
                    titem.setValue(treeNodeImpl);
                    titem.setParent(treechildren);
                    
                    titem.addEventListener("onOpen", new EventListener() {
                        public void onEvent(final Event e) throws Exception {
                            Treechildren tc = titem.getTreechildren();
                            if (tc.getChildren().isEmpty()) {
                                loadDerivationTree(
                                    String.valueOf(mdsionOrgTree.getMdsionOrgTreeId()),
                                    String.valueOf(mdsionOrgTree.getOrgId()),
                                    mdsionOrgTree.getMdsionOrgRelTypeCd(), titem,
                                    aroleProfessionalTreeList,
                                    String.valueOf(mdsionOrgTree.getOrgId()));
                                // loadDerivationTree(String.valueOf(mdsionOrgTree.getMdsionOrgTreeId()),String.valueOf(mdsionOrgTree.getOrgId()),mdsionOrgTree.getMdsionOrgRelTypeCd(),titem,String.valueOf(mdsionOrgTree.getOrgId()),isShowAllFinal,roleIds);
                                
                            }
                        }
                    });
                    
                    checkbox.addEventListener("onCheck", new EventListener() {
                        public void onEvent(final Event e) throws Exception {
                            selOpenNode(titem, checkbox.isChecked());
                        }
                    });
                    
                    new Treechildren().setParent(titem);
                    titem.setOpen(false);
                }
            } else {
                ZkUtil.showError("未绑定专业树", "提示信息");
            }
        } else {
            ZkUtil.showError("加载专业树失败", "提示信息");
        }
    }
    
    /**
     * 加载推导树
     * 
     * @param orgTreeId
     *            组织树ID
     * @param parentOrgId
     *            上级组织ID
     * @param relaCd
     *            关系类型
     * @param parentTreeitem
     *            上级节点
     * @param professionalTreeId
     *            专业树ID
     */
    public void loadDerivationTree(final String orgTreeId, final String parentOrgId,
        final String baseRelaCd, final Treeitem parentTreeitem,
        final List<AroleProfessionalTree> aroleProfessionalTreeList, final String professionalTreeId) {
        super.getJdbcTemplate().execute(new ConnectionCallback<Object>() {
            @Override
            public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
                CallableStatement cstmt = null;
                cstmt = conn
                    .prepareCall("{CALL PKG_OPERATELOG_PUBLISH.loadDerivationTree(?,?,?,?)}");
                cstmt.setString(1, orgTreeId);
                cstmt.setString(2, parentOrgId);
                cstmt.setString(3, baseRelaCd);
                cstmt.registerOutParameter(4, OracleTypes.CURSOR);
                cstmt.execute();
                ResultSet rs = (ResultSet) cstmt.getObject(4);
                while (rs.next()) {
                    final String orgId = rs.getString("ORG_ID");
                    Long orgRelaId = Long.valueOf(rs.getString("RELA_ORG_ID"));
                    String relaCd = rs.getString("RELA_CD");
                    String orgName = rs.getString("ORG_NAME");
                    OrganizationRelation orgRela = OrganizationRelation.newInstance();
                    orgRela.setOrgId(Long.valueOf(orgId));
                    orgRela.setRelaOrgId(orgRelaId);
                    orgRela.setRelaCd(relaCd);
                    
                    Treechildren tchild = new Treechildren();
                    final Treeitem titem = new Treeitem();
                    Treerow trow = new Treerow();
                    Treecell tcell = new Treecell();
                    
                    final Checkbox checkbox = new Checkbox(orgName);
                    checkbox.setAttribute("professionalTreeId", professionalTreeId);
                    checkbox.setId(UUID.randomUUID().toString());
                    checkbox.setValue(orgId + "&" + baseRelaCd);
                    for (AroleProfessionalTree aroleProfessionalTree : aroleProfessionalTreeList) {
                        if (orgId.equals(aroleProfessionalTree.getOrgId().toString())
                            && professionalTreeId.equals(aroleProfessionalTree
                                .getProfessionalTreeId().toString())) {
                            checkbox.setChecked(true);
                            break;
                        }
                    }
                    tcell.appendChild(checkbox);
                    
                    tcell.setParent(trow);
                    trow.setParent(titem);
                    
                    TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
                        orgRela);
                    titem.setValue(treeNodeImpl);
                    titem.setParent(tchild);
                    
                    if (parentTreeitem != null) {
                        if (parentTreeitem.getTreechildren() != null) {
                            titem.setParent(parentTreeitem.getTreechildren());
                        } else {
                            tchild.setParent(parentTreeitem);
                        }
                    }
                    
                    if (Integer.valueOf(rs.getString("CHILDNODES")) > 0) {// 有子节点的情况
                        titem.addEventListener("onOpen", new EventListener() {
                            public void onEvent(final Event e) throws Exception {
                                Treechildren tc = titem.getTreechildren();
                                if (tc.getChildren().isEmpty()) {
                                    loadDerivationTree(orgTreeId, orgId, baseRelaCd, titem,
                                        aroleProfessionalTreeList, professionalTreeId);
                                }
                            }
                        });
                        Treechildren treechildren = new Treechildren();
                        treechildren.setParent(titem);
                        titem.setOpen(false);
                    }
                    
                    checkbox.addEventListener("onCheck", new EventListener() {
                        public void onEvent(final Event e) throws Exception {
                            if (checkbox.isChecked()) {
                                Treeitem parentTreeitem = titem.getParentItem();
                                while (parentTreeitem != null) {
                                    Checkbox cb = (Checkbox) parentTreeitem.getTreerow()
                                        .getFirstChild().getFirstChild();
                                    cb.setChecked(true);
                                    if (parentTreeitem.getParentItem() != null) {
                                        parentTreeitem = parentTreeitem.getParentItem();
                                    } else {
                                        parentTreeitem = null;
                                    }
                                }
                                ;
                            }
                            selOpenNode(titem, checkbox.isChecked());
                        }
                    });
                }
                return null;
            }
        });
    }
    
    /**
     * 获取组织树关联的组织类型
     * 
     * @param orgId
     */
    public List<OrgTreeConfig> findOrgType(Long orgId) {
        StringBuffer hql = new StringBuffer(
            "SELECT T1.* FROM ORG_TREE_CONFIG T1,MDSION_ORG_TREE T2 WHERE T1.STATUS_CD = ? AND T2.STATUS_CD=1000 AND T1.ORG_TREE_ID = T2.MDSION_ORG_TREE_ID AND T2.ORG_ID=?");
        List params = new ArrayList();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(orgId);
        return DefaultDaoFactory.getDefaultDao().jdbcFindList(hql.toString(), params,
            OrgTreeConfig.class);
    }
    
    @Override
    public PageInfo loadOrgTreeRootNodeDw(int currentPage, int pageSize) {
        // TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer(
            "SELECT T1.* FROM MDSION_ORG_TREE T1 WHERE T1.STATUS_CD = ?");
        List params = new ArrayList();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        return DefaultDaoFactory.getDefaultDao().jdbcFindPageInfo(sql.toString(), params, currentPage, pageSize,
            MdsionOrgTree.class);
    }
}
