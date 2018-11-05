package com.certoclav.certoscale.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

//Created by Vladimir Yugay
//05.11.2018
//Utility class for handling numbers i.e. trimming to 2 decimal places
public class NumberFormatUtils {

    //This method rounds the number to a certain amount of decimal places
    public static double roundNumber(double number, int decimalPlaces){
        if (decimalPlaces < 0) throw  new IllegalArgumentException();
        BigDecimal bigDecimal = new BigDecimal(Double.toString(number));
        bigDecimal = bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
