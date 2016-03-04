package com.ideas.readers;

public enum Membership {
IVORY(3, 7, 20.0, 1000.0),SILVER(4, 10, 30.0, 2000.0),GOLD(5, 15, 40.0, 3000.0),PLATINUM(6, 20, 50.0, 5000.0);
	
	private int EXTRA_BOOK_CHARGE = 50;
	
	private int maxBooksHeldAtATime;
	private int holdingPeriod;
	private double extraChargePerDay;
	private double packageCostPerYear;
	
	private Membership(int maxBooksHeldAtATime, int holdingPeriod, double extraChargePerDay,
			 double packageCostPerYear) {
				this.maxBooksHeldAtATime = maxBooksHeldAtATime;
				this.holdingPeriod = holdingPeriod;
				this.extraChargePerDay = extraChargePerDay;
				this.packageCostPerYear = packageCostPerYear;
		
	}

	public int getMaxBooksHeld() {
		return maxBooksHeldAtATime;
	}
	public boolean canIssueMoreBooks(){
		return false;
		
	}

	public boolean isReturnPeriodWithinReturnPeriod(int days) {
		
		return holdingPeriod>days;
	}

	public double getChargesForReturn(int days, Boolean isExtraBook) {
		Double charge = 0.0;
		if(isExtraBook)
			charge += EXTRA_BOOK_CHARGE;
		if(!isReturnPeriodWithinReturnPeriod(days))
			charge = (extraChargePerDay * (days-holdingPeriod));
		return charge;
	}

	public Boolean hasExtraBook(int noOfBooksIssued) {
		return noOfBooksIssued>maxBooksHeldAtATime;
	}
}
