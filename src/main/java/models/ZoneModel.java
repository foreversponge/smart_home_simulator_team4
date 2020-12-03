package models;

/**
 * the temperature class which have 4 attribute
 * so each room would have instance of temperature which have default temperature
 * then user can change the temperature of the room according to their zone or individually
 */
public class ZoneModel {
    private String zoneName = "";
    private transient Temperature temperature; // each room would create with instance of Temperature with default temperature

    /**
     * Empty constructor of Temperature instance
     */
    public ZoneModel(String key) {
        this.zoneName = key;
        this.temperature = new Temperature();
    }

    /**
     * getter of the Temperature instance
     * @return
     */
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * setter of the Temperature instance
     * @param temperature
     */
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    /**
     * getter of the Zone Name
     * @param
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * setter of the Zone Name
     * @return zoneName
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
