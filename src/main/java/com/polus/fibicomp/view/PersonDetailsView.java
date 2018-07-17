package com.polus.fibicomp.view;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "KC_PERSON_MV")
public class PersonDetailsView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PRNCPL_ID")
	private String prncplId;

	@Column(name = "FIRST_NM")
	private String firstName;

	@Column(name = "MIDDLE_NM")
	private String middleName;

	@Column(name = "LAST_NM")
	private String lastName;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "PRNCPL_NM")
	private String prncplName;

	@Column(name = "EMAIL_ADDR")
	private String emailAddress;

	@Column(name = "PRMRY_DEPT_CD")
	private String primaryDeptCode;

	@Column(name = "UNIT_NAME")
	private String unitName;

	@Column(name = "UNIT_NUMBER")
	private String unitNumber;

	@Column(name = "PHONE_NBR")
	private String phoneNumber;

	@Column(name = "ADDR_LINE_1")
	private String addressLine1;

	public String getPrncplId() {
		return prncplId;
	}

	public void setPrncplId(String prncplId) {
		this.prncplId = prncplId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public String getPrncplName() {
		return prncplName;
	}

	public void setPrncplName(String prncplName) {
		this.prncplName = prncplName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPrimaryDeptCode() {
		return primaryDeptCode;
	}

	public void setPrimaryDeptCode(String primaryDeptCode) {
		this.primaryDeptCode = primaryDeptCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
