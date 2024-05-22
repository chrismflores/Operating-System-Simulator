
public class IdleProcess extends UserlandProcess {

	// idle class derived from UserlandProcess used for calling idle process 
	@Override
	public void main() {
		while(true) {
			// calls cooperate to switch process 
			cooperate();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}
	}

}
