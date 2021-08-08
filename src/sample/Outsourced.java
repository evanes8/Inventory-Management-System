package sample;
/**
 * Outsourced class represents parts that are produced out of house
 *
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * Constructor calls the inherited part constructor but also adds Company Name field
     * @param id part unique id
     * @param name parts name
     * @param price parts price
     * @param stock parts stock
     * @param min parts minimum required stock
     * @param max parts maximum allowed stock
     * @param companyName parts company name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;

    }
    /**
     * @param companyName the companyname to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    /**
     * @return the MachineID
     */
    public String getCompanyName()
    {
        return this.companyName;
    }


}
