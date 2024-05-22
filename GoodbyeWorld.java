public class GoodbyeWorld extends UserlandProcess {
	
	// GoodbyeWorld class derived from UserlandProcess used for testing
	@Override
	public void main() {
		while(true) {
			System.out.println("Goodbye World");
			// calls cooperate to switch process 
			cooperate();
			try {
			    Thread.sleep(50); // sleep for 50 ms
			} catch (Exception e) { }
			OS.Sleep(120);
		}
	}
	

}
