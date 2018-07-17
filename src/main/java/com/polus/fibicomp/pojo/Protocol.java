package com.polus.fibicomp.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.Parameter;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "PROTOCOL")
@NamedNativeQueries({
		@NamedNativeQuery(name = "getAllProtocols", query = "{ call GET_ALL_PROTOCOLS(?) }", resultClass = Protocol.class, callable = true),
		@NamedNativeQuery(name = "getIRBDashboard", query = "{ call GET_IRB_FOR_DASHBOARD(?,:av_person_id,:av_from_row,:av_to_row,:av_sort_column,:av_sort_order,:av_search_field) }", resultClass = Protocol.class, callable = true) })
public class Protocol implements Serializable {

	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "SEQ_PROTOCOL", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "SEQ_PROTOCOL_ID"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@Id
	@GeneratedValue(generator = "SEQ_PROTOCOL")
	@Column(name = "PROTOCOL_ID")
	private BigDecimal protocolId;

	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Version
	@Column(name = "VER_NBR", length = 8)
	protected Long versionNumber;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "PROTOCOL_TYPE_CODE")
	private String protocolTypeCode;

	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Convert(converter = JpaCharBooleanConversion.class)
	@Column(name = "ACTIVE")
	private boolean active = true;

	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;

	@Column(name = "PROTOCOL_STATUS_CODE")
	private String protocolStatusCode;

	@Column(name = "SPECIAL_REVIEW_INDICATOR")
	private String specialReviewIndicator;

	@Column(name = "VULNERABLE_SUBJECT_INDICATOR")
	private String vulnerableSubjectIndicator;

	@Column(name = "KEY_STUDY_PERSON_INDICATOR")
	private String keyStudyPersonIndicator;

	@Column(name = "FUNDING_SOURCE_INDICATOR")
	private String fundingSourceIndicator;

	@Column(name = "CORRESPONDENT_INDICATOR")
	private String correspondentIndicator;

	@Column(name = "REFERENCE_INDICATOR")
	private String referenceIndicator;

	@Column(name = "RELATED_PROJECTS_INDICATOR")
	private String relatedProjectsIndicator;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	protected String objectId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PROTOCOL"), name = "PROTOCOL_TYPE_CODE", referencedColumnName = "PROTOCOL_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolType protocolType;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PROTOCOL_2"), name = "PROTOCOL_STATUS_CODE", referencedColumnName = "PROTOCOL_STATUS_CODE", insertable = false, updatable = false)
	private ProtocolStatus protocolStatus;

	@Column(name = "APPROVAL_DATE")
	private Date approvalDate;

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProtocolTypeCode() {
		return protocolTypeCode;
	}

	public void setProtocolTypeCode(String protocolTypeCode) {
		this.protocolTypeCode = protocolTypeCode;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
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

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getProtocolStatusCode() {
		return protocolStatusCode;
	}

	public void setProtocolStatusCode(String protocolStatusCode) {
		this.protocolStatusCode = protocolStatusCode;
	}

	public String getSpecialReviewIndicator() {
		return specialReviewIndicator;
	}

	public void setSpecialReviewIndicator(String specialReviewIndicator) {
		this.specialReviewIndicator = specialReviewIndicator;
	}

	public String getVulnerableSubjectIndicator() {
		return vulnerableSubjectIndicator;
	}

	public void setVulnerableSubjectIndicator(String vulnerableSubjectIndicator) {
		this.vulnerableSubjectIndicator = vulnerableSubjectIndicator;
	}

	public String getKeyStudyPersonIndicator() {
		return keyStudyPersonIndicator;
	}

	public void setKeyStudyPersonIndicator(String keyStudyPersonIndicator) {
		this.keyStudyPersonIndicator = keyStudyPersonIndicator;
	}

	public String getFundingSourceIndicator() {
		return fundingSourceIndicator;
	}

	public void setFundingSourceIndicator(String fundingSourceIndicator) {
		this.fundingSourceIndicator = fundingSourceIndicator;
	}

	public String getCorrespondentIndicator() {
		return correspondentIndicator;
	}

	public void setCorrespondentIndicator(String correspondentIndicator) {
		this.correspondentIndicator = correspondentIndicator;
	}

	public String getReferenceIndicator() {
		return referenceIndicator;
	}

	public void setReferenceIndicator(String referenceIndicator) {
		this.referenceIndicator = referenceIndicator;
	}

	public String getRelatedProjectsIndicator() {
		return relatedProjectsIndicator;
	}

	public void setRelatedProjectsIndicator(String relatedProjectsIndicator) {
		this.relatedProjectsIndicator = relatedProjectsIndicator;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public ProtocolStatus getProtocolStatus() {
		return protocolStatus;
	}

	public void setProtocolStatus(ProtocolStatus protocolStatus) {
		this.protocolStatus = protocolStatus;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public BigDecimal getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(BigDecimal protocolId) {
		this.protocolId = protocolId;
	}

}
