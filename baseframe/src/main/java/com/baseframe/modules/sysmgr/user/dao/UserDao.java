package com.baseframe.modules.sysmgr.user.dao;

import com.baseframe.modules.sysmgr.organization.model.OrganizationModel;
import com.baseframe.modules.sysmgr.permission.model.PermissionModel;
import com.baseframe.modules.sysmgr.role.model.RoleModel;
import com.baseframe.modules.sysmgr.user.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.Map;
import java.util.List;

/**
 * UserModel: zjt
 * DateTime: 16/9/25 15:11
 * <p>
 * 1, @MapperScan标识为spring动态为其创建代理类
 * 2, 接口中的方法名称需要与Mybatis映射文件中的sql语句Id一致, 需注意方法参数,返回值
 * 3, 标注的UserDao将作为spring的bean的id(或name)使用, 以方便在service层进行注入
 */
@MapperScan
public interface UserDao {

    /**
     * 根据账号查询用户信息
     *
     * @param account
     * @return
     */
    Integer queryUserExistsByAccount(String account);

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    Integer addUser(UserModel user);

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    Integer deleteUser(String[] ids);

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    Integer updateUser(UserModel user);

    /**
     * 分页查询用户列表
     *
     * @return
     */
    List<UserModel> queryUserList(Map<String, Object> params);

    /**
     * 根据用户id查询
     *
     * @param id
     * @return
     */
    UserModel queryUserById(String id);

    /**
     * 根据用户id查询角色列表
     *
     * @param userId
     * @return
     */
    List<RoleModel> queryRoleListByUserId(String userId);

    /**
     * 删除用户角色
     *
     * @param userId
     * @return
     */
    Integer deleteRoleByUserId(String userId);

    /**
     * 添加角色
     *
     * @param params
     * @return
     */
    Integer addRoleToUser(Map<String, Object> params);

    /**
     * 根据账号查询用户信息
     *
     * @param account
     * @return
     */
    UserModel queryUserByAccount(String account);

    /**
     * 查询用户权限列表
     *
     * @param userId
     * @return
     */
    List<PermissionModel> queryPermissionListByUserId(String userId);

    /**
     * 添加组织机构
     *
     * @param userId
     * @param orgId
     * @return
     */
    Integer updateUserOrganization(@Param("userId") String userId, @Param("orgId") String orgId);

    /**
     * 查询用户的组织机构
     *
     * @param userId
     * @return
     */
    OrganizationModel queryOrgInfoByUserId(@Param("userId") String userId);

}