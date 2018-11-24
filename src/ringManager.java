
import java.io.FileWriter;
import java.net.InetAddress;
import java.rmi.Naming;

import java.util.Date;


public class ringManager {

	/*
	To Run in terminal
	cd bin folder
   start rmiregistry
   java -Djava.security.policy=java.policy ringMemberImpl one localhost two
   java -Djava.security.policy=java.policy ringMemberImpl two localhost three
   java -Djava.security.policy=java.policy ringMemberImpl three localhost one
   java -Djava.security.policy=java.policy ringManager 127.0.0.1 one ringManager.txt 25 three two
	 */

    /**
     * Constructor for ringMananger
     *
     * @param ringNodeHost
     * @param ringNodeId
     * @param fileName
     * @param circles
     * @param extraTimeNode
     * @param SkipNode
     */
    public ringManager(String ringNodeHost, String ringNodeId, String fileName, int circles, String extraTimeNode, String SkipNode) {

        // Next member of the node
        ringMember ringMember;
        // This starts the security manager
        System.setSecurityManager(new SecurityManager());

        try {
            // This sets up the host and where the manager is running
            InetAddress inetAddress = InetAddress.getLocalHost();
            String hostName = inetAddress.getHostName();
            System.out.println("Ring manager host is " + hostName);
            System.out.println("Ring element host is " + ringNodeHost);

            // Create fileWriter and clear file
            System.out.println("Clearing " + fileName + " file");
            try {
                //Clears the file
                FileWriter fileWriter = new FileWriter(fileName, false);
                fileWriter.close();
            } catch (java.io.IOException e) {
                System.out.println("Error with File: " + e);
            }
            // Creates a new node in the ring
            ringMember = (ringMember) Naming.lookup("rmi://" + ringNodeHost + "/" + ringNodeId);
            // Gets the token to pass on
            Token token = new Token("Token - ringManager", 0, circles, ringNodeId, extraTimeNode, SkipNode);
            // This passes the token onto the next node by getting remote reference to ring element/node and inject token b
            //Passing on the Token and filename
            ringMember.takeToken(token, fileName);
            
            
        } catch (Exception e) {
            System.err.println("Error: " + e + " in ringManager");
        }
        System.out.println("Connecting to Node");
    } // End of Constructor

    public static void main(String argv[]) {
        //So the user does not put in two few/many parameters
        if ((argv.length < 6) || (argv.length > 6)) {
            System.out.println("Usage: [Host][ID][filename] [Circles] [extra time node] [skip] ");
            System.out.println("Only " + argv.length + " parameters entered");
            System.exit(1);
        }
        //Setting the Parameters
        String ringNodeHost = argv[0];
        String ringNodeId = argv[1];
        String fileName = argv[2];
        int circles = Integer.parseInt(argv[3]);
        String extraTimeNode = argv[4];
        String skipNode = argv[5];
        try {
            //Instantiate ringManager with parameters
            ringManager ringManger = new ringManager(ringNodeHost, ringNodeId, fileName, circles, extraTimeNode, skipNode);
            System.out.println("Ring Manager hostname is " + ringNodeHost);
            System.out.println("Ring Node ID is " + ringNodeId);
        } catch (Exception e) {
            System.err.println("RingManger error, main method :" + e);
        }

    }


}