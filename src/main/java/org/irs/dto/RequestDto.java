package org.irs.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDto {

     public  String params;

     public String fileName;
     @Override
     public String toString() {
         return "RequestDto [params=" + params + ", fileName=" + fileName + "]";
     }
}