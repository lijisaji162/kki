package com.polus.fibicomp.pojo;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ParameterId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "NMSPC_CD")
	private String namespaceCode;
	@Column(name = "CMPNT_CD")
	private String componentCode;
	@Column(name = "PARM_NM")
	private String name;
	@Column(name = "APPL_ID")
	private String applicationId;

	public ParameterId() {
		namespaceCode = null;
		componentCode = null;
		name = null;
		applicationId = null;
	}

	public ParameterId(String namespaceCode, String componentCode, String name, String applicationId) {
		this.namespaceCode = namespaceCode;
		this.componentCode = componentCode;
		this.name = name;
		this.applicationId = applicationId;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(obj, this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getCacheKey() {
		return this.applicationId + this.componentCode + this.namespaceCode + this.name;
	}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
