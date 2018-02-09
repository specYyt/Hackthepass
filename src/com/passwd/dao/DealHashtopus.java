package com.passwd.dao;


public interface DealHashtopus {
	
	/*
	 * 通过taskId来获取Haslistid
	 */
	public int getHashlist(int taskId);
	
	/*
	 * 通过Hashlistid去获取hashlist是否已经被cracked
	 */
	public int getCracked(int Hashlistid);
	
	/*
	 * 当某一个任务的cracked为1是，删除另外几个在tasks里面的任务
	 */
	public void delnoCracked(int taskId);	
	
	/*
	 * 为任务创建优先级
	 */
	public void crtPriority(int priority, int taskId);
	
	/*
	 * 查看倒数第二条记录
	 */
	public int getPriority();
	
	/*
	 * 通过taskId获取Keyspace
	 */
	public long getKeyspace(int taskId);
	
	/*
	 * 通过taskId获取taskId
	 */
	public long getProgress(int taskId);
	

}
