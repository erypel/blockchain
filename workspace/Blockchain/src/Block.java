import java.util.Date;

public class Block {
	public String hash; //digital signature
	public String previousHash; //the previous block's hash
	private String data; //will be a simple message for practice's sake
	private long timeStamp; //as a number of milliseconds since 1/1/1970
	
	//Block constructor
	public Block(String data, String previousHash)
	{
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
	}
}
