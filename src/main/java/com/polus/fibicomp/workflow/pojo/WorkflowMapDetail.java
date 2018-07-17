package com.polus.fibicomp.workflow.pojo;

import java.io.Serializable;
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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_WORKFLOW_MAP_DETAIL")
public class WorkflowMapDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "mapDetailIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "mapDetailIdGenerator")
	@Column(name = "MAP_DETAIL_ID")
	private Integer mapDetailId;

	@Column(name = "MAP_ID")
	private Integer mapId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_WORKFLOW_MAP_DETAIL"), name = "MAP_ID", referencedColumnName = "MAP_ID", insertable = false, updatable = false)
	private WorkflowMap workflowMap;

	@Column(name = "APPROVAL_STOP_NUMBER")
	private Integer approvalStopNumber;

	@Column(name = "APPROVER_NUMBER")
	private Integer approverNumber;

	@Column(name = "APPROVER_PERSON_ID")
	private String approverPersonId;

	@Column(name = "APPROVER_PERSON_NAME")
	private String approverPersonName;

	@Column(name = "PRIMARY_APPROVER_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean primaryApproverFlag;

	@Column(name = "IS_ROLE")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isRole;

	@Column(name = "ROLE_TYPE_CODE")
	private Integer roleTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_WORKFLOW_MAP_DETAIL"), name = "ROLE_TYPE_CODE", referencedColumnName = "ROLE_TYPE_CODE", insertable = false, updatable = false)
	private WorkflowRoleType workflowRoleType;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	public Integer getMapDetailId() {
		return mapDetailId;
	}

	public void setMapDetailId(Integer mapDetailId) {
		this.mapDetailId = mapDetailId;
	}

	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}

	public Integer getApprovalStopNumber() {
		return approvalStopNumber;
	}

	public void setApprovalStopNumber(Integer approvalStopNumber) {
		this.approvalStopNumber = approvalStopNumber;
	}

	public Integer getApproverNumber() {
		return approverNumber;
	}

	public void setApproverNumber(Integer approverNumber) {
		this.approverNumber = approverNumber;
	}

	public String getApproverPersonId() {
		return approverPersonId;
	}

	public void setApproverPersonId(String approverPersonId) {
		this.approverPersonId = approverPersonId;
	}

	public Boolean getPrimaryApproverFlag() {
		return primaryApproverFlag;
	}

	public void setPrimaryApproverFlag(Boolean primaryApproverFlag) {
		this.primaryApproverFlag = primaryApproverFlag;
	}

	public Boolean getIsRole() {
		return isRole;
	}

	public void setIsRole(Boolean isRole) {
		this.isRole = isRole;
	}

	public Integer getRoleTypeCode() {
		return roleTypeCode;
	}

	public void setRoleTypeCode(Integer roleTypeCode) {
		this.roleTypeCode = roleTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public WorkflowMap getWorkflowMap() {
		return workflowMap;
	}

	public void setWorkflowMap(WorkflowMap workflowMap) {
		this.workflowMap = workflowMap;
	}

	public String getApproverPersonName() {
		return approverPersonName;
	}

	public void setApproverPersonName(String approverPersonName) {
		this.approverPersonName = approverPersonName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public WorkflowRoleType getWorkflowRoleType() {
		return workflowRoleType;
	}

	public void setWorkflowRoleType(WorkflowRoleType workflowRoleType) {
		this.workflowRoleType = workflowRoleType;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
