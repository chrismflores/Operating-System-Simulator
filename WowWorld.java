
public class WowWorld extends UserlandProcess {

	@Override
	void main() {
		while(true) {
			System.out.println("Wow World");
			// calls cooperate to switch process 
			cooperate();
			try {
			    Thread.sleep(50); // sleep for 50 ms
			} catch (Exception e) { }
			OS.Sleep(400);
		}
	}
	

}
