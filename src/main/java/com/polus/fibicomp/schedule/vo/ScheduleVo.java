package com.polus.fibicomp.schedule.vo;

import java.util.List;

import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleActItems;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttachType;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttachment;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttendance;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleMinutes;
import com.polus.fibicomp.committee.pojo.MinuteEntryType;
import com.polus.fibicomp.committee.pojo.ProtocolContingency;
import com.polus.fibicomp.committee.pojo.ScheduleActItemType;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;

public class ScheduleVo {

	private Integer scheduleId;

	private CommitteeSchedule committeeSchedule;

	private Boolean status;

	private String message;

	private Committee committee;

	private String committeeId;

	private List<ScheduleStatus> scheduleStatus;

	private List<ScheduleActItemType> scheduleActItemTypes;

	private CommitteeScheduleActItems committeeScheduleActItems;

	private Integer commScheduleActItemsId;

	private List<MinuteEntryType> minuteEntrytypes;

	private List<ProtocolContingency> protocolContingencies;

	private CommitteeScheduleAttachment newCommitteeScheduleAttachment;

	private CommitteeScheduleMinutes newCommitteeScheduleMinute;

	private List<CommitteeScheduleAttachType> attachmentTypes;

	private CommitteeScheduleAttendance updatedAttendance;

	private Integer commScheduleMinuteId;

	private Integer commScheduleAttachId;

	private Integer commScheduleAttendanceId;

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public List<ScheduleStatus> getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(List<ScheduleStatus> scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public List<ScheduleActItemType> getScheduleActItemTypes() {
		return scheduleActItemTypes;
	}

	public void setScheduleActItemTypes(List<ScheduleActItemType> scheduleActItemTypes) {
		this.scheduleActItemTypes = scheduleActItemTypes;
	}

	public CommitteeScheduleActItems getCommitteeScheduleActItems() {
		return committeeScheduleActItems;
	}

	public void setCommitteeScheduleActItems(CommitteeScheduleActItems committeeScheduleActItems) {
		this.committeeScheduleActItems = committeeScheduleActItems;
	}

	public Integer getCommScheduleActItemsId() {
		return commScheduleActItemsId;
	}

	public void setCommScheduleActItemsId(Integer commScheduleActItemsId) {
		this.commScheduleActItemsId = commScheduleActItemsId;
	}

	public List<MinuteEntryType> getMinuteEntrytypes() {
		return minuteEntrytypes;
	}

	public void setMinuteEntrytypes(List<MinuteEntryType> minuteEntrytypes) {
		this.minuteEntrytypes = minuteEntrytypes;
	}

	public List<ProtocolContingency> getProtocolContingencies() {
		return protocolContingencies;
	}

	public void setProtocolContingencies(List<ProtocolContingency> protocolContingencies) {
		this.protocolContingencies = protocolContingencies;
	}

	public CommitteeScheduleAttachment getNewCommitteeScheduleAttachment() {
		return newCommitteeScheduleAttachment;
	}

	public void setNewCommitteeScheduleAttachment(CommitteeScheduleAttachment newCommitteeScheduleAttachment) {
		this.newCommitteeScheduleAttachment = newCommitteeScheduleAttachment;
	}

	public CommitteeScheduleMinutes getNewCommitteeScheduleMinute() {
		return newCommitteeScheduleMinute;
	}

	public void setNewCommitteeScheduleMinute(CommitteeScheduleMinutes newCommitteeScheduleMinute) {
		this.newCommitteeScheduleMinute = newCommitteeScheduleMinute;
	}

	public List<CommitteeScheduleAttachType> getAttachmentTypes() {
		return attachmentTypes;
	}

	public void setAttachmentTypes(List<CommitteeScheduleAttachType> attachmentTypes) {
		this.attachmentTypes = attachmentTypes;
	}

	public CommitteeScheduleAttendance getUpdatedAttendance() {
		return updatedAttendance;
	}

	public void setUpdatedAttendance(CommitteeScheduleAttendance updatedAttendance) {
		this.updatedAttendance = updatedAttendance;
	}

	public Integer getCommScheduleMinuteId() {
		return commScheduleMinuteId;
	}

	public void setCommScheduleMinuteId(Integer commScheduleMinuteId) {
		this.commScheduleMinuteId = commScheduleMinuteId;
	}

	public Integer getCommScheduleAttachId() {
		return commScheduleAttachId;
	}

	public void setCommScheduleAttachId(Integer commScheduleAttachId) {
		this.commScheduleAttachId = commScheduleAttachId;
	}

	public Integer getCommScheduleAttendanceId() {
		return commScheduleAttendanceId;
	}

	public void setCommScheduleAttendanceId(Integer commScheduleAttendanceId) {
		this.commScheduleAttendanceId = commScheduleAttendanceId;
	}
}
