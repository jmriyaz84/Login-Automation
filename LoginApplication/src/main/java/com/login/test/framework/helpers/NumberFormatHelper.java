package com.login.test.framework.helpers;

/**
 * Created by s746032 on 07/03/2016.
 */
public class NumberFormatHelper {


        public static Double reFormatDoubleString(String numberString) throws Exception{
            String numberToReplace = numberString.replaceAll(",", "");
            return Double.parseDouble(numberToReplace);
        }


}
