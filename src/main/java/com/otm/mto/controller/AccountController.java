package com.otm.mto.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.otm.mto.model.Account;
import com.otm.mto.model.Bank;

public class AccountController {
	static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pgsql");
	static EntityManager entityManager = entityManagerFactory.createEntityManager();
	static EntityTransaction entityTransaction = entityManager.getTransaction();
	//JPQL to get data from database
	public List<Account> getAccountlist(){
		try {
			entityTransaction.begin();
			String jpql = "SELECT a FROM Account a";
			TypedQuery<Account> query = entityManager.createQuery(jpql, Account.class);
			List<Account> accountList = query.getResultList();
			entityTransaction.commit();
			return accountList;
		}
	    catch(Exception e){
	    	entityTransaction.rollback();
	    	System.out.println("ERROR : Could not fetch Account");
	    }
		return null;
	}
	//find account using primary key
	public Account findAccount(int acc_id) {
		return entityManager.find(Account.class, acc_id);
	}
	//add bank and account
	public boolean addAccount(Bank bank, List<Account> accounts) {
		if (bank!=null && accounts!=null) {
			entityTransaction.begin();
			for (Account account : accounts) {
				entityManager.persist(account);
			}
			entityManager.merge(bank);
			entityTransaction.commit();
			return true;
		}
		return false;
	}
	//update account data
	public boolean updateAccount(int acc_id,int updateChoice,Object accNameToUpdate,Object accountBalanceToUpdate, List<Account> accounts) {
		Account accountToUpdate = entityManager.find(Account.class, acc_id);
		switch(updateChoice) {
		case 1 :
			if (accountToUpdate!=null) {
				accountToUpdate.setAccount_holder_name(String.valueOf(accNameToUpdate));
				accounts.add(accountToUpdate);
				entityTransaction.begin();
				entityManager.merge(accountToUpdate);
				entityTransaction.commit();
				return true;
			}
			break;
		case 2 :
			if (accountToUpdate!=null) {
				accountToUpdate.setBalance((Double)accountBalanceToUpdate);
				accounts.add(accountToUpdate);
				entityTransaction.begin();
				entityManager.merge(accountToUpdate);
				entityTransaction.commit();
				return true;
			}
			break;
		case 3 :
			if (accountToUpdate!=null) {
				accountToUpdate.setAccount_holder_name(String.valueOf(accNameToUpdate));
				accountToUpdate.setBalance((Double)accountBalanceToUpdate);
				entityTransaction.begin();
				entityManager.merge(accountToUpdate);
				entityTransaction.commit();
				return true;
			}
			break;
		}
		return false;
	}
	//delete particular account
	public boolean deleteParticularAccount(int bankToFind, int accountIdToDelete) {
		Bank bank = entityManager.find(Bank.class, bankToFind);
		if (bank!=null) {
			List<Account> accounts = bank.getAccounts();
			if (accounts!=null) {
				Account accountToRemove = null;
				for (Account account : accounts) {
					if (account.getAccount_id()==accountIdToDelete) {
						accountToRemove = account;
						break;
					}
				}
				if (accountToRemove!=null) {
					entityTransaction.begin();
					accounts.remove(accountToRemove);
					entityTransaction.commit();
					entityTransaction.begin();
					entityManager.remove(accountToRemove);
					entityTransaction.commit();
					return true;
				}
				//Account with given Id does not exists
				return false;
			}
			//Accounts does not exists in bank
			return false;
		}
		//Bank with given Id does not exists
		return false;
	}
	
}
