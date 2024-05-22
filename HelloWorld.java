
public class HelloWorld extends UserlandProcess {
	
	// HelloWorld class derived from UserlandProcess used for testing
	@Override
	void main() {
		while(true) {
			System.out.println("Hello World");
			// calls cooperate to switch process 
			cooperate();
			try {
			    Thread.sleep(50); // sleep for 50 ms
			} catch (Exception e) { }
		}
		
	}

}
