package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FIBI_COMM_MEMBER_STATUS_CHANGE")
public class CommitteeMemberStatusChange implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_MEMBER_STATUS_CHANGE_ID")
	private Integer commMemberStatusChangeId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MBR_STATUS_CHNG_3"), name = "COMM_MEMBERSHIP_ID", referencedColumnName = "COMM_MEMBERSHIP_ID", insertable = false, updatable = false)
	private CommitteeMemberships committeeMemberships;

	@Column(name = "COMM_MEMBERSHIP_ID")
	private Integer commMembershipId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MBR_STATUS_CHNG_2"), name = "MEMBERSHIP_STATUS_CODE", referencedColumnName = "MEMBERSHIP_STATUS_CODE", insertable = false, updatable = false)
	private CommitteeMembershipStatus committeeMembershipStatus;

	@Column(name = "MEMBERSHIP_STATUS_CODE")
	private Integer membershipStatusCode;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCommMemberStatusChangeId() {
		return commMemberStatusChangeId;
	}

	public void setCommMemberStatusChangeId(Integer commMemberStatusChangeId) {
		this.commMemberStatusChangeId = commMemberStatusChangeId;
	}

	public CommitteeMemberships getCommitteeMemberships() {
		return committeeMemberships;
	}

	public void setCommitteeMemberships(CommitteeMemberships committeeMemberships) {
		this.committeeMemberships = committeeMemberships;
	}

	public CommitteeMembershipStatus getCommitteeMembershipStatus() {
		return committeeMembershipStatus;
	}

	public void setCommitteeMembershipStatus(CommitteeMembershipStatus committeeMembershipStatus) {
		this.committeeMembershipStatus = committeeMembershipStatus;
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

	public Integer getCommMembershipId() {
		return commMembershipId;
	}

	public void setCommMembershipId(Integer commMembershipId) {
		this.commMembershipId = commMembershipId;
	}

	public Integer getMembershipStatusCode() {
		return membershipStatusCode;
	}

	public void setMembershipStatusCode(Integer membershipStatusCode) {
		this.membershipStatusCode = membershipStatusCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
