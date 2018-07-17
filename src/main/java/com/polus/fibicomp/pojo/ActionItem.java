package com.polus.fibicomp.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACTION_ITM_V")
public class ActionItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ACTN_ITM_ID")
	private String id;

	@Column(name = "PRNCPL_ID")
	private String principalId;

	@Column(name = "ASND_DT")
	private Timestamp dateAssigned;

	@Column(name = "RQST_CD")
	private String actionRequestCd;

	@Column(name = "ACTN_RQST_ID")
	private String actionRequestId;

	@Column(name = "DOC_HDR_ID")
	private String documentId;

	@Column(name = "GRP_ID")
	private String groupId;

	@Column(name = "DOC_HDR_TTL")
	private String docTitle;

	@Column(name = "DOC_TYP_LBL")
	private String docLabel;

	@Column(name = "DOC_HDLR_URL")
	private String docHandlerURL;

	@Column(name = "DOC_TYP_NM")
	private String docName;

	@Column(name = "RSP_ID")
	private String responsibilityId = "1";

	@Column(name = "ROLE_NM")
	private String roleName;

	@Column(name = "DLGN_PRNCPL_ID")
	private String delegatorPrincipalId;

	@Column(name = "DOC_HDR_STAT_CD")
	private String docRouteStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public Timestamp getDateAssigned() {
		return dateAssigned;
	}

	public void setDateAssigned(Timestamp dateAssigned) {
		this.dateAssigned = dateAssigned;
	}

	public String getActionRequestCd() {
		return actionRequestCd;
	}

	public void setActionRequestCd(String actionRequestCd) {
		this.actionRequestCd = actionRequestCd;
	}

	public String getActionRequestId() {
		return actionRequestId;
	}

	public void setActionRequestId(String actionRequestId) {
		this.actionRequestId = actionRequestId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getDocLabel() {
		return docLabel;
	}

	public void setDocLabel(String docLabel) {
		this.docLabel = docLabel;
	}

	public String getDocHandlerURL() {
		return docHandlerURL;
	}

	public void setDocHandlerURL(String docHandlerURL) {
		this.docHandlerURL = docHandlerURL;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getResponsibilityId() {
		return responsibilityId;
	}

	public void setResponsibilityId(String responsibilityId) {
		this.responsibilityId = responsibilityId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDelegatorPrincipalId() {
		return delegatorPrincipalId;
	}

	public void setDelegatorPrincipalId(String delegatorPrincipalId) {
		this.delegatorPrincipalId = delegatorPrincipalId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDocRouteStatus() {
		return docRouteStatus;
	}

	public void setDocRouteStatus(String docRouteStatus) {
		this.docRouteStatus = docRouteStatus;
	}
}
