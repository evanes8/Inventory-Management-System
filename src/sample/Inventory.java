package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.util.Locale;

/**
 * This class keeps track of the total current inventory of both parts and products
 * as well as allows new parts to be added, deleted or modified.
 * FUTURE ENHANCEMENT the inventory class could be modified to preform various searching and sorting
 * algorithms, like ordering the products by price, and filtering based on search paramters other
 * than just name and ID
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList(); ;

    /**
     *Adds a new part to the inventory.
     * @param newPart new part to be added
     *
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Searches the inventory of parts for part with matching id to searched id
     * @param partid the part id you are searching for
     * @return the Part whose id matches your search
     * @throws IllegalArgumentException couldnt find the id that was searched for
     */
    public static Part lookupPart(int partid) throws IllegalArgumentException {
       Part foundPart = null;
        for (Part i : Inventory.getAllParts()) {
            if (i.getId() == partid) foundPart=i;
        }
        if(foundPart!=null)return foundPart;
        else throw new IllegalArgumentException("No part with this Id was found.");
    }

    /**
     * Searches for parts whose names begin with the searched for string
     * @param partname the name of the part/parts you are searching for
     * @return an list of parts who match search paramters
     * @throws IllegalArgumentException Couldnt find any parts who match the searched for name.
     */
    public static ObservableList<Part> lookupPart(String partname) throws IllegalArgumentException{
       ObservableList<Part> newPartsList = FXCollections.observableArrayList();
        for (Part i : getAllParts()){
            if (i.getName().toUpperCase(Locale.ROOT).startsWith(partname.toUpperCase(Locale.ROOT)))
                newPartsList.add(i);
        }
        if(newPartsList.size()<1) throw new IllegalArgumentException("No part with this Name was found.");
return newPartsList;
   }

    /**
     * Replaces the part at specified index with a new part
     * @param index index of part to be updated in inventory list
     * @param newPart new part to replace old part
     */
    public static void updatePart(int index, Part newPart){
        allParts.set(index, newPart);

    }

    /**
     * removes a specific part from inventory list
     * @param selectedPart the part to be deleted
     * @return true if successful
     */
    public static boolean deletePart(Part selectedPart){

        return allParts.remove(selectedPart);


    }

    /**
     * Adds a product to the inventory list
     * @param newProduct the product to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Searches the inventory of products for a product with matching id to searched id
     * @param productId id of the part searching for
     * @return the product that is being searched for
     * @throws IllegalArgumentException if no product with this id was found
     */
    public static Product lookupProduct(int productId) throws IllegalArgumentException{
        Product foundProduct = null;
        for (Product i : Inventory.getAllProducts()) {
            if (i.getId() == productId) foundProduct=i;
        }
        if(foundProduct!=null)return foundProduct;
        else throw new IllegalArgumentException("No Product with this Id was found.");
    }

    /**Searches the inventory of products for products based on name
     * @param productName the name of the product/products searched for
     * @return list of parts whose names match search criteria
     * @throws IllegalArgumentException if no product with this name was found
     */
    public static ObservableList<Product> lookupProduct(String productName) throws IllegalArgumentException{
        ObservableList<Product> newProductsList = FXCollections.observableArrayList();
        for (Product i : getAllProducts()){
            if (i.getName().toUpperCase(Locale.ROOT).startsWith(productName.toUpperCase(Locale.ROOT)))
                newProductsList.add(i);
        }
        if(newProductsList.size()<1) throw new IllegalArgumentException("No Product with this Name was found.");
        return newProductsList;
    }

    /**
     * Replaces the product at specified index with a new product
     * @param index index of product to be replaced
     * @param newProduct replacement product
     */
    public static void updateProduct(int index, Product newProduct){
    allProducts.set(index,newProduct);
    }
    /**
     * removes a specific product from inventory list
     * @param selectedProduct the product to be deleted
     * @return true if successful
     */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

    /**
     *
     * @return inventory list of parts
     */
    public  static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     *
     * @return inventory list of products
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }


}
