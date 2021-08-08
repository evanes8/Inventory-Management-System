package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Defines a product which can be made up of zero or more parts
 * As with an individual part has a unique id, a name, price, stock value, min and max stock values
 *
 * RUNTIME ERROR the associated parts Observable list needed to be instantiated with observable arraylist in order to
 * function as expected.
 *
 *
 */

public class Product {
private ObservableList<Part> associatedParts= FXCollections.observableArrayList();
private int id;
private String name;
private double price;
private int stock;
private int min;
private int max;

Product(int id, String name, double price, int stock, int min, int max){
this.id=id;
this.name=name;
this.price=price;
this.stock=stock;
this.min=min;
this.max=max;

}

    /**
     *
     * @return the id
     */

    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     * @param part the part to add
     */
    public void addAssociatedPart(Part part){
        this.associatedParts.add(part);

    }

    /**
     *
     * @param selectedAssociatedPart the part to be removed
     * @return true if part successfully removed
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
       return this.associatedParts.remove(selectedAssociatedPart);
    }

    /**
     *
     * @return the list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return this.associatedParts;
    }



}
