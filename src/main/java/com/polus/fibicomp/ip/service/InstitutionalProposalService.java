package com.polus.fibicomp.ip.service;

import org.springframework.stereotype.Service;

@Service
public interface InstitutionalProposalService {

	public String generateInstitutionalProposalNumber();

	public boolean createInstitutionalProposal(Integer proposalId, String ipNumber, String userName);

}
