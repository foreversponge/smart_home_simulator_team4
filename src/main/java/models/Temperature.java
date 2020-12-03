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

    /**
     * contsructor of Temperature instance
     * @param morningTemp
     * @param dayTemp
     * @param nightTemp
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
     * @return
     */
    public double getDefaultTemp() {
        return defaultTemp;
    }

    /**
     * getter default temperature
     * @param defaultTemp
     */
    public void setDefaultTemp(double defaultTemp) {
        this.defaultTemp = defaultTemp;
    }

    /**
     * getter morning temperature
     * @return
     */
    public double getMorningTemp() {
        return morningTemp;
    }

    /**
     * setter morning temperature
     * @param morningTemp
     */
    public void setMorningTemp(double morningTemp) {
        this.morningTemp = morningTemp;
    }

    /**
     * setter day temperature
     * @return
     */
    public double getDayTemp() {
        return dayTemp;
    }

    /**
     * getter day temperature
     * @param dayTemp
     */
    public void setDayTemp(double dayTemp) {
        this.dayTemp = dayTemp;
    }

    /**
     * getter night temperature
     * @return
     */
    public double getNightTemp() {
        return nightTemp;
    }

    /**
     * setter night temperature
     * @param nightTemp
     */
    public void setNightTemp(double nightTemp) {
        this.nightTemp = nightTemp;
    }

//    /**
//     * getter boolean replace temperature
//     * @return boolean replace temperature
//     */
//    public boolean getReplaceTemp() {
//        return replaceTemp;
//    }
//
//    /**
//     * setter boolean replace temperature
//     * @param replaceTemp
//     */
//    public void setOverrideTemp(boolean replaceTemp) {
//        this.replaceTemp = replaceTemp;
//    }
//    /**
//     * getter override temperature
//     * @return override temperature
//     */
//    public double getOverrideTemp() {
//        return overrideTemp;
//    }
//
//    /**
//     * setter override temperature
//     * @param overrideTemp
//     */
//    public void setOverrideTemp(double overrideTemp) {
//        this.overrideTemp = overrideTemp;
//    }

    /**
     * to String method
     * @return
     */
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
