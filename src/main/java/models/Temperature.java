package models;

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
