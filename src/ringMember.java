

public interface ringMember extends java.rmi.Remote {
    /**
     * Method to pass tokens and which filename to write it to
     *
     * @param item
     * @param filename
     * @throws java.rmi.RemoteException
     */
    void takeToken(Token item, String filename) throws java.rmi.RemoteException;

}
