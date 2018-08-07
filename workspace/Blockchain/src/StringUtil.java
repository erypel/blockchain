import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
	public final static String SHA_256 = "SHA-256";
	public final static String UTF_8 = "UTF-8";
	
	/**
	 * 
	 * This method takes a string and applies SHA256 encryption
	 * algorithm to it. it returns a generated signature as a String
	 * 
	 * @param input: data to be encrypted with SHA256
	 * @return a SHA256 encrypted hexadecimal String
	 */
	public static String applySha256(String input)
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance(SHA_256);
			// encrypts input with sha256
			byte[] hash = digest.digest(input.getBytes(UTF_8));
			
			//we want a hexadecimal string for the hash
			StringBuffer hexString = new StringBuffer();
			for(int i = 0; i < hash.length; i++)
			{
				// mask the variable so it leaves only the value
				// in the last 8 bits. Ignore the rest of the bits
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1)
				{
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		}
		//NoSuchAlgorithmException or UnsupportedEncodingException
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
