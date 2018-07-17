package com.polus.fibicomp.ip.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleTypes;

@Transactional
@Service(value = "institutionalProposalDao")
public class InstitutionalProposalDaoImpl implements InstitutionalProposalDao {

	protected static Logger logger = Logger.getLogger(InstitutionalProposalDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public boolean createInstitutionalProposal(Integer proposalId, String ipNumber, String userName) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		try {
			String functionName = "fn_smu_feed_inst_proposal";
			String functionCall = "{ ? = call "  + functionName + "(?,?,?) }";			
			statement = connection.prepareCall(functionCall);
			statement.registerOutParameter(1, OracleTypes.INTEGER);
			statement.setInt(2, proposalId);
			statement.setString(3, ipNumber);
			statement.setString(4, userName);
			statement.execute();
			int result = statement.getInt(1);
			if (result == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
