package ci553.happyshop.catalogue;

import java.util.ArrayList;
import java.util.Objects;

public class Trolley {

    private ArrayList<Product> products = new ArrayList<>();

    /**
     * Adds a product to the trolley.
     * Currently the product is simply appended to the end of the list.
     *
     * TODO — Improve trolley organisation:
     * 1. Merge products with the same product ID (combine quantities).
     * 2. Sorts the products in the trolley by product ID.
     *
     * Possible approaches:
     *
     * Approach 1 (simplest):
     * Modify addProduct() to handle merging and sorting directly.
     *
     * Approach 2:
     * Create a new method (e.g. addProductOrganised()) that performs the
     * merging and sorting logic.
     * Then call this method instead of addProduct() from addToTrolley() in CustomerModel.
     *
     * Approach 3 (more advanced):
     * Create a subclass such as OrganizedTrolley that overrides addProduct().
     * Then in CustomerModel:
     *     private Trolley trolley = new OrganizedTrolley();
     * This allows the trolley behaviour to be changed without modifying
     * the existing Trolley class.
     *
     * Approach 4 (advanced design using a design pattern – Factory Pattern)
     * (covered later in the "Design Patterns" topic).
     *
     * Instead of directly creating the trolley in CustomerModel:
     *   private Trolley trolley = new Trolley();
     * create a method such as:
     *   Trolley createTrolley(), which returns a Trolley.
     * Then create a subclass such as OrganizedCustomerModel that overrides
     * createTrolley() to return an OrganizedTrolley.
     *
     * The system can decide which model to use (CustomerModel or OrganizedCustomerModel)
     * in CustomerClient, Main, or other appropriate places in the application.
     * Either model can be used independently, or both can coexist,
     * allowing different trolley behaviours at the same time.
     *
     * code examples:
     * public CustomerModel(...) { trolley = createTrolley(); ...}
     *
     * protected Trolley createTrolley() { return new Trolley(); }
     *
     * class OrganizedCustomerModel extends CustomerModel {
     *     @Override
     *     protected Trolley createTrolley() {
     *         return new OrganizedTrolley();
     *     }
     * }
     */
    public void addProduct(Product product) {
        for(Product x : products){
            if(x.getProductId().equals(product.getProductId())){
                x.setOrderedQuantity(x.getOrderedQuantity() + 1);
                return;
            }
        }
        products.add(product);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void clear() {
        products.clear();
    }
}
