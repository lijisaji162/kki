package com.polus.fibicomp.vo;

import java.util.HashMap;
import java.util.List;

public class CommitmentsVO {

	private List<HashMap<String, Object>> costShareDetails;
	private List<HashMap<String, Object>> fAndADetails;
	private List<HashMap<String, Object>> benefitsRates;
	
	public List<HashMap<String, Object>> getCostShareDetails() {
		return costShareDetails;
	}

	public void setCostShareDetails(List<HashMap<String, Object>> costShareDetails) {
		this.costShareDetails = costShareDetails;
	}

	public List<HashMap<String, Object>> getfAndADetails() {
		return fAndADetails;
	}

	public void setfAndADetails(List<HashMap<String, Object>> fAndADetails) {
		this.fAndADetails = fAndADetails;
	}

	public List<HashMap<String, Object>> getBenefitsRates() {
		return benefitsRates;
	}

	public void setBenefitsRates(List<HashMap<String, Object>> benefitsRates) {
		this.benefitsRates = benefitsRates;
	}
}
