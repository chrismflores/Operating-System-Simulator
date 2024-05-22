import java.time.Clock;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
	
	// I need a way to get the message back to usrland

	HashMap<Integer, PCB> PCBmap = new HashMap<Integer, PCB>();
	HashMap<Integer, PCB> PCBsWaiting = new HashMap<Integer, PCB>();

	Clock clock = Clock.systemDefaultZone();

	private LinkedList<PCB> realTime = new LinkedList<>();
	private LinkedList<PCB> interactive = new LinkedList<>();
	private LinkedList<PCB> background = new LinkedList<>();
	private LinkedList<PCB> sleeping = new LinkedList<>();
	private Timer timer;

	private PCB currentlyRunning;
	
	public PCB getCurrentlyRunning() {
		return currentlyRunning;
	}


	TimerTask interrupt = new interrupt();

	// constructor for Scheduler
	// The interrupt causes the requeststop
	public Scheduler() {
		timer = new Timer(true);
		timer.schedule(interrupt, 250, 250);
	}

	// creates the interrupt for the timertask
	public class interrupt extends TimerTask {
		// calls run
		@Override
		public void run() {
			if (currentlyRunning != null) {
				currentlyRunning.requestStop();
			}

		}
	}
	

	// adds current process to list and sees if currentlyRunning is null and calls
	// switch process
//	public int CreateProcess(UserlandProcess up) {
//		processes.add(up);
//		if (currentlyRunning == null) {
//			SwitchProcess();
//		} else if (currentlyRunning.isDone()) {
//			SwitchProcess();
//		}
//
//		return 0;
//	}

	// checks to see is currentlyRunning is null or done, and removes first on list
	public void SwitchProcess() {
		clearTLB();
		OS.debug("In Switch process");
		if(currentlyRunning == null) {
			OS.debug("currently Running is null");
			
		} else if (!currentlyRunning.isDone()) {
			// if currently running is not null and is alive (not dead) add process to end
			// of the right queue
			OS.debug("In Switch process after first if");

			
			// Current Priority is Real Time
			if (currentlyRunning.currentP == OS.Priority.RealTime) {

				realTime.addLast(currentlyRunning);

			} else if (currentlyRunning.currentP == OS.Priority.Interactive) {

				interactive.addLast(currentlyRunning);

			} else if (currentlyRunning.currentP == OS.Priority.Background) {
				
				background.addLast(currentlyRunning);
			}

		} else if(currentlyRunning.isDone()) {
			PCBmap.remove(currentlyRunning.getPid());
		}
		
		

			setCurrentlyRunning();


		for (int i = 0; i < sleeping.size(); i++) {
			if (sleeping.get(i).minimumWakeUp < clock.millis()) {
				// removing the process that needs to be awake, were decrementing i because
				// would retirve the wrong item if it was i, because we incremented i,
				PCB awakeProcess = sleeping.remove(i--);
//				System.out.println("AHHHH Waking Up");
				if (awakeProcess.currentP == OS.Priority.RealTime) {
					realTime.addLast(awakeProcess);

				} else if (awakeProcess.currentP == OS.Priority.Interactive) {
					interactive.addLast(awakeProcess);

				} else if (awakeProcess.currentP == OS.Priority.Background) {
					background.addLast(awakeProcess);
				}
			}
		}

		OS.debug("Outta switch process");


	}
	
	private void clearTLB() {
		UserlandProcess.TLB[0][0] = -1; 
		UserlandProcess.TLB[1][0] = -1; 
		UserlandProcess.TLB[0][1] = -1; 
		UserlandProcess.TLB[1][1] = -1; 

		}
	// sets the currently running to the correct process
	public void setCurrentlyRunning() {
		OS.debug("In set currently running in scheduler");

		Random rand = new Random();
		int randomNumber = rand.nextInt(10) + 1;

		if(realTime.isEmpty() && interactive.isEmpty()) {
			randomNumber = 1;
		} else if (!interactive.isEmpty() && realTime.isEmpty()) {
			randomNumber = rand.nextInt(4) + 2;
		}
		
//		System.out.println("Number Randomly generated " + randomNumber);
		OS.debug("past first if in set currently running");

		OS.debug("The currently Running is " + currentlyRunning);
		OS.debug("LIST" + interactive);
		if (randomNumber >= 6) {
			currentlyRunning = realTime.removeFirst();

		} else if (randomNumber >= 2 && randomNumber <= 4) {
			if (!interactive.isEmpty()) {
				currentlyRunning = interactive.removeFirst();
			} else {
			currentlyRunning = realTime.removeFirst();
			}
		} else if (randomNumber == 1) {
				currentlyRunning = background.removeFirst();
		}
		
	}

