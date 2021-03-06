package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Commands.CmdReadFile;
import Commands.CmdSchedule;
import Commands.CmdSuggest;
import Project.MainSystem;

class TestCmdSuggest {
	
	@BeforeEach
	void setup() {
		MainSystem system = MainSystem.getInstance();
		system.clear();
	}

	@Test
	void testCmdSuggestAtStart() throws Exception {
		setOutput();
		
		String[] cmdParts = new String[] {"suggest"};
		(new CmdSuggest()).execute(cmdParts);
		
		String expected = "The best performing algorithm(s):\n"
				+ "Case           Best algorithms\n";
		
		String actual = getOutput();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testCmdSuggestAfterSchedule() throws Exception {
		setOutput();
		
		String[] cmdParts = new String[]{"readfile","./src/TestSamples/sample_0.txt"};
		(new CmdReadFile()).execute(cmdParts);
		cmdParts = new String[]{"readfile","./src/TestSamples/sample_1.txt"};
		(new CmdReadFile()).execute(cmdParts);
		
		cmdParts = new String[]{"schedule"};
		(new CmdSchedule()).execute(cmdParts);
		
		cmdParts = new String[]{"suggest"};
		(new CmdSuggest()).execute(cmdParts);
		
		String expected = "2 unscheduled inputs scheduled successfully.\n"
				+ "The best performing algorithm(s): Shortest Remaining Time Next\n"
				+ "Case           Best algorithms\n"
				+ "Case #0        SRT\n"
				+ "Case #1        SPN, SRT\n";
		
		String actual = getOutput();
		
		assertEquals(expected, actual);
	}
	
	// Handling console output
	PrintStream oldPrintStream;
	ByteArrayOutputStream bos;
	
	private void setOutput() throws Exception {
		oldPrintStream = System.out;
		bos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bos));
	}
	
	private String getOutput() {
		System.setOut(oldPrintStream);
		return bos.toString();
	}
}
