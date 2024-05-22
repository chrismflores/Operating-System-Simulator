//blueprint for what a Message is
public class KernelMessage {
	
	private int senderPID;    // Sender's Process ID
	private int targetPID;    // Target Process ID
	private int signal;       // Signal or Message Type
	private byte[] data;      // Message Data
	
	// Constructor to create a new Kernel Message
	public KernelMessage(int senderPID, int targetPID, int signal, byte[] data){
		this.senderPID = senderPID;
		this.targetPID = targetPID;
		this.signal = signal;
		this.data = data;
	}
	
	// Copy Constructor to create a new Kernel Message based on an existing one
	public KernelMessage(KernelMessage message, int senderPID) {
		this.senderPID = senderPID;
		this.targetPID = message.getTargetPID();
		this.signal = message.getSignal();
		this.data = message.getData();
	}
	
	// Getter methods
	public int getSenderPID() {
		return senderPID;
	}
	
	public int getTargetPID() {
		return targetPID;
	}
	
	public int getSignal() {
		return signal;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public String toString() {
		return null;
	}
	
}
