package com.ews.parkswift.web.rest.dto.parking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.ews.parkswift.domain.util.CustomLocalDateSerializer;
import com.ews.parkswift.domain.util.ISO8601LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.paypal.svcs.types.ap.Receiver;

public class PaymentDTO {

	private Long paymentId;
	private BigDecimal amountCharged;
	private DateTime transactionDateTime;
	private String status;
	private String paypallPaymentResponse;
	private String paymentReferenceNumber;
	private DateTime transferToOwnerAccountDateTime;
	private DateTime transferToParkSwiftAccountDateTime;
	private BigDecimal amountToTransferOwnerAccount;
	private BigDecimal amountToTransferParkSwiftAccount;
	private String todel;
	private List<Receiver> listReceivers = new ArrayList<>();
	private String paypalURLWithPayKey;
	private String payKey;
	private String ownersPaypalEmail;
	private String senderPaypalEmail;
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	private DateTime payKeyExpiryDate;
	
	public String getSenderPaypalEmail() {
		return senderPaypalEmail;
	}
	public void setSenderPaypalEmail(String senderPaypalEmail) {
		this.senderPaypalEmail = senderPaypalEmail;
	}
	public DateTime getPayKeyExpiryDate() {
		return payKeyExpiryDate;
	}
	public void setPayKeyExpiryDate(DateTime payKeyExpiryDate) {
		this.payKeyExpiryDate = payKeyExpiryDate;
	}
	public String getOwnersPaypalEmail() {
		return ownersPaypalEmail;
	}
	public void setOwnersPaypalEmail(String ownersPaypalEmail) {
		this.ownersPaypalEmail = ownersPaypalEmail;
	}
	public String getPayKey() {
		return payKey;
	}
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	public String getPaypalURLWithPayKey() {
		return paypalURLWithPayKey;
	}
	public void setPaypalURLWithPayKey(String payURLWithPayKey) {
		this.paypalURLWithPayKey = payURLWithPayKey;
	}
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	public BigDecimal getAmountCharged() {
		return amountCharged;
	}
	public void setAmountCharged(BigDecimal amountCharged) {
		this.amountCharged = amountCharged;
	}
	public DateTime getTransactionDateTime() {
		return transactionDateTime;
	}
	public void setTransactionDateTime(DateTime transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPaypallPaymentResponse() {
		return paypallPaymentResponse;
	}
	public void setPaypallPaymentResponse(String paypallPaymentResponse) {
		this.paypallPaymentResponse = paypallPaymentResponse;
	}
	public String getPaymentReferenceNumber() {
		return paymentReferenceNumber;
	}
	public void setPaymentReferenceNumber(String paymentReferenceNumber) {
		this.paymentReferenceNumber = paymentReferenceNumber;
	}
	public DateTime getTransferToOwnerAccountDateTime() {
		return transferToOwnerAccountDateTime;
	}
	public void setTransferToOwnerAccountDateTime(
			DateTime transferToOwnerAccountDateTime) {
		this.transferToOwnerAccountDateTime = transferToOwnerAccountDateTime;
	}
	public DateTime getTransferToParkSwiftAccountDateTime() {
		return transferToParkSwiftAccountDateTime;
	}
	public void setTransferToParkSwiftAccountDateTime(
			DateTime transferToParkSwiftAccountDateTime) {
		this.transferToParkSwiftAccountDateTime = transferToParkSwiftAccountDateTime;
	}
	public BigDecimal getAmountToTransferOwnerAccount() {
		return amountToTransferOwnerAccount;
	}
	public void setAmountToTransferOwnerAccount(
			BigDecimal amountToTransferOwnerAccount) {
		this.amountToTransferOwnerAccount = amountToTransferOwnerAccount;
	}
	public BigDecimal getAmountToTransferParkSwiftAccount() {
		return amountToTransferParkSwiftAccount;
	}
	public void setAmountToTransferParkSwiftAccount(
			BigDecimal amountToTransferParkSwiftAccount) {
		this.amountToTransferParkSwiftAccount = amountToTransferParkSwiftAccount;
	}
	public String getTodel() {
		return todel;
	}
	public void setTodel(String todel) {
		this.todel = todel;
	}
	public List<Receiver> getListReceivers() {
		return listReceivers;
	}
	public void setListReceivers(List<Receiver> listReceivers) {
		this.listReceivers = listReceivers;
	}
	

}
