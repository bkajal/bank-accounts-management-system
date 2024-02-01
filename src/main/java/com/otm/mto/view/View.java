package com.otm.mto.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.hibernate.exception.ConstraintViolationException;

import com.otm.mto.controller.AccountController;
import com.otm.mto.controller.BankController;
import com.otm.mto.model.Account;
import com.otm.mto.model.Bank;



public class View {
	static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pgsql");
	static EntityManager entityManager = entityManagerFactory.createEntityManager();
	static EntityTransaction entityTransaction = entityManager.getTransaction();
	static BankController bank_Controller = new BankController();
	static AccountController acc_Controller = new AccountController();
	static Scanner myInput = new Scanner(System.in);
	static Bank bank = new Bank();
	static Account account = new Account();
	static List<Account> accounts = new ArrayList();
	static List<Account> accountList = acc_Controller.getAccountlist();
	static List<Bank> bankList = bank_Controller.getBankList();
	static {
		System.out.println("   ----------------------------- ");
		System.out.println("  |  --- $WELCOME TO BANK$ ---  |");
		System.out.println("   ----------------------------- ");
	}
	public static void main(String[] args) {
	try {
	do {
		System.out.println("\n-------------------------------------- ");
		System.out.println("==> Select Operation to Perform : ");
		System.out.println("-->1.Add Bank and Account\n-->2.Add More Accounts to Existing Bank\n-->3.Get Bank Details\n-->4.Get Account Details\n-->5.Update Account Details\n-->6.Remove Bank\n-->7.Delete Account\n-->0.Exit");
		System.out.print("\n==>Enter digit respective to desired option : ");
		int userchoice = myInput.nextInt();
		myInput.nextLine();
		switch(userchoice) {
		case 0 :
			myInput.close();
			System.out.println(" -------------- ");
			System.out.println("| ---EXITED--- |");
			System.out.println(" -------------- ");
			System.exit(0);
			break;
		case 1 :
			//Add Bank and Account
			//Bank Details
			bank = new Bank();
			System.out.print("Enter Bank Name : ");
			bank.setName(myInput.nextLine());
			System.out.print("Enter Bank Headquarters : ");
			bank.setHeadQuarters(myInput.nextLine());
			
			boolean addMoreAccounts;
			do {
				account = new Account();
				//Account Details
				System.out.print("Enter Account Holder Name : ");
				account.setAccount_holder_name(myInput.nextLine());
				System.out.print("Enter Account Balance : ");
				account.setBalance(myInput.nextDouble());
				myInput.nextLine();
				//Set Mapping bank to account
				account.setBank(bank);
				//Update account list
				accounts.add(account);
				System.out.print("==>Do you want to add more accounts ? \n 1.YES 0.NO : ");
				byte addAccount = myInput.nextByte();
				myInput.nextLine();
				if(addAccount==1) {
					addMoreAccounts = true;
				}
				else {
					addMoreAccounts = false;
				}
				
			}while(addMoreAccounts);
			//Set Mapping Accounts to Bank
			bank.setAccounts(accounts);
			
			//Verification of adding Accounts
			if (bank_Controller.addBank(bank, accounts)) {
				System.out.println(" ----------------------------------------------- ");
				System.out.println("|   --- Bank & Associated Account Created ---   |");
				System.out.println(" ----------------------------------------------- ");
			} else {
				System.out.println(" --------------------------------- ");
				System.out.println("|  --- Unable to Insert Data ---  |");
				System.out.println(" --------------------------------- ");
			}
			
			break;
		case 2 :
			//Add Many Accounts to Existing Bank
			 //jpql to get bank from database
			if (bankList!=null) {
				for (Bank bank : bankList) {
						System.out.println("Bank ID : "+bank.getBank_id()+" BANK : "+bank.getName());
				}				
			} else {
				System.out.println(" --------------------------- ");
				System.out.println("|  --No Data in Database--  |");
				System.out.println(" --------------------------- ");
			}
			 //Find existing bank
			System.out.print("Enter Bank Id : ");
			Bank findBank = bank_Controller.findBank(myInput.nextInt());
			myInput.nextLine();
			if (findBank!=null) {
				boolean addManyAccounts;
				do {
					account = new Account();
					//Account Details
					System.out.print("Enter Account Holder Name : ");
					account.setAccount_holder_name(myInput.nextLine());
					System.out.print("Enter Account Balance : ");
					account.setBalance(myInput.nextDouble());
					myInput.nextLine();
					//Mapping bank to account
					account.setBank(findBank);
					//Update account list
					accounts.add(account);
					System.out.print("Do you want to add more accounts ? \n 1.YES 0.NO : ");
					byte addAccountToBank = myInput.nextByte();
					myInput.nextLine();
					if(addAccountToBank==1) {
						addManyAccounts = true;
					}
					else {
						addManyAccounts = false;
					}
					
				}while(addManyAccounts);
				//Set Accounts to found Bank
				findBank.setAccounts(accounts);
				
				//Verification
				if (acc_Controller.addAccount(findBank, accounts)) {
					System.out.println(" ----------------------------- ");
					System.out.println("|  --Account Added to Bank--  |");
					System.out.println(" ----------------------------- ");
				} else {
					System.out.println(" --------------------------------- ");
					System.out.println("|   --No More Accounts to Add--   |");
					System.out.println(" --------------------------------- ");
				}
			} else {
				System.out.println(" -------------------------- ");
				System.out.println("|    --Bank Not Found--    |");
				System.out.println(" -------------------------- ");
			}
			break;
		case 3 :
			//Fetch Bank
			//jpql to get bank from database
			if (bankList!=null) {
				for (Bank bank : bankList) {
						System.out.println("Bank ID : "+bank.getBank_id()+" BANK : "+bank.getName());
				}				
			} else {
				System.out.println(" --------------------------- ");
				System.out.println("|  --No Data in Database--  |");
				System.out.println(" --------------------------- ");
			}
			//find bank
			System.out.print("Enter Bank Id : ");
			Bank bank = bank_Controller.findBank(myInput.nextInt());
			myInput.nextLine();
			if (bank!=null) {
				System.out.println("   -------------------------- ");
				System.out.println("  |     --Bank Details--     |");
				System.out.println("  |    Bank ID : "+bank.getBank_id());
				System.out.println("  |    Bank Name : "+bank.getName());
				System.out.println("  |    Bank headQuarters : "+bank.getHeadQuarters());
				System.out.println("   -------------------------- ");
				accounts = bank.getAccounts();
				for (Account account : accounts) {
					System.out.println("   ------------------------------ ");
					System.out.println("  |    --Associated Accounts--   |");
					System.out.println("  |     Account ID : "+account.getAccount_id());
					System.out.println("  |     Account Holder Name : "+account.getAccount_holder_name());
					System.out.println("  |     Account Balance : "+account.getBalance());
					System.out.println("   ------------------------------ ");
				}
			}
			else {
				System.out.println(" --------------------------------------- ");
				System.out.println("|   --No Records found with given Id--  |");
				System.out.println(" --------------------------------------- ");
			}
			break;
		case 4 :
			//Fetch Account
			//jpql to get account from database
			if (accountList!=null) {
				for (Account account : accountList) {
					System.out.println("ID : "+account.getAccount_id()+" NAME : "+account.getAccount_holder_name());
				}
			} else {
				System.out.println(" --------------------------- ");
				System.out.println("|  --No Data in Database--  |");
				System.out.println(" --------------------------- ");
			}
			
			//find account
			System.out.print("Enter Account Id : ");
		    Account findAccount = acc_Controller.findAccount(myInput.nextInt());
			myInput.nextLine();
			if (findAccount!=null) {
				System.out.println("   --------------------------------------- ");
				System.out.println("  |           --Account Details--         |");
				System.out.println("  |   Account ID : "+findAccount.getAccount_id());
				System.out.println("  |   Account Holder Name : "+findAccount.getAccount_holder_name());
				System.out.println("  |   Account Balance : "+findAccount.getBalance());
				System.out.println("   --------------------------------------- ");
				//account belongs to bank
				Bank accInBank = findAccount.getBank();
				System.out.println("   --------------------------------------- ");
				System.out.println("  |          -- Associated Bank --        |");
				System.out.println("  |    Bank ID : "+accInBank.getBank_id());
				System.out.println("  |    Bank Name : "+accInBank.getName());
				System.out.println("  |    Bank headQuarters : "+accInBank.getHeadQuarters());	
				System.out.println("   --------------------------------------- ");
			}
			else {
				System.out.println(" -------------------------------------- ");
				System.out.println("|  --No Records found with given Id--  |");
				System.out.println(" -------------------------------------- ");
			}
			break;
		case 5 :
			//Update Account Details
			//jpql to get account from database
			if (accountList!=null) {
				for (Account account : accountList) {
					System.out.println("ID : "+account.getAccount_id()+" NAME : "+account.getAccount_holder_name());
				}
			} else {
				System.out.println(" --------------------------- ");
				System.out.println("|  --No Data in Database--  |");
				System.out.println(" --------------------------- ");
			}
			//find account
			System.out.print("Enter Account Id : ");
			int accountId = myInput.nextInt();
			myInput.nextLine();
			System.out.println("==>What do you need to update? ");
			System.out.println("-->1.Update Account Name\n-->2.Update Account Balance\n3.-->Update Account Name and Balance");
			System.out.print("Select Digit to Update Data : ");
			int updateChoice = myInput.nextInt();
			myInput.nextLine();
			Object accNameToUpdate=null;
			Object accountBalanceToUpdate = 0.0;
			switch(updateChoice) {
			case 1 :
				//Update Account Name
				System.out.print("Enter Account Name to Update : ");
				accNameToUpdate = myInput.nextLine();
				break;
			case 2 :
				//Update Account Balance
				System.out.print("Enter Account Balance to Update : ");
				Object accBalanceToUpdate = myInput.nextDouble();
				myInput.nextLine();
				break;
			case 3 :
				//Update Account Name and Balance
				System.out.print("Enter Account Name to Update : ");
				Object accountNameToUpdate = myInput.nextLine();
				System.out.print("Enter Account Balance to Update : ");
				accountBalanceToUpdate = myInput.nextDouble();
				myInput.nextLine();
				break;
			default :
				
				System.out.println(" -------------------------- ");
				System.out.println("|  --Enter Valid Choice--  |");
				System.out.println(" -------------------------- ");
			}
			//Verification
			if(acc_Controller.updateAccount(accountId, updateChoice, accNameToUpdate, accountBalanceToUpdate, accounts)) {
				System.out.println(" ----------------------------- ");
				System.out.println("|   --Account Data Updated--  | ");
				System.out.println(" ----------------------------- ");
			}
			else {
				System.out.println(" --------------------------------------- ");
				System.out.println("|   --No Data present with Given Id--   |");
				System.out.println(" --------------------------------------- ");
			}
			break;
		case 6 :
			//Remove Bank
			//jpql to get bank from database
			if (bankList!=null) {
				for (Bank Bank : bankList) {
						System.out.println("Bank ID : "+Bank.getBank_id()+" BANK : "+Bank.getName());
				}				
			} else {
				System.out.println(" --------------------------- ");
				System.out.println("|  --No Data in Database--  |");
				System.out.println(" --------------------------- ");
			}
			//find bank to remove
			System.out.print("Enter Bank Id : ");
			int bankIdToRemove = myInput.nextInt();
			myInput.nextLine();
			//Verification
			if(bank_Controller.removeBank(bankIdToRemove)) {
				System.out.println(" ----------------------------------------------------- ");
				System.out.println("|  --Bank and Accounts associated has been Removed--  |");
				System.out.println(" ----------------------------------------------------- ");
			}
			else {
				System.out.println(" --------------------------------------- ");
				System.out.println("|   --No Data present with Given Id--   |");
				System.out.println(" --------------------------------------- ");
			}
			break;
		case 7 :
			//Remove Account
			//jpql to get bank from database
			if (bankList!=null) {
				for (Bank Bank : bankList) {
						System.out.println("Bank ID : "+Bank.getBank_id()+" BANK : "+Bank.getName());
				}				
			} else {
				System.out.println(" --------------------------- ");
				System.out.println("|  --No Data in Database--  |");
				System.out.println(" --------------------------- ");
			}
			System.out.print("Enter Bank Id : ");
			int bankIdToFind = myInput.nextInt();
			myInput.nextLine();
			//jpql to get account from database
			if (accountList!=null) {
				for (Account account : accountList) {
					System.out.println("ID : "+account.getAccount_id()+" NAME : "+account.getAccount_holder_name());
				}
			} else {
				System.out.println(" --------------------------- ");
				System.out.println("|  --No Data in Database--  |");
				System.out.println(" --------------------------- ");
			}
			System.out.println("--!PLEASE ENTER ACCOUNT ASSOCIATED WITH ABOVE BANK!--");
			//find account to delete
			System.out.print("Enter Account Id : ");
			int accountIdToDelete = myInput.nextInt();
			myInput.nextLine();
			//Verification
			if (acc_Controller.deleteParticularAccount(bankIdToFind, accountIdToDelete)) {
				System.out.println(" -------------------------------------------------------- ");
				System.out.println("|   --Account "+accountIdToDelete+" has been Removed--   |");
				System.out.println(" -------------------------------------------------------- ");
			} else {
				System.out.println(" ------------------------------------------ ");
				System.out.println("|  --DATA with Given Id does not exists--  |");
				System.out.println(" ------------------------------------------ ");
			}
			break;
		default :
			System.out.println(" --------------------------------- ");
			System.out.println("|  --PLEASE ENTER VALID CHOICE--  |");
			System.out.println(" --------------------------------- ");
			break;
		}
	}
	while(true);
	}
	catch(InputMismatchException e) {
		System.out.println("ERROR : --ENTER VALID DATA VALUE--");
	}
	catch (ConstraintViolationException e) {
		System.out.println("ERROR: --(Primary key) ID reapeated--");
	}
	catch (Exception e) {
		System.out.println("ERROR: --SOMETHING WENT ERONG--\n --ENTER VALID DATA--");
	}
	}
}