// adds current process to list and sees if currentlyRunning is null and calls switch process 
	public int CreateProcess(UserlandProcess up, OS.Priority currentPriority) {
		PCB pcb = new PCB(up, currentPriority);
		
		PCBmap.put(pcb.getPid(), pcb);
		
		OS.debug("the background list" + background);
		
		OS.debug("In Create Process in Scheduler");
		switch (pcb.currentP) {
            case RealTime:
            	realTime.add(pcb);
                break;
            case Interactive:
                interactive.add(pcb);
                break;
            case Background:
                background.add(pcb);
                break;
        }

		OS.debug("the background list" + background);

		OS.debug("past the switch in create process ");
		if (currentlyRunning == null) {
			SwitchProcess();
		} else if (currentlyRunning.isDone()) {
			SwitchProcess();
		}

		OS.debug("Finished the Create Process in scheduler");
		
		return pcb.getPid();
    }
	
	//returns the current process’ pid
	int GetPid() {
		return currentlyRunning.getPid();
	}
	
	// returns the pid of a process with that name. 
	int GetPidByName(String name) {
		int pid = 0;
		for (PCB i : PCBmap.values()) {
			if(name.equals(i.getName())) {
				pid = i.getPid();
				return pid;
			}
		}
		OS.debug("GetPidByName didnt work");
		return -1;
	}
	
	// the sleep method 
	public void Sleep(int milliseconds) {
		sleeping.add(currentlyRunning);
		currentlyRunning.minimumWakeUp =  clock.millis() + milliseconds;
		// setting currently to null because I dont want to add a sleeping process to list since we are zzzzz
		setCurrentlyRunning();
		
	}
	
	// sends meesage to another process 
	public void SendMessage(KernelMessage kernelMessage) {
		KernelMessage message = new KernelMessage(kernelMessage, GetPid());
		//looking for if a message is for something that is waiting
		if(PCBsWaiting.containsKey(message.getTargetPID())){
			PCB reciever = PCBsWaiting.get(message.getTargetPID());
			reciever.addtoMessageQueue(message);
		} else if(PCBmap.containsKey(message.getTargetPID())) {
			PCB reciever = PCBmap.get(message.getTargetPID());
			reciever.addtoMessageQueue(message);
		}
		
		OS.debug("Send Message did not work");
	}
	
	// Wait for message, wist for the message from send message 
	public KernelMessage WaitForMessage() {
		if(currentlyRunning.hasMessage()) {
			return currentlyRunning.getfromMessageQueue();
		} else {
			PCBsWaiting.put(currentlyRunning.getPid(), currentlyRunning);
		}		
 
		return null;
		// TODO Auto-generated method stub
		
	}
	
	public void GetMapping(int virtualPageNumber) {
		currentlyRunning.GetMapping(virtualPageNumber);
	}
	
	public void AllocateMemory(int size) {
		int pages = size / 1024;
		currentlyRunning.addpagestoMemory(pages);
		
	}
	
	public void FreeMemory(int pointer, int size) {
		currentlyRunning.FreeMemory(pointer, size);

	}

	
	
	
}
