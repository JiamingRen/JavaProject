package Src;

// Java program to illustrate reading data from file
// using nio.File
import java.util.*;


//read order txt file by lines and save into list
public class Order {
    TxtByLine readTxtByLine = new TxtByLine();

    public List<String> readOrder(){

        List<String> l = readTxtByLine.readFileInList("D:\\JavaProject\\BundleCalculator\\Input\\order.txt");
        System.out.println("order: " + l);
        return l;
    }

}