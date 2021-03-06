package Project;

import java.util.ArrayList;

public class ProcessResult {
	private final Process process;
	private final ArrayList<Interval> serviceIntervals;
	private final int exitTime;
	private final int serviceTime;
	private final int turnaroundTime;
	private final int queuingTime;
	private final int queuingTimeCPU;
	private final int queuingTimeIO;
	private double ratioTS;
	
	protected ProcessResult(ProcessInCPU processInCPU) {
		process = processInCPU.getProcess();
		serviceIntervals = processInCPU.getServiceTimes();
		exitTime = serviceIntervals.get(serviceIntervals.size() - 1).getEnd();
		
		queuingTimeCPU = processInCPU.getQueuingTimeCPU();
		queuingTimeIO = processInCPU.getQueuingTimeIO();
		queuingTime = queuingTimeIO + queuingTimeCPU;
		
		int acc = 0;
		for (int i = 0; i < process.getServicesCount(); i++) {
			acc += this.process.getServiceTime(i);
		}
		serviceTime = acc;
		turnaroundTime = exitTime - process.getArrivalTime(); 
		ratioTS = turnaroundTime / serviceTime;
	}
	
	public static ArrayList<ProcessResult> createResultList(ArrayList<ProcessInCPU> processes) {
		ArrayList<ProcessResult> results = new ArrayList<ProcessResult>(); 
		for (ProcessInCPU rawProcess : processes) {
			results.add(new ProcessResult(rawProcess));
		}
		return results;
	}
	public static double[] calcMaxAvgQueuingTime(ArrayList<ProcessResult> processes) {
		double[] result = new double[2];
		int accumulator = 0;
		for (ProcessResult process : processes) {
			if (process.getQueuingTime() > result[0]) result[0] = process.getQueuingTime();
			accumulator += process.getQueuingTime();
		}
		result[1] = (double) accumulator / processes.size();
		return result;
	}
	public static double[] calcMaxAvgTurnaroundTime(ArrayList<ProcessResult> processes) {
		double[] result = new double[2];
		int accumulator = 0;
		for (ProcessResult process : processes) {
			if (process.getTurnaroundTime() > result[0]) result[0] = process.getTurnaroundTime();
			accumulator += process.getTurnaroundTime();
		}
		result[1] = (double) accumulator / processes.size();
		return result;
	}
	public static double[] calcMaxAvgRatioTS(ArrayList<ProcessResult> processes) {
		double[] result = new double[2];
		int accumulator = 0;
		for (ProcessResult process : processes) {
			if (process.getratioTS() > result[0]) result[0] = process.getratioTS();
			accumulator += process.getratioTS();
		}
		result[1] = (double) accumulator / processes.size();
		return result;
	}
	
	// GETTERS
	public int getExitTime() { return exitTime; }
	public int getTimeInCPU() {
		int accumulator = 0;
		for (Interval i : serviceIntervals) {
			accumulator += i.getEnd() - i.getStart();
		}
		return accumulator;
	}
 	
	public void print() {
	    System.out.print(String.format("Process #%d: ", process.getId()));
	    for (Interval pair : serviceIntervals) {
	    	pair.print();
	    }
	    System.out.println();
	    System.out.println("CPU Queuing Time: " + getQueuingTimeCPU());
	    System.out.println("Keyboard Queuing Time: " + getQueuingTimeIO());
	    System.out.println("Turnaround Time: " + getTurnaroundTime());
	    System.out.println();
	}

	public Process getProcess() {
		return process;
	}

	public int getQueuingTimeCPU() {
		return queuingTimeCPU;
	}

	public ArrayList<Interval> getServiceItervals() {
		return serviceIntervals;
	}
	
	public int getQueuingTimeIO() {
		return queuingTimeIO;
	}

	public int getQueuingTime() {
		return queuingTime;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public int getTurnaroundTime() {
		return turnaroundTime;
	}

	public double getratioTS() {
		return ratioTS;
	}

}
