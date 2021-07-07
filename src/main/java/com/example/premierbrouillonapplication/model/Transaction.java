package com.example.premierbrouillonapplication.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_transaction")
	private int id;

	@Column(name = "Commentaire")
	private String commentaire;

	@Column(name = "Amount")
	private Double amount;

	@OneToOne
	@JoinColumn(name = "Willpayperson", referencedColumnName = "id_person")
	private Person payeur;

	@OneToOne
	@JoinColumn(name = "Willbepayedperson", referencedColumnName = "id_person")
	private Person payee;

	public Transaction() {
		super();
	}
	
	public Transaction(String commentaire, Double amount, Person payeur, Person payee) {
		super();
		this.commentaire = commentaire;
		this.amount = amount;
		this.payeur = payeur;
		this.payee = payee;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double integer) {
		this.amount = integer;
	}

	public Person getPayeur() {
		return payeur;
	}

	public void setPayeur(Person payeur) {
		this.payeur = payeur;
	}

	public Person getPayee() {
		return payee;
	}

	public void setPayee(Person payee) {
		this.payee = payee;
	}

}
