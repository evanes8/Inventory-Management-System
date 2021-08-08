package sample;

/**
 * InHouse class represents parts that are produced inhouse
 *
 */
public class InHouse extends Part{
    private int machineId;

    /**
     * Creates an Inhouse part, calls the part constructor but also adds a machine id part field
     * @param id unique id of the part
     * @param name parts name
     * @param price parts price
     * @param stock parts stock
     * @param min parts min required stock
     * @param max parts max required stock
     * @param machineID parts machine id
     */
     public InHouse(int id, String name,
                   double price, int stock, int min, int max, int machineID)
    {
        super(id, name, price, stock, min, max);
        this.machineId=machineID;

    }
    /**
     * @param machineId the machineid to set
     */
    public void setMachineID(int machineId)
    {
        this.machineId = machineId;
    }

    /**
     * @return the MachineID
     */
    public int getMachineId()
    {
        return this.machineId;
    }


}
