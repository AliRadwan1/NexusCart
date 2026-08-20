package com.nexus_cart.microservices.walet_microservice.wallets;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus_cart.microservices.walet_microservice.exceptions.InsufficientBalanceException;
import com.nexus_cart.microservices.walet_microservice.exceptions.InvalidAmountException;
import com.nexus_cart.microservices.walet_microservice.exceptions.WalletAlreadyExistsException;
import com.nexus_cart.microservices.walet_microservice.exceptions.WalletNotFoundException;
import com.nexus_cart.microservices.walet_microservice.transactions.Transaction;
import com.nexus_cart.microservices.walet_microservice.transactions.TransactionRepository;
import com.nexus_cart.microservices.walet_microservice.transactions.TransactionType;

@Service
public class WalletService {
	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	// Create Wallet
	public Wallet createWallet(String userId) {
		Optional<Wallet> wallet = walletRepository.findByUserId(userId);

		if (wallet.isPresent()) {
			throw new WalletAlreadyExistsException("User already has a wallet");
		}

		Wallet newWallet = new Wallet();

		newWallet.setUserId(userId);
		newWallet.setBalance(BigDecimal.ZERO);

		walletRepository.save(newWallet);

		return newWallet;
	}

	// Get Wallet By User ID
	public Wallet getWalletByUserId(String userId) {
		Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException(userId));

		return wallet;
	}

	// Deposit Funds
	@Transactional
	public Wallet deposit(String userId, BigDecimal amount) {
		Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException(userId));

		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Deposit amount must be greater than 0");
		}

		BigDecimal newBalance = wallet.getBalance().add(amount);
		wallet.setBalance(newBalance);

		walletRepository.save(wallet);

		// Record the transaction
		Transaction transaction = new Transaction();
		transaction.setWalletId(wallet.getId());
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setAmount(amount);
		transactionRepository.save(transaction);

		return wallet;
	}

	// Withdraw Funds
	@Transactional
	public Wallet withdraw(String userId, BigDecimal amount) {
		Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException(userId));

		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Withdrawal amount must be greater than 0");
		}

		if (amount.compareTo(wallet.getBalance()) > 0) {
			throw new InsufficientBalanceException("Amount of withdrawl exceeded the balance");
		}

		BigDecimal newBalance = wallet.getBalance().subtract(amount);
		wallet.setBalance(newBalance);

		walletRepository.save(wallet);

		// Record the Transaction
		Transaction transaction = new Transaction();
		transaction.setWalletId(wallet.getId());
		transaction.setType(TransactionType.WITHDRAWAL);
		transaction.setAmount(amount);
		transactionRepository.save(transaction);

		return wallet;
	}

	@Transactional
	public void transfer(String senderUserId, String receiverUserId, BigDecimal amount) {
		Wallet senderWallet = walletRepository.findByUserId(senderUserId)
				.orElseThrow(() -> new WalletNotFoundException(senderUserId));
		Wallet receiverWallet = walletRepository.findByUserId(receiverUserId)
				.orElseThrow(() -> new WalletNotFoundException(receiverUserId));

		if (senderUserId.equals(receiverUserId)) {
			throw new InvalidAmountException("Cannot transfer money to the same account");
		}

		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("transaction amount must be greater than 0");
		}

		if (amount.compareTo(senderWallet.getBalance()) > 0) {
			throw new InsufficientBalanceException("Amount of transaction exceeded the balance");
		}

		BigDecimal newSenderBalance = senderWallet.getBalance().subtract(amount);
		BigDecimal newReceiverBalance = receiverWallet.getBalance().add(amount);

		senderWallet.setBalance(newSenderBalance);
		receiverWallet.setBalance(newReceiverBalance);

		walletRepository.save(senderWallet);
		walletRepository.save(receiverWallet);

		// Record Transaction
		Transaction send = new Transaction();
		send.setWalletId(senderWallet.getId());
		send.setType(TransactionType.TRANSFER_OUT);
		send.setAmount(amount);
		transactionRepository.save(send);

		Transaction recieve = new Transaction();
		recieve.setWalletId(receiverWallet.getId());
		recieve.setType(TransactionType.TRANSFER_IN);
		recieve.setAmount(amount);
		transactionRepository.save(recieve);

	}
}
