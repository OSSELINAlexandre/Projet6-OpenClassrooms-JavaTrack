package com.example.paymybuddy.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.paymybuddy.dto.LoginRegistration;
import com.example.paymybuddy.model.BankOperation;
import com.example.paymybuddy.model.ConnectionBetweenBuddies;
import com.example.paymybuddy.model.ExternalBankAccount;
import com.example.paymybuddy.model.Person;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.service.UserServices;

@Controller
public class UserController {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	UserServices userServices;

	private Person currentUser;
	private List<Transaction> listOfAllTransactions;
	private List<BankOperation> listOfAllOperations;
	private List<ConnectionBetweenBuddies> listOfAllConnexionOfBuddies;
	private List<ExternalBankAccount> listOfAllBankAccountOwned;
	
	
	

	@GetMapping("/setuserattributes")
	public ModelAndView setAllTheDataFromAuthenticatedUser(HttpSession session) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		logger.info("="
				+ authentication.getAuthorities().toString());
		currentUser = userServices.getThePersonAfterAuthentication(authentication.getName());

		ModelAndView theview = new ModelAndView("redirect:/userHome");

		listOfAllTransactions = currentUser.getAllTransactions();
		listOfAllOperations = currentUser.getListOfALLOperations();
		listOfAllConnexionOfBuddies = currentUser.getListOfBuddies();
		listOfAllBankAccountOwned = currentUser.getListOfAllBankAccountOwned();

		session.setAttribute("currentUser", currentUser);
		session.setAttribute("listOfAllConnexionOfBuddies", listOfAllConnexionOfBuddies);
		session.setAttribute("listOfAllTransactions", listOfAllTransactions);
		session.setAttribute("listOfAllOperations", listOfAllOperations);
		session.setAttribute("listOfAllBankAccountOwned", listOfAllBankAccountOwned);

		return theview;

	}

	@GetMapping("/login")
	public String returnMainPage(@RequestParam("errorFlag") Optional<Boolean> errorFlag, Model model) {

		return "login";
	}

	@GetMapping("/profileUser")
	public String returnUserProfile(HttpSession session) {

		return "user_profile";
	}

	@GetMapping("/register")
	public String registeringNewPerson(@RequestParam("passwordmatch") Optional<Boolean> passwordmatch,
			@RequestParam("existingmail") Optional<Boolean> existingmail, Model model, HttpSession session) {

		model.addAttribute("newUser", new LoginRegistration());

		if (passwordmatch.isPresent()) {
			model.addAttribute("passwordmatch", true);
		}
		if (existingmail.isPresent()) {
			model.addAttribute("existingmail", true);
		}

		return "register";
	}

	@GetMapping("/registersucessfully")
	public String registerIsASucess() {

		return "register_successfully";
	}

	@PostMapping("/process_register")
	public ModelAndView processRegistration(LoginRegistration person, HttpSession session, Model model) {

		if (userServices.checkIfTheEmailExistsInTheDB(person)) {

			ModelAndView theView = new ModelAndView("redirect:/register");
			theView.addObject("existingmail", true);
			return theView;

		} else if (!person.getPassword().equals(person.getSecondTestPassword())) {

			ModelAndView theView = new ModelAndView("redirect:/register");
			theView.addObject("passwordmatch", true);
			return theView;

		} else {

			ModelAndView theView = new ModelAndView("redirect:/registersucessfully");
			userServices.saveANewPersonInTheDB(person);

			return theView;

		}

	}
	
	
	
	///////////////////////////////////////////////////// SETTER FOR TESTING PURPOSES, CAN BE DELETED AFTERWARDS //////////////////////////////
	

	public void setUserServices(UserServices userServices) {
		this.userServices = userServices;
	}

	public void setCurrentUser(Person currentUser) {
		this.currentUser = currentUser;
	}

	public void setListOfAllTransactions(List<Transaction> listOfAllTransactions) {
		this.listOfAllTransactions = listOfAllTransactions;
	}

	public void setListOfAllOperations(List<BankOperation> listOfAllOperations) {
		this.listOfAllOperations = listOfAllOperations;
	}

	public void setListOfAllConnexionOfBuddies(List<ConnectionBetweenBuddies> listOfAllConnexionOfBuddies) {
		this.listOfAllConnexionOfBuddies = listOfAllConnexionOfBuddies;
	}

	public void setListOfAllBankAccountOwned(List<ExternalBankAccount> listOfAllBankAccountOwned) {
		this.listOfAllBankAccountOwned = listOfAllBankAccountOwned;
	}
	
	
	
	

}
