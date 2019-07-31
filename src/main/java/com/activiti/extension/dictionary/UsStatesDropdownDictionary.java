package com.activiti.extension.dictionary;

import com.activiti.extension.model.LabelValueBean;
import java.util.ArrayList;
import java.util.List;

public class UsStatesDropdownDictionary {
    private static List<LabelValueBean> usStates = new ArrayList<>();

    static {
        usStates.add(new LabelValueBean("AL", "AL"));
        usStates.add(new LabelValueBean("AK", "AK"));
        usStates.add(new LabelValueBean("AZ", "AZ"));
        usStates.add(new LabelValueBean("AR", "AR"));
        usStates.add(new LabelValueBean("CA", "CA"));
        usStates.add(new LabelValueBean("CO", "CO"));
        usStates.add(new LabelValueBean("CT", "CT"));
        usStates.add(new LabelValueBean("DE", "DE"));
        usStates.add(new LabelValueBean("DC", "DC"));
        usStates.add(new LabelValueBean("FL", "FL"));
        usStates.add(new LabelValueBean("GA", "GA"));
        usStates.add(new LabelValueBean("HI", "HI"));
        usStates.add(new LabelValueBean("ID", "ID"));
        usStates.add(new LabelValueBean("IL", "IL"));
        usStates.add(new LabelValueBean("IN", "IN"));
        usStates.add(new LabelValueBean("IA", "IA"));
        usStates.add(new LabelValueBean("KS", "KS"));
        usStates.add(new LabelValueBean("KY", "KY"));
        usStates.add(new LabelValueBean("LA", "LA"));
        usStates.add(new LabelValueBean("ME", "ME"));
        usStates.add(new LabelValueBean("MD", "MD"));
        usStates.add(new LabelValueBean("MA", "MA"));
        usStates.add(new LabelValueBean("MI", "MI"));
        usStates.add(new LabelValueBean("MN", "MN"));
        usStates.add(new LabelValueBean("MS", "MS"));
        usStates.add(new LabelValueBean("MO", "MO"));
        usStates.add(new LabelValueBean("MT", "MT"));
        usStates.add(new LabelValueBean("NE", "NE"));
        usStates.add(new LabelValueBean("NV", "NV"));
        usStates.add(new LabelValueBean("NH", "NH"));
        usStates.add(new LabelValueBean("NJ", "NJ"));
        usStates.add(new LabelValueBean("NM", "NM"));
        usStates.add(new LabelValueBean("NY", "NY"));
        usStates.add(new LabelValueBean("NC", "NC"));
        usStates.add(new LabelValueBean("ND", "ND"));
        usStates.add(new LabelValueBean("OH", "OH"));
        usStates.add(new LabelValueBean("OK", "OK"));
        usStates.add(new LabelValueBean("OR", "OR"));
        usStates.add(new LabelValueBean("PA", "PA"));
        usStates.add(new LabelValueBean("RI", "RI"));
        usStates.add(new LabelValueBean("SC", "SC"));
        usStates.add(new LabelValueBean("SD", "SD"));
        usStates.add(new LabelValueBean("TN", "TN"));
        usStates.add(new LabelValueBean("TX", "TX"));
        usStates.add(new LabelValueBean("UT", "UT"));
        usStates.add(new LabelValueBean("VT", "VT"));
        usStates.add(new LabelValueBean("VA", "VA"));
        usStates.add(new LabelValueBean("WA", "WA"));
        usStates.add(new LabelValueBean("WV", "WV"));
        usStates.add(new LabelValueBean("WI", "WI"));
        usStates.add(new LabelValueBean("WY", "WY"));
    }

    private UsStatesDropdownDictionary() {}

    public static List<LabelValueBean> getUsStates() {
        return usStates;
    }
}
