package Project;

public class Service {
	private int serviceTime;
	private ServiceType type;
	
	// CONSTRUCTOR
	private Service(ServiceType type, int serviceTime) {
		this.type = type;
		this.serviceTime = serviceTime; 
	}

	public static Service create(String type, String serviceTime) {
		if (type.equals("C")) {
        	return new Service(ServiceType.CPU, Integer.parseInt(serviceTime));
		} else if (type.equals("K")) {
        	return new Service(ServiceType.Keyboard, Integer.parseInt(serviceTime));
        } else {
        	return null;
        }
	}
	
	// GETTERS
	public int getServiceTime() {
		return serviceTime;
	}
	public ServiceType getType() {
		return type;
	}
}
