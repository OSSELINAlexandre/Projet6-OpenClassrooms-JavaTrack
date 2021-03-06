package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.paymybuddy.model.ConnectionBetweenBuddies;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.constant.*;
import com.example.paymybuddy.dto.BuddiesInConnexion;
import com.example.paymybuddy.dto.PaymentData;
import com.example.paymybuddy.service.TransactionsServices;

@Controller
public class TransferController {

	@Autowired
	private TransactionsServices transactionServices;

	private Person currentUser;
	private List<Transaction> listOfAllTransactions;
	private List<ConnectionBetweenBuddies> listOfAllConnexionOfBuddies;

	@GetMapping("/transfer")
	@Transactional
	public String returnTransferPage(@RequestParam("errorFindingBuddy") Optional<Boolean> errorFindingBuddy,
			@RequestParam("errorPayingBuddy") Optional<Boolean> errorPayingBuddy,
			@RequestParam("alreadyHavingBuddy") Optional<Boolean> alreadyHavingBuddy,
			@RequestParam("cannotAddYourself") Optional<Boolean> cannotAddYourself,
			@RequestParam("yourBuddyHasTooMuchMoney") Optional<Boolean> yourBuddyHasTooMuchMoney, Model model,
			HttpSession session) {

		currentUser = (Person) session.getAttribute("currentUser");

		listOfAllTransactions = (List<Transaction>) session.getAttribute("listOfAllTransactions");
		listOfAllConnexionOfBuddies = (List<ConnectionBetweenBuddies>) session
				.getAttribute("listOfAllConnexionOfBuddies");

		model.addAttribute("listTransactions", listOfAllTransactions);
		model.addAttribute("currentUser", currentUser);

		model.addAttribute("buddy", new BuddiesInConnexion());
		model.addAttribute("paymentID", new PaymentData());

		model.addAttribute("listOfBuddies",
				transactionServices.createTheListOfBuddyForTransaction(listOfAllConnexionOfBuddies));
		model.addAttribute("listOfAllTransactions", listOfAllTransactions);

		if (errorFindingBuddy.isPresent()) {

			model.addAttribute("errorFindingBuddy", true);
		}
		if (errorPayingBuddy.isPresent()) {

			model.addAttribute("errorPayingBuddy", true);

		}
		if (alreadyHavingBuddy.isPresent()) {

			model.addAttribute("alreadyHavingBuddy", true);

		}
		if (cannotAddYourself.isPresent()) {

			model.addAttribute("cannotAddYourself", true);

		}
		if (yourBuddyHasTooMuchMoney.isPresent()) {

			model.addAttribute("yourBuddyHasTooMuchMoney", true);

		}

		return "transfer_page";
	}

	@PostMapping("/transfer_validation")
	public ModelAndView verificationOfExistenceOfBuddyForTransfer(BuddiesInConnexion bud, HttpSession session,
			Model model) {

		ModelAndView theView = new ModelAndView("redirect:/transfer");
		Boolean aBuddyHasBeenAddedToTheCurrentUser = transactionServices.addingABuddyToTheCurrentUser(bud, currentUser,
				session);

		if (transactionServices.checkIfThisPersonExistsInTheDB(bud) && aBuddyHasBeenAddedToTheCurrentUser == false) {

			if (transactionServices.findByEmailFromRepo(bud.getEmail()).getId() == currentUser.getId()) {

				theView.addObject("cannotAddYourself", true);

			} else {

				theView.addObject("alreadyHavingBuddy", true);
			}

		} else if (!transactionServices.checkIfThisPersonExistsInTheDB(bud)
				&& aBuddyHasBeenAddedToTheCurrentUser == false) {

			theView.addObject("errorFindingBuddy", true);

		}

		return theView;

	}

	@PostMapping("/processingPayment")
	public ModelAndView processThePaymentBetweenBuddies(PaymentData pay, Model model, HttpSession session) {

		ModelAndView theView = new ModelAndView("redirect:/transfer");

		if (transactionServices.checkIfCurrentUserHasSufficientAmounts(currentUser,
				pay.getAmount() * (1 + Fees.CLASSIC_FEE_APP))) {

			if (transactionServices.checkIfGoingToBePayedBuddyDoesNotHaveTooMuchMoney(pay.getPersonToPay(),
					pay.getAmount())) {
				transactionServices.adjustTheAccountsBetweenBuddies(pay, currentUser, session);
			} else {

				theView.addObject("yourBuddyHasTooMuchMoney", true);
			}

		} else {

			theView.addObject("errorPayingBuddy", true);
		}

		return theView;

	}

	///////////////////////////////////////////////////// SETTER FOR TESTING PURPOSES, CAN BE DELETED AFTERWARDS //////////////////////////////


	public void setTransactionServices(TransactionsServices transactionServices) {
		this.transactionServices = transactionServices;
	}

	public void setCurrentUser(Person currentUser) {
		this.currentUser = currentUser;
	}

	public void setListOfAllTransactions(List<Transaction> listOfAllTransactions) {
		this.listOfAllTransactions = listOfAllTransactions;
	}

	public void setListOfAllConnexionOfBuddies(List<ConnectionBetweenBuddies> listOfAllConnexionOfBuddies) {
		this.listOfAllConnexionOfBuddies = listOfAllConnexionOfBuddies;
	}

}
