package Src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BundleCalculator {

    FormatInformation formatInformation = new FormatInformation();

    public double totalPrice;

    public List<Double> getBundleSize(List<String> format) {
        List<Double> bundleSize = new ArrayList<Double>();
        for (int i = 0; i < format.size(); i++) {
            String tempBundleSzie = ((String) format.get(i)).split("\\@")[0].trim();
            bundleSize.add(Double.parseDouble(tempBundleSzie));
        }
        return bundleSize;
    }

    public List<Double> getBundlePrice(List<String> format) {
        List<Double> bundlePrice = new ArrayList<Double>();
        for (int i = 0; i < format.size(); i++) {
            String tempBundlePrice = ((String) format.get(i)).split("\\@")[1].trim().replace("$", "");
            bundlePrice.add(Double.parseDouble(tempBundlePrice));
        }
        return bundlePrice;
    }

    public HashMap<String, Integer> getOrder(List<String> format) {
        HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
        for (int i = 0; i < format.size(); i++) {
            String tempOrderAmount = ((String) format.get(i)).split(" ")[0].trim();
            String tempOrderFormat = ((String) format.get(i)).split(" ")[1].trim();
            orderMap.put(tempOrderFormat.toLowerCase(), Integer.parseInt(tempOrderAmount));
        }
        return orderMap;
    }

    public void getTotalPrice(String formatCode, HashMap<String, Integer> orderMap) {

        formatCode = formatCode.toLowerCase();

        double formatAmount = orderMap.get(formatCode);
        // store total price
        // get bundle price and bundle size by format code
        List<String> format = formatInformation.findFormat(formatCode);
        List<Double> bundlePrice = getBundlePrice(format);
        Collections.reverse(bundlePrice);
        List<Double> bundleSize = getBundleSize(format);
        Collections.reverse(bundleSize);

        try {
            FileWriter fWriter = new FileWriter("bill.txt",true);
            fWriter.write(formatCode + "\n");
            for (int i = 0; i < bundleSize.size(); i++) {
                if (i + 1 < bundleSize.size()) {
                    double bundle = (double) (formatAmount / (Double) bundleSize.get(i));
                    // System.out.println("bundle: " + i + " :" + bundle);
                    totalPrice += (int) bundle * (Double) bundlePrice.get(i);
                    formatAmount = (Double) ((bundle - (int) bundle) * (Double) bundleSize.get(i));
                    // System.out.println("formatAmount: " + i + " :" + formatAmount);
                    fWriter.write("BundleSize: "+ (Double) bundleSize.get(i) + " have " + (int) bundle + " bundles" + "\n");
                    fWriter.write("Pirce Calculation: \n");
                    fWriter.write((int) bundle + " x " + (Double) bundlePrice.get(i) + " = " + (int) bundle * (Double) bundlePrice.get(i) + "\n");
                }
                if (i + 1 == bundleSize.size()) {
                    double bundle = (formatAmount / (double) bundleSize.get(i));
                    // System.out.println("bundle: " + i + " :" + bundle);
                    totalPrice += (int) bundle * (double) bundlePrice.get(i);
                    formatAmount = (bundle - (int) bundle) * (double) bundleSize.get(i);
                    // System.out.println("formatAmount: " + i + " :" + formatAmount);
                }
                if (formatAmount < (Double) bundleSize.get(bundleSize.size() - 1)) {
                    totalPrice += formatAmount * (Double) bundlePrice.get(bundlePrice.size() - 1)
                            / (Double) bundleSize.get(bundleSize.size() - 1);
                }
            }
            fWriter.write("\n");
            fWriter.close();
        } catch (IOException e) {
            // Print the exception
            System.out.print(e.getMessage());
        }
    }

    public static void main(String[] args) {

        BundleCalculator BundleCalculator = new BundleCalculator();
        Order order = new Order();
        // get customer order
        List<String> readOrder = order.readOrder();
        // save customer order in a map
        HashMap<String, Integer> orderMap = BundleCalculator.getOrder(readOrder);

        for (String i : orderMap.keySet()) {
            BundleCalculator.getTotalPrice(i, orderMap);

        }
        try {
            FileWriter fWriter = new FileWriter("bill.txt", true);
            fWriter.write("Total Price: " + BundleCalculator.totalPrice + "\n");
            fWriter.write("\n");
            fWriter.close();
        } catch (IOException e) {
            // Print the exception
            System.out.print(e.getMessage());
        }
        System.out.println(BundleCalculator.totalPrice);
    }

}
