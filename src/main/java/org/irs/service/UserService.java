package org.irs.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.irs.database.Datasources;
import org.irs.dto.RequestDto;
import org.irs.dto.ResponseDto;
import org.irs.dto.UserDto;
import org.irs.util.CommonMethods;
import org.irs.util.ConstantValues;
import org.irs.util.PasswordUtil;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class UserService {
    @Inject
    Datasources datasourcesObj;
    @Inject
    CommonMethods commonMethods;
    public ResponseDto fetchData (RequestDto requestDto)throws Exception{
        ResponseDto responseDto=new ResponseDto();
        String query=null;
        if(ConstantValues.queryHashMap.containsKey(requestDto.fileName)){
            query=ConstantValues.queryHashMap.get(requestDto.fileName);
        }else{
            query = commonMethods.readFile(requestDto.fileName);
            if(query==null || query.trim().isEmpty()){
                responseDto.setFailure("No Criteria Found");
                return responseDto;
            }else{
                ConstantValues.queryHashMap.put(requestDto.fileName,query);
            }
        }
        
        String updateQuery=commonMethods.formatQuery(query,requestDto.params);
        List<Object> list=fnExecuteQuery(updateQuery);
        if(list.isEmpty()){
            responseDto.setFailure("No Record Found");
            return responseDto;
        }
        responseDto.data=list;
        return  responseDto;

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
     

    private boolean mobileExists(Connection connection, String mobileNumber) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM users WHERE mobile_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mobileNumber);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count")>0;
            }
        }
        return false;
    }


      public void createUser(UserDto userDto) throws SQLException {
        Connection connection = null;
        try {
            String roleName="citizen";
            connection = datasourcesObj.getConnection();
            connection.setAutoCommit(false);
            if (mobileExists(connection,userDto.mobileNumber)) {
                throw new IllegalArgumentException("Mobile number already registered");
            }
    
             String passwordHash = PasswordUtil.hashPassword(userDto.password); // Hash the password
            // Insert user and get the generated user ID
            Long userId = insertUser(connection, userDto.mobileNumber, userDto.username, passwordHash, userDto.email);

            // Find role ID by role name
            Long roleId = findRoleIdByName(connection, roleName);
            if (roleId == null) {
                throw new SQLException("Role not found: " + roleName);
            }

            // Insert into user_roles table
            assignRoleToUser(connection, userId, roleId);

            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Long insertUser(Connection connection, String mobileNumber, String username, String passwordHash, String email) throws SQLException {
        String insertUserSQL = "INSERT INTO users (mobile_number, full_name, password_hash, email) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement pstmt = connection.prepareStatement(insertUserSQL)) {
            pstmt.setString(1, mobileNumber);
            pstmt.setString(2, username);
            pstmt.setString(3, passwordHash);
            pstmt.setString(4, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            } else {
                throw new SQLException("Failed to insert user.");
            }
        }
    }

    private Long findRoleIdByName(Connection connection, String roleName) throws SQLException {
        String selectRoleSQL = "SELECT id FROM roles WHERE role_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectRoleSQL)) {
            pstmt.setString(1, roleName);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
        }
        return null;
    }

    private void assignRoleToUser(Connection connection, Long userId, Long roleId) throws SQLException {
        String insertUserRoleSQL = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertUserRoleSQL)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, roleId);
            pstmt.executeUpdate();
        }
    }

}
