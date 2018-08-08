import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.security.Security;

public class Blockchain {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	// lit of all unspent transactions
	public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
	public static float minimumTransaction = 0.1f;

	// testing variables
	public static int difficulty = 5;
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;

	public static void main(String[] args) {
		testWalletAndSignature();
		// testAddingBlocksToBlockchain();
	}

	public static void testAddingBlocksToBlockchain() {
		// add blocks to blockchain
		blockchain.add(new Block("0"));
		System.out.println("Trying to Mine block 1... ");
		blockchain.get(0).mineBlock(difficulty);

		blockchain.add(new Block(blockchain.get(blockchain.size() - 1).hash));
		System.out.println("Trying to Mine block 2... ");
		blockchain.get(1).mineBlock(difficulty);

		blockchain.add(new Block(blockchain.get(blockchain.size() - 1).hash));
		System.out.println("Trying to Mine block 3... ");
		blockchain.get(2).mineBlock(difficulty);

		System.out.println("\nBlockchain is Valid: " + isChainValid());

		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println("\nThe block chain: ");
		System.out.println(blockchainJson);
	}

	public static void testWalletAndSignature() {
		// Setup Bouncy Castle as a security provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		// create the new wallets
		walletA = new Wallet();
		walletB = new Wallet();
		Wallet coinbase = new Wallet();

		// create genesis transaction, which will send 100 coins to walletA
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey); // manually sign the genesis transaction
		genesisTransaction.transactionId = "0"; // manually set the transaction id
		// manually add transaction outputs
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value,
				genesisTransaction.transactionId));
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); // it's important to store
																							// our first transaction in
																							// the UTXOs list.

		System.out.println("Creating and Mining Genesis Block...");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);

		// testing
		Block block1 = new Block(genesis.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block2 = new Block(block1.hash);
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block3 = new Block(block2.hash);
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		isChainValid();
		/*
		 * // Test public and private keys
		 * System.out.println("Private and pulic keys:");
		 * System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
		 * System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
		 * 
		 * // create a test transaction from WalletA to WalletB Transaction transaction
		 * = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
		 * transaction.generateSignature(walletA.privateKey);
		 * 
		 * // verify the signature works and verify it from the public key
		 * System.out.println("Is the signature verified?");
		 * System.out.println(transaction.verifySignature());
		 */
	}

	public static Boolean isChainValid() {
		Block curr;
		Block prev;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>();
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

		// loop through blockchain to check hashes
		for (int i = 1; i < blockchain.size(); i++) {
			curr = blockchain.get(i);
			prev = blockchain.get(i - 1);

			// compare registered hash and calculated hash
			if (!curr.hash.equals(curr.calculateHash())) {
				System.out.println("#Current hashes are not equal");
				return false;
			}

			// compare previous hash and registered previous hash
			if (!prev.hash.equals(curr.previousHash)) {
				System.out.println("#Previous hashes are not equal");
				return false;
			}

			// check if hash is solved
			if (!curr.hash.substring(0, difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined yet");
				return false;
			}

			// loop thru blockchain transactions
			TransactionOutput tempOutput;
			for (int t = 0; t < curr.transactions.size(); t++) {
				Transaction currentTransaction = curr.transactions.get(t);

				if (!currentTransaction.verifySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false;
				}
				if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false;
				}

				for (TransactionInput in : currentTransaction.inputs) {
					tempOutput = tempUTXOs.get(in.transactionOutputId);

					if (tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}
					if (in.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}

					tempUTXOs.remove(in.transactionOutputId);
				}

				for (TransactionOutput out : currentTransaction.outputs) {
					tempUTXOs.put(out.id, out);
				}

				if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
			}
		}
		System.out.println("Blockchain is valid");
		return true;
	}

	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}

}
