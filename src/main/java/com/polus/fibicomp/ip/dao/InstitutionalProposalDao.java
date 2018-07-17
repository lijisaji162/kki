package com.polus.fibicomp.ip.dao;

import org.springframework.stereotype.Service;

@Service
public interface InstitutionalProposalDao {

	public boolean createInstitutionalProposal(Integer proposalId, String ipNumber, String userName);

}
