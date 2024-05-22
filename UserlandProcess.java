import java.util.concurrent.Semaphore;

public abstract class UserlandProcess implements Runnable {

	Thread thread;
	Semaphore semaphore;
	boolean quantum;
	
	// TLB [2][2]array
	static int[][] TLB = {{-1,-1},{-1,-1}};
	
	// Memory array
    static byte[]memory = new byte[1024 * 1024];

	// Constructor to initialize components
	public UserlandProcess() {
		thread = new Thread(this);
		semaphore = new Semaphore(0);
		thread.start();
	}

	// if the boolean is true, set the boolean to false and call OS.switchProcess()
	void cooperate() {
		if (quantum == true) {
			quantum = false;
			
			OS.switchProcess();
		}

	}

	// true when the Java thread is not alive
	boolean isDone() {
		if (thread.isAlive()) {
			return false;
		}
		return true;
	}

	// indicates if the semaphore is 0
	boolean isStopped() {
		if (semaphore.availablePermits() == 0) {
			return true;
		}
		return false;

	}

	// will represent the main of our “program”
	abstract void main();

	// sets the boolean indicating that this process’ quantum has expired
	void requestStop() {
		quantum = true;
	}

	// acquire the semaphore, then call main
	public void run() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
		}
		main();
	}

	// releases (increments) the semaphore, allowing this thread to run
	void start() {
		semaphore.release();

	}

	// acquires (decrements) the semaphore, stopping this thread from running
	void stop() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
		}

	}
	
	byte Read(int address) {
		int virtualPage = address / 1024;
		int pageOffset = address % 1024;
		int physicalPage;
		// Check if the virtual page to physical page mapping exists in the TLB.
		for(int i = 0; i < 2; i++) {
			// if the mapping is found, get the physical address.
			if (TLB[0][i] == virtualPage) {
				physicalPage = TLB[i][0];
				break;
			}			
		}
		// if not found, perform an OS call to get the mapping.
		OS.GetMapping(virtualPage);
		return 0;
		
	}
	
	void Write(int address, byte value) {
		int pageNumber;

	}
	
	
}
