package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBERSHIP_ROLE")
public class MembershipRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MEMBERSHIP_ROLE_CODE")
	private String membershipRoleCode;

	@Column(name = "DESCRIPTION")
    private String description;

	@Column(name = "COMMITTEE_TYPE_CODE")
    private String committeeTypeCode;

    @Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public String getMembershipRoleCode() {
		return membershipRoleCode;
	}

	public void setMembershipRoleCode(String membershipRoleCode) {
		this.membershipRoleCode = membershipRoleCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommitteeTypeCode() {
		return committeeTypeCode;
	}

	public void setCommitteeTypeCode(String committeeTypeCode) {
		this.committeeTypeCode = committeeTypeCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

}
