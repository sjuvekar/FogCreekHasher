import java.math.BigInteger;
import java.util.HashMap;

public class Unhasher {

	/**
	 * Letters from which hash is built
	 */
	public static String sLetters = "acdegilmnoprstuw";
	
	/**
	 * Static BigIntegers to use in comparisons
	 */
	private final static BigInteger sSeven = BigInteger.valueOf(7L);
	private final static BigInteger sThirtySeven = BigInteger.valueOf(37L);
	private final static BigInteger sZero = BigInteger.valueOf(0L);
	
	/**
	 * Memoization table from hash values seen so far to original strings
	 */
	private HashMap<BigInteger, String> mMemoizationTable = new HashMap<BigInteger, String>();
	
	public Unhasher() {
		mMemoizationTable.put(sSeven, "");
	}
	
	/**
	 * Main tail recursive algorithm. 
	 * @param hash
	 * @param len
	 * @param suffix
	 * @return
	 */
	private String unhash_tail(BigInteger hash, int len, String suffix) {
		// Base case. If len == 0, then return "suffix
		if (len == 0) {
			return (hash.compareTo(sSeven) == 0)? suffix: "";
		}
		
		// Base case. If hash < 7, then return ""
		if (hash.compareTo(sSeven) == -1  || (hash.compareTo(sSeven) == 0 && len > 0)) return "";
		
		// Base case: if hash is found in table, return the element
		if (mMemoizationTable.containsKey(hash)) {
			String prefix = mMemoizationTable.get(hash);
			BigInteger temp_hash = hash;
			String temp_str = new String(prefix);
			
			for (int i = 0; i < suffix.length(); i++) {
				char temp_char = suffix.charAt(i); 
				temp_str += temp_char;
				int index = sLetters.indexOf(temp_char);
				temp_hash = temp_hash.multiply(sThirtySeven).add(BigInteger.valueOf(index));
				mMemoizationTable.put(temp_hash, temp_str);
			}
			return prefix + suffix;
		}
		
		// Finally, recursively call the method for smaller size. 
		for (int i = 0; i < sLetters.length(); i++) {
			char new_char = sLetters.charAt(i);
			BigInteger residue = hash.subtract(BigInteger.valueOf(i));
			if (residue.mod(sThirtySeven).compareTo(sZero) != 0) continue;
			return unhash_tail(residue.divide(sThirtySeven), len - 1, new_char + suffix);
		}
		
		return "";
	}
	
	public String unhash(BigInteger hash, int len) {
		return unhash_tail(hash, len, "");
	}
	
	public static void main(String args[]) {
		Unhasher u = new Unhasher();
		System.out.println(u.unhash(BigInteger.valueOf(945924806726376L), 9));
		System.out.println(u.unhash(BigInteger.valueOf(680131659347L), 7));
	}
}
