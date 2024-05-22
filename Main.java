public class Main {
	/** multithread operating system simulator **/
	public static void main(String[] args) {
		
		// calls OS.Startup for testing and creating the kernel
		OS.Startup(new HelloWorld());
//		// calls OS.CreateProcess for testing and creating the process 
//		OS.CreateProcess(new GoodbyeWorld(), OS.Priority.Interactive);
//		// calls OS.CreateProcess for testing and creating the process 
//		OS.CreateProcess(new WowWorld(), OS.Priority.RealTime);
//		// calls OS.CreateProcess for testing and creating the process 
//		OS.CreateProcess(new GoodbyeWorld(), OS.Priority.Background);
//		// calls OS.CreateProcess for testing and creating the process 
//		OS.CreateProcess(new WowWorld(), OS.Priority.RealTime);
		OS.CreateProcess(new Ping());
		OS.CreateProcess(new Pong());
		int pingPid = OS.GetPidByName("Ping");
		int pongPid = OS.GetPidByName("Pong");
        KernelMessage pingMessage = new KernelMessage(pingPid, pongPid, 0, "Hello Pong".getBytes());
        OS.SendMessage(pingMessage);
        OS.WaitForMessage();
        OS.AllocateMemory(4048);
        OS.FreeMemory(5, 2);
        OS.GetMapping(10);
	}
}