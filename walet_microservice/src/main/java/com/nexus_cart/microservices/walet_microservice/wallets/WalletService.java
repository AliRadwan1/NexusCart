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

@Service
public class WalletService {
	@Autowired
	private WalletRepository walletRepository;

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

		return wallet;
	}
}
