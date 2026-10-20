package ci553.happyshop.utility;

import ci553.happyshop.catalogue.Product;

import java.util.ArrayList;

/**
 * This class builds a formatted, receipt-like summary from a list of products.
 * It is used by:
 * 1. CustomerModel – to display the trolley and receipt
 * 2. The Order class – to generate a summary for writing to an order's file
 */

public class ProductListFormatter {
    /**
     * Builds a formatted string showing each product's ID, description,
     * quantity ordered, and total price. Also includes a total price at the end.
     * @param proList a List of products
     * @return A nicely formatted string representation of the product list with totals
     */
    public static String buildString(ArrayList<Product> proList) {
        boolean append = true;
        StringBuilder sb = new StringBuilder();
        double totalPrice=0;
        for (Product pr : proList) {
            int orderedQuantity = pr.getOrderedQuantity();
            //%-18.18s, format the argument as a String,
            // -18 → Left-align the string in 18-character wide space.
            //.18 → Truncate the string to at most 18 characters
            String aProduct=String.format(" %-7s %-18.18s (%2d) £%7.2f\n",
                    pr.getProductId(),
                    pr.getProductDescription(),
                    pr.getOrderedQuantity(),
                    pr.getUnitPrice() * orderedQuantity);

            if(append){
                sb.append(aProduct);
            }
            totalPrice = totalPrice + pr.getUnitPrice() * orderedQuantity;
        }

        String lineSeparator = "-".repeat(44) + "\n";
        String total = String.format(" %-35s £%7.2f\n", "Total", totalPrice);

        sb.append(lineSeparator);
        sb.append(total);
        return sb.toString();
    }
}
