import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device {

	private RandomAccessFile[] randomAccessFile = new RandomAccessFile[10];
   
	// Opens a file with read and write permissions
	@Override
	public int Open(String s) {
		if (s != null && !s.isEmpty()) {
			for (int i = 0; i < randomAccessFile.length; i++) {
				if (randomAccessFile[i] == null) {
					try {
						randomAccessFile[i] = new RandomAccessFile(s, "rw");
						return i;
					} catch (FileNotFoundException e) {
						return -1;
					}
				}
			}
		}
		return -1;
	}
	
    // Closes the opened file
	@Override
	public void Close(int id) {
		if (id >= 0 && id < randomAccessFile.length) {
			try {
				randomAccessFile[id].close();
				randomAccessFile[id] = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
    // Reads bytes from the opened file
	@Override
	public byte[] Read(int id, int size) {
		byte[] data = new byte[size];
		if (id >= 0 && id < randomAccessFile.length && randomAccessFile[id] != null) {
			try {
				randomAccessFile[id].read(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return data;
	}
	
    // Seeks to a specific position in the opened file
	@Override
	public void Seek(int id, int to) {
		if (id >= 0 && id < randomAccessFile.length && randomAccessFile[id] != null) {
			try {
				randomAccessFile[id].seek(to);
			} catch (IOException e) {
				e.printStackTrace(); 
			}
		}
	}
    // Write bytes to the opened file
	@Override
	public int Write(int id, byte[] data) {
		if (id >= 0 && id < randomAccessFile.length && randomAccessFile[id] != null) {
			try {
				randomAccessFile[id].write(data);
				return data.length;
			} catch (IOException e) {
				e.printStackTrace(); 
			}
		}
		return -1;
	}

}
