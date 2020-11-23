import java.io.*;
import java.util.Scanner;

/**
 * Author:  Stefan Huang
 * Date:    2020-11-17
 * First little project
 *      I have a list of positive integers that may contain duplicates,
 *      write a program that find all the unique pairs that have sum of 100.
*/

public class p1 {
    //some helper functions

   //quick sort, final array is low to high
   private static void qsort(int[] data, int head, int tail) {
       if (head < tail) {
           //set pivot k and index of smaller entries i
           int k = data[tail];
           int i = head - 1;
           for (int j = head; j < tail; j++)
               //if an entry is smaller than the pivot k, swap it towards the front
               if (data[j] <= k) {
                   i++;
                   int tmp = data[i];
                   data[i] = data[j];
                   data[j] = tmp;
               }
           //swap the pivot to the next position of the smaller entries
           int tmp = data[i + 1];
           data[i + 1] = data[tail];
           data[tail] = tmp;
           //recursively sort the entries before and after the pivot
           qsort(data, head, i - 1);
           qsort(data, i + 1, tail);
       }
   }

    //remove duplicates in an array
    private static int removeDuplicate(int[] data, int size) {
        int sizeBackup = size;
        for (int i = 1; i < size; i++)
            if (data[i] == data[i - 1]) {
                //remove the entry at index i
                for (int j = i; j < size - 1; j++)
                    data[j] = data[j + 1];
                i--;
                size--;
            }
        //since all data provided is positive, here I make the unused entries in the array 0 to indicate no useful data
        //instead of creating a new array with the smaller size
        for (int i = size; i < sizeBackup; i++) data[i] = 0;
        return size;
    }

    //remove num>=100
    private static int removeLargeEntries(int[] data, int size) {
        int i = 0;
        //find the first entry not smaller than 100
        while (i < size && data[i] < 100) i++;
        //fill the rest of the array with 0 to indicate no useful data
        for (int j = i; j < size; j++)
            data[j] = 0;
        return i;
    }

    private static int findPair(int[] data, int i, int mid, int size) {
        //initialize the final location, where -1 represent not found
        int location = -1;
        //loop through the numbers larger than 50 to find if such pair exist for data[i]
        for (int j = mid; j < size; j++)
            if (data[i] + data[j] == 100)
                location = j;
        return location;
    }

    //main function
    public static void main(String args[]) throws IOException {
        Scanner in = new Scanner(new File("input.txt"));
        FileWriter out = new FileWriter("output.txt");
        int size;
        size = in.nextInt();
        int[] data = new int[size];
        for (int i = 0; i < size; i++)
            data[i] = in.nextInt();
        in.close();
        //sort the data
        qsort(data, 0, size - 1);
        //search for double 50s
        boolean two50sExist = false;
        int tmp = 0;
        while (data[tmp] < 50 && tmp < size) tmp++;
        if (data[tmp + 1] == 50)
            two50sExist = true;
        //remove duplicates
        size = removeDuplicate(data, size);
        //remove entries larger or equal to 100
        size = removeLargeEntries(data, size);
        //find the index of the largest number under 50
        int mid = 0;
        while (data[mid] < 50 && mid < size) mid++;
        //set a counter for total pairs
        int counter = 0;
        //here I loop from the lowest number to the largest under 50
        //this is because in the findPair function I only scan the numbers larger than 50 to find a match
        for (int i = 0; i < mid; i++) {
            int match = findPair(data, i, mid, size);
            if (match != -1) {
                counter++;
                out.write("(" + data[i] + ", " + data[match] + ") is a unique pair.\n");
            }
        }
        if (two50sExist) {
            counter++;
            out.write("(50, 50) is a unique pair.\n");
        }
        switch(counter) {
            case 0:
                out.write("No such pair detected.\n");
                break;
            case 1:
                out.write("Found " + counter + " unique pair.\n");
                break;
            default:
                out.write("Found " + counter + " unique pairs.\n");
        }
        out.close();
    }
}
