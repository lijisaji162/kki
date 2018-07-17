package com.polus.fibicomp.report.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.ProtocolType;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.report.vo.ReportVO;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.ProtocolView;

@Service
public interface ReportDao {

	public ReportVO fetchApplicationByGrantCallId(ReportVO reportVO);

	public List<GrantCall> fetchOpenGrantIds();

	public List<ProtocolType> fetchAllProtocolTypes();

	public List<Proposal> fetchApplicationsByGrantCallType(Integer grantCallTypeCode);

	public List<ProtocolView> fetchProtocolsByProtocolType(String protocolTypeCode);

	public List<GrantCallType> fetchAllGrantCallTypes();

	public List<Integer> fetchProposalIdByGrantTypeCode(Integer grantTypeCode);

	public List<Integer> fetchAwardCountByGrantType(List<Integer> proposalId);

	public ReportVO fetchAwardByGrantCallId(ReportVO reportVO);

	public ReportVO fetchExpenditureByAward(ReportVO reportVO);

	public List<AwardView> fetchAwardNumbers();

	public List<AwardView> fetchAwardByAwardNumbers(List<Integer> awardIds);

}
