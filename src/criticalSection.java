
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Date;
import java.rmi.*;

public class criticalSection extends Thread {
    // Declaring global Variables
    private String thisId;
    private String thisHost;
    private String nextId;
    private String nextHost;
    private Token token; // This is what is passed in the Ring
    private String fileName; // The file we write to.

    /**
     * Critical Section Constructor
     *
     * @param tHost
     * @param tId
     * @param nHost
     * @param nId
     * @param token
     * @param fileName
     */
    public criticalSection(String tHost, String tId, String nHost, String nId, Token token, String fileName) {
        // Setting Variables
        thisHost = tHost;
        thisId = tId;
        nextHost = nHost;
        nextId = nId;
        this.fileName = fileName;
        this.token = token;
    }// End of Constructor

    public void run() {
        //Skipping a Node (Advanced feature 5)
        if (!token.skip(thisId)  ) {
            //token.setCurrentNoOfCirculates(token.getCurrentNoOfCircluates()+1);
        	if((token.getCounter() < token.getMaxNoOfCirculates()))
        	{
        		process();
        	}
            
        }//End of Skip if statement
        propagate(); //This is the method which sends on tokens. 
    } // End of Run Method
    
    //CleanUp (Advanced Feature 6) - Ends each node when reached maximum one by one. 
    private void die() {
    	token = token.cleanUp(); //This is creating the destroy token
        if (!token.keepAlive()) {
			try {
				if(Naming.list("rmi://"+thisHost+"/").length > 1)
				{
					System.out.println("Ring member "+ thisId + " bites the dust");
					ringMember ringMember = (ringMember) Naming.lookup("rmi://" + nextHost + "/" + nextId);
					ringMember.takeToken(token, fileName); //Passes the destroy token onto the next member
					System.exit(0); //Ends the current member
				} 
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				System.exit(0);
				e.printStackTrace();
			}
        }
    }

    /**
     * Method where the main process happens, this is where it is written to a file and extra time allowed for certain nodes. 
     * @return void
     */
    private void process() {
        try {
            System.out.println("Writing to file: " + fileName);
            //Works out the time
            Date timestmp = new Date();
            String timestamp = timestmp.toString();
            // Creating File writer, this is what writes to the file
            FileWriter fileWriter = new FileWriter(fileName, true);
            // New printwriter - adds whats in the filewriter to the document
            PrintWriter printWriter = new PrintWriter(fileWriter, true);
            try {
                //Extra Time for certain nodes (Advanced Feature 4)
                if (token.getMoreTimeNodeId().equals(thisId)) {
                    Thread.sleep(1000);
                    System.out.println("Extra time has been awarded to: " + token.getMoreTimeNodeId());
                } else {
                    // To slow the program done so it's more readable
                    Thread.sleep(500);
                } //End of extra time If statement
            } catch (InterruptedException e) {
                e.printStackTrace();
            }//End of try/catch

            //Adds to the file
            printWriter.println("Record from ring node on Host: " + thisHost + ", ID: " + thisId + ", at what time: " + timestamp + ", Token: " + token.getTokenName() + ", Counter: " + token.getCounter());
            //Closes the print and file writer
            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + fileName + " in takeToken method:- " + e);
        }//End of try/catch
    }

    /**
     * This sets up the host and passes tokens along 
     * @return void
     */
    private void propagate() {
        //This Increments a token, Passes to next Node,
        // Gets a token. Increments token count. Passes token to the next node.

        try {
            //This checks whether the token has ran over its certain number of circles (Advanced Feature 3)
            if (token.getCounter() < token.getMaxNoOfCirculates()) {
            	System.out.println("Look up RMIregistry with: rmi://" + nextHost + "/" + nextId);
            	//Sets up a ring member
            	ringMember ringMember = (ringMember) Naming.lookup("rmi://" + nextHost + "/" + nextId);
                //Increments the token counter (Advanced Feature 1)
            	System.out.println("Received token count value is: " + token.increamentCounter());
                System.out.println("Token received: entering critical region");
                System.out.println("Token id is: " + token.getTokenName());
                //Passing the token on
                ringMember.takeToken(token, fileName);
                System.out.println("Token released: exiting critical region");
                System.out.println("----------------------------------------");
                System.out.println("");
            } else {
                System.err.println("It has reached the maximum number of circulates which is :" + token.getMaxNoOfCirculates());
                die(); //When maximum is met, what kills it
            }
        } catch (Exception e) {
            System.err.println("RMIregistry lookup failed: " + e);
        } // End of try/catch
    }//End of Propagate method

} // End of Class