package com.polus.fibicomp.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polus.fibicomp.pojo.PrincipalBo;

public interface ApplicationUserRepository extends JpaRepository<PrincipalBo, Long> {

	/**
	 * This method is used to validate user credentials.
	 * @param principalName - Username of the login user.
	 * @return PrincipalBo - user Object.
	 */
	public PrincipalBo findByPrincipalName(String principalName);

}
