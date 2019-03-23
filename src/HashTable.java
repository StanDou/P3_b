import java.util.LinkedList;
//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           HashTable
// Files:           HashTable.java
// Course:          cs400, Spring, 2019 
//
// Author:          SHAO BIN DANIEL SHI HONG
// Email:           Shong78@wisc.edu
// Lecturer's Name: Andy Kuemmel
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:  none
// Partner Email:   none
// Partner Lecturer's Name: none
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//    ___ Write-up states that pair programming is allowed for this assignment.
//    ___ We have both read and understand the course Pair Programming Policy.
//    ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here.  Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do.  If you received no outside help from either type
//   of source, then please explicitly indicate NONE.
//
// Persons:         (identify each person and describe their help in detail)
// Online Sources:  (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

// TODO: comment and complete your HashTableADT implementation
// DO ADD UNIMPLEMENTED PUBLIC METHODS FROM HashTableADT and DataStructureADT TO YOUR CLASS
// DO IMPLEMENT THE PUBLIC CONSTRUCTORS STARTED
// DO NOT ADD OTHER PUBLIC MEMBERS (fields or methods) TO YOUR CLASS
//
// TODO: implement all required methods
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// TODO: explain your hashing algorithm here
// NOTE: you are not required to design your own algorithm for hashing,
// since you do not know the type for K,
// you must use the hashCode provided by the <K key> object
// and one of the techniques presented in lecture
//
/**
 * This is the class represents the HashTable
 * 
 * @author SHAO BIN DANIEL SHI HONG
 *
 * @param <K> Type of Key
 * @param <V> Type of value associated with key
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {
  /**
   * KeyNode is the class associates Key and Value
   * 
   * @author SHAO BIN DANIEL SHI HONG
   *
   * @param <K> Type of Key
   * @param <V> Type of value associated with key
   */
  private class KeyNode<K, V> {
    public K key;
    public V value;
    public KeyNode<K, V> nextNode;
  }

  // TODO: ADD and comment DATA FIELD MEMBERS needed for your implementation

  // TODO: comment and complete a default no-arg constructor

  double loadFactorThreshold; // The threshold that determines when to resize
  private int capacity; // Capacity determines how many keys the hashTable could contain
  private int numkeys; // number of keys currently stored in Hashtable
  private KeyNode<K, V>[] table; // the hashtable

  /**
   * No argument Constructor
   */
  public HashTable() {
    // initialize a reasonable and efficient hashtable
    table = new KeyNode[31]; // prime-number-sized table
    loadFactorThreshold = 0.75; // Good LF
    numkeys = 0; // set numkeys to 0
    capacity = 31; // size of the table


  }



  /**
   * This constructor would create the hash table designed based on the passed-in argument
   * 
   * @param initialCapacity     the Initial size of hash table
   * @param loadFactorThreshold determines when we should resize the table and rehash keys.
   */
  public HashTable(int initialCapacity, double loadFactorThreshold) {

    table = new KeyNode[initialCapacity]; // create table based on given info
    this.loadFactorThreshold = loadFactorThreshold;// set LFThreshold based on given info

    numkeys = 0; // the table is originally empty
    capacity = initialCapacity; // set capacity

  }


  /**
   * Add the key,value pair to the data structure and increase the number of keys. If key is null,
   * throw IllegalNullKeyException; If key is already in data structure, throw
   * DuplicateKeyException();
   */
  @Override
  public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {

    if (key == null) {
      throw new IllegalNullKeyException();
    }

    int hashcode = key.hashCode(); // obtains hash code of Key;
    if (hashcode < 0) { // negative hashcode is not allowed
      hashcode *= -1;
    }
    int target_index = hashcode % capacity;// decode the hash code to be index

    // creates new node associated with the given
    // key and value

    KeyNode<K, V> node = new KeyNode();
    node.key = key;
    node.value = value;
    node.nextNode = null;

    if (table[target_index] != null) {

      // This method would append
      // the node to the end of node list
      // return false if duplicate key
      if (!insertNode(table[target_index], node)) {
        throw new DuplicateKeyException();
      }


    } else {

      table[target_index] = node; // store the list at target index
    }

    numkeys++;

    // check if this insertion would cause LFThreshold violation
    if (getLoadFactor() >= getLoadFactorThreshold()) {

      reSize(); // this method will resize the whole table
                // and rehash reach node .
    }

  }

  /**
   * This is the private helping method help to append a Node at the end of node list
   * 
   * @param current       current Node we are looking at, we will check if it's next is null
   * @param node_Inserted the Node that we want to insert
   * @throws DuplicateKeyException
   */
  private boolean insertNode(KeyNode<K, V> current, KeyNode<K, V> node_Inserted) {
    if (current.nextNode == null) {
      if (current.key == node_Inserted.key) {
        return false;
      }
      current.nextNode = node_Inserted; // append the node to the end of node list;
      return true;
    } else {
      return insertNode(current.nextNode, node_Inserted); // checking the next node for insertion
    }


  }

  /**
   * This method resize the table and rehash all nodes;
   */
  private void reSize() {

    int new_size = this.capacity * 2 + 1;
    this.capacity = new_size; // updates new capacity
    KeyNode<K, V>[] new_table = new KeyNode[new_size];

    for (int i = 0; i < table.length; i++) { // iterate through each nodeList in the array
      // and rehash every Node
      if (table[i] != null) {

        reHash(new_table, table[i]);
      }
    }
    this.table = new_table; // set up the new_table


  }

  /**
   * This method rehash the given node to a new table
   * 
   * @param              new_table, new table formed after hitting LFThreshold
   * @param current_Node node we want to rehash
   */
  private void reHash(KeyNode<K, V>[] new_table, KeyNode<K, V> current_Node) {
    int hashCode;
    int index;
    if (current_Node.nextNode == null) {
      // This is the last node we need to rehash
      hashCode = current_Node.key.hashCode();

      if (hashCode < 0) {// negative hashCode is not allowed
        hashCode *= -1;
      }

      index = hashCode % new_table.length;

      // make a copy of current_node

      KeyNode<K, V> new_node = new KeyNode();
      new_node.key = current_Node.key;
      new_node.value = current_Node.value;
      new_node.nextNode = null;

      if (new_table[index] == null) {

        new_table[index] = new_node;

      } else {
        // this method would append the new node
        // to the nodeList
        insertNode(new_table[index], new_node);
      }

      return;

    } else {
      //key rehashing
      reHash(new_table, current_Node.nextNode);

    }

  }



  /**
   * If key is found, remove the key,value pair from the data structure decrease number of keys.
   * return true If key is null, throw IllegalNullKeyException If key is not found, return false
   */
  @Override
  public boolean remove(K key) throws IllegalNullKeyException {
    // 3 cases
    // 1. only one node
    // 2. a node list, but remove head
    // 3. linkedlist node not head
    if (key == null) {
      throw new IllegalNullKeyException();
    }

    int hashCode = key.hashCode();

    if (hashCode < 0) {// negative hashcode is not allowed
      hashCode *= -1;
    }

    int index = hashCode % capacity; // decode hashcode to index

    if (table[index] == null) {
      return false;
    }

    if (table[index].nextNode == null) { // This is the only node at this index.

      if (table[index].key == key) {

        table[index] = null;
        numkeys--; 

        return true;

      } else {

        return false;

      }

    } else {
      if (table[index].key == key) { // the node to be removed is head of list

        table[index] = table[index].nextNode;
        numkeys--;
        return true;

      } else { // find the node and delete the node from the list
        return removeNode(table[index], key);
      }

    }


  }

  /**
   * This is the private helper method helping to remove the node with specified key
   * 
   * @param current
   * @param key
   * @return
   */
  private boolean removeNode(KeyNode<K, V> current, K key) {

    if (current == null) {
      return false;  // no matching key
    } else if (current.nextNode.key == key) {
     
      current.nextNode = current.nextNode.nextNode;
      numkeys--;
      return true;
      
    } else {
      // keep checking nextNode
      return removeNode(current.nextNode, key);

    }



  }

  /**
   * Returns the value associated with the specified key Does not remove key or decrease number of
   * keys If key is null, throw IllegalNullKeyException If key is not found, throw
   * KeyNotFoundException().
   */
  @Override
  public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {

    if (key == null) {
      throw new IllegalNullKeyException();
    }

    int hashCode = key.hashCode();

    if (hashCode < 0) {// negative hashcode is not allowed
      hashCode *= -1;
    }

    int index = hashCode % capacity; // decode hashcode to index

    if (table[index] == null) {
      throw new KeyNotFoundException();
    } else {

      V value = helpGet(table[index], key);
      if (value == null) {
        throw new KeyNotFoundException();
      } else {
        return value;
      }
    }

  }

  /**
   * This is the helper method helping to get the value of the node with matching key
   * 
   * @param current
   * @param key
   * @return
   */
  private V helpGet(KeyNode<K, V> current, K key) {
    if (current == null) {

      return null;

    } else if (current.key == key) {

      return current.value;

    } else {

      return helpGet(current.nextNode, key);

    }


  }
  /**
   * This method returns number of keys 
   * in the Hash Table
   */
  @Override
  public int numKeys() {

    return this.numkeys;
  }
  /**
   * This method returns the loadFactorThreshold
   */
  @Override
  public double getLoadFactorThreshold() {

    return this.loadFactorThreshold;
  }
  /**
   * This method returns the current loadFactor
   */
  @Override
  public double getLoadFactor() {

    return ((double) numkeys / (double) capacity);
  }
  /**
   * This method returns the capacity of table
   */
  @Override
  public int getCapacity() {

    return capacity;
  }

  /**
   * return 5 : an array of linkedNodes
   */
  @Override
  public int getCollisionResolution() {

    return 5;
  }

  // TODO: add all unimplemented methods so that the class can compile



}
