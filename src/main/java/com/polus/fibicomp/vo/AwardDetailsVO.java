package com.polus.fibicomp.vo;

import java.util.HashMap;
import java.util.List;

public class AwardDetailsVO {

	private List<HashMap<String, Object>> awardDetails;
	private List<HashMap<String, Object>> awardPersons;
	private List<HashMap<String, Object>> awardSponsorContact;
	private List<HashMap<String, Object>> awardUnitContact;
	private List<HashMap<String, Object>> awardFundedProposals;
	private List<HashMap<String, Object>> awardSpecialReviews;

	public List<HashMap<String, Object>> getAwardDetails() {
		return awardDetails;
	}

	public void setAwardDetails(List<HashMap<String, Object>> awardDetails) {
		this.awardDetails = awardDetails;
	}

	public List<HashMap<String, Object>> getAwardPersons() {
		return awardPersons;
	}

	public void setAwardPersons(List<HashMap<String, Object>> awardPersons) {
		this.awardPersons = awardPersons;
	}

	public List<HashMap<String, Object>> getAwardSponsorContact() {
		return awardSponsorContact;
	}

	public void setAwardSponsorContact(List<HashMap<String, Object>> awardSponsorContact) {
		this.awardSponsorContact = awardSponsorContact;
	}

	public List<HashMap<String, Object>> getAwardUnitContact() {
		return awardUnitContact;
	}

	public void setAwardUnitContact(List<HashMap<String, Object>> awardUnitContact) {
		this.awardUnitContact = awardUnitContact;
	}

	public List<HashMap<String, Object>> getAwardFundedProposals() {
		return awardFundedProposals;
	}

	public void setAwardFundedProposals(List<HashMap<String, Object>> awardFundedProposals) {
		this.awardFundedProposals = awardFundedProposals;
	}

	public List<HashMap<String, Object>> getAwardSpecialReviews() {
		return awardSpecialReviews;
	}

	public void setAwardSpecialReviews(List<HashMap<String, Object>> awardSpecialReviews) {
		this.awardSpecialReviews = awardSpecialReviews;
	}

}
