package com.example.poster.easysellingsellers.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by POSTER on 27.08.2017.
 */

public class RequestToAddClientToCompaniesTable {
    private String requestId;
    private String dataRequest;
    private String clientUid;
    private String companyId;
    private String statusRequest;
    private String position;

    public RequestToAddClientToCompaniesTable() {
    }

    public RequestToAddClientToCompaniesTable(String requestId, String dataRequest, String clientUid, String companyId, String statusRequest, String position) {
        this.requestId = requestId;
        this.dataRequest = dataRequest;
        this.clientUid = clientUid;
        this.companyId = companyId;
        this.statusRequest = statusRequest;
        this.position = position;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("requestId" , requestId);
        result.put("dataRequest" , dataRequest);
        result.put("clientUid" , clientUid);
        result.put("companyId" , companyId);
        result.put("statusRequest" , statusRequest);
        result.put("position" , position);
        return result;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDataRequest() {
        return dataRequest;
    }

    public void setDataRequest(String dataRequest) {
        this.dataRequest = dataRequest;
    }

    public String getClientUid() {
        return clientUid;
    }

    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        this.statusRequest = statusRequest;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
