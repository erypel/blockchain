import java.util.Date;

public class Block {
	public String hash; // digital signature
	public String previousHash; // the previous block's hash
	private String data; // will be a simple message for practice's sake
	private long timeStamp; // as a number of milliseconds since 1/1/1970
	private int nonce;
	
	
	// Block constructor
	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		
		// make sure to do this after setting the other values
		this.hash = calculateHash();
	}

	public String calculateHash() {
		String calculatedHash = StringUtil.applySha256(
				previousHash + 
				Long.toString(timeStamp) +
				Integer.toString(nonce) +
				data);
		return calculatedHash;
	}
	
	/**
	 * Low difficulty of 1 or 2 can be solved nearly instantly on most computers
	 * 4-6 is good for testing.
	 * @param difficulty
	 */
	public void mineBlock(int difficulty) {
		// create a String with difficulty * "0"
		String target = new String(new char[difficulty]).replace('\0', '0');
		while(!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
		System.out.println("Block mined!! : " + hash);
	}
}
