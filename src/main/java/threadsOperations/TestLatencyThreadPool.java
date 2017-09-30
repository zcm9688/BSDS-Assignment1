/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsOperations;

import java.util.concurrent.ExecutorService;   
import java.util.concurrent.Executors;
import com.mycompany.assignment1.NewJerseyClient;
import java.util.ArrayList;
/**
 *
 * @author chuzhan
 */
public class TestLatencyThreadPool {
    private NewJerseyClient client;
    private int THREADAMOUNT = 10;
    private int ITERATIONTIMES = 100;
    private String IPADDRESS = "http://localhost:8080/Assignment1/webresources";
    private String PORT = "8080";
    public static int totalRequest;
    public static int totalResponse;
    public ArrayList<Double> latency; 
    
    
    public TestLatencyThreadPool(int threadAmount, int iterationTimes, String ipAddress, String port){
        client = new NewJerseyClient();
        THREADAMOUNT = threadAmount;
        ITERATIONTIMES = iterationTimes;
        IPADDRESS = ipAddress;
        PORT = port;
        latency = new ArrayList<>();
        //REQUESTLOCK = new Object();
    }
    
    public  void initializeThreads(){
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(THREADAMOUNT);
        double startTime = System.currentTimeMillis()/1000.0;
        System.out.println("Times:  " + startTime);
        System.out.println("All threads running ....");
   
        for(int i=0; i<THREADAMOUNT; i++){
            fixedThreadPool.execute(new Runnable(){
            @Override
            public void run(){
                for(int i=0; i<ITERATIONTIMES; i++){
                    double getStart = System.currentTimeMillis()/1000.0;
                    String currentGet = client.getText();
                    if(currentGet.equals("alive")){
                        totalResponseAdd();
                    }
                    double getEnd = System.currentTimeMillis()/1000.0;
                    latency.add(getEnd - getStart);
                    totalRequestAdd();
                    double postStart = System.currentTimeMillis()/1000.0;
                    client.putText("run", Integer.class);
                    double postEnd = System.currentTimeMillis()/1000.0;
                    latency.add(postEnd - postStart);
            }
        }
     }); 
      }
        
    fixedThreadPool.shutdown();
    while(true){
        if(fixedThreadPool.isTerminated()){
            System.out.println("client   " + THREADAMOUNT +"   " + ITERATIONTIMES + "  " + IPADDRESS + "  " + PORT);
            //System.out.println("All threads complete");
            double endTime = System.currentTimeMillis()/1000.0;
            //System.out.println("Time: " + endTime);
            System.out.println("Total number of requests sent  " + totalRequest);
            System.out.println("Total number of Successful responses  " + totalResponse);
            System.out.println("Test Wall Time: " + (endTime - startTime));
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
    
    
}
