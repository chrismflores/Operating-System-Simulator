
public class Pong extends UserlandProcess {

	@Override
	void main() {
		while(true) {
			System.out.println("I am Pong");
			// calls cooperate to switch process 
			cooperate();
			try {
			    Thread.sleep(50); // sleep for 50 ms
			} catch (Exception e) { }
		}
				
	}

}
