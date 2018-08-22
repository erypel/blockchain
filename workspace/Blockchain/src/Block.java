import java.util.ArrayList;
import java.util.Date;

public class Block {
	public String hash; // digital signature
	public String previousHash; // the previous block's hash
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); // our data
	private long timeStamp; // as a number of milliseconds since 1/1/1970
	private int nonce;

	// Block constructor
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();

		// make sure to do this after setting the other values
		this.hash = calculateHash();
	}

	public String calculateHash() {
		String calculatedHash = StringUtil
				.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
		return calculatedHash;
	}

	/**
	 * Low difficulty of 1 or 2 can be solved nearly instantly on most computers 4-6
	 * is good for testing.
	 * 
	 * @param difficulty
	 */
	public void mineBlock(int difficulty) {
		ArrayList<String> merkleTree = StringUtil.getMerkleTree(transactions);
		merkleRoot = StringUtil.getMerkleRoot(merkleTree);
		// create a String with difficulty * "0"
		String target = new String(new char[difficulty]).replace('\0', '0');
		while (!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
		System.out.println("Block mined!! : " + hash);
	}

	// add transactions to this block
	public boolean addTransaction(Transaction transaction) {
		// process transaction and check if valid. Ignore if this is the genesis block
		if (transaction == null)
			return false;
		if ((previousHash != "0")) {
			if (transaction.processTransaction() != true) {
				System.out.println("#Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}
}
