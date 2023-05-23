import java.util.*;
import java.io.*;

/**
 * Class that compresses files into smaller sizes based on the frequency
 * of characters within the file. 
 * @class Data Structures
 * @author Akshan Agarwal
 * @author Henry Russell
 * @time one week
 */
public class HuffmanTree {

    private HuffmanNode root; // the overall root node of the tree

    private static class HuffmanNode implements Comparable<HuffmanNode> {
        
        /* Even though the following fields are declared public, this nested
         * class is itself private. So only the methods inside the TreeIntSet
         * class can actually instantiate TreeNode objects. */
        
        public Integer letter; // the data field
        public HuffmanNode left; // reference to the left subtree
        public HuffmanNode right; // reference to the right subtree
        public int frequency; 

        /**
         * A constructor for nodes midway through the Huffman tree.
         * @param frequency the combined frequency of two child nodes with letters.
         * 
         */
        public HuffmanNode(int frequency) { //constructor without letter
        
            this.letter = -1;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }
        /**
         * A constructor for the leaf nodes of the Huffman tree.
         * @param letter- the letter assigned to this node
         * @param frequency- the frequency with which the letter appears
         */
        public HuffmanNode(int letter, int frequency) { //constructor with letter (for leaves)
            this.letter = letter;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }
        
    /*
     * Method to compare two HuffmanNodes based on their frequency
     * so that the priority queue can order them appropriatley.
     */
        public int compareTo(HuffmanNode other){
            if (this.frequency > other.frequency){
                return 1;
            }
            else if (this.frequency == other.frequency){
                return 0;
            }
            else return -1;
        }
    
    }

     /** Returns a string representation of this tree. 
      * 
      * The elements are returned in ascending order, enclosed in curly braces.
      */
    public String toString() {
        String str = toString(this.root);        
        return str;
    }
    
    
    /** Returns the string representation of the tree rooted at the given node.
      * 
      * Performs an in-order traversal of the supplied tree, so that elements
      * are returned in ascending order.
      * 
      * @param node the root of the tree to be traversed.
      * @return a string representation of the tree rooted at node.
      */
    private String toString(HuffmanNode node) {
        if (node == null)
            return "";
        
        return (toString(node.left) + String.valueOf(node.frequency) + ", " + toString(node.right));
    }
    
    /**
     * Constructs a Huffman coding tree using the given array of frequencies, where 
     * count[i] is the number of occurrences of the character with the ASCII value i.
     * @param count an array of letter frequencies
     */
   
    public HuffmanTree(int[] count){
        PriorityQueue<HuffmanNode> mergingQueue = new PriorityQueue<HuffmanNode>();
        
        for (int i = 0; i< count.length; i++){ // loop through array and add letters w frequencies to PQ
            if(count[i] != 0)
            {
            HuffmanNode letter = new HuffmanNode((char)(i) , count[i]);
            mergingQueue.add(letter); //is this auto sorted?
            }
        }

        HuffmanNode eof = new HuffmanNode(count.length, 1);
        mergingQueue.add(eof);
        constructTree(mergingQueue);
        this.root = mergingQueue.remove();
    }
    /**
     * A helper method to the HuffmanTree constructor that takes a priority queue of nodes
     * ordered by frequency and builds a tree
     * @param q the prioirity queue of nodes ordered by frequency
     */
    public void constructTree(PriorityQueue<HuffmanNode> q){
        if(q.size() == 0){
            return;
        }
        if(q.size() == 1){
            return;
        }
        HuffmanNode Node1 = q.poll();
        HuffmanNode Node2 = q.poll();
        HuffmanNode newNode = new HuffmanNode(Node1.frequency + Node2.frequency);
        newNode.left = Node1;
        newNode.right = Node2;
        q.add(newNode);
        constructTree(q);
    }
    

    
    /**
     * Writes the Huffman tree to the supplied out- put stream using the format specified 
     * in the Implementation Details section. You may as- sume that the supplied output 
     * stream is valid and already open.
     * @param output the PrintStream object that we print to
     */
    public void write(PrintStream output){
        //first number corresponds to ASCII value. After that 1 if we went right and 0 if we went left.
        writeHelper("", output, this.root);
     }
     
