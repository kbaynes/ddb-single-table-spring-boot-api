package com.k9b9.singletableapi.model;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.k9b9.singletableapi.ddb.SingleTableDdb;
import com.k9b9.singletableapi.dto.Dto;
import com.k9b9.singletableapi.dto.DtoUtils;

/**
 * Users
 */
public class Customers {

    public final static String SKEY = "customer";
    private SingleTableDdb ddb;

    public Customers(SingleTableDdb ddb) {
        this.ddb = ddb;
    }

	public boolean putCustomer(Dto dto) {
		// build Item
        Item item = DtoUtils.toItem(dto);
        this.ddb.putItem(item);
        // TODO add metrics for capacity used
        return true;
    }
}