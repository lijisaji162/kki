package com.polus.fibicomp.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "ROLODEX")
public class Rolodex implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ROLODEX_ID")
	private Integer rolodexId;

	@Column(name = "ADDRESS_LINE_1")
	private String addressLine1;

	@Column(name = "ADDRESS_LINE_2")
	private String addressLine2;

	@Column(name = "ADDRESS_LINE_3")
	private String addressLine3;

	@Column(name = "CITY")
	private String city;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "COUNTRY_CODE")
	private String countryCode;

	@Column(name = "COUNTY")
	private String county;

	@Column(name = "DELETE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean deleteFlag;

	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	@Column(name = "FAX_NUMBER")
	private String faxNumber;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "ORGANIZATION")
	private String organization;

	@Column(name = "OWNED_BY_UNIT")
	private String ownedByUnit;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "POSTAL_CODE")
	private String postalCode;

	@Column(name = "PREFIX")
	private String prefix;

	@Column(name = "SPONSOR_ADDRESS_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean sponsorAddressFlag;

	@Column(name = "SPONSOR_CODE")
	private String sponsorCode;

	@Column(name = "STATE")
	private String state;

	@Column(name = "SUFFIX")
	private String suffix;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "ACTV_IND")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean active;

	/*
	 * @ManyToOne(cascade = { CascadeType.REFRESH })
	 * 
	 * @JoinColumn(name = "OWNED_BY_UNIT", referencedColumnName = "UNIT_NUMBER",
	 * insertable = false, updatable = false) private Unit unit;
	 */

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Version
	@Column(name = "VER_NBR", length = 8)
	protected Long versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	protected String objectId;

	@Transient
	public String fullName;

	public String getFullName() {
		final StringBuilder name = new StringBuilder();
		if (this.getLastName() != null) {
			name.append(this.getLastName());
			name.append(", ");
		}
		if (this.getPrefix() != null) {
			name.append(this.getPrefix());
			name.append(" ");
		}
		if (this.getFirstName() != null) {
			name.append(this.getFirstName());
			name.append(" ");
		}
		if (this.getMiddleName() != null) {
			name.append(this.getMiddleName());
		}
		return name.length() > 0 ? name.toString() : null;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
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

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOwnedByUnit() {
		return ownedByUnit;
	}

	public void setOwnedByUnit(String ownedByUnit) {
		this.ownedByUnit = ownedByUnit;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isSponsorAddressFlag() {
		return sponsorAddressFlag;
	}

	public void setSponsorAddressFlag(boolean sponsorAddressFlag) {
		this.sponsorAddressFlag = sponsorAddressFlag;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/*
	 * public Unit getUnit() { return unit; }
	 * 
	 * public void setUnit(Unit unit) { this.unit = unit; }
	 */

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
