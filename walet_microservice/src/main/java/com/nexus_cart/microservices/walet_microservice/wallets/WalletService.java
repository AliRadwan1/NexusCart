package com.nexus_cart.microservices.walet_microservice.wallets;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus_cart.microservices.walet_microservice.dto.WalletTransactionRequest;
import com.nexus_cart.microservices.walet_microservice.dto.WalletTransactionResponse;
import com.nexus_cart.microservices.walet_microservice.exceptions.InsufficientBalanceException;
import com.nexus_cart.microservices.walet_microservice.exceptions.InvalidAmountException;
import com.nexus_cart.microservices.walet_microservice.exceptions.WalletAlreadyExistsException;
import com.nexus_cart.microservices.walet_microservice.exceptions.WalletNotFoundException;
import com.nexus_cart.microservices.walet_microservice.transactions.Transaction;
import com.nexus_cart.microservices.walet_microservice.transactions.TransactionRepository;
import com.nexus_cart.microservices.walet_microservice.transactions.TransactionType;

/**
 * Service class responsible for wallet domain logic, including account creation,
 * balance queries, deposits, withdrawals, and peer-to-peer transfers.
 */
@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	/**
	 * Creates a new wallet initialized with a zero balance for a specified user.
	 *
	 * @param userId Unique identifier of the user account.
	 * @return The created {@link Wallet} entity.
	 * @throws WalletAlreadyExistsException If a wallet already exists for the given user ID.
	 */
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

	/**
	 * Retrieves the active wallet entity associated with a user ID.
	 *
	 * @param userId Unique identifier of the user.
	 * @return The {@link Wallet} entity.
	 * @throws WalletNotFoundException If no wallet record matches the specified user ID.
	 */
	public Wallet getWalletByUserId(String userId) {
		return walletRepository.findByUserId(userId)
				.orElseThrow(() -> new WalletNotFoundException(userId));
	}

	/**
	 * Deposits funds into a user's wallet balance and records an audit log entry.
	 *
	 * @param userId Unique identifier of the user account.
	 * @param amount Monetary value to credit.
	 * @return A {@link WalletTransactionResponse} containing the generated transaction ID, wallet ID, amount, and new balance.
	 * @throws WalletNotFoundException If the wallet is not found.
	 * @throws InvalidAmountException If amount is null or less than or equal to zero.
	 */
	@Transactional
	public WalletTransactionResponse deposit(WalletTransactionRequest request) {
		Wallet wallet = walletRepository.findByUserId(request.getUserId())
				.orElseThrow(() -> new WalletNotFoundException(request.getUserId()));

		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Deposit amount must be greater than 0");
		}

		BigDecimal newBalance = wallet.getBalance().add(request.getAmount());
		wallet.setBalance(newBalance);

		walletRepository.save(wallet);

		Transaction transaction = new Transaction();
		transaction.setWalletId(wallet.getId());
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setAmount(request.getAmount());
		Transaction savedTransaction = transactionRepository.save(transaction);

		return new WalletTransactionResponse(savedTransaction.getId(), wallet.getId(), request.getAmount(), newBalance);
	}

	/**
	 * Deducts funds from a user's wallet, records a withdrawal transaction log, and
	 * returns transaction details for external order processing.
	 *
	 * @param userId Unique identifier of the user account.
	 * @param amount Monetary value to withdraw.
	 * @return A {@link WalletTransactionResponse} containing the generated transaction ID, wallet ID, amount, and new balance.
	 * @throws WalletNotFoundException If the wallet is not found.
	 * @throws InvalidAmountException If amount is null or less than or equal to zero.
	 * @throws InsufficientBalanceException If the requested amount exceeds current balance.
	 */
	@Transactional
	public WalletTransactionResponse withdraw(WalletTransactionRequest request) {
		Wallet wallet = walletRepository.findByUserId(request.getUserId())
				.orElseThrow(() -> new WalletNotFoundException(request.getUserId()));

		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Withdrawal amount must be greater than 0");
		}

		if (request.getAmount().compareTo(wallet.getBalance()) > 0) {
			throw new InsufficientBalanceException("Amount of withdrawal exceeded the balance");
		}

		BigDecimal newBalance = wallet.getBalance().subtract(request.getAmount());
		wallet.setBalance(newBalance);

		walletRepository.save(wallet);

		Transaction transaction = new Transaction();
		transaction.setWalletId(wallet.getId());
		transaction.setType(TransactionType.WITHDRAWAL);
		transaction.setAmount(request.getAmount());
		Transaction savedTransaction = transactionRepository.save(transaction);

		return new WalletTransactionResponse(savedTransaction.getId(), wallet.getId(), request.getAmount(), newBalance);
	}

	/**
	 * Transfers funds from one user wallet to another, creating inverse audit records for each party.
	 *
	 * @param senderUserId Unique identifier of the source user account.
	 * @param receiverUserId Unique identifier of the destination user account.
	 * @param amount Monetary value to transfer.
	 * @throws WalletNotFoundException If either sender or receiver wallet does not exist.
	 * @throws InvalidAmountException If sender matches receiver or if amount is less than or equal to zero.
	 * @throws InsufficientBalanceException If sender balance is insufficient for the transaction amount.
	 */
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
			throw new InvalidAmountException("Transaction amount must be greater than 0");
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

	/**
	 * deletes the wallet of a certain user
	 * 
	 * @param userId
	 */
	public void deleteWallet(String userId) {
		walletRepository.deleteByUserId(userId);
	}
}