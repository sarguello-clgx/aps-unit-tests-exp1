package com.activiti.extension.dictionary;

import com.activiti.extension.model.LabelValueBean;
import com.corelogic.model.VerificationMethodEnum;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ResultDropdownDictionary {

    private static List<LabelValueBean> thirdPartyOrderReportOptions = new ArrayList<>();
    private static List<LabelValueBean> thirdPartyVerifyReportOptions = new ArrayList<>();
    private static List<LabelValueBean> waitingForCorrespondenceOptions = new ArrayList<>();
    private static List<LabelValueBean> waitingForReportThirdPartyOptions = new ArrayList<>();
    private static List<LabelValueBean> transcribeOptions = new ArrayList<>();
    private static List<LabelValueBean> clarificationNeededOptions = new ArrayList<>();
    private static List<LabelValueBean> thirdPartyOptionsBlankTwnClientName = new ArrayList<>();

    static {
        thirdPartyOrderReportOptions.add(new LabelValueBean("Report Ordered", "pending"));
        thirdPartyOrderReportOptions.add(new LabelValueBean("Unable to Order Report", "notVerified"));
        thirdPartyOrderReportOptions.add(new LabelValueBean("Update Verification Method", "updatemethod"));

        thirdPartyVerifyReportOptions.add(new LabelValueBean("Verified Employment", "verified"));

        waitingForCorrespondenceOptions.add(new LabelValueBean("Upload the Verification Document", "uploadedDoc"));
        waitingForCorrespondenceOptions.add(new LabelValueBean("Unable to Verify", "notVerified"));
        waitingForCorrespondenceOptions.add(new LabelValueBean("Left VM", "vm"));
        waitingForCorrespondenceOptions.add(new LabelValueBean("Update Verification Method", "updatemethod"));
        waitingForCorrespondenceOptions.add(new LabelValueBean("Redeliver Request", "redeliver"));

        waitingForReportThirdPartyOptions.add(new LabelValueBean("Upload the Verification Document", "uploadedDoc"));
        waitingForReportThirdPartyOptions.add(new LabelValueBean("Unable to Verify", "notVerified"));
        waitingForReportThirdPartyOptions.add(new LabelValueBean("Update Verification Method", "updatemethod"));

        transcribeOptions.add(new LabelValueBean("Verified Employment", "verified"));
        transcribeOptions.add(new LabelValueBean("Unable to Verify", "notVerified"));
        transcribeOptions.add(new LabelValueBean("Update Verification Method", "updatemethod"));

        clarificationNeededOptions.add(new LabelValueBean("Verified Employment", "verified"));
        clarificationNeededOptions.add(new LabelValueBean("Unable to Verify", "notVerified"));
        clarificationNeededOptions.add(new LabelValueBean("Left VM", "vm"));
        clarificationNeededOptions.add(new LabelValueBean("Update Verification Method", "updatemethod"));
        clarificationNeededOptions.add(new LabelValueBean("Redeliver Request", "redeliver"));

        thirdPartyOptionsBlankTwnClientName.add(new LabelValueBean("Unable to Order Report", "notVerified"));
        thirdPartyOptionsBlankTwnClientName.add(new LabelValueBean("Update Verification Method", "updatemethod"));
    }

    private ResultDropdownDictionary() {
    }

    public static List<LabelValueBean> getResults(String verificationMethod,
                                                  boolean verifyIncome, String taskName,
                                                  boolean noTwnRelation) {
        return Optional.of(verificationMethod)
                .map(VerificationMethodEnum::fromString)
                .map(vm -> getResults(vm, verifyIncome, taskName, noTwnRelation))
                .orElseThrow(() -> new IllegalArgumentException("unknown verification method " + verificationMethod));
    }

    public static List<LabelValueBean> getResults(VerificationMethodEnum verificationMethodEnum, boolean verifyIncome,
                                                  String taskName, boolean noTwnRelation) {
        if ("qc-transcribe".equals(taskName)) {
            return Collections.singletonList(new LabelValueBean("Verified Employment", "verified"));
        }
        switch (verificationMethodEnum) {
            case PHONE:
                return getPhoneResults(verifyIncome);
            case FAX:
            case EMAIL:
            case MAIL:
                return getFaxEmailMailResults(taskName);
            case THIRD_PARTY:
                return getThirdPartyResults(taskName, noTwnRelation);
        }
        return getPhoneResults(verifyIncome);
    }

    private static List<LabelValueBean> getThirdPartyResults(String taskName, boolean noTwnRelation) {
        switch (taskName) {
            case "orderReport":
                if (noTwnRelation) {
                    return thirdPartyOptionsBlankTwnClientName;
                } else {
                    return thirdPartyOrderReportOptions;
                }
            case "clarification":
                return thirdPartyOrderReportOptions;
            case "transcribe":
                return thirdPartyVerifyReportOptions;
            case "waitingForCorrespondence":

                return waitingForReportThirdPartyOptions;

            default:
                throw new IllegalArgumentException("no valid state for the task=" + taskName);
        }
    }

    private static List<LabelValueBean> getFaxEmailMailResults(String taskName) {
        switch (taskName) {
            case "waitingForCorrespondence":
                return getWaitingForCorrespondenceOptions();
            case "clarification":
                return getClarificationNeededOptions();
            case "transcribe":
                return getTranscribeOptions();
            default:
                return ImmutableList.<LabelValueBean>builder()
                        .add(new LabelValueBean("Verified Employment", "verified"))
                        .add(new LabelValueBean("Unable to Verify", "notVerified"))
                        .add(new LabelValueBean("Update Verification Method", "updatemethod"))
                        .add(new LabelValueBean("Redeliver Request", "redeliver"))
                        .build();
        }
    }

    public static List<LabelValueBean> getPhoneResults(boolean verifyIncome) {
        if (verifyIncome) {
            return ImmutableList.<LabelValueBean>builder()
                    .add(new LabelValueBean("Unable to Verify", "notVerified"))
                    .add(new LabelValueBean("Left VM", "vm"))
                    .add(new LabelValueBean("Employer Requested Callback", "callback"))
                    .add(new LabelValueBean("Update Verification Method", "updatemethod"))
                    .build();
        } else {
            return ImmutableList.<LabelValueBean>builder()
                    .add(new LabelValueBean("Verified Employment", "verified"))
                    .add(new LabelValueBean("Unable to Verify", "notVerified"))
                    .add(new LabelValueBean("Left VM", "vm"))
                    .add(new LabelValueBean("Employer Requested Callback", "callback"))
                    .add(new LabelValueBean("Update Verification Method", "updatemethod"))
                    .add(new LabelValueBean("Redeliver Request", "redeliver"))
                    .build();
        }
    }

    public static List<LabelValueBean> getWaitingForCorrespondenceOptions() {
        return waitingForCorrespondenceOptions;
    }

    public static List<LabelValueBean> getTranscribeOptions() {
        return transcribeOptions;
    }

    public static List<LabelValueBean> getClarificationNeededOptions() {
        return clarificationNeededOptions;
    }
}
