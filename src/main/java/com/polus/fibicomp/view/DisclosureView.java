package com.polus.fibicomp.view;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "DISCLOSURE_MV")
public class DisclosureView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COI_DISCLOSURE_ID")
	private Integer coiDisclosureId;

	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;

	@Column(name = "COI_DISCLOSURE_NUMBER")
	private String coiDisclosureNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "DISCLOSURE_DISPOSITION_CODE")
	private Integer disclosureDispositionCode;

	@Column(name = "DISCLOSURE_DISPOSITION")
	private String disclosureDisposition;

	@Column(name = "DISCLOSURE_STATUS_CODE")
	private Integer disclosureStatusCode;

	@Column(name = "DISCLOSURE_STATUS")
	private String disclosureStatus;

	@Column(name = "MODULE_ITEM_KEY")
	private String moduleItemKey;

	@Column(name = "DISC_ACTIVE_STATUS")
	private Integer discActiveStatus;

	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCoiDisclosureId() {
		return coiDisclosureId;
	}

	public void setCoiDisclosureId(Integer coiDisclosureId) {
		this.coiDisclosureId = coiDisclosureId;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getCoiDisclosureNumber() {
		return coiDisclosureNumber;
	}

	public void setCoiDisclosureNumber(String coiDisclosureNumber) {
		this.coiDisclosureNumber = coiDisclosureNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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

	public Integer getDisclosureDispositionCode() {
		return disclosureDispositionCode;
	}

	public void setDisclosureDispositionCode(Integer disclosureDispositionCode) {
		this.disclosureDispositionCode = disclosureDispositionCode;
	}

	public String getDisclosureDisposition() {
		return disclosureDisposition;
	}

	public void setDisclosureDisposition(String disclosureDisposition) {
		this.disclosureDisposition = disclosureDisposition;
	}

	public Integer getDisclosureStatusCode() {
		return disclosureStatusCode;
	}

	public void setDisclosureStatusCode(Integer disclosureStatusCode) {
		this.disclosureStatusCode = disclosureStatusCode;
	}

	public String getDisclosureStatus() {
		return disclosureStatus;
	}

	public void setDisclosureStatus(String disclosureStatus) {
		this.disclosureStatus = disclosureStatus;
	}

	public String getModuleItemKey() {
		return moduleItemKey;
	}

	public void setModuleItemKey(String moduleItemKey) {
		this.moduleItemKey = moduleItemKey;
	}

	public Integer getDiscActiveStatus() {
		return discActiveStatus;
	}

	public void setDiscActiveStatus(Integer discActiveStatus) {
		this.discActiveStatus = discActiveStatus;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Timestamp getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
