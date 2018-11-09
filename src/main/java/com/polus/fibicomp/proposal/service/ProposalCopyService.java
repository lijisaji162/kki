package com.polus.fibicomp.proposal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.proposal.vo.ProposalVO;

@Transactional
@Service(value = "proposalCopyService")
public interface ProposalCopyService {

	/**
	 * This method is used to make a copy of proposal.
	 * @param vo - Object of ProposalVO class.
	 * @return a String of details of proposal.
	 */
	public String copyProposal(ProposalVO vo);

}
