package Src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BundleCalculator {

    public static double totalPrice;

    public static List getBundleSize(List format) {
        List<Double> bundleSize = new ArrayList<Double>();
        for (int i = 0; i < format.size(); i++) {
            String tempBundleSzie = ((String) format.get(i)).split("\\@")[0].trim();
            bundleSize.add(Double.parseDouble(tempBundleSzie));
        }
        return bundleSize;
    }

    public static List getBundlePrice(List format) {
        List<Double> bundlePrice = new ArrayList<Double>();
        for (int i = 0; i < format.size(); i++) {
            String tempBundlePrice = ((String) format.get(i)).split("\\@")[1].trim().replace("$", "");
            bundlePrice.add(Double.parseDouble(tempBundlePrice));
        }
        return bundlePrice;
    }

    public static HashMap getOrder(List format) {
        HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
        for (int i = 0; i < format.size(); i++) {
            String tempOrderAmount = ((String) format.get(i)).split(" ")[0].trim();
            String tempOrderFormat = ((String) format.get(i)).split(" ")[1].trim();
            orderMap.put(tempOrderFormat.toLowerCase(), Integer.parseInt(tempOrderAmount));
        }
        return orderMap;
    }

    public static void getTotalPrice(String formatCode, HashMap<String, Integer> orderMap) {
        formatCode = formatCode.toLowerCase();

        double formatAmount = orderMap.get(formatCode);
        // store total price
        // get bundle price and bundle size by format code
        List format = ReadFormat.findFormat(formatCode);
        List bundlePrice = getBundlePrice(format);
        Collections.reverse(bundlePrice);
        List bundleSize = getBundleSize(format);
        Collections.reverse(bundleSize);
        for (int i = 0; i < bundleSize.size(); i++) {
            if (i + 1 < bundleSize.size()) {
                double bundle = (double) (formatAmount / (Double) bundleSize.get(i));
                // System.out.println("bundle: " + i + " :" + bundle);
                totalPrice += (int) bundle * (Double) bundlePrice.get(i);
                formatAmount = (Double) ((bundle - (int) bundle) * (Double) bundleSize.get(i));
                // System.out.println("formatAmount: " + i + " :" + formatAmount);

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
    }

    public static void main(String[] args) {

        // get customer order
        List order = ReadOrder.readOrder();
        // save customer order in a map
        HashMap<String, Integer> orderMap = getOrder(order);
        for (String i : orderMap.keySet()) {
            getTotalPrice(i,orderMap);

        }
        System.out.println(totalPrice);
    }

}
