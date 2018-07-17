package com.polus.fibicomp.vo;

import java.util.HashMap;
import java.util.List;

import com.polus.fibicomp.pojo.AwardHierarchy;

public class AwardHierarchyVO {

	private AwardHierarchy awardHierarchy;
	private List<HashMap<String, Object>> hierarchyBudgetVersions;

	public List<HashMap<String, Object>> getHierarchyBudgetVersions() {
		return hierarchyBudgetVersions;
	}

	public void setHierarchyBudgetVersions(List<HashMap<String, Object>> hierarchyBudgetVersions) {
		this.hierarchyBudgetVersions = hierarchyBudgetVersions;
	}

	public AwardHierarchy getAwardHierarchy() {
		return awardHierarchy;
	}

	public void setAwardHierarchy(AwardHierarchy awardHierarchy) {
		this.awardHierarchy = awardHierarchy;
	}

}
