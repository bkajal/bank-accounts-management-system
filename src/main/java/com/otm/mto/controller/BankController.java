package com.otm.mto.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.otm.mto.model.Account;
import com.otm.mto.model.Bank;

public class BankController {
	static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pgsql");
	static EntityManager entityManager = entityManagerFactory.createEntityManager();
	static EntityTransaction entityTransaction = entityManager.getTransaction();
	//JPQL to get data from database
	public List<Bank> getBankList(){
		try {
			entityTransaction.begin();
			String jpql = "SELECT b FROM Bank b";
			TypedQuery<Bank> query = entityManager.createQuery(jpql, Bank.class);
			List<Bank> bankList = query.getResultList();
			entityTransaction.commit();
			return bankList;
		} catch (Exception e) {
			entityTransaction.rollback();
			System.out.println("ERROR : Could not fetch Bank");
		}
		return null;
	}
	//add bank and account
	public boolean addBank(Bank bank, List<Account> accounts) {
		if (bank!=null && accounts!=null) {
			entityTransaction.begin();
			entityManager.persist(bank);
			for (Account account : accounts) {
				entityManager.persist(account);
			}
			entityTransaction.commit();
			return true;
		}
		return false;
	}
	//find bank
	public Bank findBank(int bank_id) {
		return entityManager.find(Bank.class, bank_id);
	}
	//remove bank
	public boolean removeBank(int bank_id) {
		Bank bank = entityManager.find(Bank.class, bank_id);
		if(bank!=null) {
			List<Account> accounts = bank.getAccounts();
			entityTransaction.begin();
			for (Account account : accounts) {
				entityManager.remove(account);
			}
			entityManager.remove(bank);
			entityTransaction.commit();
			return true;
		}
		return false;
	}
}
