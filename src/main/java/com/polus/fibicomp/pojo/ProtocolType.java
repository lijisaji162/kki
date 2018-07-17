package com.polus.fibicomp.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "PROTOCOL_TYPE")
public class ProtocolType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROTOCOL_TYPE_CODE")
	private String protocolTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Convert(converter = JpaCharBooleanConversion.class)
	@Column(name = "GLOBAL_FLAG")
	private boolean globalFlag;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Version
	@Column(name = "VER_NBR", length = 8)
	protected Long versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	protected String objectId;

	public String getProtocolTypeCode() {
		return protocolTypeCode;
	}

	public void setProtocolTypeCode(String protocolTypeCode) {
		this.protocolTypeCode = protocolTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isGlobalFlag() {
		return globalFlag;
	}

	public void setGlobalFlag(boolean globalFlag) {
		this.globalFlag = globalFlag;
	}

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
}
