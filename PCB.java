import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class PCB  {
	
	private static int nextpid;
	private int pid;
	private UserlandProcess ulp;
	OS.Priority currentP;
	private int count = 0;
	long minimumWakeUp;
	VFS vfs;
	int[] array;
	private String name;
	private LinkedList<KernelMessage> messageQueue = new LinkedList<>();
	// Array to store virtual to physical page mapping
    VirtualToPhysicalMapping[] pageTable;
    
	/* creates thread, sets pid */
	public PCB(UserlandProcess up, OS.Priority priority) {
		this.ulp = up;
		nextpid++;
		pid = nextpid;
		currentP = priority;
		minimumWakeUp = 0;
		array = new int[10];
		Arrays.fill(array, -1);
		name = up.getClass().getSimpleName();
		// changed pageTable to 
		pageTable = new VirtualToPhysicalMapping[100];
        Arrays.fill(pageTable, -1);
	}
	
	public String getName() {
		return name;
	}
	
	public void addtoMessageQueue(KernelMessage message) {
		messageQueue.add(message);
	}
	
	public KernelMessage getfromMessageQueue() {
		return messageQueue.removeFirst();
	}
	
	public boolean hasMessage() {
		if(messageQueue.size() > 1) {
			return true;
		} else 
			return false;
	}
	/* calls userlandprocess’ stop. Loops with Thread.sleep() until 
	  UserlandProcess.isStopped() is true.  */
	void stop() {
		ulp.stop();
		while (ulp.isStopped() == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
		}
	}
	
	/* calls userlandprocess’ isDone() */
	boolean isDone() {
		return ulp.isDone();
		
	}
	
	/* calls userlandprocess’ start() */
	void start() {
		ulp.start();
	}
	// request stop and demotes if ran more then 5 times 
	public void requestStop() {
		ulp.requestStop();
		if(count > 5) {
			if(currentP == OS.Priority.RealTime) {
				System.out.println(">>>>DEMOTED");
				currentP = OS.Priority.Interactive;
				count = 0;
			} else if(currentP == OS.Priority.Interactive) {
				System.out.println(">>>>DEMOTED");
				currentP = OS.Priority.RealTime;
				count = 0;
			}
		}
		count++;

	}
	
	public int getPid() {
		return pid;
	}

	public void GetMapping(int virtualPageNumber) {
		VirtualToPhysicalMapping physicalMapping = pageTable[virtualPageNumber];
        // Update one of the TLB entries randomly in the GetMapping method.
        Random random = new Random();
        int randomUpdate = random.nextInt(2);
		UserlandProcess.TLB[0][randomUpdate] = virtualPageNumber;
		UserlandProcess.TLB[1][randomUpdate] = physicalMapping.getPhysicalPageNumber();
		
	}

	// returns the start virtual address
	public void addpagestoMemory(int page) {
		// adds page to pageTable
		for(int i = 0; i < pageTable.length; i++) {
			for(int j = 0; j < page; j++) {
				if (pageTable[i] == pageTable[i + j]) {
					pageTable[i] = pageTable[page];
				}
			}
		}
	}
	
	// takes the virtual address and the amount to free
	public void FreeMemory(int pointer, int size) {
		for (int i = pointer; i < pointer + size; i++) {
			if (i >= 0 && i < pageTable.length) {
				pageTable[i] = null;
	        }
		}
	}
	
	
	
}
