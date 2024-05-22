import java.util.concurrent.Semaphore;

public class Kernel implements Runnable, Device {

	Scheduler scheduler;
	private Semaphore mySemaphore;
	private Thread myThread;
	private boolean[] memoryUsage; 
	
	// Constructor to initialize the Kernel components
	public Kernel() {
		mySemaphore = new Semaphore(0);
		myThread = new Thread(this);
		scheduler = new Scheduler();
		myThread.start();
        memoryUsage = new boolean[1024];
	}

	public void run() {
		while (true) {
			try {
				// acquires the semaphore
				mySemaphore.acquire();
			} catch (InterruptedException e) {
			}
			
			switch (OS.currentCall) {
			case CreateProcess:
				// calls create process in scheduler and gets the OS value 
				OS.value = scheduler.CreateProcess((UserlandProcess) OS.parameters.get(0), (OS.Priority) OS.parameters.get(1));
				break;

			case SwitchProcess:
				// calls switch process in scheduler
				scheduler.SwitchProcess();
				break;
				
			case Sleep:
				scheduler.Sleep((int) OS.parameters.get(0));
				break;
				
			case GetPID:
				scheduler.GetPid();
				break;
				
			case GetPIDByName:
				scheduler.GetPidByName((String) OS.parameters.get(0));
				break;
				
			case SendMessage:
				scheduler.SendMessage((KernelMessage) OS.parameters.get(0));
				break;
				
			case WaitForMessage:
				scheduler.WaitForMessage();
				break;
				
			case GetMapping:
				scheduler.GetMapping((int) OS.parameters.get(0));
				break;
				
			case AllocateMemory:
				scheduler.AllocateMemory((int) OS.parameters.get(0));
				break;
			
			case FreeMemory:
				scheduler.FreeMemory((int) OS.parameters.get(0), (int) OS.parameters.get(1));
				break;
				
			default:
				break;
				
			}

			// calls start in UserlandProcess
			scheduler.getCurrentlyRunning().start();
		}
	}
	// releases (increments) the semaphore, allowing this thread to run
	public void start() {
		mySemaphore.release();

	}

	// Open for kernel
	@Override
	public int Open(String s) {
		int result = 0;
		for(int i = 0; i < scheduler.getCurrentlyRunning().array.length; i++) { 
			if(scheduler.getCurrentlyRunning().array[i] != -1) {
				
				return -1;
			} else {
				result = scheduler.getCurrentlyRunning().vfs.Open(s);
				if(result == -1) {
					
					return -1;
				} else {
					scheduler.getCurrentlyRunning().array[i] = scheduler.getCurrentlyRunning().vfs.VFSid[i];
				}

			}
		}
		
		
		return -1;
	}

	@Override
	public void Close(int id) {
		for(int i = 0; i <scheduler.getCurrentlyRunning().array.length; i++) {
			if(scheduler.getCurrentlyRunning().array[i] == -1) {
			}
			if(scheduler.getCurrentlyRunning().array[i] != -1) {
			}
		}
	}


	@Override
	public byte[] Read(int id, int size) {
		for(int i = 0; i <scheduler.getCurrentlyRunning().array.length; i++) {
			if(scheduler.getCurrentlyRunning().array[i] == -1) {
			}
			if(scheduler.getCurrentlyRunning().array[i] != -1) {
			}
		}		return null;
	}


	@Override
	public void Seek(int id, int to) {
		for(int i = 0; i <scheduler.getCurrentlyRunning().array.length; i++) {
			if(scheduler.getCurrentlyRunning().array[i] == -1) {
			}
			if(scheduler.getCurrentlyRunning().array[i] != -1) {
			}
		}
	}


	@Override
	public int Write(int id, byte[] data) {
		for(int i = 0; i <scheduler.getCurrentlyRunning().array.length; i++) {
			if(scheduler.getCurrentlyRunning().array[i] == -1) {
			}
			if(scheduler.getCurrentlyRunning().array[i] != -1) {
			}
		}	
		return (Integer) null;
	}
	


}
