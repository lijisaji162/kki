package com.polus.fibicomp.grantcall.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_GRANT_CALL_CONTACTS")
public class GrantCallContact implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "grantContactIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "grantContactIdGenerator")
	@Column(name = "GRANT_CONTACT_ID", updatable = false, nullable = false)
	private Integer grantContactId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_GRANT_CALL_CONTACTS"), name = "GRANT_HEADER_ID", referencedColumnName = "GRANT_HEADER_ID")
	private GrantCall grantCall;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "IS_SMU_PERSON")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isSMUPerson;

	public Integer getGrantContactId() {
		return grantContactId;
	}

	public void setGrantContactId(Integer grantContactId) {
		this.grantContactId = grantContactId;
	}

	public GrantCall getGrantCall() {
		return grantCall;
	}

	public void setGrantCall(GrantCall grantCall) {
		this.grantCall = grantCall;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsSMUPerson() {
		return isSMUPerson;
	}

	public void setIsSMUPerson(Boolean isSMUPerson) {
		this.isSMUPerson = isSMUPerson;
	}

}
