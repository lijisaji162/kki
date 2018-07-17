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

public class GrantCallIdGenerator implements IdentifierGenerator {

	protected static Logger logger = Logger.getLogger(GrantCallIdGenerator.class.getName());

	@Override
	public Serializable generate(SessionImplementor sessionImplementor, Object object) throws HibernateException {
		String prefix = "GC";
		Connection connection = sessionImplementor.connection();

		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("select count(1) from FIBI_GRANT_CALL_HEDAER");

			if (rs.next()) {
				int id = rs.getInt(1) + 1001;
				String generatedId = prefix + "" + new Integer(id).toString();
				logger.info("Generated Grant Call Id : " + generatedId);
				return generatedId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
