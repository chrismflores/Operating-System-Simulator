
public class VFS implements Device {
	
	Device[] devices;; // (Device/id) combination
	int[] VFSid; // VFS id
	RandomDevice randomDevice;
	FakeFileSystem fakeFileSystem;
	
	public VFS() {
		 devices = new Device[10];
		 VFSid = new int[10];
		 randomDevice = new RandomDevice();
		 fakeFileSystem = new FakeFileSystem();

	}
	// open a device based on user input
	@Override
	public int Open(String s) {
		String[] input = s.split(" ", 2);
		String device = input[0];
		String passedToDevice = input[1];
		
		if(device.equals("random")) {
			for(int i = 0; i < devices.length; i++) {
				if(devices[i] == null) {
					devices[i] = randomDevice;
					VFSid[i] = randomDevice.Open(passedToDevice);
					return i;
				}
			}			
		} else if (device.equals("file")) {
			for(int i = 0; i < devices.length; i++) {
				if(devices[i] == null) {
					devices[i] = fakeFileSystem;
					VFSid[i] = fakeFileSystem.Open(passedToDevice);
					return i;
				}
			}

		}
		return -1;
	}
	// closes device
	@Override
	public void Close(int id) {
		if (id >= 0 && id < devices.length) {
			devices[id].Close(VFSid[id]);
			devices[id] = null;
			id = -1;
		}
	}
	// read from a device
	@Override
	public byte[] Read(int id, int size) {
		if (id >= 0 && id < devices.length && devices[id] != null) {
			return devices[id].Read(VFSid[id], size);
		}
		return null;
	}
	//seek within a device
	@Override
	public void Seek(int id, int to) {
		if (id >= 0 && id < devices.length && devices[id] != null) {
			devices[id].Seek(VFSid[id], to);
		}
		
	}
	// write to a device 
	@Override
	public int Write(int id, byte[] data) {
		if (id >= 0 && id < devices.length && devices[id] != null) {
			return devices[id].Write(VFSid[id], data);
		}
		return -1;
	}

}

