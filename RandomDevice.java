import java.util.Random;

public class RandomDevice implements Device {

	private Random[] randomArray = new Random[10];


	// Opens a device with or without a seed, initialize seed if its provided 
	@Override
	public int Open(String s) {
		if (s != null && !s.isEmpty()) {
			int seed = Integer.parseInt(s);
			for (int i = 0; i < randomArray.length; i++) {
				if (randomArray[i] == null) {
					randomArray[i] = new Random(seed);
					return i;
				}
			}
		} else {
			for (int i = 0; i < randomArray.length; i++) {
				if (randomArray[i] == null) {
					randomArray[i] = new Random();
					return i;
				}
			}
		}
		return -1; // No empty spot found
	}

    // Closes a device by setting its element to null
	@Override
	public void Close(int id) {
		if (id >= 0 && id < randomArray.length) {
			randomArray[id] = null;
		}
	}
	
    // Reads bytes from a device if it exists and is open
	@Override
	public byte[] Read(int id, int size) {
		if (id >= 0 && id < randomArray.length && randomArray[id] != null) {
			byte[] data = new byte[size];
			randomArray[id].nextBytes(data);
			return data;
		}
		return null;

	}

	// Seeks to a specific position in the device if it exists and is open
	@Override
	public void Seek(int id, int to) {
		if (id >= 0 && id < randomArray.length && randomArray[id] != null) {
			byte[] data = new byte[to];
			randomArray[id].nextBytes(data);
		}
	}
	
    // Writes to a device
	@Override
	public int Write(int id, byte[] data) {
		// TODO Auto-generated method stub
		return 0;
	}

}
