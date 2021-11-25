package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import Commands.CmdReadFile;
import Commands.CmdSchedule;
import Commands.CmdSuggest;
import Exceptions.ExCaseNotFound;
import Exceptions.ExInvalidServiceType;
import Project.AlgorithmType;
import Project.MainSystem;
import Project.Process;
import Project.Service;

//@TestMethodOrder(MethodOrderer.MethodName.class)

class TestMainSystem {

	MainSystem system = MainSystem.getInstance();
	
	
	@BeforeEach
	public void setUp() throws Exception {system.clear();}
	
	@Test
	void testMainSystem_00_createService() {
		
		Service expected = Service.create("C", "5");
		Service actual = system.createService("C", "5");
		assertEquals(expected.getServiceTime(), actual.getServiceTime());
		assertEquals(expected.getType(), actual.getType());
	}
	
	@Test
	void testMainSystem_03_createProcess() {
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/1-perfect.txt"});
		Process expected = Process.create(0, 0, null);
		Process actual = system.createProcess("0", 0, null);
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getArrivalTime(), actual.getArrivalTime());
	}
	
	
	
	@Test
	void testMainSystem_02_scheduleAlgorithms_0_Input() {
		int expected = 0;
		int actual = system.scheduleAlgorithms();
		assertEquals(expected, actual);
	}
	
	@Test
	void testMainSystem_01_scheduleAlgorithms_1_Input() {
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/1-perfect.txt"});
		int expected = 1;
		int actual = system.scheduleAlgorithms();
		System.out.println(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testMainSystem_04_openAlgo() {
		boolean expected = true;
		boolean actual = system.openAlgo(AlgorithmType.FCFS);
		assertEquals(expected, actual);
	}
	
	@Test
	void testMainSystem_05_openAlgo_Again() {
		boolean expected = false;
		boolean actual = system.openAlgo(AlgorithmType.FB);
		actual = system.openAlgo(AlgorithmType.FB);
		assertEquals(expected, actual);
	}
	
	@Test
	void testMainSystem_06_openCase() throws ExCaseNotFound {
		ExCaseNotFound ex = assertThrows(ExCaseNotFound.class, () -> {system.openCase(0);});
	}
	
	@Test
	void testMainSystem_07_openCase() throws ExCaseNotFound {
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/1-perfect.txt"});
		(new CmdSchedule()).execute(new String[]{"schedule"});
		boolean expected = true;
		boolean actual = system.openCase(8);
		assertEquals(expected, actual);
		actual = system.openCase(1);
		expected = false;
		assertEquals(expected, actual);
	}
	
	@Test
	void testMainSystem_08_close() throws ExCaseNotFound {
		boolean expected = false;
		boolean actual = system.close();
		assertEquals(expected, actual);
		
		actual = system.openAlgo(AlgorithmType.FCFS);
		actual = system.close();
		expected = true;
		assertEquals(expected, actual);
		
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/1-perfect.txt"});
		(new CmdSchedule()).execute(new String[]{"schedule"});
		actual = system.openCase(0);
		actual = system.close();
		expected = true;
		assertEquals(expected, actual);
	}
	
	@Test
	void testMainSystem_09_display() throws Exception {
		
		// Set the System.out output
		setOutput();
		
		String expected = "Scheduled cases:\n"
				+ "Unscheduled files: 0\n";
		system.display();
		String actual = getOutput();
		assertEquals(expected, actual);
		
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/1-perfect.txt"});
		(new CmdSchedule()).execute(new String[]{"schedule"});
		boolean wws = system.openCase(3);
		system.display();
		actual = getOutput();
		expected = "Scheduled cases:\n"
				+ "Unscheduled files: 0\n";
		System.out.println("WWWWWW" + actual + "WWWWW");
		assertEquals(expected, actual);
		
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/1-perfect.txt"});
		(new CmdSchedule()).execute(new String[]{"schedule"});
		wws = system.openCase(1);
		wws = system.openAlgo(AlgorithmType.FCFS);
		system.display();
		actual = getOutput();
		expected = "Scheduled cases:\n"
				+ "Unscheduled files: 0\n";
		System.out.println("WWWWWW" + actual + "WWWWW");
		assertEquals(expected, actual);
		
		
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/1-perfect.txt"});
		(new CmdSchedule()).execute(new String[]{"schedule"});
		wws = system.close();
		wws = system.close();
		wws = system.openAlgo(AlgorithmType.FCFS);
		system.display();
		actual = getOutput();
		expected = "Scheduled cases:\n"
				+ "Unscheduled files: 0\n";
		System.out.println("WWWWWW" + actual + "WWWWW");
		assertEquals(expected, actual);
		
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/1-perfect.txt"});
		(new CmdSchedule()).execute(new String[]{"schedule"});
		wws = system.close();
		wws = system.close();
		system.display();
		actual = getOutput();
		expected = "Scheduled cases:\n"
				+ "Unscheduled files: 0\n";
		System.out.println("WWWWWW" + actual + "WWWWW");
		assertEquals(expected, actual);
		
	}
	
	@Test
	void testMainSystem_10_suggest() {
		HashMap<Integer, ArrayList<AlgorithmType>> expected = new HashMap<>();
		ArrayList<AlgorithmType> wws = new ArrayList<>();
		wws.add(AlgorithmType.SPN);
		wws.add(AlgorithmType.SRT);
		expected.put(1, wws);
		expected.put(2, wws);
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/sample_1.txt"});
		(new CmdReadFile()).execute(new String[]{"readfile", "./src/TestSamples/sample_3.txt"});
		(new CmdSchedule()).execute(new String[]{"schedule"});
		HashMap<Integer, ArrayList<AlgorithmType>> actual = system.suggest();
		assertEquals(expected, actual);
	}
	
	@Test
	void testMainSystem_11_changeBestIndicator() {
		boolean expected = true;
		boolean actual = system.changeBestIndicator("AQT");
		assertEquals(expected, actual);
		
		expected = false;
		actual = system.changeBestIndicator("WWS");
		assertEquals(expected, actual);
	}
	
	@Test
	void testMainSystem_12_stopReading() {
		system.startReading();
		system.stopReading(false);
		system.getAllInputs();
	}
	
	
	
	// Handling console output
	PrintStream oldPrintStream;
	ByteArrayOutputStream bos;

	  private void setOutput() throws Exception {
	    oldPrintStream = System.out;
	    bos = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(bos));
	  }

	  private String getOutput() { // throws Exception
	    System.setOut(oldPrintStream);
	    return bos.toString();
	  }
	
}
