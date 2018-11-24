# V5 Distributed Operating Systems Assignment

## Anna Frances Rasburn (2411187)

### How to Run this Application
####To Run in Terminal
* Firstly you need to be in the bin folder
* In terminal you start the rmiregistry
* In its own terminal run a java -Djava.security command (seen below)
```bash
   cd bin folder
   start rmiregistry
   java -Djava.security.policy=java.policy ringMemberImpl one localhost two localhost
   java -Djava.security.policy=java.policy ringMemberImpl two localhost three localhost
   java -Djava.security.policy=java.policy ringMemberImpl three localhost one localhost
   java -Djava.security.policy=java.policy ringManager 127.0.0.1 one ringManager.txt 25 three two
```



