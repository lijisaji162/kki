package com.polus.fibicomp.schedule.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleActItems;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttachType;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttachment;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttendance;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleMinutes;
import com.polus.fibicomp.committee.pojo.MinuteEntryType;
import com.polus.fibicomp.committee.pojo.ProtocolContingency;
import com.polus.fibicomp.committee.pojo.ScheduleActItemType;
import com.polus.fibicomp.view.ProtocolView;

@Service
public interface ScheduleDao {

	/**
	 * This method is used to get a protocol based on parameters.
	 * @param protocolId - Id of the submitted protocol.
	 * @param personId - personId of the PI of submitted protocol.
	 * @param fullName - fullName of the PI of submitted protocol.
	 * @return an object of protocol.
	 */
	public ProtocolView fetchProtocolViewByParams(Integer protocolId, String personId, String fullName);

	/**
	 * This method is used to fetch all schedule actionItem type.
	 * @return a list of Schedule actionItem type.
	 */
	public List<ScheduleActItemType> fetchAllScheduleActItemType();

	/**
	 * This method is used to update committee schedule.
	 * @param committeeSchedule - Object of CommitteeSchedule.
	 * @return an object of updated CommitteeSchedule.
	 */
	public CommitteeSchedule updateCommitteeSchedule(CommitteeSchedule committeeSchedule);

	/**
	 * This method is used to add other actions.
	 * @param committeeScheduleActItems - Object of CommitteeScheduleActItems.
	 * @return an object of updated CommitteeScheduleActItems.
	 */
	public CommitteeScheduleActItems addOtherActions(CommitteeScheduleActItems committeeScheduleActItems);

	/**
	 * This method is used to fetch all minute entry types.
	 * @return a list of MinuteEntryType.
	 */
	public List<MinuteEntryType> fetchAllMinuteEntryTypes();

	/**
	 * This method is used to fetch all protocol contingency.
	 * @return a list of ProtocolContingency.
	 */
	public List<ProtocolContingency> fetchAllProtocolContingency();

	/**
	 * This method is used to fetch all committee schedule attachment type.
	 * @return a list of CommitteeScheduleAttachmentType.
	 */
	public List<CommitteeScheduleAttachType> fetchAllCommitteeScheduleAttachType();

	/**
	 * This method is used to add committee schedule minute.
	 * @param committeeScheduleMinutes - Object of CommitteeScheduleMinutes.
	 * @return an object of updated CommitteeScheduleMinutes.
	 */
	public CommitteeScheduleMinutes addCommitteeScheduleMinute(CommitteeScheduleMinutes committeeScheduleMinutes);

	/**
	 * This method is used to add committee schedule attendance.
	 * @param scheduleAttendance - Object of CommitteeScheduleAttendance.
	 * @return an object of updated CommitteeScheduleAttendance.
	 */
	public CommitteeScheduleAttendance addCommitteeScheduleAttendance(CommitteeScheduleAttendance scheduleAttendance);

	/**
	 * This method is used to add Schedule attachment.
	 * @param committeeScheduleAttachment - Object of CommitteeScheduleAttachment.
	 * @return an object of updated CommitteeScheduleAttachment.
	 */
	public CommitteeScheduleAttachment addScheduleAttachment(CommitteeScheduleAttachment committeeScheduleAttachment);

	/**
	 * This method is used to fetch attachment by Id.
	 * @param attachmentId - Id of the attachment.
	 * @return an object of CommitteeScheduleAttachment.
	 */
	public CommitteeScheduleAttachment fetchAttachmentById(Integer attachmentId);

}
