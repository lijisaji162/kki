package com.polus.fibicomp.ip.service;

import org.springframework.stereotype.Service;

@Service
public interface InstitutionalProposalService {

	/**
	 * This method is used to generate institutional proposal number.
	 * @return institutional proposal number.
	 */
	public String generateInstitutionalProposalNumber();

	/**
	 * This method is used to create institutional proposal.
	 * @param proposalId - Id of the proposal.
	 * @param ipNumber - Institutional proposal number.
	 * @param userName - Username of the logged in user.
	 * @return a boolean value.
	 */
	public boolean createInstitutionalProposal(Integer proposalId, String ipNumber, String userName);

}
