package com.polus.fibicomp.role.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.role.pojo.RoleMemberBo;

@Service
public interface RoleDao {

	/**
	 * This method is used to member detail having role.
	 * @param personId - ID of the user.
	 * @param roleId - Role Id of the user.
	 * @return A list of role member object.
	 */
	public List<RoleMemberBo> fetchUserRole(String personId, String roleId);

	/**
	 * This method is used to check superuser role.
	 * @param personId - ID of the user.
	 * @param roleId - Role Id of the user.
	 * @return A boolean value to indicate whether the user have superuser role or not.
	 */
	public boolean fetchSuperUserPersonRole(String personId, String roleId);

}
