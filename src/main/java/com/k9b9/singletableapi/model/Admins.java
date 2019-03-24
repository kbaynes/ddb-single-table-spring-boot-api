package com.k9b9.singletableapi.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.k9b9.singletableapi.ddb.SingleTableDdb;
import com.k9b9.singletableapi.dto.Dto;
import com.k9b9.singletableapi.dto.DtoUtils;

/**
 * Admins model object will be used by consuming services, such as Web API
 */
public class Admins {

    public final static String ROOT_ADMIN_PKEY = "_ROOT_ADMIN";
    public final static String ADMIN_SKEY = "admin";
    private SingleTableDdb ddb;

    public Admins(SingleTableDdb ddb) {
        this.ddb = ddb;
    }

    /**
     * Each call will overwrite the current Root User. There can be only one.
     * 
     * @throws JsonProcessingException
     */
    public boolean putRootAdmin(Dto dto) {
        // validate Admin
        if (!ROOT_ADMIN_PKEY.equals(dto.pkey)) return false;
        if (!ADMIN_SKEY.equals(dto.skey)) return false;
        if ( ((Integer)dto.valueMap.get("adminLevel")).intValue() != AdminLevel.ROOT.getValue()) return false;
        // build Item
        Item item = DtoUtils.toItem(dto);
        this.ddb.putItem(item);
        // TODO add metrics for capacity used
        return true;
    }

    /**
     * Allow mulitiple admins with the same userID
     */
    public boolean putAdmin(Dto admin) {
        // validate Admin
        // do not add root via this method
        if (ROOT_ADMIN_PKEY.equals(admin.pkey)) return false;
        // must be in admin sort
        if (!ADMIN_SKEY.equals(admin.skey)) return false;
        // do not add root via this method
        int adminLevel = ((Integer)admin.valueMap.get("adminLevel")).intValue();
        boolean isValidLevel = isValidLevel(adminLevel);
        boolean isLevelRoot = isLevelRoot(adminLevel);
        if (isLevelRoot || !isValidLevel) return false;
        // build Item
        Item item = DtoUtils.toItem(admin);
        this.ddb.putItem(item);
        // TODO add metrics for capacity used
        return true;
    }

    public boolean isLevelRoot(int level) {
        return level == AdminLevel.ROOT.getValue();
    }

    public boolean isValidLevel(int level) {
        return level == AdminLevel.ROOT.getValue()
            || level == AdminLevel.ADMIN.getValue() 
            || level == AdminLevel.MANAGER.getValue() 
            || level == AdminLevel.VIEWER.getValue();
    }

    /**
     * Returns all admins
     */
    public List<Dto> getAdmins() {
        NameMap nameMap = new NameMap().with("#skey", "skey");
        ValueMap valueMap = new ValueMap().withString(":skey", Admins.ADMIN_SKEY);
        ItemCollection<ScanOutcome> items = this.ddb.table.scan("#skey = :skey", nameMap, valueMap);
        Iterator<Item> iter = items.iterator();
        List<Dto> list = new ArrayList<Dto>();
        while (iter.hasNext()) {
            Item item = iter.next();
            Dto admin = DtoUtils.toDto(item);
            list.add(admin);
        }
        return list;
    }

    public List<Dto> getAdminsByLevel(AdminLevel level) {
        NameMap nameMap = new NameMap()
            .with("#skey", "skey");
            // .with("#jsonvalue_adminLevel","jsonvalue.adminLevel");
        ValueMap valueMap = new ValueMap()
            .withString(":skey", Admins.ADMIN_SKEY)
            .withInt(":valueMap_adminLevel", level.getValue());
        ItemCollection<ScanOutcome> items = 
            this.ddb.table.scan("#skey = :skey AND valueMap.adminLevel = :valueMap_adminLevel", nameMap, valueMap);
        Iterator<Item> iter = items.iterator();
        List<Dto> list = new ArrayList<Dto>();
        while (iter.hasNext()) {
            Item item = iter.next();
            Dto admin = DtoUtils.toDto(item);
            list.add(admin);
        }
        return list;
    }

    public List<Dto> getAdminsByUserId(String userId) {
        NameMap nameMap = new NameMap()
            .with("#skey", "skey");
            // .with("#jsonvalue_adminLevel","jsonvalue.adminLevel");
        ValueMap valueMap = new ValueMap()
            .withString(":skey", Admins.ADMIN_SKEY)
            .withString(":valueMap_userId", userId);
        ItemCollection<ScanOutcome> items = 
            this.ddb.table.scan("#skey = :skey AND valueMap.userId = :valueMap_userId", nameMap, valueMap);
        Iterator<Item> iter = items.iterator();
        List<Dto> list = new ArrayList<Dto>();
        while (iter.hasNext()) {
            Item item = iter.next();
            Dto admin = DtoUtils.toDto(item);
            list.add(admin);
        }
        return list;
    }
    
}