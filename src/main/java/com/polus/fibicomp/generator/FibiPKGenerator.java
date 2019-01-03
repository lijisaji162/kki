package com.polus.fibicomp.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class FibiPKGenerator implements IdentifierGenerator {

	protected static Logger logger = Logger.getLogger(FibiPKGenerator.class.getName());

	@Override
	public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
		String identifierPropertyName = session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName();
		logger.info("identifierPropertyName : " + identifierPropertyName);
		String simpleName = obj.getClass().getSimpleName();
		logger.info("simpleName : " + simpleName);
		String query = "";
		if (simpleName.equals("Proposal")) {
			query = "SELECT MAX(PROPOSAL_ID) FROM FIBI_PROPOSAL";
		} else if (simpleName.equals("BudgetHeader")) {
			query = "SELECT MAX(BUDGET_HEADER_ID) FROM FIBI_BUDGET_HEADER";
		} else if (simpleName.equals("Workflow")) {
			query = "SELECT MAX(WORKFLOW_ID) FROM FIBI_WORKFLOW";
		} else {
			logger.info("Object name does not match.");
		}

		logger.info("query : " + query);
		Connection connection = session.connection();

		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(query);

			if (rs.next()) {
				int id = rs.getInt(1) + 1;
				logger.info("Generated Id: " + id);
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
