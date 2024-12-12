package org.irs.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.irs.database.Datasources;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader; 

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class CommonMethods {


    public String readFile(String fileName) {
    String filePath = "queries/" + fileName + ConstantValues.fileExtension;  // No need for full path
    StringBuilder lines = new StringBuilder();
    
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

        String line;
        while ((line = reader.readLine()) != null) {
            lines.append(" ").append(line);
        }
        return lines.toString();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}


    public List<Object> extractResultSet(ResultSet resultSet) throws Exception {
        List<Object> results = new ArrayList<>();
        String errorMessage = "";
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    if(columnName.equalsIgnoreCase("image_uri" ) ){
                        ;
                        row.put("image", (Collections.singletonMap("uri", columnValue)));
                    }else{
                           row.put(columnName, columnValue);
                    }
                 
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            throw new Exception(errorMessage);
        }

        return results;
    }

    public String formatQuery(String query, String params) throws Exception {
        String result = null;

        try {
            result = String.format(query, params.split(","));

        } catch (Exception e) {
            String errorMessage = "formatting query: " + e.getMessage();
            throw new Exception(errorMessage);
        }
        return result;
    }

    public static String writeJson(Object object){
        ObjectMapper objectMapper=new ObjectMapper();
        try{
           return objectMapper.writeValueAsString(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
