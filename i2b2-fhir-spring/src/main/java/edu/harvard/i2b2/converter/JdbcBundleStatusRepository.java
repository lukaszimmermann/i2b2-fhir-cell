package edu.harvard.i2b2.converter;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
public class JdbcBundleStatusRepository implements BundleStatusRepository {
	static Logger logger = LoggerFactory.getLogger(JdbcBundleStatusRepository.class);

	private JdbcOperations jdbc;

	@Autowired
	public JdbcBundleStatusRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	private static class BundleStatusMapper implements RowMapper<BundleStatus> {
	    public BundleStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
	      BundleStatus b= new BundleStatus(
	          rs.getLong("id"),
	          rs.getString("patient_id"), 
	          rs.getString("bundle_status_level")
	          );
	      b.setCreatedDate(rs.getDate("created_date"));
	      return b;
	    }
	  }
	public boolean isComplete(String pid) {
		return findOne(pid).getBundleStatusLevel().equals("COMPLETE");
	}

	public BundleStatus findOne(String pid){
		return (BundleStatus) jdbc.query(
		        "select * " +
		        " from bundle_status where patient_id="+pid+";",
		        new BundleStatusMapper()).get(0);
	}

	public boolean isProcessing(String pid) {
		
		return findOne(pid).getBundleStatusLevel().equals("PROCESSING");
	}

	public  boolean isFailed(String pid) {
		return findOne(pid).getBundleStatusLevel().equals("FAILED");
	}

	public void markProcessing(String pid) {
		System.out.println("PROCESSING:"+pid);
		jdbc.update(
		        "insert into  Bundle_Status ( patient_id, bundle_status_level,created_date)" +
		        " values (?, ?, ?)",
		        pid,"PROCESSING",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

	public  void markComplete(String pid) {
		jdbc.update(
		        "update Bundle_Status SET bundle_status_level='COMPLETE' where patient_id= ? " ,
		        pid);
	}

	public void markFailed(String pid) {
		jdbc.update(
		        "update Bundle_Status SET bundle_status_level='FAILED' where patient_id= ? )" ,
		        pid);
	}

	
	public void resetAll() {
		
	}

}