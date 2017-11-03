package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.organization.vo.OrganizationHXImportVo;
import cn.ffcs.uom.organization.vo.StaffOrganizationImportVo;
import cn.ffcs.uom.staff.model.Staff;

public interface StaffOrganizationManager {

	/**
	 * 分页取组织员工信息
	 * 
	 * @param staffOrganization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoStaffOrganization(
			StaffOrganization staffOrganization, int currentPage, int pageSize);

	/**
	 * 分页取类信息
	 * 
	 * @param staffOrganization
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffOrganization(Staff staff,
			StaffOrganization staffOrganization, int currentPage, int pageSize);
	
	/**
     * 分页取类信息(忽略状态)
     * 
     * @param staffOrganization
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageInfo queryPageInfoByStaffOrganizationNoStatusCd(Staff staff,
            StaffOrganization staffOrganization, int currentPage, int pageSize);

	/**
	 * 删除记录
	 * 
	 * @param staffOrganization
	 */
	public void removeStaffOrganization(StaffOrganization staffOrganization);

	/**
	 * 更新记录
	 * 
	 * @param staffOrganization
	 */
	public void updateStaffOrganization(StaffOrganization staffOrganization);

	/**
	 * 更新记录区别updateStaffOrganization该方法是有更新组织id员工id关系类型的情况下的更新； 做的事情是删除后新增
	 * 
	 * @param staffOrganization
	 */
	public void updateStaffOrganizationRelation(
			StaffOrganization staffOrganization);

	/**
	 * 保存记录
	 * 
	 * @param staffOrganization
	 */
	public void addStaffOrganization(StaffOrganization staffOrganization);

	/**
	 * 获取对象
	 * 
	 * @param id
	 * @return
	 */
	public StaffOrganization getById(Long id);

	/**
	 * 批量删除记录
	 * 
	 * @param staffOrganizationList
	 */
	public void removeStaffOrganizationList(
			List<StaffOrganization> staffOrganizationList);

	/**
	 * 员工批量移动
	 * 
	 * @param staffOrganizationList
	 * @param targetOrganization
	 */
	public void updateBatchMoveStaffOrganization(
			List<StaffOrganization> staffOrganizationList,
			Organization targetOrganization);

	/**
	 * 查询员工列表，查看一个员工是否在组织下已存在关系
	 * 
	 * @param staffOrganization
	 * @return
	 */
	public List<StaffOrganization> queryStaffOrganizationList(
			StaffOrganization staffOrganization);

	/**
	 * 检查userCode是否被使用过
	 * 
	 * @return
	 * @author Wangy 2013-7-5
	 */
	public boolean isExitsUserCode(String userCode);

	/**
	 * 自动生成OA账号
	 */
	public String getOrgUserCode();

	/**
	 * 同一个员工的归属关系只能有一个
	 * 
	 * @param staffOrganization
	 *            返回"0"--首次新增员工组织关系,默认第一条为归属组织关系,"1"--已存在归属组织关系
	 * @author Wangy 2013-7-9
	 */
	public String isCheckStaffOrgRalaCd(StaffOrganization staffOrganization,
			String opType);

	/**
	 * 
	 * @param staffOrganization
	 * @author Wangy 2013-7-11
	 */
	public void updateStaffOrgRalaCd(final StaffOrganization staffOrganization);

	/**
	 * 增加员工组织关系(包括组织员工、员工、员工账号、参与人、参与人角色、参与人联系人、参与人证件、参与人个人信息、群体)
	 * 
	 * @param staffOrganization
	 */
	public void addStaffOrganizationWithParty(
			StaffOrganization staffOrganization) throws Exception;

	/**
	 * 更新员工组织关系(包括组织员工、员工、员工账号、参与人、参与人角色、参与人联系人、参与人证件、参与人个人信息、群体)
	 * 
	 * @param staffOrganization
	 */
	public void updateStaffOrganizationWithParty(
			StaffOrganization staffOrganization) throws Exception;

	/**
	 * 当有新增数据到参与人时，获取人力中间表字段来改变参与人相关字段属性
	 * 
	 * @param party
	 * @return
	 */
	public StaffOrganization changePartyFieldFromOperateHrWhileAdding(
			StaffOrganization staffOrganization) throws Exception;

	public String doStaffOrgRelRule(Staff staff,
			Organization sourceOrganization, Organization targetOrganization);
	
	/**
	 * 检查批量上传的数据的正确性
	 * .
	 * 
	 * @param levalList
	 * @param waitUpLoadStaffOrganizationInfoList
	 * @param checkInfoList
	 * @param objDataArray
	 * @param totalColumn
	 * @return
	 * @throws Exception
	 * @author xiaof
	 * 2016年10月10日 xiaof
	 */
    public int checkUpLoadData(
        List<StaffOrganizationImportVo> waitUpLoadStaffOrganizationInfoList,
        List<String> checkInfoList, String[][] objDataArray, int totalColumn)
        throws Exception;
    
    /**
     * 对批量导入的员工组织关系进行新增，修改和删除
     * .
     * 
     * @param addStaffOrganizationList
     * @param editStaffOrganizationList
     * @param delStaffOrganizationList
     * @param delStaffList
     * @author xiaof
     * 2016年10月13日 xiaof
     */
    public void saveOrEditOrDelStaffOrganiaztion(List<StaffOrganization> addStaffOrganizationList,
        List<StaffOrganization> editStaffOrganizationList, List<StaffOrganization> editStaffOrganizationRelationList,
        List<StaffOrganization> delStaffOrganizationList, List<StaffOrganization> delStaffList);
    
	List<NodeVo> queryStaffOrgNodeVoList(Staff staff);
}
