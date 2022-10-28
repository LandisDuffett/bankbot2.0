package presentation;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import exceptions.BalanceBelowZeroException;
import exceptions.EmptyFieldException;
import exceptions.EmptyListException;
import exceptions.ExceedLimitException;
import exceptions.InvalidInputException;
import exceptions.MismatchException;
import exceptions.NameNotUniqueException;
import exceptions.RecordAlreadyPresentException;
import exceptions.ScanInputException;
import exceptions.SystemException;
import exceptions.TypeMismatchException;
import model.AccountPojo;
import model.CredentialPojo;
import model.SessionPojo;
import model.TransactionPojo;
import model.UserPojo;
import service.AccountService;
import service.AccountServiceImpl;
import service.SessionService;
import service.SessionServiceImpl;
import service.TransactionService;
import service.TransactionServiceImpl;
import service.UserService;
import service.UserServiceImpl;

public class LandingPage {
	private Scanner scan = new Scanner(System.in);
	static int userId = 0;
	static int sessionId;
	private UserService userService = new UserServiceImpl();
	private SessionService sessionService = new SessionServiceImpl();
	private AccountService accountService = new AccountServiceImpl();
	private TransactionService transactionService = new TransactionServiceImpl();
	private UserPojo newUserPojo = new UserPojo();
	private CredentialPojo newCredentialPojo = new CredentialPojo();
	private String choice;
	
	public void display() {
		System.out.println("*****************************");
		System.out.println("WELCOME TO BANKBOT");
		System.out.println("*****************************");
		System.out.println("1. Log in");
		System.out.println("2. Register for new online account");
		System.out.println("3. Recover password");
		System.out.println("*****************************");
		System.out.println("Please enter an option:");
		try {
			int option;
			option = scan.nextInt();
			scan.nextLine();
			switch(option) {
			case 1:
				login();
			case 2:
				createOnlineAccount();
			case 3:
				recoverPassword(0);
			default:
				System.out.println("That is not an option. Try again.");
				System.out.println();
				display();
			}
		} catch (InputMismatchException e) {
			// TODO Auto-generated catch block
			ScanInputException s = new ScanInputException();
			System.out.println(s.getMessage());
			System.out.println("Logging out. Try again.");
			logout();
		}
	}
	
	public void login() {
		CredentialPojo credentialPojo = new CredentialPojo();
		UserPojo userPojo = new UserPojo();
		int counter = 0;
		while(counter <3 && userId == 0) {
		System.out.println();
		System.out.println("LOG IN: " + (3-counter) + " attempts remaining.");
		counter++;
		System.out.println("Enter user name: ");
		credentialPojo.setUser_name(scan.nextLine());
		System.out.println("Enter password: ");
		credentialPojo.setUser_password(scan.nextLine());
		List<CredentialPojo> allCredentials;
		try {
			allCredentials = userService.getAllCredentials();
			for(CredentialPojo item: allCredentials) {
				if(item.getUser_name().equals(credentialPojo.getUser_name()) && item.getUser_password().equals(credentialPojo.getUser_password())) { 
					userId = item.getUser_id();
					userPojo = userService.getAUser(userId);
					SessionPojo newSessionPojo = new SessionPojo();
					newSessionPojo.setUserName(item.getUser_name());
					newSessionPojo.setLoginTime(LocalDateTime.now().toString());
					newSessionPojo.setLogoutTime("active");
					SessionPojo returnedSessionPojo = sessionService.addSession(newSessionPojo);
					sessionId = returnedSessionPojo.getSessionNumber();
			}
			}
			if(userId != 0) {
				System.out.println("successful login");
				System.out.println("Welcome, " + userPojo.getFirst_name()+ " " + userPojo.getLast_name() + "!");
				System.out.println();
				mainMenu();
			} else {
				System.out.println("login failed");
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());;
		}
		
		}
		if(counter >=3)
			System.out.println("You have reached the maximum number of login attempts. Try again later.");
		System.out.println();
		logout();
	}
		
