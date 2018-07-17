package com.polus.fibicomp.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "UNIT")
public class Unit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UNIT_NUMBER")
	private String unitNumber;

	@Column(name = "PARENT_UNIT_NUMBER")
	private String parentUnitNumber;

	@Column(name = "ORGANIZATION_ID")
	private String organizationId;

	@Column(name = "UNIT_NAME")
	private String unitName;

	@Column(name = "ACTIVE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean active;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	protected String objectId;

	@Version
	@Column(name = "VER_NBR", length = 8)
	protected Long versionNumber;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_UNIT_PARENT_UNIT_NUMBER"), name = "PARENT_UNIT_NUMBER", referencedColumnName = "UNIT_NUMBER", insertable = false, updatable = false)
	private Unit parentUnit;

	@OneToMany(mappedBy = "unit")
	@OrderBy("unitNumber")
	private List<UnitAdministrator> unitAdministrators;

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getParentUnitNumber() {
		return parentUnitNumber;
	}

	public void setParentUnitNumber(String parentUnitNumber) {
		this.parentUnitNumber = parentUnitNumber;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Unit getParentUnit() {
		return parentUnit;
	}

	public void setParentUnit(Unit parentUnit) {
		this.parentUnit = parentUnit;
	}

	public List<UnitAdministrator> getUnitAdministrators() {
		return unitAdministrators;
	}

	public void setUnitAdministrators(List<UnitAdministrator> unitAdministrators) {
		this.unitAdministrators = unitAdministrators;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
