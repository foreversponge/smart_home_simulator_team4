package models;

/**
 * the temperature class which have 4 attribute
 * so each room would have instance of temperature which have default temperature
 * then user can change the temperature of the room according to their zone or individually
 */
public class Temperature {
    private double defaultTemp=20;
    private double morningTemp;
    private double dayTemp;
    private double nightTemp;
    private double overrideTemp;

    /**
     * contsructor of Temperature instance
     * @param morningTemp morning temperature
     * @param dayTemp day temperature
     * @param nightTemp night temperature
     */
    public Temperature(double morningTemp, double dayTemp, double nightTemp) {
        this.morningTemp = morningTemp;
        this.dayTemp = dayTemp;
        this.nightTemp = nightTemp;
    }

    /**
     * Empty constructor of Temperature instance
     */
    public Temperature() {
        this.defaultTemp=20;
        this.morningTemp = 20;
        this.dayTemp = 20;
        this.nightTemp = 20;
    }

    /**
     * set default Temperature
     * @return defaultTemp value
     */
    public double getDefaultTemp() {
        return defaultTemp;
    }

    /**
     * getter default temperature
     * @param defaultTemp value
     */
    public void setDefaultTemp(double defaultTemp) {
        this.defaultTemp = defaultTemp;
    }

    /**
     * getter morning temperature
     * @return morningTemp value
     */
    public double getMorningTemp() {
        return morningTemp;
    }

    /**
     * setter morning temperature
     * @param morningTemp value
     */
    public void setMorningTemp(double morningTemp) {
        this.morningTemp = morningTemp;
    }

    /**
     * setter day temperature
     * @return dayTemp value
     */
    public double getDayTemp() {
        return dayTemp;
    }

    /**
     * getter day temperature
     * @param dayTemp value
     */
    public void setDayTemp(double dayTemp) {
        this.dayTemp = dayTemp;
    }

    /**
     * getter night temperature
     * @return nightTemp value
     */
    public double getNightTemp() {
        return nightTemp;
    }

    /**
     * setter night temperature
     * @param nightTemp value
     */
    public void setNightTemp(double nightTemp) {
        this.nightTemp = nightTemp;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "defaultTemp=" + defaultTemp +
                ", morningTemp=" + morningTemp +
                ", dayTemp=" + dayTemp +
                ", nightTemp=" + nightTemp +
                '}';
    }
}
