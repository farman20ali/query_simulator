package org.irs.dto;

import java.util.List;

public class ResponseDto {

    public boolean mBoolean;
    public String errorMessage;
    public List<Object> data;

    public void setFailure(String message) {
        this.mBoolean = true;
        this.errorMessage = message;
    }

    @Override
    public String toString() {
        return "ResponseDto [mBoolean=" + mBoolean + ", errorMessage=" + errorMessage + "]";
    }
}
