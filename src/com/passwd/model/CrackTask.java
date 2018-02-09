/**
 * 
 */
package com.passwd.model;

/**
 * @author Admin
 *
 */
public class CrackTask {

	private int task_id;
	private long keyspace;
	private long cprogress;
	private double progress;

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public long getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(long keyspace) {
		this.keyspace = keyspace;
	}

	public long getCprogress() {
		return cprogress;
	}

	public void setCprogress(long cprogress) {
		this.cprogress = cprogress;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

}
