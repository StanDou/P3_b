/**
 * Filename:   MyProfiler.java
 * Project:    p3b-201901     
 * Authors:    TODO: add your name(s) and lecture numbers here
 *
 * Semester:   Spring 2019
 * Course:     CS400
 * 
 * Due Date:   TODO: add assignment due date and time
 * Version:    1.0
 * 
 * Credits:    TODO: name individuals and sources outside of course staff
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

// Used as the data structure to test our hash table against
import java.util.TreeMap;
/**
 * This is the class used for testing
 * the performance of custom HashTable and
 * java built-in TreeMap
 * @author SHAO BIN DANIEL SHI HONG
 *
 * @param <K> KEY
 * @param <V> VALUE
 */
public class MyProfiler<K extends Comparable<K>, V> {

    HashTable<K, V> hashtable;
    TreeMap<K, V> treemap;
    /**
     * This constructor initialize the HashTable
     * and TreeMap
     */
    public MyProfiler() {
       hashtable = new HashTable<K,V>();
       treemap = new TreeMap<K,V>();
    }
    /**
     * This method insert to both datastructure
     * 
     * @param key
     * @param value
     */
    public void insert(K key, V value) {
      try {
        
        hashtable.insert(key, value);
        treemap.put(key, value);
        
      } catch (IllegalNullKeyException | DuplicateKeyException e) {
       
        e.printStackTrace();
      }
        
    }
    
    public void retrieve(K key) {
        try {
          
          hashtable.get(key);
          treemap.get(key);
          
        } catch (IllegalNullKeyException | KeyNotFoundException e) {
          
          e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) {
        try {
            
            int numElements = Integer.parseInt(args[0]);
            //create new profile
            MyProfiler<Integer,Integer>  profile = new MyProfiler<Integer,Integer>();
            
            //Insert many data for testing
            for(int i = 0; i < numElements ; i++) {
              profile.insert(i, i);
            }
            
            //retrieve all data for testing
            for(int j = 0; j < numElements; j++) {
              profile.retrieve(j);
              
            }
            
            
            
            // TODO: complete the main method. 
            // Create a profile object. 
            // For example, Profile<Integer, Integer> profile = new Profile<Integer, Integer>();
            // execute the insert method of profile as many times as numElements
            // execute the retrieve method of profile as many times as numElements
            // See, ProfileSample.java for example.
            
        
            String msg = String.format("Inserted and retreived %d (key,value) pairs", numElements);
            System.out.println(msg);
        }
        catch (Exception e) {
            System.out.println("Usage: java MyProfiler <number_of_elements>");
            System.exit(1);
        }
    }
}
