/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsOperations;
 
import java.util.concurrent.ExecutorService;   
import java.util.concurrent.Executors;
import com.mycompany.assignment1.NewJerseyClient;
import java.util.Arrays;
//import java.util.ArrayList;
/**
 *
 * @author chuzhan
 */
public class threadPoolExecutor {
    private NewJerseyClient client;
    private int THREADAMOUNT = 10;
    private int ITERATIONTIMES = 100;
    private String IPADDRESS = "http://localhost:8080/Assignment1/webresources";
    private String PORT = "8080";
    public static int totalRequest;
    public static int totalResponse;
    public Long[] latency;
    public int requestFinishedTime;
    //public Long totalLatencyTime;
    
    
    public threadPoolExecutor(int threadAmount, int iterationTimes, String ipAddress, String port){
        client = new NewJerseyClient();
        THREADAMOUNT = threadAmount;
        ITERATIONTIMES = iterationTimes;
        IPADDRESS = ipAddress;
        PORT = port;
        latency = new Long[THREADAMOUNT*ITERATIONTIMES*2];
        //REQUESTLOCK = new Object();
    }
    
    public  void initializeThreads(){
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(THREADAMOUNT);
        double startTime = System.currentTimeMillis()/1000.0;
        //System.out.println("Times:  " + startTime);
        System.out.println("All threads running ....");
   
        for(int i=0; i<THREADAMOUNT; i++){
            fixedThreadPool.execute(new Runnable(){
            @Override
            public void run(){
                for(int i=0; i<ITERATIONTIMES; i++){
                    long getStart = System.currentTimeMillis();
                    String currentGet = client.getText();
                    if(currentGet.equals("alive")){
                        totalResponseAdd();
                    }
                    long getEnd = System.currentTimeMillis();
                    latenctAdd(getEnd - getStart);
                    //System.out.println(getEnd - getStart +  "   " + requestFinishedTime);
                    totalRequestAdd();
                    long postStart = System.currentTimeMillis();
                    client.putText("run", Integer.class);
                    long postEnd = System.currentTimeMillis();
                    latenctAdd(postEnd - postStart);
            }
        }
     }); 
      }
        
    fixedThreadPool.shutdown();
    while(true){
        if(fixedThreadPool.isTerminated()){
            //System.out.println("client   " + THREADAMOUNT +"   " + ITERATIONTIMES + "  " + IPADDRESS + "  " + PORT);
            //System.out.println("All threads complete");
            double endTime = System.currentTimeMillis()/1000.0;
            //System.out.println("Time: " + endTime);
            System.out.println("Total number of requests sent  " + totalRequest);
            System.out.println("Total number of Successful responses  " + totalResponse);
            System.out.println("Test Wall Time: " + (endTime - startTime));
            System.out.println(requestFinishedTime);
            Arrays.sort(latency);
            System.out.println("Median of all letencies: " + latency[requestFinishedTime/2] + "   mileseconds");
            long totalLatencyTime = 0;
            for(long lantencyy : latency){
                totalLatencyTime += lantencyy;
            }
            System.out.println("Mean of all letencies:  " + totalLatencyTime/(latency.length) + "   mileseconds");
            int nfprecentile = (int) (requestFinishedTime*0.95);
            int nnprecentile = (int) (requestFinishedTime*0.99);
            System.out.println("95 percentile latency is:  " + latency[nfprecentile]);
            System.out.println("99 percentile latency is:  " + latency[nnprecentile]);
            break;
        }
    }
    
    }
   
    
    private synchronized void totalRequestAdd() {
        totalRequest++;
    }
    
    private synchronized void totalResponseAdd() {
        totalResponse++;
    }
    
    private synchronized void latenctAdd(long latencyTime) {
        latency[requestFinishedTime++] = latencyTime;
    }
    
}