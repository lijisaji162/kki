package com.polus.fibicomp.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwardTermsAndReportsVO {
	
	private List<HashMap<String, Object>> awardApprovdEquipment;
	private List<HashMap<String, Object>> approvedTravel;
	private Map<String, List<HashMap<String, Object>>> awardTerms;
	private List<HashMap<String, Object>> awardPayment;
	private Map<String, List<HashMap<String, Object>>> awardReport;
	private List<HashMap<String, Object>> awardPaymntSchedule;
	private List<HashMap<String, Object>> awardPaymntInvoice;
	
	public List<HashMap<String, Object>> getAwardApprovdEquipment() {
		return awardApprovdEquipment;
	}

	public void setAwardApprovdEquipment(List<HashMap<String, Object>> awardApprovdEquipment) {
		this.awardApprovdEquipment = awardApprovdEquipment;
	}

	public List<HashMap<String, Object>> getApprovedTravel() {
		return approvedTravel;
	}

	public void setApprovedTravel(List<HashMap<String, Object>> approvedTravel) {
		this.approvedTravel = approvedTravel;
	}
	
	public List<HashMap<String, Object>> getAwardPayment() {
		return awardPayment;
	}

	public void setAwardPayment(List<HashMap<String, Object>> awardPayment) {
		this.awardPayment = awardPayment;
	}

	public Map<String, List<HashMap<String, Object>>> getAwardReport() {
		return awardReport;
	}

	public void setAwardReport(Map<String, List<HashMap<String, Object>>> awardReport) {
		this.awardReport = awardReport;
	}

	public List<HashMap<String, Object>> getAwardPaymntSchedule() {
		return awardPaymntSchedule;
	}

	public void setAwardPaymntSchedule(List<HashMap<String, Object>> awardPaymntSchedule) {
		this.awardPaymntSchedule = awardPaymntSchedule;
	}

	public List<HashMap<String, Object>> getAwardPaymntInvoice() {
		return awardPaymntInvoice;
	}

	public void setAwardPaymntInvoice(List<HashMap<String, Object>> awardPaymntInvoice) {
		this.awardPaymntInvoice = awardPaymntInvoice;
	}

	public Map<String, List<HashMap<String, Object>>> getAwardTerms() {
		return awardTerms;
	}

	public void setAwardTerms(Map<String, List<HashMap<String, Object>>> awardTerms) {
		this.awardTerms = awardTerms;
	}
}
