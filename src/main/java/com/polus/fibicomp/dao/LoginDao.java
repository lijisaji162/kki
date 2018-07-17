package com.polus.fibicomp.dao;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;

@Service
public interface LoginDao {

	/**
	 * This method is used to authenticate the user.
	 * @param userName - Username of the user.
	 * @param password - Password of the user.
	 * @return PrincipalBo containing user details.
	 */
	public PrincipalBo authenticate(String userName, String password);

	/**
	 * This method is used to read person data.
	 * @param userName - Username of the user.
	 * @return A PersonDTO object.
	 */
	public PersonDTO readPersonData(String userName);

	/**
	 * This method is used to find the role of the user.
	 * @param personId - ID of the user.
	 * @return A boolean value to specify the user role.
	 */
	public boolean isUnitAdmin(String personId);
}
