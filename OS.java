import java.util.ArrayList;

public class OS {

	// Enum for CallTypes 
	enum CallType {
		CreateProcess, SwitchProcess, Sleep, GetPID, GetPIDByName, SendMessage, 
		WaitForMessage, GetMapping, AllocateMemory, FreeMemory;
	}
	
	// Enum for priority
	enum Priority {
	    RealTime, Interactive, Background;
	}
	
	static CallType currentCall;
	static Priority currentPriority;
	static ArrayList<Object> parameters = new ArrayList<>();
	static Object value;
	static Kernel kernel = new Kernel();
	
	// sets the current call 
	static void setCurrentCall(CallType calltype) {
		currentCall = calltype;
	}
	
	// gets the current call 
	public static CallType getCurrentCall() {
		return currentCall;
	}
	
	public static void switchToKernel() {
		// calls kernel
		kernel.start();
		// checks the isCurrentlyRunning
		PCB isCurrentlyRunning = kernel.scheduler.getCurrentlyRunning();
		if (isCurrentlyRunning != null) {
			isCurrentlyRunning.stop();
		} else if (isCurrentlyRunning == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		try
        {
            Thread.sleep(100); // sleep for 1 ms
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
	}

	// Make an enum entry for CreateProcess, clears the parameters
	// and sets current call to create process

	public static int CreateProcess(UserlandProcess up) {
		parameters.clear();
		parameters.add(up);
		parameters.add(OS.Priority.Interactive);
		currentCall = CallType.CreateProcess;
		// Switch to kernel
		switchToKernel();
		
		return (int) value;

	}
	// clear the parameters and sets current call to switch process
	public static void switchProcess() {
		parameters.clear();
		currentCall = CallType.SwitchProcess;
		// Switch to kernel
		switchToKernel();

	}

	// Creates the Kernel() and calls CreateProcess twice – once for “init” and once
	public static void Startup(UserlandProcess init) {
		CreateProcess(init, OS.Priority.Background);
//		System.out.println("TESTT");
		CreateProcess(new IdleProcess(), OS.Priority.Background);	
		
	}
	// sleep method
	public static void Sleep(int milliseconds) {
		parameters.clear();
		parameters.add(milliseconds);
		currentCall = CallType.Sleep;
		// Switch to kernel
		switchToKernel();

	}
	// new create process 
	public static int CreateProcess(UserlandProcess up, Priority priority) {
		parameters.clear();
		parameters.add(up);
		parameters.add(priority);
		currentCall = CallType.CreateProcess;
		// Switch to kernel
		switchToKernel();
		debug("Made it to the create process OS after kernel");
		return (int) value;

	}
	
	//returns the current process’ pid
	public static int GetPid() {
		parameters.clear();
		currentCall = CallType.GetPID;
		// Switch to kernel
		switchToKernel();
		return (int) value;
	}
	
	// returns the pid of a process with that name. 
	public static int GetPidByName(String name) {
		parameters.clear();
		parameters.add(name);
		currentCall = CallType.GetPIDByName;
		// Switch to kernel
		switchToKernel();
		return (int) value;
	}
	
	public static void SendMessage(KernelMessage km) {
		parameters.clear();
		parameters.add(km);
		currentCall = CallType.SendMessage;
		// Switch to kernel
		switchToKernel();
	}
	
	public static KernelMessage WaitForMessage() {
		parameters.clear();
		currentCall = CallType.WaitForMessage;
		// Switch to kernel
		switchToKernel();
		return null;
	}
	
	public static void GetMapping(int virtualPage) {
		parameters.clear();
		parameters.add(virtualPage);
		currentCall = CallType.GetMapping;
		// Switch to kernel
		switchToKernel();
	}
	
	public static int AllocateMemory(int size) {
		if(size % 1024 != 0) {
			return -1;
		}
		parameters.clear();
		parameters.add(size);
		currentCall = CallType.AllocateMemory;
		// Switch to kernel
		switchToKernel();
		return (int) value;
	}
	
	public static boolean FreeMemory(int pointer, int size) {
		if(size % 1024 != 0 && pointer % 1024 != 0) {
			System.out.println("OS call FreeMemory \n "
			+ "size or pointer is not a multiple of 1024");
			return false;
		}
		parameters.clear();
		parameters.add(pointer);
		parameters.add(size);
		currentCall = CallType.FreeMemory;
		// Switch to kernel
		switchToKernel();
		return (boolean) value;
	}



	
	
	// used for debugging
	public static void debug(String string) {
//		System.out.println(string);
	}



	
	
	
	

}
