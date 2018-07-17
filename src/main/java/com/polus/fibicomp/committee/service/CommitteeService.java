package com.polus.fibicomp.committee.service;

import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.vo.CommitteeVo;

@Service
public interface CommitteeService {

	/**
	 * This method is used to create committee.
	 * @param committeeTypeCode - Type code of committee.
	 * @return committee
	 */
	public String createCommittee(Integer committeeTypeCode);

	/**
	 * This method is used to save committee.
	 * @param vo - Object of CommitteeVo.
	 * @return Response object of committee.
	 */
	public String saveCommittee(CommitteeVo vo);

	/**
	 * This method is used to fetch initial datas for committee.
	 * @return a String of details for committee.
	 */
	public String fetchInitialDatas();

	/**
	 * This method is used to load a committee by Id.
	 * @param committeeId - Id of the committee.
	 * @return committee - a String of details of committee.
	 */
	public String loadCommitteeById(String committeeId);

	/**
	 * This method is used to create a schedule.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return  a String of details of committee schedule.
	 * @throws ParseException
	 */
	public String addSchedule(CommitteeVo committeeVo) throws ParseException;

	/**
	 * This method is used to save area of research.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with area of research.
	 */
	public String saveAreaOfResearch(CommitteeVo committeeVo);

	/**
	 * This method is used to delete area of research.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with updated area of research list.
	 */
	public String deleteAreaOfResearch(CommitteeVo committeeVo);

	/**
	 * This method is used to delete Schedule.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details of committee with updated list of committee schedules.
	 */
	public String deleteSchedule(CommitteeVo committeeVo);

	/**
	 * This method is used to filter CommitteeScheduleDates.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details with filtered list of committee schedule.
	 */
	public String filterCommitteeScheduleDates(CommitteeVo committeeVo);

	/**
	 * This method is used to reset CommitteeScheduleDates.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details with list of committee schedule.
	 */
	public String resetCommitteeScheduleDates(CommitteeVo committeeVo);

	/**
	 * This method is used to update CommitteeSchedule.
	 * @param committeeVo - Object of CommitteeVo.
	 * @return a String of details with updated list of committee schedule.
	 */
	public String updateCommitteeSchedule(CommitteeVo committeeVo);

}
