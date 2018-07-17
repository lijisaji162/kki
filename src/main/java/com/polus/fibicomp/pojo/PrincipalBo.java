package com.polus.fibicomp.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "KRIM_PRNCPL_T")
public class PrincipalBo implements Serializable {

	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "SEQ_PRINCIPAL", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "KRIM_PRNCPL_ID_S"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@Id
	@GeneratedValue(generator = "SEQ_PRINCIPAL")
	@Column(name = "PRNCPL_ID")
	private String principalId;

	@Column(name = "PRNCPL_NM")
	private String principalName;

	@Column(name = "ENTITY_ID")
	private String entityId;

	@Column(name = "PRNCPL_PSWD")
	private String password;

	@Column(name = "ACTV_IND")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean active;

	@Version
	@Column(name = "VER_NBR", length = 8)
	protected Long versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true, nullable = false)
	protected String objectId;

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
