package com.polus.fibicomp.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.pojo.UnitAdministrator;

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
	public List<UnitAdministrator> isUnitAdmin(String personId);

	/**
	 * This method is used to unit administrator detail.
	 * @param personId - ID of the user.
	 * @return A list of units.
	 */
	public List<Unit> isUnitAdminDetail(String personId);
}
