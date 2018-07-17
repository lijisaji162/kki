package com.polus.fibicomp.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "UNIT_ADMINISTRATOR")
@IdClass(UnitAdministrator.UnitAdministratorId.class)
public class UnitAdministrator implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UnitAdministrator.class);

	@Id
	@Column(name = "PERSON_ID")
	private String personId;

	@Id
	@Column(name = "UNIT_ADMINISTRATOR_TYPE_CODE")
	private String unitAdministratorTypeCode;

	@Id
	@Column(name = "UNIT_NUMBER")
	private String unitNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	protected String objectId;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Version
	@Column(name = "VER_NBR", length = 8)
	protected Long versionNumber;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_UNIT_ADMINISTRATOR_KRA"), name = "UNIT_NUMBER", referencedColumnName = "UNIT_NUMBER", insertable = false, updatable = false)
	private Unit unit;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_UNIT_ADMINISTRATOR_KRA"), name = "UNIT_ADMINISTRATOR_TYPE_CODE", referencedColumnName = "UNIT_ADMINISTRATOR_TYPE_CODE", insertable = false, updatable = false)
	private UnitAdministratorType unitAdministratorType;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getUnitAdministratorTypeCode() {
		return unitAdministratorTypeCode;
	}

	public void setUnitAdministratorTypeCode(String unitAdministratorTypeCode) {
		this.unitAdministratorTypeCode = unitAdministratorTypeCode;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public UnitAdministratorType getUnitAdministratorType() {
		return unitAdministratorType;
	}

	public void setUnitAdministratorType(UnitAdministratorType unitAdministratorType) {
		this.unitAdministratorType = unitAdministratorType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static org.apache.log4j.Logger getLog() {
		return LOG;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
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

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public static final class UnitAdministratorId implements Serializable {

		private static final long serialVersionUID = 1L;

		private String unitNumber;

		private String personId;

		private String unitAdministratorTypeCode;

		public String getUnitNumber() {
			return this.unitNumber;
		}

		public void setUnitNumber(String unitNumber) {
			this.unitNumber = unitNumber;
		}

		public String getPersonId() {
			return this.personId;
		}

		public void setPersonId(String personId) {
			this.personId = personId;
		}

		public String getUnitAdministratorTypeCode() {
			return this.unitAdministratorTypeCode;
		}

		public void setUnitAdministratorTypeCode(String unitAdministratorTypeCode) {
			this.unitAdministratorTypeCode = unitAdministratorTypeCode;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("unitNumber", this.unitNumber).append("personId", this.personId)
					.append("unitAdministratorTypeCode", this.unitAdministratorTypeCode).toString();
		}

		@Override
		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (other == this)
				return true;
			if (other.getClass() != this.getClass())
				return false;
			final UnitAdministratorId rhs = (UnitAdministratorId) other;
			return new EqualsBuilder().append(this.unitNumber, rhs.unitNumber).append(this.personId, rhs.personId)
					.append(this.unitAdministratorTypeCode, rhs.unitAdministratorTypeCode).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.unitNumber).append(this.personId)
					.append(this.unitAdministratorTypeCode).toHashCode();
		}

		public int compareTo(UnitAdministratorId other) {
			return new CompareToBuilder().append(this.unitNumber, other.unitNumber)
					.append(this.personId, other.personId)
					.append(this.unitAdministratorTypeCode, other.unitAdministratorTypeCode).toComparison();
		}
	}

}
