/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsOperations;

/**
 *
 * @author chuzhan
 */
public class ThreadPool {
    public static void main(String[] args){
        threadPoolExecutor poolExecutor = new threadPoolExecutor(10, 100, "http://localhost:8080/Assignment1/webresources", "8080");
        poolExecutor.initializeThreads();
    }
}
