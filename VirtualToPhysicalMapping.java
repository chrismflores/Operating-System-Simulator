
public class VirtualToPhysicalMapping {
	int physicalPageNumber;
    int diskPageNumber;

    VirtualToPhysicalMapping() {
        physicalPageNumber = -1;
        diskPageNumber = -1;
    }

	public int getPhysicalPageNumber() {
		return physicalPageNumber;
	}
    
}
