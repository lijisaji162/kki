package com.polus.fibicomp.proposal.prereview.dao;

import java.util.List;

import javax.persistence.LockModeType;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.proposal.prereview.pojo.PreReviewStatus;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewType;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewer;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReview;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReviewAttachment;

@Transactional
@Service(value = "proposalPreReviewDao")
public class ProposalPreReviewDaoImpl implements ProposalPreReviewDao {

	protected static Logger logger = Logger.getLogger(ProposalPreReviewDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<PreReviewType> fetchAllPreReviewTypes() {
		return hibernateTemplate.loadAll(PreReviewType.class);
	}

	@Override
	public List<PreReviewStatus> fetchAllPreReviewStatus() {
		return hibernateTemplate.loadAll(PreReviewStatus.class);
	}

	@Override
	public PreReviewStatus getPreReviewStatusByCode(String statusCode) {
		return hibernateTemplate.get(PreReviewStatus.class, statusCode);
	}

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Override
	public ProposalPreReview saveOrUpdatePreReview(ProposalPreReview preReview) {
		hibernateTemplate.saveOrUpdate(preReview);
		return preReview;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProposalPreReview> loadAllProposalPreReviewsByProposalId(Integer proposalId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalPreReview.class);
		criteria.add(Restrictions.eq("proposalId", proposalId));
		List<ProposalPreReview> preReviews = criteria.list();
		return preReviews;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProposalPreReview> fetchPreReviewsByCriteria(Integer proposalId, String personId, String preReviewStatus) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalPreReview.class);
		criteria.add(Restrictions.eq("proposalId", proposalId));
		if (personId != null) {
			criteria.add(Restrictions.eq("reviewerPersonId", personId));
		}
		criteria.add(Restrictions.eq("reviewStatusCode", preReviewStatus));
		List<ProposalPreReview> preReviews = criteria.list();
		return preReviews;
	}

	@Override
	public ProposalPreReviewAttachment fetchAttachmentById(Integer attachmentId) {
		return hibernateTemplate.get(ProposalPreReviewAttachment.class, attachmentId);
	}

	@Override
	public List<PreReviewer> fetchAllPreReviewer() {
		return hibernateTemplate.loadAll(PreReviewer.class);
	}

}
