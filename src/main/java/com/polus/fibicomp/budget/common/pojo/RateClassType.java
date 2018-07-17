package com.polus.fibicomp.budget.common.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "RATE_CLASS_TYPE")
public class RateClassType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "RATE_CLASS_TYPE")
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SORT_ID")
	private Integer sortId;

	@Column(name = "PREFIX_ACTIVITY_TYPE")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean prefixActivityType;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public Boolean getPrefixActivityType() {
		return prefixActivityType;
	}

	public void setPrefixActivityType(Boolean prefixActivityType) {
		this.prefixActivityType = prefixActivityType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
