package com.polus.fibicomp.role.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.role.pojo.RoleMemberBo;

@Service
public interface RoleDao {

	public List<RoleMemberBo> fetchCreateProposalPersonRole(String personId, String roleId);

}
