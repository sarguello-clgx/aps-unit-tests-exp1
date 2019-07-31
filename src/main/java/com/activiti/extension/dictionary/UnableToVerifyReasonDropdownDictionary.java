package com.activiti.extension.dictionary;

import com.activiti.extension.model.LabelValueBean;
import com.corelogic.model.VerificationMethodEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnableToVerifyReasonDropdownDictionary {

    private static List<LabelValueBean> unableToVerifyReason = new ArrayList<>();
    private static List<LabelValueBean> unableToVerifyReasonClarification = new ArrayList<>();

    static {
        unableToVerifyReason.add(new LabelValueBean("Needs clarification", "Needs clarification"));

        unableToVerifyReasonClarification.add(new LabelValueBean("No Records Found", "No Records Found"));
        unableToVerifyReasonClarification.add(new LabelValueBean("Employer Declined", "Employer Declined"));
        unableToVerifyReasonClarification.add(new LabelValueBean("Request Being Processed by Employer", "Request Being Processed by Employer"));
    }

    private UnableToVerifyReasonDropdownDictionary() {
    }

    public static List<LabelValueBean> getUnableToVerifyReasons(VerificationMethodEnum verificationMethodEnum,
                                                                boolean verifyIncome,
                                                                boolean noTwnRelation,
                                                                Long processVersion) {
        switch (verificationMethodEnum) {
            case THIRD_PARTY:
                return getUnableToVerifyReasonThirdParty(verifyIncome, noTwnRelation, processVersion);
            default:
                return Collections.emptyList();
        }
    }

    private static List<LabelValueBean> getUnableToVerifyReasonThirdParty(boolean verifyIncome, boolean noTwnRelation, Long processVersion) {
        List<LabelValueBean> result = new ArrayList<>();
        result.add(new LabelValueBean("No matched result returned from 3rd party", "No matched result returned from 3rd party"));
        result.add(new LabelValueBean("Does not allow resellers", "Does not allow resellers"));
        if (verifyIncome) {
            result.add(new LabelValueBean("Requires Salary Key", "Requires Salary Key"));
        }
        result.add(new LabelValueBean("Requires Employer ID", "Requires Employer ID"));
        result.add(new LabelValueBean("SSN mismatch", "SSN mismatch"));
        if (processVersion >= 258) {
            result.add(new LabelValueBean("Requires Date of Birth", "Requires Date of Birth"));
        }
        if (noTwnRelation) {
            String notConfigured = "Account not configured for fulfillment via The Work Number";
            result.add(new LabelValueBean(notConfigured, notConfigured));
        }
        return result;
    }

    public static List<LabelValueBean> getUnableToVerifyReason() {
        return unableToVerifyReason;
    }

    public static List<LabelValueBean> getUnableToVerifyReasonClarification() {
        return unableToVerifyReasonClarification;
    }

}
