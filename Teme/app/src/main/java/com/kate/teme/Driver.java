package com.kate.teme;

/**
 * Created by irving on 6/9/15.
 */
public class Driver {
String DriverName;
String CarDriven;
    public Driver(String driverName, String carNAme) {
        this.DriverName= driverName;
        this.setCarDriven(carNAme);

    }

    public String getDriverName(){
        return  DriverName;
    }

    public String getCarDriven() {
        return CarDriven;
    }

    public void setCarDriven(String carDriven) {
        CarDriven = carDriven;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }
}
