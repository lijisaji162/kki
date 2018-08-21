package com.polus.fibicomp.role.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "KRIM_ROLE_MBR_ATTR_DATA_T")
public class RoleMemberAttributeDataBo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ATTR_DATA_ID")
	private String id;

	@Column(name = "ROLE_MBR_ID")
	private String assignedToId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_KRIM_ROLE_MBR_ATTR_DATA_T"), name = "ROLE_MBR_ID", referencedColumnName = "ROLE_MBR_ID", insertable=false, updatable=false)
	private RoleMemberBo roleMemberBo;

	@Column(name = "ATTR_VAL")
	private String attributeValue;

	@Column(name = "KIM_ATTR_DEFN_ID")
	private String kimAttributeId;

	@Column(name = "KIM_TYP_ID")
	private String kimTypeId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssignedToId() {
		return assignedToId;
	}

	public void setAssignedToId(String assignedToId) {
		this.assignedToId = assignedToId;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getKimAttributeId() {
		return kimAttributeId;
	}

	public void setKimAttributeId(String kimAttributeId) {
		this.kimAttributeId = kimAttributeId;
	}

	public String getKimTypeId() {
		return kimTypeId;
	}

	public void setKimTypeId(String kimTypeId) {
		this.kimTypeId = kimTypeId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public RoleMemberBo getRoleMemberBo() {
		return roleMemberBo;
	}

	public void setRoleMemberBo(RoleMemberBo roleMemberBo) {
		this.roleMemberBo = roleMemberBo;
	}
}
