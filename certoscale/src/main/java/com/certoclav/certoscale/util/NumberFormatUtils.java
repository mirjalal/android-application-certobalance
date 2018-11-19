package com.certoclav.certoscale.util;

import java.util.Locale;

//Created by Vladimir Yugay
//05.11.2018
//Utility class for handling numbers i.e. trimming to 2 decimal places
public class NumberFormatUtils {

    public static String roundNumber(double number, int decimalPlaces){
        return String.format(Locale.US,"%.4f",number);
    }
}
