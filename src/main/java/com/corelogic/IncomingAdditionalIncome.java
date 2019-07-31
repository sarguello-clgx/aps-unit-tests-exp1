package com.corelogic;

import com.activiti.extension.bean.CustomApsRestCallBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncomingAdditionalIncome implements Serializable {
    private String incomeType;
    private String yearToDate;
    private String pastYear;
    private String twoYearsPast;

    public IncomingAdditionalIncome(String incomeType, String yearToDate, String pastYear, String twoYearsPast) {
        String undefined = "undefined";
        this.incomeType = undefined.equals(incomeType)? null: incomeType;
        this.yearToDate = undefined.equals(yearToDate)? null: yearToDate;
        this.pastYear = undefined.equals(pastYear)? null: pastYear;
        this.twoYearsPast = undefined.equals(twoYearsPast)? null: twoYearsPast;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public IncomingAdditionalIncome setIncomeType(String incomeType) {
        this.incomeType = incomeType;
        return this;
    }

    public String getYearToDate() {
        return yearToDate;
    }

    public IncomingAdditionalIncome setYearToDate(String yearToDate) {
        this.yearToDate = yearToDate;
        return this;
    }

    public String getPastYear() {
        return pastYear;
    }

    public IncomingAdditionalIncome setPastYear(String pastYear) {
        this.pastYear = pastYear;
        return this;
    }

    public String getTwoYearsPast() {
        return twoYearsPast;
    }

    public IncomingAdditionalIncome setTwoYearsPast(String twoYearsPast) {
        this.twoYearsPast = twoYearsPast;
        return this;
    }

    @Override
    public String toString() {
        try {
            return CustomApsRestCallBean.objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException |NullPointerException e) {
            return null;
        }
    }
}
