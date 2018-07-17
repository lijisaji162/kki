package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_COMM_SCHEDULE_MINUTES")
public class CommitteeScheduleMinutes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "muinutesIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "muinutesIdGererator")
	@Column(name = "COMM_SCHEDULE_MINUTES_ID", updatable = false, nullable = false)
	private Integer commScheduleMinutesId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_COMM_SCHEDULE_MINUTES"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
	private CommitteeSchedule committeeSchedule;

	@Column(name = "ENTRY_NUMBER")
	private Integer entryNumber;

	@Column(name = "MINUTE_ENTRY_TYPE_CODE")
	private Integer minuteEntryTypeCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCHEDULE_MINUTES"), name = "MINUTE_ENTRY_TYPE_CODE", referencedColumnName = "MINUTE_ENTRY_TYPE_CODE", insertable = false, updatable = false)
	private MinuteEntryType minuteEntrytype;

	@Column(name = "PROTOCOL_CONTINGENCY_CODE")
	private String protocolContingencyCode;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCH_MINUTES2"), name = "PROTOCOL_CONTINGENCY_CODE", referencedColumnName = "PROTOCOL_CONTINGENCY_CODE", insertable = false, updatable = false)
	private ProtocolContingency protocolContingency;

	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "PROTOCOL_ID")
	private Integer protocolId;

	@Column(name = "SUBMISSION_ID")
	private Long submissionId;

	@Column(name = "REVIEWER_ID")
	private Integer reviewerId;

	@Column(name = "SUBMISSION_NUMBER")
	private Integer submissionNumber;

	@Column(name = "PRIVATE_COMMENT_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean privateCommentFlag;

	@Column(name = "MINUTE_ENTRY")
	private String minuteEntry;

	@Column(name = "FINAL_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean finalFlag;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "CREATE_TIMESTAMP")
	private Timestamp createTimestamp;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "COMM_SCHEDULE_ACT_ITEMS_ID")
	private Integer commScheduleActItemsId;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCH_MINUTES3"), name = "COMM_SCHEDULE_ACT_ITEMS_ID", referencedColumnName = "COMM_SCHEDULE_ACT_ITEMS_ID", insertable = false, updatable = false)
	private CommitteeScheduleActItems scheduleActItems;

	@Transient
	private boolean generateAttendance = false;

	public Integer getCommScheduleMinutesId() {
		return commScheduleMinutesId;
	}

	public void setCommScheduleMinutesId(Integer commScheduleMinutesId) {
		this.commScheduleMinutesId = commScheduleMinutesId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getEntryNumber() {
		return entryNumber;
	}

	public void setEntryNumber(Integer entryNumber) {
		this.entryNumber = entryNumber;
	}

	public Integer getMinuteEntryTypeCode() {
		return minuteEntryTypeCode;
	}

	public void setMinuteEntryTypeCode(Integer minuteEntryTypeCode) {
		this.minuteEntryTypeCode = minuteEntryTypeCode;
	}

	public MinuteEntryType getMinuteEntrytype() {
		return minuteEntrytype;
	}

	public void setMinuteEntrytype(MinuteEntryType minuteEntrytype) {
		this.minuteEntrytype = minuteEntrytype;
	}

	public String getProtocolContingencyCode() {
		return protocolContingencyCode;
	}

	public void setProtocolContingencyCode(String protocolContingencyCode) {
		this.protocolContingencyCode = protocolContingencyCode;
	}

	public ProtocolContingency getProtocolContingency() {
		return protocolContingency;
	}

	public void setProtocolContingency(ProtocolContingency protocolContingency) {
		this.protocolContingency = protocolContingency;
	}

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

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public Long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}

	public Integer getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Integer reviewerId) {
		this.reviewerId = reviewerId;
	}

	public Integer getSubmissionNumber() {
		return submissionNumber;
	}

	public void setSubmissionNumber(Integer submissionNumber) {
		this.submissionNumber = submissionNumber;
	}

	public Boolean getPrivateCommentFlag() {
		return privateCommentFlag;
	}

	public void setPrivateCommentFlag(Boolean privateCommentFlag) {
		this.privateCommentFlag = privateCommentFlag;
	}

	public String getMinuteEntry() {
		return minuteEntry;
	}

	public void setMinuteEntry(String minuteEntry) {
		this.minuteEntry = minuteEntry;
	}

	public Boolean getFinalFlag() {
		return finalFlag;
	}

	public void setFinalFlag(Boolean finalFlag) {
		this.finalFlag = finalFlag;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
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

	public Integer getCommScheduleActItemsId() {
		return commScheduleActItemsId;
	}

	public void setCommScheduleActItemsId(Integer commScheduleActItemsId) {
		this.commScheduleActItemsId = commScheduleActItemsId;
	}

	public CommitteeScheduleActItems getScheduleActItems() {
		return scheduleActItems;
	}

	public void setScheduleActItems(CommitteeScheduleActItems scheduleActItems) {
		this.scheduleActItems = scheduleActItems;
	}

	public boolean isGenerateAttendance() {
		return generateAttendance;
	}

	public void setGenerateAttendance(boolean generateAttendance) {
		this.generateAttendance = generateAttendance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
