package com.example.momomanagement.Agent;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class TransactionModel implements Parcelable {

    private String transactionID, transactionType, phoneNumber;
    private double amount, amountSentWithdrawn, charge, commissionEarned, eCashBalance;
    private Timestamp DateTime;

    public TransactionModel() {
    }

    public TransactionModel(String transactionID, String transactionType, String phoneNumber, double amount, double amountSentWithdrawn, double charge, double commissionEarned, double eCashBalance, Timestamp dateTime) {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
        this.amountSentWithdrawn = amountSentWithdrawn;
        this.charge = charge;
        this.commissionEarned = commissionEarned;
        this.eCashBalance = eCashBalance;
        DateTime = dateTime;
    }

    protected TransactionModel(Parcel in) {
        transactionID = in.readString();
        transactionType = in.readString();
        phoneNumber = in.readString();
        amount = in.readDouble();
        amountSentWithdrawn = in.readDouble();
        charge = in.readDouble();
        commissionEarned = in.readDouble();
        eCashBalance = in.readDouble();
        DateTime = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountSentWithdrawn() {
        return amountSentWithdrawn;
    }

    public void setAmountSentWithdrawn(double amountSentWithdrawn) {
        this.amountSentWithdrawn = amountSentWithdrawn;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getCommissionEarned() {
        return commissionEarned;
    }

    public void setCommissionEarned(double commissionEarned) {
        this.commissionEarned = commissionEarned;
    }

    public double geteCashBalance() {
        return eCashBalance;
    }

    public void seteCashBalance(double eCashBalance) {
        this.eCashBalance = eCashBalance;
    }

    public Timestamp getDateTime() {
        return DateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        DateTime = dateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(transactionID);
        parcel.writeString(transactionType);
        parcel.writeString(phoneNumber);
        parcel.writeDouble(amount);
        parcel.writeDouble(amountSentWithdrawn);
        parcel.writeDouble(charge);
        parcel.writeDouble(commissionEarned);
        parcel.writeDouble(eCashBalance);
        parcel.writeParcelable(DateTime, i);
    }
}
