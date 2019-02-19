package com.polus.fibicomp.proposal.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.pojo.ProposalPersonRole;

@Entity
@Table(name = "FIBI_PROPOSAL_PERSONS")
public class ProposalPerson implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "proposalPersonIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "proposalPersonIdGenerator")
	@Column(name = "PROPOSAL_PERSON_ID")
	private Integer proposalPersonId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_PROPOSAL_PERSONS"), name = "PROPOSAL_ID", referencedColumnName = "PROPOSAL_ID")
	private Proposal proposal;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "ROLODEX_ID")
	private Integer rolodexId;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "PROP_PERSON_ROLE_ID")
	private Integer personRoleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_PROPOSAL_PERSONS"), name = "PROP_PERSON_ROLE_ID", referencedColumnName = "PROP_PERSON_ROLE_ID", insertable = false, updatable = false)
	private ProposalPersonRole proposalPersonRole;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "PERCENTAGE_OF_EFFORT")
	private BigDecimal percentageOfEffort;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposalPerson", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalPersonUnit> units;

	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	@Column(name = "ORGANISATION")
	private String organisation;

	public ProposalPerson() {
		units = new ArrayList<>();
	}

	public Integer getProposalPersonId() {
		return proposalPersonId;
	}

	public void setProposalPersonId(Integer proposalPersonId) {
		this.proposalPersonId = proposalPersonId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Timestamp getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public ProposalPersonRole getProposalPersonRole() {
		return proposalPersonRole;
	}

	public void setProposalPersonRole(ProposalPersonRole proposalPersonRole) {
		this.proposalPersonRole = proposalPersonRole;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getPersonRoleId() {
		return personRoleId;
	}

	public void setPersonRoleId(Integer personRoleId) {
		this.personRoleId = personRoleId;
	}

	public BigDecimal getPercentageOfEffort() {
		return percentageOfEffort;
	}

	public void setPercentageOfEffort(BigDecimal percentageOfEffort) {
		this.percentageOfEffort = percentageOfEffort;
	}

	public List<ProposalPersonUnit> getUnits() {
		return units;
	}

	public void setUnits(List<ProposalPersonUnit> units) {
		this.units = units;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

}
