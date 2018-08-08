import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.security.Security;

public class Blockchain {
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	// lit of all unspent transactions
	public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
	
	// testing variables
	public static int difficulty = 5;
	public static Wallet walletA;
	public static Wallet walletB;
	
	public static void main(String[] args) {
		testWalletAndSignature();
		testAddingBlocksToBlockchain();
	}
	
	public static void testAddingBlocksToBlockchain() {
		// add blocks to blockchain
		blockchain.add(new Block("I am the first block", "0"));
		System.out.println("Trying to Mine block 1... ");
		blockchain.get(0).mineBlock(difficulty);

		blockchain.add(new Block("I am the second block", blockchain.get(blockchain.size() - 1).hash));
		System.out.println("Trying to Mine block 2... ");
		blockchain.get(1).mineBlock(difficulty);

		blockchain.add(new Block("I am the third block", blockchain.get(blockchain.size() - 1).hash));
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

		// Test public and private keys
		System.out.println("Private and pulic keys:");
		System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
		System.out.println(StringUtil.getStringFromKey(walletA.publicKey));

		// create a test transaction from WalletA to WalletB
		Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
		transaction.generateSignature(walletA.privateKey);

		// verify the signature works and verify it from the public key
		System.out.println("Is the signature verified?");
		System.out.println(transaction.verifySignature());
	}
	
	public static Boolean isChainValid()
	{
		Block curr;
		Block prev;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		//loop through blockchain to check hashes
		for(int i = 1; i < blockchain.size(); i++)
		{
			curr = blockchain.get(i);
			prev = blockchain.get(i-1);
			
			//compare registered hash and calculated hash
			if(!curr.hash.equals(curr.calculateHash()))
			{
				System.out.println("Current hashes are not equal");
				return false;
			}
			
			//compare previous hash and registered previous hash
			if(!prev.hash.equals(curr.previousHash))
			{
				System.out.println("Previous hashes are not equal");
				return false;
			}
			
			//check if hash is solved
			if(!curr.hash.substring(0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined yet");
				return false;
			}
		}
		return true;
	}

}
