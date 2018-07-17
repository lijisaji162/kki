package com.polus.fibicomp.budget.common.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "RATE_TYPE")
@IdClass(RateType.RateTypeId.class)
public class RateType implements Serializable, Comparable<RateType> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "RATE_CLASS_CODE")
	private String rateClassCode;

	@Id
	@Column(name = "RATE_TYPE_CODE")
	private String rateTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "RATE_CLASS_CODE", referencedColumnName = "RATE_CLASS_CODE", insertable = false, updatable = false)
	private RateClass rateClass;

	public String getRateClassCode() {
		return rateClassCode;
	}

	public void setRateClassCode(String rateClassCode) {
		this.rateClassCode = rateClassCode;
	}

	public String getRateTypeCode() {
		return rateTypeCode;
	}

	public void setRateTypeCode(String rateTypeCode) {
		this.rateTypeCode = rateTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RateClass getRateClass() {
		return rateClass;
	}

	public void setRateClass(RateClass rateClass) {
		this.rateClass = rateClass;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int compareTo(RateType rateType) {
		return new CompareToBuilder().append(this.rateClassCode, rateType.rateClassCode).append(this.rateTypeCode, rateType.rateTypeCode).toComparison();
	}

	public static final class RateTypeId implements Serializable, Comparable<RateTypeId> {

		private static final long serialVersionUID = 1L;

		private String rateClassCode;

		private String rateTypeCode;

		public String getRateClassCode() {
			return rateClassCode;
		}

		public void setRateClassCode(String rateClassCode) {
			this.rateClassCode = rateClassCode;
		}

		public String getRateTypeCode() {
			return rateTypeCode;
		}

		public void setRateTypeCode(String rateTypeCode) {
			this.rateTypeCode = rateTypeCode;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("rateClassCode", this.rateClassCode).append("rateTypeCode", this.rateTypeCode).toString();
		}

		@Override
		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (other == this)
				return true;
			if (other.getClass() != this.getClass())
				return false;
			final RateTypeId rhs = (RateTypeId) other;
			return new EqualsBuilder().append(this.rateClassCode, rhs.rateClassCode).append(this.rateTypeCode, rhs.rateTypeCode).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.rateClassCode).append(this.rateTypeCode).toHashCode();
		}

		@Override
		public int compareTo(RateTypeId other) {
			return new CompareToBuilder().append(this.rateClassCode, other.rateClassCode).append(this.rateTypeCode, other.rateTypeCode).toComparison();
		}
	}
}
