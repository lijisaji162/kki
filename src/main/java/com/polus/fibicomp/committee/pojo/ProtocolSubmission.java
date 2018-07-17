package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "PROTOCOL_SUBMISSION_V")
public class ProtocolSubmission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUBMISSION_ID")
	private Long submissionId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PROTOCOL_SUBMISSION8"), name = "FIBI_SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
	private CommitteeSchedule committeeSchedule;

	@Column(name = "SUBMISSION_NUMBER")
	private Integer submissionNumber;

	@Column(name = "FIBI_COMMITTEE_ID")
	private String committeeId;

	@Column(name = "PROTOCOL_ID")
	private Long protocolId;

	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "PI_PERSON_NAME")
	private String piPersonName;

	@Column(name = "PI_PERSON_ID")
	private String piPersonId;

	@Column(name = "PROTOCOL_TITLE")
	private String protocolTitle;

	@Column(name = "SUBMISSION_TYPE_CODE")
	private String submissionTypeCode;

	@ManyToOne(optional = false)
	@JoinColumn(name = "SUBMISSION_TYPE_CODE", referencedColumnName = "SUBMISSION_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolSubmissionType protocolSubmissionType;

	@Column(name = "SUBMISSION_TYPE_QUAL_CODE")
	private String submissionTypeQualifierCode;

	@ManyToOne(optional = false)
	@JoinColumn(name = "SUBMISSION_TYPE_QUAL_CODE", referencedColumnName = "SUBMISSION_TYPE_QUAL_CODE", insertable = false, updatable = false)
	private ProtocolSubmissionQualifierType qualifierType;

	@Column(name = "SUBMISSION_STATUS_CODE")
	private String submissionStatusCode;

	@ManyToOne(optional = false)
	@JoinColumn(name = "SUBMISSION_STATUS_CODE", referencedColumnName = "SUBMISSION_STATUS_CODE", insertable = false, updatable = false)
	private ProtocolSubmissionStatus submissionStatus;

	@Column(name = "PROTOCOL_REVIEW_TYPE_CODE")
	private String protocolReviewTypeCode;

	@ManyToOne(optional = false)
	@JoinColumn(name = "PROTOCOL_REVIEW_TYPE_CODE", referencedColumnName = "PROTOCOL_REVIEW_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolReviewType protocolReviewType;

	@Column(name = "SUBMISSION_DATE")
	private Date submissionDate;

	@Column(name = "PROTOCOL_ACTIVE")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean protocolActive;

	@Transient
	private String documentNumber;

	public Long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}

	public Long getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Long protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getPiPersonName() {
		return piPersonName;
	}

	public void setPiPersonName(String piPersonName) {
		this.piPersonName = piPersonName;
	}

	public String getProtocolTitle() {
		return protocolTitle;
	}

	public void setProtocolTitle(String protocolTitle) {
		this.protocolTitle = protocolTitle;
	}

	public String getSubmissionTypeCode() {
		return submissionTypeCode;
	}

	public void setSubmissionTypeCode(String submissionTypeCode) {
		this.submissionTypeCode = submissionTypeCode;
	}

	public ProtocolSubmissionType getProtocolSubmissionType() {
		return protocolSubmissionType;
	}

	public void setProtocolSubmissionType(ProtocolSubmissionType protocolSubmissionType) {
		this.protocolSubmissionType = protocolSubmissionType;
	}

	public String getSubmissionTypeQualifierCode() {
		return submissionTypeQualifierCode;
	}

	public void setSubmissionTypeQualifierCode(String submissionTypeQualifierCode) {
		this.submissionTypeQualifierCode = submissionTypeQualifierCode;
	}

	public ProtocolSubmissionQualifierType getQualifierType() {
		return qualifierType;
	}

	public void setQualifierType(ProtocolSubmissionQualifierType qualifierType) {
		this.qualifierType = qualifierType;
	}

	public String getSubmissionStatusCode() {
		return submissionStatusCode;
	}

	public void setSubmissionStatusCode(String submissionStatusCode) {
		this.submissionStatusCode = submissionStatusCode;
	}

	public ProtocolSubmissionStatus getSubmissionStatus() {
		return submissionStatus;
	}

	public void setSubmissionStatus(ProtocolSubmissionStatus submissionStatus) {
		this.submissionStatus = submissionStatus;
	}

	public String getProtocolReviewTypeCode() {
		return protocolReviewTypeCode;
	}

	public void setProtocolReviewTypeCode(String protocolReviewTypeCode) {
		this.protocolReviewTypeCode = protocolReviewTypeCode;
	}

	public ProtocolReviewType getProtocolReviewType() {
		return protocolReviewType;
	}

	public void setProtocolReviewType(ProtocolReviewType protocolReviewType) {
		this.protocolReviewType = protocolReviewType;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	public Boolean getProtocolActive() {
		return protocolActive;
	}

	public void setProtocolActive(Boolean protocolActive) {
		this.protocolActive = protocolActive;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public String getPiPersonId() {
		return piPersonId;
	}

	public void setPiPersonId(String piPersonId) {
		this.piPersonId = piPersonId;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Integer getSubmissionNumber() {
		return submissionNumber;
	}

	public void setSubmissionNumber(Integer submissionNumber) {
		this.submissionNumber = submissionNumber;
	}

}
