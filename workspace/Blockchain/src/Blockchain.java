import com.google.gson.GsonBuilder;
import java.util.ArrayList;

public class Blockchain {
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();

	public static void main(String[] args) {
		//create blocks
		Block genesisBlock = new Block("I am the first block", "0");
		//System.out.println("hash for block 1 : " + genesisBlock.hash);
		
		Block secondBlock = new Block("I am the second block", genesisBlock.hash);
		//System.out.println("hash for block 2 : " + secondBlock.hash);
		
		Block thirdBlock = new Block("I am the third block", secondBlock.hash);
		//System.out.println("hash for block 3 : " + thirdBlock.hash);
		
		//add blocks to blockchain
		blockchain.add(genesisBlock);
		blockchain.add(secondBlock);
		blockchain.add(thirdBlock);
		
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println(blockchainJson);
	}
	
	public static Boolean isChainValid()
	{
		Block curr;
		Block prev;
		
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
		}
		return true;
	}

}
