package com.k9b9.singletableapi.dto;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * DtoUtils
 */
public class DtoUtils {

    public static String toJson(Object dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String strJson = objectMapper.writeValueAsString(dto);
        return strJson;
    }

	public static Item toItem(Dto dto) {
		if (dto == null)
            return null;
        String jsonValueMap = null;
        try {
            jsonValueMap = DtoUtils.toJson(dto.valueMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        PrimaryKey key = new PrimaryKey("pkey", dto.pkey, "skey", dto.skey);
        Item item = new Item().withPrimaryKey(key).withJSON("valueMap", jsonValueMap);
        return item;
    }
    
    public static Dto toDto(Item item) {
        String pkey = item.getString("pkey");
        String skey = item.getString("skey");
        // simply for debug - remove from prod
        String json = item.getJSON("valueMap");
        System.out.println("toDto, json =" + json);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> valueMap;
        try {
            valueMap = objectMapper.readValue(json, Map.class);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
        Dto dto = new Dto(pkey,skey,valueMap);
        return dto;
	}
}