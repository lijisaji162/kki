package com.polus.fibicomp.schedule.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.schedule.vo.ScheduleVo;

@Service
public interface ScheduleService {

	/**
	 * This method is used to load schedule by Id.
	 * @param scheduleId - Id of a schedule.
	 * @return a String of details of schedule.
	 */
	public String loadScheduleById(Integer scheduleId);

	/**
	 * This method is used to update schedule.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of updated schedule.
	 */
	public String updateSchedule(ScheduleVo scheduleVo);

	/**
	 * This method is used to add other actions for a schedule.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with a list of other actions.
	 */
	public String addOtherActions(ScheduleVo scheduleVo);

	/**
	 * This method is used to delete other actions.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with updated list of other actions.
	 */
	public String deleteOtherActions(ScheduleVo scheduleVo);

	/**
	 * This method is used to add committee schedule minute.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with minutes.
	 */
	public String addCommitteeScheduleMinute(ScheduleVo scheduleVo);

	/**
	 * This method is used to update schedule attendance.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with list of attendance.
	 */
	public String updateScheduleAttendance(ScheduleVo scheduleVo);

	/**
	 * This method is used for attendance to mark present member.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with present member.
	 */
	public String addOthersPresent(ScheduleVo scheduleVo);

	/**
	 * This method is used to delete schedule minute.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with updated list of minutes.
	 */
	public String deleteScheduleMinute(ScheduleVo scheduleVo);

	/**
	 * This method is used to delete schedule attachment.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with updated list of attachment.
	 */
	public String deleteScheduleAttachment(ScheduleVo scheduleVo);

	/**
	 * This method is used to add schedule attachment.
	 * @param files - attached files.
	 * @param formDataJSON - form data for the attachment.
	 * @return a String of details of schedule data with list of attachments.
	 */
	public String addScheduleAttachment(MultipartFile[] files, String formDataJSON);

	/**
	 * This method is used to delete schedule attendance.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with updated list of attendance.
	 */
	public String deleteScheduleAttendance(ScheduleVo scheduleVo);

	/**
	 * This method is used to update schedule attachment.
	 * @param scheduleVo - Object of ScheduleVo.
	 * @return a String of details of schedule data with updated list of attachments.
	 */
	public String updateScheduleAttachment(ScheduleVo scheduleVo);

	/**
	 * This method is used to download schedule attachment.
	 * @param attachmentId - Id of the attachment to download.
	 * @return attachmentData.
	 */
	public ResponseEntity<byte[]> downloadScheduleAttachment(Integer attachmentId);

	/**
	 * This method is used to update committee schedule minute.
	 * @param vo - Object of ScheduleVo.
	 * @return a String of details of schedule data with updated minute.
	 */
	public String updateCommitteeScheduleMinute(ScheduleVo vo);

}
