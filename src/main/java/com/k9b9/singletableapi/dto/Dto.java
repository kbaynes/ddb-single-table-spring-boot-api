package com.k9b9.singletableapi.dto;

import java.util.Map;

/**
 * Data Transfer Object for SingleTableDdb
 * 
 * Objects in valueMap may be only of the following types, with no circular references:
 * String
 * Boolean, boolean
 * Byte, byte
 * Date (as ISO_8601 millisecond-precision string, shifted to UTC)
 * Calendar (as ISO_8601 millisecond-precision string, shifted to UTC)
 * Long, long
 * Integer, int
 * Double, double
 * Float, float
 * BigDecimal
 * BigInteger
 */
public class Dto {

    public String pkey;
    public String skey;
    public Map<String,Object> valueMap;
    
    public Dto() {}

    public Dto(String pkey, String skey, Map<String,Object> valueMap) {
        this.pkey = pkey;
        this.skey = skey;
        this.valueMap = valueMap;
    }

    @Override
    public String toString() {
        return DtoUtils.toItem(this).toString();
    }
}