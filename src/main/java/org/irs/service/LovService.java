package org.irs.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.irs.database.Datasources;
import org.irs.dto.RequestDto;
import org.irs.dto.ResponseDto;
import org.irs.util.CommonMethods;
import org.irs.util.ConstantValues;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class LovService {
    @Inject
    Datasources datasourcesObj;
    @Inject
    CommonMethods commonMethods;
    public List<Object> fetchData (String fileName)throws Exception{
        List<Object> results = new ArrayList<>();

        String query=null;
        if(ConstantValues.queryHashMap.containsKey(fileName)){
            query=ConstantValues.queryHashMap.get(fileName);
        }else{
            query = commonMethods.readFile(fileName);
            if(query==null || query.trim().isEmpty()){
                return results;
            }else{
                ConstantValues.queryHashMap.put(fileName,query);
            }
        }
        
        String updateQuery=commonMethods.formatQuery(query,"");
        List<Object> list=fnExecuteQuery(updateQuery);
        if(list.isEmpty()){
            return results;
        }
       
        return  list;

    }


    public List<Object> fnExecuteQuery(String query) throws Exception {

        List<Object> results = new ArrayList<>();


        try (Connection connection = datasourcesObj
                .getConnection();

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);) {

            results = commonMethods.extractResultSet(resultSet);


        } catch (Exception e) {

            String errorMessage = "Error while executing query: " + e.getMessage();
            throw new Exception(errorMessage);
        }
        return results;
    }



}
