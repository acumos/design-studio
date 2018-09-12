package org.acumos.sqldatabroker.service;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

import javax.sql.DataSource;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.acumos.sqldatabroker.jdbc.datasource.DataSourceFactory;
import org.acumos.sqldatabroker.util.EELFLoggerDelegator;
import org.acumos.sqldatabroker.vo.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component("SQLDatabrokerServiceImpl")
public class SQLDatabrokerServiceImpl implements SQLDatabrokerService {

	
	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SQLDatabrokerServiceImpl.class);
	
	private final String SQLSELECT = "select * from ";
	private final String MYSQLLIMIT_ONE = " LIMIT %s, 1 ";
	
	@Autowired
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationService confService;
	
	@Autowired
	@Qualifier("ProtobufServiceImpl")
	private ProtobufService protoService;
	
	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	@Override
	public void writeDataTo(OutputStream out) throws ServiceException, IOException {
		// TODO Need to be implemented in next Release Sprint

	}

	@Override
	public byte[] getOneRecord() throws ServiceException {
		
		Configuration conf = null;
		byte[] result = null;
		String line = null;
		
		//DB variables 
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		//SQL variables
		int columnCount = 0;
		int start = confService.getStart();
		StringBuilder sb = new StringBuilder();
		
		
		try {
			DataSource ds = dataSourceFactory.getDataSource(confService.getConf());
			
			conf = confService.getConf();
			
			sb.append(SQLSELECT);
			sb.append(conf.getDatabaseName());
			sb.append(".");
			sb.append(conf.getTableName());
			sb.append(String.format(MYSQLLIMIT_ONE, start));
			String sql = sb.toString();
			
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			columnCount = rsmd.getColumnCount();
			
			if(rs.next()){
				sb = new StringBuilder();
				//for every row process the columns 
				for(int i = 1 ; i <= columnCount ; i++) {
					sb.append(rs.getString(i));
					if(i < columnCount ) { 
						sb.append(",");
					}
				}
				line = sb.toString();
				result = protoService.convertToProtobufFormat(line);
				confService.setStart(start+1);
			}
			
		} catch (CloneNotSupportedException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "No environment configuration found", e);
			throw new ServiceException("No environment configuration found!  Please set the Environment configuration.","401", "Exception in getOneRecord()", e);
		} catch (SQLException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "SQL Exception : Error getting data", e);
			throw new ServiceException("SQL Exception : Error getting data !!","401", "Exception in getOneRecord()", e);
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Exception : Error getting data", e);
			throw new ServiceException("Exception : Error getting data !!","401", "Exception in getOneRecord()", e);
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "SQL Exception : Not able to close resultset", e);
				}
			}
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "SQL Exception : Not able to close statement", e);
				}
			}
		}
		
		return result;
	}

}
