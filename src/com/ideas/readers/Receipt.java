package com.ideas.readers;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

	private List<IssueRequest> issueDetails;
	private List<Double> chargeList;
	private double totalCharge;
	private User user;

	public Receipt(User user, IssueRequest issueDetail, double chargesForReturn) {
		this.user = user;
		issueDetails = new ArrayList<>();
		issueDetails.add(issueDetail);
		chargeList = new ArrayList<>();
		chargeList.add(chargesForReturn);
		this.totalCharge = chargesForReturn;
	}

	public Receipt(User user) {
		this.user = user;
		this.totalCharge = 0;
	}

	public double getTotalCharge() {
		return totalCharge;
	}

	public void addDetail(IssueRequest issue, double charge) {
		if(null == issueDetails){
			issueDetails = new ArrayList<>();
		}
		if(null == chargeList){
			chargeList = new ArrayList<>();
		}
		issueDetails.add(issue);
		chargeList.add(charge);
		totalCharge+=charge;
	}
	
	
	

}
