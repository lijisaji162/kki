package com.polus.fibicomp.pojo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

/**
 * Person DTO with basic details about a person
 */
public class PersonDTO {

	private String personID = "";

	private String firstName = "";

	private String lastName = "";

	private String fullName = "";

	private String email = "";

	private BigDecimal roleNumber = null;

	private String userName = "";

	private Integer createNo = 0;

	private String hasDual = "";

	private String unitNumber = "";

	private boolean isUnitAdmin = false;

	private boolean isLogin = false;

	private String accessToken = "p6creG39TOQL3Je";

	private Collection<? extends GrantedAuthority> jwtRoles;

	private List<Unit> leadUnits;

	private boolean isCreateProposal = false;

	private List<UnitAdministrator> unitAdministrators;

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public String getHasDual() {
		return hasDual;
	}

	public void setHasDual(String hasDual) {
		this.hasDual = hasDual;
	}

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getRoleNumber() {
		return roleNumber;
	}

	public void setRoleNumber(BigDecimal roleNumber) {
		this.roleNumber = roleNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCreateNo() {
		return createNo;
	}

	public void setCreateNo(Integer createNo) {
		this.createNo = createNo;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public boolean isUnitAdmin() {
		return isUnitAdmin;
	}

	public void setUnitAdmin(boolean isUnitAdmin) {
		this.isUnitAdmin = isUnitAdmin;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Collection<? extends GrantedAuthority> getJwtRoles() {
		return jwtRoles;
	}

	public void setJwtRoles(Collection<? extends GrantedAuthority> jwtRoles) {
		this.jwtRoles = jwtRoles;
	}

	public boolean isCreateProposal() {
		return isCreateProposal;
	}

	public void setCreateProposal(boolean isCreateProposal) {
		this.isCreateProposal = isCreateProposal;
	}

	public List<Unit> getLeadUnits() {
		return leadUnits;
	}

	public void setLeadUnits(List<Unit> leadUnits) {
		this.leadUnits = leadUnits;
	}

	public List<UnitAdministrator> getUnitAdministrators() {
		return unitAdministrators;
	}

	public void setUnitAdministrators(List<UnitAdministrator> unitAdministrators) {
		this.unitAdministrators = unitAdministrators;
	}

}
