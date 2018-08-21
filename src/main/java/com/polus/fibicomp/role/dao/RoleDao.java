package com.polus.fibicomp.role.dao;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.role.pojo.RoleMemberBo;

@Service
public interface RoleDao {

	public RoleMemberBo fetchCreateProposalPersonRole(String personId, String roleId);

}
