package com.sdn.test;

public class Stats {

	public int carCount;
	public int personCount;
	public int relCount;
	public int deleteCount;
	public int updateCount;

	public void cumulate(Stats stats) {
		carCount += stats.carCount;
		personCount += stats.personCount;
		relCount += stats.relCount;
		deleteCount += stats.deleteCount;
		updateCount += stats.updateCount;
	}
}
