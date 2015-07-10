package com.kate.teme;

/**
 * Created by irving on 7/8/15.
 */
public class OnlineData   {
    Driver driver;
    String ItemKey;
    public OnlineData(Driver driverdata, String ItemKey) {
        this.driver= driverdata;
        this.setitemkey(ItemKey);

    }

    private void setitemkey(String itemkey) {
        this.ItemKey=itemkey;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public String getItemKey() {
        return ItemKey;
    }
}
