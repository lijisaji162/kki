package com.polus.fibicomp.pojo;

import java.io.Serializable;

import javax.persistence.*;

@IdClass(ParameterId.class)
@Entity
@Table(name = "KRCR_PARM_T")
public class ParameterBo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NMSPC_CD")
	private String namespaceCode;

	@Id
	@Column(name = "CMPNT_CD")
	private String componentCode;

	@Id
	@Column(name = "PARM_NM")
	private String name;

	@Id
	@Column(name = "APPL_ID")
	private String applicationId;

	@Column(name = "VAL")
	private String value;

	@Column(name = "PARM_DESC_TXT", length = 2048)
	private String description;

	@Column(name = "PARM_TYP_CD")
	private String parameterTypeCode;

	public String getNamespaceCode() {
		return namespaceCode;
	}

	public void setNamespaceCode(String namespaceCode) {
		this.namespaceCode = namespaceCode;
	}

	public String getComponentCode() {
		return componentCode;
	}

	public void setComponentCode(String componentCode) {
		this.componentCode = componentCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParameterTypeCode() {
		return parameterTypeCode;
	}

	public void setParameterTypeCode(String parameterTypeCode) {
		this.parameterTypeCode = parameterTypeCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
