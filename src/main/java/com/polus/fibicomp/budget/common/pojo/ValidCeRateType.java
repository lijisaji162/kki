package com.polus.fibicomp.budget.common.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "VALID_CE_RATE_TYPES")
@IdClass(ValidCeRateType.ValidCeRateTypeId.class)
public class ValidCeRateType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COST_ELEMENT")
	private String costElement;

	@Id
	@Column(name = "RATE_CLASS_CODE")
	private String rateClassCode;

	@Id
	@Column(name = "RATE_TYPE_CODE")
	private String rateTypeCode;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "RATE_CLASS_CODE", referencedColumnName = "RATE_CLASS_CODE", insertable = false, updatable = false)
	private RateClass rateClass;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumns({
			@JoinColumn(name = "RATE_CLASS_CODE", referencedColumnName = "RATE_CLASS_CODE", insertable = false, updatable = false),
			@JoinColumn(name = "RATE_TYPE_CODE", referencedColumnName = "RATE_TYPE_CODE", insertable = false, updatable = false) })
	private RateType rateType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "COST_ELEMENT", referencedColumnName = "COST_ELEMENT", insertable = false, updatable = false)
	private CostElement costElementBo;

	@Column(name = "ACTIVE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean active;

	public String getCostElement() {
		return costElement;
	}

	public void setCostElement(String costElement) {
		this.costElement = costElement;
	}

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

	public RateClass getRateClass() {
		return rateClass;
	}

	public void setRateClass(RateClass rateClass) {
		this.rateClass = rateClass;
	}

	public RateType getRateType() {
		return rateType;
	}

	public void setRateType(RateType rateType) {
		this.rateType = rateType;
	}

	public CostElement getCostElementBo() {
		return costElementBo;
	}

	public void setCostElementBo(CostElement costElementBo) {
		this.costElementBo = costElementBo;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static final class ValidCeRateTypeId implements Serializable, Comparable<ValidCeRateTypeId> {

		private static final long serialVersionUID = 1L;

		private String costElement;

		private String rateClassCode;

		private String rateTypeCode;

		public String getCostElement() {
			return costElement;
		}

		public void setCostElement(String costElement) {
			this.costElement = costElement;
		}

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
			return new ToStringBuilder(this).append("costElement", this.costElement).append("rateClassCode", this.rateClassCode).append("rateTypeCode", this.rateTypeCode).toString();
		}

		@Override
		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (other == this)
				return true;
			if (other.getClass() != this.getClass())
				return false;
			final ValidCeRateTypeId rhs = (ValidCeRateTypeId) other;
			return new EqualsBuilder().append(this.costElement, rhs.costElement).append(this.rateClassCode, rhs.rateClassCode).append(this.rateTypeCode, rhs.rateTypeCode).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.costElement).append(this.rateClassCode).append(this.rateTypeCode).toHashCode();
		}

		@Override
		public int compareTo(ValidCeRateTypeId other) {
			return new CompareToBuilder().append(this.costElement, other.costElement).append(this.rateClassCode, other.rateClassCode).append(this.rateTypeCode, other.rateTypeCode).toComparison();
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	}
}
