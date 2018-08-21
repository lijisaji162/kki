package com.polus.fibicomp.role.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.util.InactivatableFromToUtils;

@Entity
@Table(name = "KRIM_ROLE_MBR_T")
public class RoleMemberBo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ROLE_MBR_ID")
    private String id;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name="MBR_ID")
	private String memberId;

	@Column(name="MBR_TYP_CD")
	private String typeCode;

	@Column(name = "ACTV_FRM_DT")
	private Timestamp activeFromDateValue;

	@Column(name = "ACTV_TO_DT")
	private Timestamp activeToDateValue;

	@JsonManagedReference
	@OneToMany(mappedBy = "roleMemberBo", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<RoleMemberAttributeDataBo> attributeDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Timestamp getActiveFromDateValue() {
		return activeFromDateValue;
	}

	public void setActiveFromDateValue(Timestamp activeFromDateValue) {
		this.activeFromDateValue = activeFromDateValue;
	}

	public Timestamp getActiveToDateValue() {
		return activeToDateValue;
	}

	public void setActiveToDateValue(Timestamp activeToDateValue) {
		this.activeToDateValue = activeToDateValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Transient
	public boolean isActive(Timestamp activeAsOfDate) {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), new DateTime(activeAsOfDate.getTime()));
    }

	@Transient
	public boolean isActive(DateTime activeAsOfDate) {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), activeAsOfDate);
    }

	@Transient
	public boolean isActive() {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), null);
    }

	@Transient
	public DateTime getActiveFromDate() {
        return this.activeFromDateValue == null ? null : new DateTime(this.activeFromDateValue.getTime());
    }

	@Transient
	public DateTime getActiveToDate() {
        return this.activeToDateValue == null ? null : new DateTime(this.activeToDateValue.getTime());
    }

	public List<RoleMemberAttributeDataBo> getAttributeDetails() {
		return attributeDetails;
	}

	public void setAttributeDetails(List<RoleMemberAttributeDataBo> attributeDetails) {
		this.attributeDetails = attributeDetails;
	}

}
