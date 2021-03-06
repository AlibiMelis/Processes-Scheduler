package Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import Exceptions.ExInsufficientCommandArguments;
import Exceptions.ExInvalidInput;
import Exceptions.ExInvalidServiceType;
import Project.Command;
import Project.MainSystem;
import Project.Service;

public class CmdReadFile implements Command {

	@Override
	public void execute(String[] cmdParts) throws ExInsufficientCommandArguments {
		if (cmdParts.length < 2) throw new ExInsufficientCommandArguments("Please, provide filepath.");
		// Reading filepath name from user input
		String filepathname = cmdParts[1];
		
		// File object
        Scanner inFile = null;
        
        MainSystem system = MainSystem.getInstance();
        boolean finished = false;
        system.startReading();
        
        try {
        	// Reading File
        	inFile = new Scanner(new File(filepathname));
        	
        	// Placeholder for number of Processes (Read first line of input file)
        	int numberOfProcesses = Integer.parseInt(inFile.nextLine());
        	if (numberOfProcesses < 0) throw new ExInvalidInput("Number of processes should be non-negative integer");
        	
        	// Iterating through all the processes in file
        	for (int i = 0; i < numberOfProcesses; ++i) {
        		
    			String processLine = inFile.nextLine().trim();
    			
    			String[] processLineParts = processLine.split(" ");
    			
    			if (processLineParts.length != 4) throw new ExInvalidInput("Process Line Should have 4 arguments (e.g. # 0 5 3)");
    			if (!processLineParts[0].equals("#")) throw new ExInvalidInput("Process should be initialized with # character (e.g. # 0 5 3)");
    			if (Integer.parseInt(processLineParts[1]) < 0) throw new ExInvalidInput("Process ID should be non-negative integer");
    			if (Integer.parseInt(processLineParts[2]) < 0) throw new ExInvalidInput("Process Arrival Time should be non-negative integer");
    			if (Integer.parseInt(processLineParts[3]) < 1) throw new ExInvalidInput("Number of services in the process should be positive integer");
    			
    			// Iterating through all services
    			String[] serviceLineParts;    			
    			ArrayList<Service> services = new ArrayList<>();
    			for (int j = 0; j < Integer.parseInt(processLineParts[3]); ++j) {
    				
    				serviceLineParts = inFile.nextLine().trim().split(" ");
    				if (serviceLineParts.length != 2) throw new ExInvalidInput("Service Line Should have 2 arguments (e.g. C 5)");
    				if (!serviceLineParts[0].equals("C") && !serviceLineParts[0].equals("K")) throw new ExInvalidServiceType("Service Type Should be C or K");
    				if (Integer.parseInt(serviceLineParts[1]) < 1) throw new ExInvalidInput("Service time should be positive integer");
					Service s = system.createService(serviceLineParts[0], serviceLineParts[1]);
    				services.add(s);
    			}
    			system.createProcess(processLineParts[1], Integer.parseInt(processLineParts[2]), services);
        	}
        	
        	finished = true;
        	
        	
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
        	System.out.println(e.getMessage());
        } catch (ExInvalidInput e) {
        	System.out.println(e.getMessage());
        } catch(ExInvalidServiceType e) {
        	System.out.println(e.getMessage());
        } finally {
        	system.stopReading(finished);
            if (inFile != null)
            	inFile.close();
        }
		
	}

}
