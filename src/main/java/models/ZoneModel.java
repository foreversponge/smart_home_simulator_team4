package models;

/**
 * the zone class
 * so each zone would have instance of temperature which have default temperature
 * then user can change the temperature of the zone
 */
public class ZoneModel {
    private String zoneName = "";
    private transient Temperature temperature; // each room would create with instance of Temperature with default temperature

    /**
     * Empty constructor of Temperature instance
     * @param  key String
     */
    public ZoneModel(String key) {
        this.zoneName = key;
        this.temperature = new Temperature();
    }

    /**
     * getter of the Temperature instance
     * @return temperature object
     */
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * setter of the Temperature instance
     * @param temperature instance of Temperature
     */
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    /**
     * getter of the Zone Name
     * @return  zoneName name of zone
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * setter of the Zone Name
     * @param  zoneName name of zone
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
