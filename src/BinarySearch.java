/**
 * Created by Team El Camino
 */
public class BinarySearch {

    public static void main(String[] argv){
        BinarySearchTest();
    }

    public static void BinarySearchTest(){
        System.out.println("Max of function results: "+ RunBinarySearchWithFunction(0, 5));

        int[] arr = {1,2,3,4,5,6};
        System.out.println("Index of target: "+ RunBinarySearchWithArray(arr, 2));
    }

    public static int RunF(int val){
        int[] arr = {1,2,3,4,0,0};
        return arr[val];
    }

    // Binary searches the result of a function call for the max.
    // Inputs: the lower and upper bound.
    // Returns the max.
    public static int RunBinarySearchWithFunction(int low, int high){
        int max = 0;
        while (low <= high){
            int mid = low + (high - low)/2;
            if(max > RunF(mid)){
                high = mid - 1;
            }else if(max < RunF(mid)){
                max = RunF(mid);
                low = mid + 1;
            }else{
                return mid;
            }
        }

        return max;
    }

    // Binary searches the contents of an array.
    // Inputs: the array to search and a target.
    // Returns the index of the target or -1 if not found.
    public static int RunBinarySearchWithArray(int[] array, int target){
        int low = 0;
        int high = array.length - 1;

        while (low <= high){
            int mid = low + (high - low)/2;
            if(target < array[mid]){
                high = mid - 1;
            }else if(target > array[mid]){
                low = mid + 1;
            }else{
                return mid;
            }
        }

        return -1;
    }
}
