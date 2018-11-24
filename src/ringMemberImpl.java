
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.*;

public class ringMemberImpl extends java.rmi.server.UnicastRemoteObject implements ringMember {
    //Declaring Global Variables
    private static String nextId;
    private static String nextHost;
    private static String thisId;
    private static String thisHost;
    private criticalSection c;
    private String token;

    /**
     * ringMemeberImpl constructor
     *
     * @param tNode
     * @param tId
     * @param nNode
     * @param nId
     * @throws RemoteException
     */
    public ringMemberImpl(String tNode, String tId, String nNode, String nId) throws RemoteException {
        thisHost = tNode;
        thisId = tId;
        nextHost = nNode;
        nextId = nId;
    }//End of Constructor

    /**
     * This method takes a token from one node and passes it to another
     * @param token
     * @param filename
     * @throws RemoteException
     */
    public synchronized void takeToken(Token token, String filename) throws RemoteException {
        //To check whether the token is null
        if (this.token == null) {
            this.token = token.getTokenName();
        }
        //Starting the Critical Section thread
        c = new criticalSection(thisHost, thisId, nextHost, nextId, token, filename);
        c.start();
        System.out.println("Exiting method: takeToken: ringMemberImpl");

    } //End of takeToken Method

    //Main Method
    public static void main(String argv[]) {

        //Sets up a client - ring member
        try {
            //Connects client to host
            InetAddress inetAddress = InetAddress.getLocalHost();
            String hostName = inetAddress.getHostName();
            System.out.println("Ring manager host is " + hostName);

            //If statement that makes sure that the user enters 3 non-optional parameters values
            if ((argv.length < 4) || (argv.length > 4)) {
                System.out.println("Usage: [this node ID][next host][next node ID]");
                System.out.println("Only " + argv.length + " parameters entered");
                System.exit(1);
            }
            //Setting the parameters
            String thisId = argv[0];
            String thisHost = argv[1];
            String nextId = argv[2];
            String nextHost = argv[3];

            //Registering a RMI Security Manager
            System.setSecurityManager(new RMISecurityManager());

            //Creating a ringmember
            ringMemberImpl ringMember = new ringMemberImpl(thisHost, thisId, nextHost, nextId);
            System.out.println("ring member: " + thisHost + " binding to RMI registry");
            Naming.rebind("//" + thisHost + "/" + thisId, ringMember);
            System.out.println("Ring element " + thisHost + "/" + thisId + " is bound with RMIregistry");
            System.out.println("Server Bound");
        } catch (Exception e) {
            System.err.println("Error in ringMemberImpl method: " + e);
            e.printStackTrace();
        }//End of Try/catch
        System.out.println("Look up RMIRegistry with: rmi//" + thisHost + "/" + thisId);
    }//End of Main Method

}//End of ringMemberImpl class