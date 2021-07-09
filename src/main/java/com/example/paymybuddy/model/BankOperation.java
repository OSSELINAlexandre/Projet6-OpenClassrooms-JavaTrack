package com.example.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Account_operation")
public class BankOperation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_operation")
	private int id;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "typeoftransaction")
	private boolean depositIsTrueWithdrawIsFalse;

	@OneToOne
	@JoinColumn(name = "accountholder", referencedColumnName = "id_person")
	private Person holder;

	public BankOperation() {
		super();
	}

	public BankOperation(int id, Double amount, boolean depositIsTrueWithdrawIsFalse, Person holder) {
		super();
		this.id = id;
		this.amount = amount;
		this.depositIsTrueWithdrawIsFalse = depositIsTrueWithdrawIsFalse;
		this.holder = holder;
	}

	public BankOperation(int id, Double amount) {
		super();
		this.id = id;
		this.amount = amount;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Person getHolder() {
		return holder;
	}

	public void setHolder(Person holder) {
		this.holder = holder;
	}

	public boolean isDepositIsTrueWithdrawIsFalse() {
		return depositIsTrueWithdrawIsFalse;
	}

	public void setDepositIsTrueWithdrawIsFalse(boolean depositIsTrueWithdrawIsFalse) {
		this.depositIsTrueWithdrawIsFalse = depositIsTrueWithdrawIsFalse;
	}

}