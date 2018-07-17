package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

@Entity
@Table(name = "FIBI_COMM_MEMBER_ROLES")
public class CommitteeMemberRoles implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
	@GenericGenerator(name = "commMemberRolesIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "commMemberRolesIdGererator")
	@Column(name = "COMM_MEMBER_ROLES_ID", updatable = false, nullable = false)
	private Integer commMemberRolesId;

    @JsonBackReference
    @ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MEMBER_ROLES"), name = "COMM_MEMBERSHIP_ID", referencedColumnName = "COMM_MEMBERSHIP_ID")
	private CommitteeMemberships committeeMemberships;

	@Column(name = "MEMBERSHIP_ROLE_CODE")
	private String membershipRoleCode;

	@Column(name = "MEMBERSHIP_ROLE_DESCRIPTION")
	private String membershipRoleDescription;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCommMemberRolesId() {
		return commMemberRolesId;
	}

	public void setCommMemberRolesId(Integer commMemberRolesId) {
		this.commMemberRolesId = commMemberRolesId;
	}

	public CommitteeMemberships getCommitteeMemberships() {
		return committeeMemberships;
	}

	public void setCommitteeMemberships(CommitteeMemberships committeeMemberships) {
		this.committeeMemberships = committeeMemberships;
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

	public String getMembershipRoleCode() {
		return membershipRoleCode;
	}

	public void setMembershipRoleCode(String membershipRoleCode) {
		this.membershipRoleCode = membershipRoleCode;
	}

	public String getMembershipRoleDescription() {
		return membershipRoleDescription;
	}

	public void setMembershipRoleDescription(String membershipRoleDescription) {
		this.membershipRoleDescription = membershipRoleDescription;
	}

}
