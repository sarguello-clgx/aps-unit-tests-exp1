package com.activiti.extension.dictionary;

import com.activiti.extension.model.LabelValueBean;
import java.util.ArrayList;
import java.util.List;

public class VerificationMethodDropdownDictionary {

    private static List<LabelValueBean> voeVerificationMethods = new ArrayList<>();
    private static List<LabelValueBean> voiVerificationMethods = new ArrayList<>();

    static {
        voeVerificationMethods.add(new LabelValueBean("3rd Party", "3rd Party"));
        voeVerificationMethods.add(new LabelValueBean("Email", "Email"));
        voeVerificationMethods.add(new LabelValueBean("Fax", "Fax"));
        voeVerificationMethods.add(new LabelValueBean("Mail", "Mail"));
        voeVerificationMethods.add(new LabelValueBean("Military", "Military"));
        voeVerificationMethods.add(new LabelValueBean("Phone", "Phone"));
        voeVerificationMethods.add(new LabelValueBean("Self Employed", "Self Employed"));

        voiVerificationMethods.add(new LabelValueBean("3rd Party", "3rd Party"));
        voiVerificationMethods.add(new LabelValueBean("Email", "Email"));
        voiVerificationMethods.add(new LabelValueBean("Fax", "Fax"));
        voiVerificationMethods.add(new LabelValueBean("Mail", "Mail"));
        voiVerificationMethods.add(new LabelValueBean("Military", "Military"));
        voiVerificationMethods.add(new LabelValueBean("Self Employed", "Self Employed"));
    }

    private VerificationMethodDropdownDictionary() {}

    public static List<LabelValueBean> getVoeVerificationMethods() {
        return voeVerificationMethods;
    }

    public static List<LabelValueBean> getVoiVerificationMethods() {
        return voiVerificationMethods;
    }

}
