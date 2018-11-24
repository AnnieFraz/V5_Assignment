public class Token implements java.io.Serializable {

    private final boolean keepAlive;
    private String tokenName; //This is the name of the token
    private int counter; //This is a counter to show how many times the token has been called
    private String skipTurnID; //This is to work out which node to skip
    private int maxNoOfCirculate; //How many time a token is passed
    private int currentNoOfCirculates; //Currently
    private String moreTimeNodeId;//To work out which node that needs to get more time
    private String currentNodeId; //This variable gets the current node that the token was used by
    
  

    /**
     * Constructor: Token - has everything in it
     *
     * @param tokenName
     * @param counter
     * @param maxNoOfCirculate
     * @param startNodeID
     * @param extraTimeNode
     * @param skipNode
     */
    public Token(String tokenName, int counter, int maxNoOfCirculate, String startNodeID, int currentNoOfCirculates, String extraTimeNode, String skipNode, boolean keepAlive) {
        this.tokenName = tokenName;
        this.counter = counter;
        this.skipTurnID = skipNode;
        this.maxNoOfCirculate = maxNoOfCirculate;
        this.currentNoOfCirculates = currentNoOfCirculates;
        this.moreTimeNodeId = extraTimeNode;
        this.currentNodeId = startNodeID;
        this.keepAlive = keepAlive;
    }//End of Constructor

    public Token(String tokenName, int counter, int maxNoOfCirculate, String startNodeID, String extraTimeNode, String skipNode) {
        this(tokenName, counter, maxNoOfCirculate, startNodeID, 0, extraTimeNode, skipNode, true);
        //this Token is keeping things alive

    }

    public Token(Token token, String tokenName, boolean keepAlive){
        //This token kills things
        this(tokenName, token.counter, token.maxNoOfCirculate, token.currentNodeId, token.currentNoOfCirculates, token.moreTimeNodeId, token.skipTurnID, keepAlive);
    }

    /**
     * This method works out whether to skip the node or not
     *
     * @param thisId
     * @return boolean
     */
    public boolean skip(String thisId) {
        return skipTurnID.equals(thisId) && currentNoOfCirculates % 2 == 0;
    }
    
   

//Naming.list().length

    /**
     * This Increments the counter of the token
     *
     * @return counter
     */
    public int increamentCounter() {
        counter = counter + 1;
        return counter;
    }

    /**
     * This creates a new token to safety stops each node from running
     */
    //TODO: This
    public Token cleanUp() {
        Token endToken = new Token(this, "cleanup", false);
        return endToken; 
    }

    /**
     * Getter method for getTokenName()
     *
     * @return tokenName
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * Setter method for TokenName
     *
     * @param tokenName
     */
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    /**
     * Getter method for counter
     *
     * @return counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Setter method for counter
     *
     * @param counter
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * getter for skip turn ID
     *
     * @return skipTurnID
     */
    public String getSkipTurnID() {
        return skipTurnID;
    }

    /**
     * setter for skip turn ID
     *
     * @param skipTurnID
     */
    public void setSkipTurnID(String skipTurnID) {
        this.skipTurnID = skipTurnID;
    }

    /**
     * GETTER for max number of circles
     *
     * @return maxNoOfCirculate
     */
    public int getMaxNoOfCirculates() {
        return maxNoOfCirculate;
    }

    /**
     * SETTER for max number of circles
     *
     * @param maxNoOfCirculates
     */
    public void setMaxNoOfCirculates(int maxNoOfCirculates) {
        this.maxNoOfCirculate = maxNoOfCirculate;
    }

    /**
     * GETTER for current number of circles
     *
     * @return currentNoOfCirculates
     */
    public int getCurrentNoOfCirculates() {
        return currentNoOfCirculates;
    }

    /**
     * SETTER for current number of circles
     *
     * @param currentNoOfCirculates
     */
    public void setCurrentNoOfCirculates(int currentNoOfCirculates) {
        this.currentNoOfCirculates = currentNoOfCirculates;
    }

    /**
     * Getter for current Node ID
     *
     * @return currentNodeID
     */
    public String getCurrentNodeId() {
        return currentNodeId;
    }

    /**
     * SETTER for current Node Id
     *
     * @param currentNodeId
     */
    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    /**
     * GETTER for more time Node ID
     *
     * @return moreTimeNodeId
     */
    public String getMoreTimeNodeId() {
        return moreTimeNodeId;
    }

    /**
     * SETTER for more time Node ID
     *
     * @param moreTimeNodeId
     */
    public void setMoreTimeNodeId(String moreTimeNodeId) {
        this.moreTimeNodeId = moreTimeNodeId;
    }

    public boolean keepAlive(){
        return keepAlive;
    }
}
