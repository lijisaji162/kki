package com.polus.fibicomp.pojo;

import java.util.ArrayList;

/**
 * AwardHierarchy DTO with award hierarchy details
 *
 */
public class AwardHierarchy {

	public String awardNumber;
	public String parentAwardNumber;
	public String awardId;
	public String principalInvestigator;
	public String accountNumber;
	public boolean isSelected;
	public Integer StatusCode;
	public String colorCode;
	public int level;
	public ArrayList<AwardHierarchy> children;
	public String rootAwardNumber;
	public String name;
	public boolean isOpen;

	public String getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(String principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public String getAwardNumber() {
		return awardNumber;
	}

	public void setAwardNumber(String awardNumber) {
		this.awardNumber = awardNumber;
	}

	public String getParentAwardNumber() {
		return parentAwardNumber;
	}

	public void setParentAwardNumber(String parentAwardNumber) {
		this.parentAwardNumber = parentAwardNumber;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<AwardHierarchy> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<AwardHierarchy> children) {
		this.children = children;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Integer getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(Integer statusCode) {
		StatusCode = statusCode;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getAwardId() {
		return awardId;
	}

	public void setAwardId(String awardId) {
		this.awardId = awardId;
	}

	public String getRootAwardNumber() {
		return rootAwardNumber;
	}

	public void setRootAwardNumber(String rootAwardNumber) {
		this.rootAwardNumber = rootAwardNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

}
