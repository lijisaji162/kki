package com.polus.fibicomp.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.polus.fibicomp.view.AwardView;

/*@Entity
@Table(name = "AWARD_PERSONS")*/
public class AwardPerson implements Serializable {

	private static final long serialVersionUID = 1L;

	/*@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AWARD_PERSON")
	@SequenceGenerator(name = "SEQ_AWARD_PERSON", sequenceName = "SEQUENCE_AWARD_ID", allocationSize = 50)
	@Column(name = "AWARD_PERSON_ID")*/
	private Long awardContactId;

	//@Column(name = "PERSON_ID")
	protected String personId;

	//@Column(name = "FULL_NAME")
	private String fullName;

	//@Column(name = "CONTACT_ROLE_CODE")
	protected String roleCode;

	//@Column(name = "KEY_PERSON_PROJECT_ROLE")
	private String keyPersonRole;

	//@Column(name = "AWARD_NUMBER")
	private String awardNumber;

	//@Column(name = "AWARD_ID")
	private Integer awardId;

	//@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	//@Column(name = "UPDATE_USER")
	private String updateUser;

	//@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	//@Version
	//@Column(name = "VER_NBR", length = 8)
	protected Long versionNumber;

	//@Column(name = "OBJ_ID", length = 36, unique = true)
	protected String objectId;

	//@ManyToOne
	//@JoinColumn(foreignKey = @ForeignKey(name = ""), name = "AWARD_ID", referencedColumnName = "AWARD_ID", insertable = false, updatable = false)
	private AwardView awardView;

	public Long getAwardContactId() {
		return awardContactId;
	}

	public void setAwardContactId(Long awardContactId) {
		this.awardContactId = awardContactId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getKeyPersonRole() {
		return keyPersonRole;
	}

	public void setKeyPersonRole(String keyPersonRole) {
		this.keyPersonRole = keyPersonRole;
	}

	public String getAwardNumber() {
		return awardNumber;
	}

	public void setAwardNumber(String awardNumber) {
		this.awardNumber = awardNumber;
	}

	public Integer getAwardId() {
		return awardId;
	}

	public void setAwardId(Integer awardId) {
		this.awardId = awardId;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AwardView getAwardView() {
		return awardView;
	}

	public void setAwardView(AwardView awardView) {
		this.awardView = awardView;
	}

	// private AwardView award;
}
