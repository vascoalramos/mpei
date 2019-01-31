package Modules;

import java.util.ArrayList;
import java.util.HashMap;

public class MinHash {
    /***************************************************************************************************
     *                                           Attributes                                           *
     **************************************************************************************************/
    /*
     * For the MinHash
     */
    private int[][] minHash;
    private HashMap<String, ArrayList<Integer>> dataSet;
    private double totalHashes;

    /*
     * For the HashFunction
     */
    private HashFunction ourHashFunction;


    /*************************************************************************************************
     *                                         Constructors                                         *
     ************************************************************************************************/

    /*
     * Purpose:
     *      Initializes parameters of MinHash
     *      Initializes parameters of Hash Function
     *
     * Arguments:
     *      -> HashMap<String,ArrayList<Integer>> dataSet: Set with the data to hash
     *      -> int totalHashes: number of Hash Functions
     */
    public MinHash(HashMap<String, ArrayList<Integer>> dataSet, int totalHashes) {
        this.dataSet = dataSet;
        this.totalHashes = totalHashes;
        ourHashFunction = new HashFunction((int) this.totalHashes);
        createMinHash(dataSet);
    }


    /************************************************************************************************
     *                                       Private Methods                                       *
     ***********************************************************************************************/

    /*
     * Purpose:
     *     Gets minimum value from an array of Integers
     *
     * Argument:
     *      -> int[} hK: array of integers to get minimum
     *
     * Return:
     * 		-> int: minimum value of array hK
     */
    private int minimum(int[] hK) {
        int min = Integer.MAX_VALUE;
        //Go through all values of hK and update variable min accordingly
        for (int i1 : hK) {
            if (i1 < min) {
                min = i1;
            }
        }

        return min;
    }

    /*
     * Purpose:
     *     Creates a minHash
     *
     * Argument:
     *      -> HashMap<String,ArrayList<Integer>> dataSet: HashMap with Strings as keys
     *      	and the value of each key is a ArrayList of Integers
     */
    private void createMinHash(HashMap<String, ArrayList<Integer>> dataSet) {

        int keysLength = dataSet.keySet().size();
        int valuesLength;
        String[] keys = dataSet.keySet().toArray(new String[dataSet.keySet().size()]);
        
        int[] hK;

        //Initialise minHash
        this.minHash = new int[keysLength][(int) this.totalHashes];

        //For each key in the dataset
        for (int n = 0; n < keysLength; n++) {
        	//Generate totalHashes Hashes
            for (int j = 0; j < totalHashes; j++) {
                valuesLength = dataSet.get(keys[n]).size();
                hK = new int[valuesLength];
                
                //For each value corresponding to the key in the dataset generate a hash
                for (int i = 0; i < valuesLength; i++) {
                    hK[i] = ourHashFunction.getHash(dataSet.get(keys[n]).get(i), j);
                }
                
                //Use the minimum hash generated
                this.minHash[n][j] = minimum(hK);
                
                //Note: Normally we would have the minHash's rows be the hashes generated by the hash function and the columns be the dataSet keys
                //however, due to make the process of working with the minHash in java we had to put the hashes in the columns and values in the rows
            }
        }
    }


    /***********************************************************************************************
     *                                             Getters                                        *
     **********************************************************************************************/

    /*
     * Purpose:
     *      Returns total amount of intersections between two 1D arrays
     *
     * Arguments:
     *      -> int[} a: array of integers a to be evaluated
     *      -> int[} b: array of integers b to be evaluated
     *
     * Return:
     * 		-> int: value of the intersection, i.e, number of equal "rows"
     */
    public int intersections(int[] a, int[] b) {
        int sum = 0;
        //Check if entries at index I of array A and array B are the same
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b[i]) {
                sum++;
            }
        }

        return sum;
    }


    /*
     * Purpose:
     * 		Gets the minHash Bi-Dimensional Array
     *
     * Return:
     * 		-> int[][]: bi-dimensional array of integers
     */
    public int[][] getMinHash() {
        return minHash;
    }


    /*
     * Purpose:
     * 		Gets the number of Hash Functions
     *
     * Return:
     * 		-> double: number of hash functions
     */
    public double getTotalHashes() {
        return totalHashes;
    }


    /*
     * Purpose:
     * 		Gets the Data SET
     *
     * Return:
     * 		-> HashMap<String, ArrayList<Integer>>: data set
     */
    public HashMap<String, ArrayList<Integer>> getDataSet() {
        return dataSet;
    }


    /***********************************************************************************************
     *                                        Public Methods                                      *
     **********************************************************************************************/

    /*
     * Purpose:
     * 		Prints the Modules.MinHash Bi-Dimensional Array
     */
    public void printMinHash() {
        int keysLength = this.dataSet.keySet().size();

        for (int i = 0; i < keysLength; i++) {
            for (int j = 0; j < this.totalHashes; j++) {
                System.out.printf("%d ; ", minHash[i][j]);
            }
            System.out.println("");
        }

    }


    /*
     * Purpose:
     * 		Prints all Similarities of the Initial Set taking in account a value of guidance (threshold)
     *
     * Argument:
     * 		-> double threshold: value that defines if some element is Similar to another element
     */
    public void printSimilarities(double threshold) {
        int keysLength = dataSet.keySet().size();
        String[] keys = dataSet.keySet().toArray(new String[dataSet.keySet().size()]);

        double sim;

        //For each key in the dataset
        for (int i = 0; i < keysLength; i++) {
        	//Search all keys that come after key "i"
            for (int j = i + 1; j < keysLength; j++) {
                sim = intersections(minHash[i], minHash[j]) / this.totalHashes;
                if (1 - sim <= threshold) {
                    System.out.printf("Distancia : %f -> Key 1: %s Key 2: %s\n", 1 - sim, keys[i], keys[j]);
                }

            }
        }

    }
    
    /*
     * Purpose:
     * 		Returns a list with all Similarities of the Initial Set taking in account a value of guidance (threshold)
     *
     * Argument:
     * 		-> double threshold: value that defines if some element is Similar to another element
     */
    public ArrayList<String> getSimilarities(double threshold) {
        int keysLength = dataSet.keySet().size();
        String[] keys = dataSet.keySet().toArray(new String[dataSet.keySet().size()]);
        ArrayList<String> similarities = new ArrayList<String>();

        double sim;

        //For each key in the dataset
        for (int i = 0; i < keysLength; i++) {
        	//Search all keys that come after key "i"
            for (int j = i + 1; j < keysLength; j++) {
                sim = intersections(minHash[i], minHash[j]) / this.totalHashes;
                if (1 - sim <= threshold) {
                	similarities.add((1-sim) + " ;- " + keys[i] + " ;- " + keys[j]);
                }

            }
        }
        
        return similarities;
    }
}
