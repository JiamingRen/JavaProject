package Src;

// Java program to illustrate reading data from file
// using nio.File
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormatInformation {
    
    TxtByLine readTxtByLine  = new TxtByLine();

    public List<String> findFormat(String theFormat) {

        //read txt file
        List<String> l = readTxtByLine.readFileInList("D:\\JavaProject\\BundleCalculator\\Input\\format.txt");

        // find required format and its bundleSize and Price
        // regex to find bundle size and price
        List<String> allMatches = new ArrayList<String>();
        String pattern = "(\\d{1,2}\\s\\@\\s\\$\\d{1,4}\\.?\\d{1,2})";
        Pattern r = Pattern.compile(pattern);
        for (int i = 0; i < l.size(); i++) {
            //get and check format code
            String part = (String) l.get(i);
            String[] parts = part.split("\\|");
            //if match return all bundle size and price
            if (parts[1].trim().toLowerCase().equals(theFormat)) {
                Matcher m = r.matcher(part);
                while (m.find()) {
                    // System.out.println("Found value: " + m.group());
                    allMatches.add(m.group());
                }
            }
        }
        return allMatches;
    }
}