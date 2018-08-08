
public class TransactionInput {
	public String transactionOutputId; //reference to TransationOutputs -> transactionId
	public TransactionOutput UTXO; //Contains the unspent transaction output
	
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
}
