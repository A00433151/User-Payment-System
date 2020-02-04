package com.dpenny.mcda5510.entity;

public class Transaction {
	private int id;
	private String nameOnCard;
	private String cardNumber;
	private double unitPrice;
	private int quantity;
	private double totalPrice;
	private String expDate;
	private String createdOn;
	private String createdBy;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNameOnCard() {
		return nameOnCard;
	}
	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String toString() {
		String results=new String();
		results="[ID: "+id+", NameOnCard: "+nameOnCard+", CardNumber: "+cardNumber+", UnitPrice: +"+unitPrice+
				", Quantity: "+quantity+", TotalPrice: "+totalPrice+", ExpDate: "+expDate+", CreatedOn: "+createdOn+
				", CreatedBy: "+createdBy+"]";
		return results;
	}
}
