import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;

public class StringUtil {
	public final static String SHA_256 = "SHA-256";
	public final static String UTF_8 = "UTF-8";

	/**
	 * 
	 * This method takes a string and applies SHA256 encryption algorithm to it. it
	 * returns a generated signature as a String
	 * 
	 * @param input:
	 *            data to be encrypted with SHA256
	 * @return a SHA256 encrypted hexadecimal String
	 */
	public static String applySha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance(SHA_256);
			// encrypts input with sha256
			byte[] hash = digest.digest(input.getBytes(UTF_8));

			// we want a hexadecimal string for the hash
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				// mask the variable so it leaves only the value
				// in the last 8 bits. Ignore the rest of the bits
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		}
		// NoSuchAlgorithmException or UnsupportedEncodingException
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Applies ECDSA Signature and returns the result (as bytes)
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		Signature dsa;
		byte[] output = new byte[0];
		try {
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}

	// Verifies a String signature
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getStringFromKey(Key key) {

		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	public static String getMerkleRoot(ArrayList<String> merkleTree) {
		String merkleRoot = (merkleTree.size() == 1) ? merkleTree.get(0) : "";
		return merkleRoot;
	}

	// method to create a Merkle Tree out of the transaction list
	// Tacks in array of transactions and returns a merkle tree
	public static ArrayList<String> getMerkleTree(ArrayList<Transaction> transactions) {
		ArrayList<String> tree = new ArrayList<>();
		// add all the hashes of the transactions to the tree
		for (Transaction t : transactions) {
			tree.add(t.transactionId); // add the hash
		}

		// Offset in the list where the currently processed level starts
		int levelOffset = 0;

		// Step thru each level and stop when we reach the root (levelSize == 1)
		for (int levelSize = transactions.size(); levelSize > 1; levelSize = (levelSize + 1) / 2) {
			// for each pair of nodes on the level:
			for (int left = 0; left < levelSize; left += 2) {
				/*
				 * The right hand node can be the same as the left hand in the case that we
				 * don't have enough transactions.
				 */
				int right = Math.min(left + 1, levelSize - 1);
				String leftTransaction = tree.get(levelOffset + left);
				String rightTransaction = tree.get(levelOffset + right);
				tree.add(StringUtil.applySha256(leftTransaction + rightTransaction));
			}
			// move to next level
			levelOffset += levelSize;
		}
		return tree;
	}
}
