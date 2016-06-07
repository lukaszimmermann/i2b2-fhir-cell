package edu.harvard.i2b2.converter;


public interface BundleStatusRepository {
	
	public boolean isComplete(String pid);
	
	public boolean isProcessing(String pid) ;

	public boolean isFailed(String pid);

	public void markProcessing(String pid) ;
	
	public void markComplete(String pid) ;

	public void markFailed(String pid);

	public void resetAll();

	public BundleStatus findOne(String pid);	

}