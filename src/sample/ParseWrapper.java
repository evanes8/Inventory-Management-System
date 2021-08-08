package sample;

import javafx.scene.control.TextField;

/**
 * This class helps to get more descriptive error messages by catching the standard number format exception
 * and throwing a new one with a custom error mesage, which is displayed to the user by an alert in the
 * main class
 */
public class ParseWrapper {
    /**
     * @param idText texfield for the Id
     * @return the parsed Id
     * @throws NumberFormatException when canot be parsed
     */
    public static int parseId(TextField idText) throws NumberFormatException{
    try {
        return Integer.parseInt(idText.getText());
    }
    catch(NumberFormatException e){
        throw new NumberFormatException("Incorrect ID Input");
    }

}
    /**
     * @param nameText texfield for the name
     * @return the name
     */
    public static String parseName(TextField nameText){
       return nameText.getText();


    }
    /**
     * @param stockText texfield for the stock
     * @return the parsed stock
     * @throws NumberFormatException when cannot be parsed
     */
    public static int parseStock(TextField stockText ){
    try{
        return Integer.parseInt(stockText.getText());
    }
    catch(NumberFormatException e){
        throw new NumberFormatException("Incorrect Inventory Input");
    }
    }

    /**
     * @param costText texfield for the cost
     * @return the parsed cost
     * @throws NumberFormatException when cannot be parsed
     */
    public static Double parseCost(TextField costText){
        try {
            return Double.parseDouble(costText.getText());
        }
        catch(NumberFormatException e){
            throw new NumberFormatException("Incorrect Price Input");
        }

    }

    /**
     * @param minText texfield for the min stock
     * @return the parsed min stock
     * @throws NumberFormatException when cannot be parsed
     */
    public static int parseMin(TextField minText) throws NumberFormatException {
        try {
            return Integer.parseInt(minText.getText());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Incorrect Minimum Input");
        }
    }

    /**
     * @param maxText texfield for the max stock
     * @return the parsed max stock
     * @throws NumberFormatException when cannot be parsed
     */
    public static int parseMax(TextField maxText) throws NumberFormatException {
        try {
            return Integer.parseInt(maxText.getText());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Incorrect Maximum Input");
        }
    }

    /**
     * @param machineIdText texfield for the machineid
     * @return the parsed machine Id
     * @throws NumberFormatException when cannot be parsed
     */
    public static int parseMachineId(TextField machineIdText) throws NumberFormatException {
        try {
            return Integer.parseInt(machineIdText.getText());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Incorrect Machine ID Input");
        }
    }


    /**
     * @param companyNameText texfield for the company name
     * @return the company name
     */
    public static String parseCompanyName(TextField companyNameText){
        return companyNameText.getText();


    }




}