     /**
      * A helper method for the write method
      * @param val the line containing information on how to traverse to the leaf node
      * @param output the output that we print to
      * @param node the current node we are getting data from
      */
    public void writeHelper(String val, PrintStream output, HuffmanNode node){
        if(node.left == null && node.right == null){
            output.println(node.letter);
            output.println(val);
            return;
            // System.out.println(node.letter);
            }
      
       if(node.left != null){
            writeHelper(val + "0", output, node.left);
            
        }
        //checks if the right child is null
        if(node.right != null){
            writeHelper(val + "1", output, node.right);
        }  
    }


    
    /**
     * Constructs a Huffman tree from a file that contains the description of a tree 
     * stored in the format specified in the Implementation Details section. You may 
     * assume that the supplied in- put stream is valid and already open.
     * @param input the scanner input
     * @throws IllegalArgumentException
     */
    public HuffmanTree(Scanner input) throws IllegalArgumentException{
        
        this.root = new HuffmanNode(0);
        
        while (input.hasNextLine()){
            // System.out.println("1");
            int letter = Integer.parseInt(input.nextLine());
            String val = input.nextLine();
            // System.out.println(val);
            //checks if the function call returns true or false
            if (buildTree(root, val, letter) == false){
                new IllegalArgumentException();
            }
        
        }
      
    }

       
    /**
     * A helper method to build the huffman tree from a scanner object.
     * @param tree the original root node we are starting with
     * @param val the bits containing info on how to traverse through our tree
     * @param letter the letter we are 
     * @return
     */
    public boolean buildTree(HuffmanNode tree, String val, Integer letter ){
        //base case: val is null
        
        String l ="" + val.charAt(0);
        if (val.length() == 1){ //if we are at the last bit of our string
            if(l.equals("0")){ //go left
                HuffmanNode newNode = new HuffmanNode(letter, 1);
                tree.left = newNode;
                return true; 
            }
            if(l.equals("1")){ //go right
                HuffmanNode newNode = new HuffmanNode(letter, 1);
                tree.right = newNode;
                return true; 
            }
        }


        if(l.equals("0")){
            if(tree.left == null){
                HuffmanNode tempNode = new HuffmanNode(0);
                tree.left = tempNode;
            }
            buildTree(tree.left, val.substring(1), letter);
            
        }
        if(l.equals("1")){
            if(tree.right == null){
                HuffmanNode tempNode = new HuffmanNode(0);
                tree.right = tempNode;
            }
            buildTree(tree.right, val.substring(1), letter);
            
        }
        return false;
      
        
    }

    


    
    /**
     * Reads the individual bits from the input stream and writes the corresponding 
     * characters to the supplied output stream. It stops reading when a character with 
     * a value equal to the eof parameter is encountered. This is a pseudo-EOF character 
     * (see Implementation Details section) that should not be written to output. 
     * You may assume that the supplied stream parameters are valid and already open.
     * @param input the binary input supplied to us
     * @param output the output stream
     * @param eof hardcoded value to stop the method
     */

  public void decode(BitInputStream input, PrintStream output, int eof){  
        HuffmanNode tempNode = this.root;  
        //checks for as long as the eof value is unequal to the current character's ascii value
        while((int)(tempNode.letter) != eof){
            //checks if both the children of the node are null
            if(tempNode.right == null && tempNode.left == null){
                int n = tempNode.letter;
                output.write((int)(n));
                tempNode = this.root;
                
            }
            
            int first = input.readBit();
            if(first == 0){
                tempNode = tempNode.left;
            }
            else{
                tempNode = tempNode.right;                 
            }
        
        }  
    }
}