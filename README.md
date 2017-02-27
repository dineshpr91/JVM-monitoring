# JVM-monitoring
Projects in this repositories:
1) Thread Pool Utility:
This utility is designed to get the thread pool stats in the application. Currently no tool enables the thread pool related information. This is a custom solution to get the thread pool usage. This give the below:
- Thread pool size
- Active threads
- Blocking thread
- Waiting threads
- New created threads

The thread pool information and thread dump can be created using MBeans.The Mbeans can be found in MBeans tab in Jconsole or Visual VM at ThreadMbeanStats -> ThreadPoolAgent.
To use this utility we just need to copy the jar of this project and initialize an object of "com.mbean.stats.starter:CustomMbeanStarter.java" file. This needs to be done only once when the server is started. Jar can be downloaded from thread-pool-utility/target. The jar is compiled on JDK7