	public void createOnlineAccount() {
		System.out.println();
		System.out.println("REGISTER FOR AN ONLINE ACCOUNT");
		System.out.println("Directions:");
		System.out.println("1. For each field, enter your input and then press 'Enter'.");
		System.out.println("2. Restrict input for any field to 20 characters unless stated otherwise.");
		System.out.println();
		System.out.println("Please enter your first name: ");
		newUserPojo.setFirst_name(scan.nextLine());
		System.out.println("Please enter your last name: ");
		newUserPojo.setLast_name(scan.nextLine());
		System.out.println("Please enter a login user name: ");
		newCredentialPojo.setUser_name(scan.nextLine());
		System.out.println("Please enter a login password: ");
		newCredentialPojo.setUser_password(scan.nextLine());
		System.out.println("Please enter a user pin of 4 digits (first digit may not be zero):");
		try {
			newCredentialPojo.setUser_pin(scan.nextInt());
			scan.nextLine();
			System.out.println("Please enter your mother's maiden name:");
			newCredentialPojo.setUser_maiden(scan.nextLine());
		} catch (InputMismatchException i) {
			MismatchException m = new MismatchException();
			System.out.println();
			System.out.println(m.getMessage());
			System.out.println("Returning to welcome page...");
			scan.nextLine();
			display();
		}
		UserPojo userPojo = null;
		CredentialPojo credentialPojo = null;
		try {
			userPojo = userService.addUser(newUserPojo);
			newCredentialPojo.setUser_id(userPojo.getUser_id());
		} catch (EmptyFieldException f) {
			System.out.println(f.getMessage());
			display();
		} catch (ExceedLimitException x) {
			System.out.println(x.getMessage());
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
		if (userPojo != null) {
			try {
				try {
					credentialPojo = userService.addCredential(newCredentialPojo);
					login();
				} catch (NameNotUniqueException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					this.display();
				}
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void recoverPassword(int cnt) {
		CredentialPojo credentialPojo = new CredentialPojo();
		int counter = cnt;
		while(counter <3) {
		System.out.println();
		System.out.println("PASSWORD RECOVERY: " + (3-counter) + " attempts remaining.");
		counter++;
		System.out.println("Enter user name: ");
		credentialPojo.setUser_name(scan.nextLine());
		try {
			System.out.println("Enter your PIN: ");
			credentialPojo.setUser_pin(scan.nextInt());
			scan.nextLine();
		} catch (InputMismatchException i) {
			MismatchException m = new MismatchException();
			System.out.println();
			System.out.println(m.getMessage());
			System.out.println();
			scan.nextLine();
			recoverPassword(counter);
		}
		System.out.println("Enter mother's maiden name: ");
		credentialPojo.setUser_maiden(scan.nextLine());
		List<CredentialPojo> allCredentials;
		try {
			allCredentials = userService.getAllCredentials();
			for(CredentialPojo item: allCredentials) {
				if(item.getUser_name().equals(credentialPojo.getUser_name()) && item.getUser_pin() == credentialPojo.getUser_pin() &&
						item.getUser_maiden().equals(credentialPojo.getUser_maiden())) { 
					System.out.println("Your password is: " + item.getUser_password());
					System.out.println();
					counter = 3;
					display();
				} 
			}
			if(counter < 3)
				System.out.println("Invalid credentials. Try again.");
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());;
		}
		}
		if(counter >= 3) {
			System.out.println("You have reached the maximum number of attempts. Please see a representative at your local branch for further assistance.");
			logout();
		}
	}
	
	public void mainMenu() {
		System.out.println("*****************************");
		System.out.println("MAIN MENU");
		System.out.println("*****************************");
		System.out.println("1. Online account management");
		System.out.println("2. Bank account management");
		System.out.println("3. Manage your money");
		System.out.println("4. Log out");
		System.out.println("*****************************");
		System.out.println("Please enter an option:");
		try {
			int option = scan.nextInt();
			scan.nextLine();
			switch(option) {
			case 1:
				onlineAccount();
			case 2:
				accountManage();
			case 3:
				manageMoney();
			case 4:
				logout();
			default:
				System.out.println("Invalid input. Try again.");
				System.out.println();
				mainMenu();
			}
		} catch (InputMismatchException e) {
			// TODO Auto-generated catch block
			ScanInputException s = new ScanInputException();
			System.out.println(s.getMessage());
			System.out.println("Logging out...");
			logout();
		}
	}
	
	public void onlineAccount() {
		System.out.println("*****************************");
		System.out.println("ONLINE ACCOUNT MANAGEMENT");
		System.out.println("*****************************");
		System.out.println("1. Close online account");
		System.out.println("2. Change password");
		System.out.println("3. Return to main menu");
		System.out.println("4. Log out");
		System.out.println("*****************************");
		System.out.println("Please enter an option:");	
		try {
			int option = scan.nextInt();
			scan.nextLine();
			switch(option) {
			case 1:
				removeOnlineAccount();
			case 2:
				changePassword();
			case 3:
				mainMenu();
			case 4:
				logout();
			default:
				System.out.println("Invalid input. Try again.");
				System.out.println();
				onlineAccount();
			}
		} catch (InputMismatchException e) {
			// TODO Auto-generated catch block
			ScanInputException s = new ScanInputException();
			System.out.println(s.getMessage());
			System.out.println("Logging out...");	
			logout();
		}
		
	}
	
	public void removeOnlineAccount() {
		List<CredentialPojo> allCredentials;
		try {
			allCredentials = userService.getAllCredentials();
			System.out.println("Enter 'yes' to confirm that you wish to close your online account.");
			String choice = scan.nextLine();
			if(choice.equals("yes") || choice.equals("YES") || choice.equals("Yes")) {
				for(CredentialPojo item: allCredentials) {
					if(item.getUser_id() == userId) {
						userService.removeOnlineAccount(item);
						userId = 0;
						logout();
					}
				}
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void changePassword() {
		CredentialPojo credentialPojo = new CredentialPojo();
		System.out.println("Change password:");
		System.out.println("Enter old password: ");
		credentialPojo.setUser_password(scan.nextLine());
		System.out.println("Enter mother's maiden name: ");
		credentialPojo.setUser_maiden(scan.nextLine());
		List<CredentialPojo> allCredentials;
		try {
			allCredentials = userService.getAllCredentials();
			for(CredentialPojo item: allCredentials) {
				if(item.getUser_id() == userId && item.getUser_password().equals(credentialPojo.getUser_password()) && item.getUser_maiden().equals(credentialPojo.getUser_maiden())) { 
					System.out.println("Enter new password:");
					credentialPojo.setUser_password(scan.nextLine());
					credentialPojo.setUser_id(userId);
					userService.changePassword(credentialPojo);
					System.out.println("Password successfully updated.");
					onlineAccount();
				} 
			}
			System.out.println("Invalid credentials");
			System.out.println("Returning to online account manager menu...");
			onlineAccount();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
	}
	
	public void manageMoney() {
		System.out.println("*****************************");
		System.out.println("MONEY MANAGEMENT");
		System.out.println("*****************************");
		System.out.println("1. View account balance");
		System.out.println("2. View transaction history");
		System.out.println("3. Deposit funds");
		System.out.println("4. Withdraw funds");
		System.out.println("5. Transfer funds");
		System.out.println("6. Return to main menu");
		System.out.println("7. Log out");
		System.out.println("*****************************");
		System.out.println("Please enter an option:");	
		try {
			int option = scan.nextInt();
			scan.nextLine();
			switch(option) {
			case 1:
				viewBalance();
			case 2:
				viewTransactions();
			case 3:
				depositFunds();
			case 4:
				withdrawFunds();
			case 5:
				transferFunds();
			case 6:
				mainMenu();
			case 7:
				logout();
			default:
				System.out.println("Invalid input. Try again.");
				System.out.println();
				manageMoney();
			}
		} catch (InputMismatchException e) {
			// TODO Auto-generated catch block
			ScanInputException s = new ScanInputException();
			System.out.println(s.getMessage());
			System.out.println("Logging out...");	
			logout();
		}
	}
	
	public void viewBalance() {
		CredentialPojo credPojo = new CredentialPojo();
		System.out.println("VIEW BALANCE:");
		System.out.println("Enter your PIN:");
		credPojo.setUser_id(userId);
		credPojo.setUser_pin(scan.nextInt());
		System.out.println("Enter account number:");
		AccountPojo accPojo = new AccountPojo();
		accPojo.setAccountNumber(scan.nextInt());
		try {
			AccountPojo retAccount = accountService.getAnAccount(credPojo, accPojo);
			double total = (retAccount.getAccountBalance() + retAccount.getPending());
			System.out.println("");
			System.out.println("ACCOUNT NO\tACCOUNT TYPE\tACCOUNT BALANCE");
			System.out.println("*****************************************");
			System.out.println(retAccount.getAccountNumber()+"\t\t"+retAccount.getAccountType()+"\t"
					+total+"(of which pending="+retAccount.getPending()+")");
			System.out.println();
			manageMoney();
		} catch (EmptyListException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());;
			viewBalance();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			//System.out.println(e.getMessage());
			System.out.println(e.getMessage());
			viewBalance();
		}
	}
	
	public void viewTransactions() {
		int accno = 0;
		List<TransactionPojo> allTrans = null;
		System.out.println("VIEW TRANSACTIONS: You may view transactions initiated by you or view\n"
				+ "all transactions for a specific account.");
		System.out.println("Choose which transactions to view:");
		System.out.println("1. View all of  your transactions.");
		System.out.println("2. View all transactions for a specific account.");
		int choice = scan.nextInt();
		scan.nextLine();
		try {
		switch (choice) {
		case 1:
			allTrans = transactionService.viewTransactionsByUser(userId);
			break;
		case 2:
			System.out.println("Enter the account number whose transactions you wish to view:");
			accno = scan.nextInt();
			scan.nextLine();
			allTrans = transactionService.viewTransactionsByAccount(accno, userId);
			break;
		default:
			System.out.println("This is not an option.");
			System.out.println();
			viewTransactions();
		}
		System.out.println("tran_no"+"\t\t"+"acc_no"+"\t"+"time"+"\t\t\t"+"trans_type"+"\t"+"trans_amt"+"\t"+"updated_bal");
		System.out.println("*****************************************************************************************");
		allTrans.forEach((item) -> System.out.println(item.getTransactionNumber() + "\t\t" + 
				item.getAccountNumber()+"\t"+item.getTime()+"\t"+item.getTransactionType()+"\t"+item.getTransactionAmount()+"\t"
				+item.getUpdatedBalance()));
		System.out.println("To return to money management, enter 'a'.");
		System.out.println("To return to the main menu, enter 'm'.");
		System.out.println("To log out, enter 'l'.");
		String choice2 = scan.nextLine();
		switch(choice2) {
		case "a":
			manageMoney();
		case "m":
			mainMenu();
		case "l":
			logout();
		default:
			System.out.println("invalid entry: returning to money management");
			viewTransactions();
		}
	} catch (EmptyListException e) {
		System.out.println(e.getMessage());
		manageMoney();
	} catch (SystemException e) {
		//System.out.println(e.getMessage());
		System.out.println(e.getMessage());
		manageMoney();
	}
}
	
	public void depositFunds() {
		TypeMismatchException typeMismatchException = new TypeMismatchException();
		System.out.println("DEPOSIT FUNDS");
		System.out.println("Note that electronically deposited funds are marked as pending"
				+ " and \nwill not be available for withdrawal until actual funds are received."
				+ " Electronically deposited \nfunds will also not count as funds when determining"
				+ " under-balance penalties until actual funds are received.");
		System.out.println();
		TransactionPojo transactionPojo = new TransactionPojo();
		CredentialPojo credentialPojo = new CredentialPojo();
		AccountPojo accountPojo = new AccountPojo();
		credentialPojo.setUser_id(userId);
		try {
			System.out.println("Enter your PIN:");
			credentialPojo.setUser_pin(scan.nextInt());
			scan.nextLine();
		} catch (InputMismatchException e) {
			MismatchException m = new MismatchException();
			System.out.println(m.getMessage());
			System.out.println();
			scan.nextLine();
			depositFunds();
		}
		try {
			System.out.println("Enter the account number you are depositing funds to:");
			accountPojo.setAccountNumber(scan.nextInt());
			scan.nextLine();
		} catch (InputMismatchException e) {
			System.out.println(typeMismatchException.getMessage());
			System.out.println();
			scan.nextLine();
			depositFunds();
		}
		AccountPojo retAcct = null;
		TransactionPojo retTranPojo = null;
		try {
			retAcct = accountService.getAnAccount(credentialPojo, accountPojo);
			System.out.println("Enter the amount you are depositing:");
			try {
				transactionPojo.setTransactionAmount(scan.nextDouble());
				transactionPojo.setAccountNumber(retAcct.getAccountNumber());
				transactionPojo.setTime(LocalDateTime.now().toString());
				transactionPojo.setTransactionType("deposit");
				double upBal = (retAcct.getAccountBalance() + transactionPojo.getTransactionAmount());
				transactionPojo.setUpdatedBalance(upBal);
				transactionPojo.setUserId(userId);
				retTranPojo = transactionService.makeDeposit(transactionPojo);
				System.out.println("Transaction completed:");
				System.out.println("TrNo\tTrType\tAccNo\tAccType\t\tTrAmt\tTrTime\t\t\tCurrBal");
				System.out.println("**************************************************************************************************");
				try {
					System.out.println(retTranPojo.getTransactionNumber()+"\t"+retTranPojo.getTransactionType()+"\t"+
							retTranPojo.getAccountNumber()+"\t"+retAcct.getAccountType()+"\t"+retTranPojo.getTransactionAmount()+"\t"+
							retTranPojo.getTime()+"\t"+retTranPojo.getUpdatedBalance());
					manageMoney();
				} catch (NullPointerException n) {
					EmptyListException e = new EmptyListException();
					System.out.println(e.getMessage());
					scan.nextLine();
					depositFunds();
				}
			} catch (InputMismatchException s) {
				System.out.println(typeMismatchException.getMessage());
				System.out.println();
				scan.nextLine();
				depositFunds();
			}
		
		} catch (EmptyListException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			depositFunds();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			depositFunds();
		}
	}
	
	public void withdrawFunds() {
		TypeMismatchException typeMismatchException = new TypeMismatchException();
		System.out.println("WITHDRAW FUNDS");
		System.out.println("Note that withrdawals that would bring your balance below a balance\n"
				+ "of $0.00 are not allowed. Also, any checking account whose balance falls below $100\n"
				+ "will be assessed a $25 fine. Any savings account whose balance falls below $200\n"
				+ "will be assessed a $25 fine.");
		System.out.println();
		TransactionPojo transactionPojo = new TransactionPojo();
		CredentialPojo credentialPojo = new CredentialPojo();
		AccountPojo accountPojo = new AccountPojo();
		credentialPojo.setUser_id(userId);
		try {
			System.out.println("Enter your PIN:");
			credentialPojo.setUser_pin(scan.nextInt());
			scan.nextLine();
		} catch (InputMismatchException e) {
			MismatchException m = new MismatchException();
			System.out.println(m.getMessage());
			System.out.println();
			scan.nextLine();
			withdrawFunds();
		}
		try {
			System.out.println("Enter the account number you are withdrawing funds from:");
			accountPojo.setAccountNumber(scan.nextInt());
			scan.nextLine();
		} catch (InputMismatchException e) {
			System.out.println(typeMismatchException.getMessage());
			System.out.println();
			scan.nextLine();
			withdrawFunds();
		} 
		AccountPojo retAcct = null;
		TransactionPojo retTranPojo = null;
		try {
			retAcct = accountService.getAnAccount(credentialPojo, accountPojo);
			System.out.println("Enter the amount you are withdrawing:");
			try {
				transactionPojo.setTransactionAmount(scan.nextDouble());
			} catch (InputMismatchException s) {
				System.out.println(typeMismatchException.getMessage());
				System.out.println();
				scan.nextLine();
				withdrawFunds();
			}
			transactionPojo.setAccountNumber(retAcct.getAccountNumber());
			transactionPojo.setTime(LocalDateTime.now().toString());
			transactionPojo.setTransactionType("withdrawal");
			double upBal = (retAcct.getAccountBalance() - transactionPojo.getTransactionAmount());
			transactionPojo.setUpdatedBalance(upBal);
			transactionPojo.setUserId(userId);
			try {
				retTranPojo = transactionService.makeWithdrawal(transactionPojo);
				System.out.println("Transaction completed:");
				System.out.println("TrNo\tTrType\tAccNo\tAccType\t\tTrAmt\tTrTime\t\t\tCurrBal");
				System.out.println("**************************************************************************************************");
				try {
					
					System.out.println(retTranPojo.getTransactionNumber()+"\t"+retTranPojo.getTransactionType()+"\t"+
							retTranPojo.getAccountNumber()+"\t"+retAcct.getAccountType()+"\t"+retTranPojo.getTransactionAmount()+"\t"+
							retTranPojo.getTime()+"\t"+retTranPojo.getUpdatedBalance());
				} catch (NullPointerException n) {
					EmptyListException e = new EmptyListException();
					System.out.println(e.getMessage());
					scan.nextLine();
					withdrawFunds();
				}
			} catch (BalanceBelowZeroException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				manageMoney();
			}
		} catch (EmptyListException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			withdrawFunds();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			withdrawFunds();
		}
	}
	
	public void transferFunds() {
		TransactionPojo depositTransactionPojo = new TransactionPojo();
		TransactionPojo withdrawalTransactionPojo = new TransactionPojo();
		CredentialPojo credentialPojo = new CredentialPojo();
		AccountPojo depositAccountPojo = new AccountPojo();
		AccountPojo withdrawalAccountPojo = new AccountPojo();
		credentialPojo.setUser_id(userId);
		AccountPojo retAcct = null;
		TransactionPojo retWithTranPojo = null;
		TransactionPojo retDepTranPojo = null;
		TypeMismatchException typeMismatchException = new TypeMismatchException();
		
		System.out.println("TRANSFER FUNDS");
		System.out.println("You may transfer funds to another account owned by you or to any account held\n"
				+ "by this bank. For both types of transfer, just enter the account number of the account to \n"
				+ "which you are transfering the funds. The transfer will appear as 'transferwithdrawal<accountnumber>'\n"
				+ "in your transaction history (where <accountnumber> is the account to which you are transfering funds.");
		System.out.println();
		
		try {
			System.out.println("Enter your PIN:");
			credentialPojo.setUser_pin(scan.nextInt());
			scan.nextLine();
		} catch (InputMismatchException e) {
			MismatchException m = new MismatchException();
			System.out.println(m.getMessage());
			System.out.println();
			scan.nextLine();
			transferFunds();
		}
		try {
			System.out.println("Enter the account number you are transfering funds from:");
			withdrawalAccountPojo.setAccountNumber(scan.nextInt());
			scan.nextLine();
		} catch (InputMismatchException e) {
			System.out.println(typeMismatchException.getMessage());
			System.out.println();
			scan.nextLine();
			transferFunds();
		}
		try {
			System.out.println("Enter the account number you are transfering funds to:");
			depositAccountPojo.setAccountNumber(scan.nextInt());
			scan.nextLine();
		} catch (InputMismatchException e) {
			System.out.println(typeMismatchException.getMessage());
			System.out.println();
			scan.nextLine();
			transferFunds();
		}
	
		try {
			retAcct = accountService.getAnAccount(credentialPojo, withdrawalAccountPojo);
			double retAcct2 = accountService.getBalance(depositAccountPojo.getAccountNumber());
			System.out.println("Enter the amount you are transfering:");
			try {
				double tranAmt = scan.nextDouble();
				depositTransactionPojo.setTransactionAmount(tranAmt);
				withdrawalTransactionPojo.setTransactionAmount(tranAmt);
				depositTransactionPojo.setAccountNumber(depositAccountPojo.getAccountNumber());
				withdrawalTransactionPojo.setAccountNumber(retAcct.getAccountNumber());
				String time = LocalDateTime.now().toString();
				depositTransactionPojo.setTime(time);
				withdrawalTransactionPojo.setTime(time);
				depositTransactionPojo.setTransactionType("transferdepositfrom"+retAcct.getAccountNumber());
				withdrawalTransactionPojo.setTransactionType("transferwithdrawalto"+depositTransactionPojo.getAccountNumber());
				double withUpBal = (retAcct.getAccountBalance() + withdrawalTransactionPojo.getTransactionAmount());
				double depUpBal = (retAcct2 + depositTransactionPojo.getTransactionAmount());
				depositTransactionPojo.setUpdatedBalance(depUpBal);
				withdrawalTransactionPojo.setUpdatedBalance(withUpBal);
				depositTransactionPojo.setUserId(userId);
				withdrawalTransactionPojo.setUserId(userId);
				retDepTranPojo = transactionService.makeDeposit(depositTransactionPojo);
				try {
					retWithTranPojo = transactionService.makeWithdrawal(withdrawalTransactionPojo);
				} catch (BalanceBelowZeroException b) {
					// TODO Auto-generated catch block
					System.out.println(b.getMessage());
					transferFunds();
				}
				System.out.println("Transaction completed:");
				System.out.println("TrNo\tTrType\tAccNo\tAccType\t\tTrAmt\tTrTime\t\t\tCurrBal");
				System.out.println("**************************************************************************************************");
				try {
					System.out.println(retWithTranPojo.getTransactionNumber()+"\t"+retWithTranPojo.getTransactionType()+"\t"+
							retWithTranPojo.getAccountNumber()+"\t"+retAcct.getAccountType()+"\t"+retWithTranPojo.getTransactionAmount()+"\t"+
							retWithTranPojo.getTime()+"\t"+retWithTranPojo.getUpdatedBalance());
					manageMoney();
				} catch (NullPointerException n) {
					EmptyListException e = new EmptyListException();
					System.out.println(e.getMessage());
					scan.nextLine();
					transferFunds();
				}
			} catch (InputMismatchException s) {
				System.out.println(typeMismatchException.getMessage());
				System.out.println();
				scan.nextLine();
				transferFunds();
			}
		
		} catch (EmptyListException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			transferFunds();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			transferFunds();
		}
	}
	
	public void accountManage() {
		System.out.println("*****************************");
		System.out.println("BANK ACCOUNT MANAGEMENT");
		System.out.println("*****************************");
		System.out.println("1. View all accounts");
		System.out.println("2. Link to existing account");
		System.out.println("3. Return to main menu");
		System.out.println("4. Log out");
		System.out.println("*****************************");
		System.out.println("Please enter an option:");
		try {
			int option = scan.nextInt();
			scan.nextLine();
			switch(option) {
			case 1:
				viewaccounts();
			case 2:
				linkaccount();
			case 3:
				mainMenu();
			case 4:
				logout();
			default:
				System.out.println("Invalid input. Try again.");
				System.out.println();
				accountManage();
			}
		} catch (InputMismatchException e) {
			// TODO Auto-generated catch block
			ScanInputException s = new ScanInputException();
			System.out.println(s.getMessage());
			System.out.println();	
			mainMenu();
		}
	}
	
	public void viewaccounts() {
		System.out.println("Your accounts:");
		System.out.println("ACCOUNTNUM\tACCOUNTTYP\tACCOUNTBAL\tPENDING");
		System.out.println("********************************************************");
		try {
			CredentialPojo credPojo = new CredentialPojo();
			credPojo.setUser_id(userId);
			List<AccountPojo>everyAccount = accountService.getUserAccounts(credPojo);
			everyAccount.forEach((item) -> System.out.println(item.getAccountNumber() + "\t\t" + item.getAccountType()
					+ "\t\t" + item.getAccountBalance()+"\t\t"+item.getPending()));
			System.out.println("To return to account management, enter 'a'.");
			System.out.println("To return to the main menu, enter 'm'.");
			System.out.println("To log out, enter 'l'.");
			String choice = scan.nextLine();
			switch(choice) {
			case "a":
				accountManage();
			case "m":
				mainMenu();
			case "l":
				logout();
			default:
				System.out.println("invalid entry: returning to account management");
				accountManage();
			}
		} catch (EmptyListException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SystemException e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void linkaccount() {
		AccountPojo newAccountPojo = new AccountPojo();
		System.out.println("Link to existing account:");
		System.out.println("Enter account number of account you wish to link to:");
		newAccountPojo.setAccountNumber(scan.nextInt());
		scan.nextLine();
		System.out.println("Enter access code of account you wish to link to:");
		newAccountPojo.setAccessCode(scan.nextInt());
		scan.nextLine();
		UserPojo newUserPojo = new UserPojo();
		newUserPojo.setUser_id(userId);
		try {
			boolean success = false;
			try {
				success = accountService.linkToAccount(newUserPojo, newAccountPojo);
			} catch (RecordAlreadyPresentException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			if(success) {
				System.out.println("You are now linked to this account.");
				accountManage();
			}
			else {
				System.out.println("Attempt unsuccessful.");
				accountManage();
			}
			
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void logout() {
		System.out.println("Good-bye!");
		SessionPojo newSessionPojo = new SessionPojo();
		newSessionPojo.setSessionNumber(sessionId);
		newSessionPojo.setLogoutTime(LocalDateTime.now().toString());
		try {
			sessionService.updateSession(newSessionPojo);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		System.exit(0);
	}
	
}


