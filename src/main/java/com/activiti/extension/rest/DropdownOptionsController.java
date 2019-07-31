package com.activiti.extension.rest;

import com.activiti.extension.dictionary.ResultDropdownDictionary;
import com.activiti.extension.dictionary.UnableToVerifyReasonDropdownDictionary;
import com.activiti.extension.dictionary.UsStatesDropdownDictionary;
import com.activiti.extension.dictionary.VerificationMethodDropdownDictionary;
import com.activiti.extension.model.ListDataBean;
import com.corelogic.model.VerificationMethodEnum;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/form/dropdown")
public class DropdownOptionsController {

    public DropdownOptionsController() {
    }

    //this endpoint may deprecated in the future.
    @RequestMapping(value = "options/results", method = RequestMethod.GET)
    public ResponseEntity<ListDataBean> getResults(
            @RequestParam("verifyIncome") boolean verifyIncome,
            @RequestParam("verificationMethod") String verificationMethod,
            @RequestParam(value = "reverification", required = false, defaultValue = "false") boolean reverification,
            @RequestParam(value = "thirdPartyOrderIsPending", required = false, defaultValue = "false") boolean thirdPartyOrderIsPending,
            @RequestParam(value = "waitingForCorrespondence", required = false, defaultValue = "false") boolean waitingForCorrespondence,
            @RequestParam(value = "clarificationNeeded", required = false, defaultValue = "false") Boolean clarificationNeeded,
            @RequestParam(value = "noTwnRelation", required = false, defaultValue = "false") Boolean noTwnRelation) {
        String currentTaskName = waitingForCorrespondence ? "waitingForCorrespondence" : clarificationNeeded ? "clarification" : thirdPartyOrderIsPending ? "transcribe" : "orderReport";
        return ResponseEntity.ok(new ListDataBean(ResultDropdownDictionary.getResults(verificationMethod, verifyIncome,
                currentTaskName, noTwnRelation)));
    }

    //this endpoint is going to replace above endpoint.
    @RequestMapping(value = "options/result-selections", method = RequestMethod.GET)
    public ResponseEntity<ListDataBean> getResults(@RequestParam("verifyIncome") boolean verifyIncome,
                                                   @RequestParam("verificationMethod") String verificationMethod,
                                                   @RequestParam(value = "reverification", required = false, defaultValue = "false") boolean reverification,
                                                   @RequestParam(value = "thirdPartyOrderIsPending", required = false) Boolean thirdPartyOrderIsPending,
                                                   @RequestParam(value = "currentTaskName", required = false, defaultValue = "") String currentTaskName,
                                                   @RequestParam(value = "noTwnRelation", required = false, defaultValue = "false") Boolean noTwnRelation) {
        if (thirdPartyOrderIsPending != null && verificationMethod.equals("3rd Party")) {
            if (thirdPartyOrderIsPending) {
                currentTaskName = "orderReport";
            } else {
                currentTaskName = "transcribe";
            }
        }
        return ResponseEntity.ok(new ListDataBean(ResultDropdownDictionary.getResults(verificationMethod, verifyIncome,
                currentTaskName, noTwnRelation)));
    }

    @RequestMapping(value = "options/verification-methods", method = RequestMethod.GET)
    public ResponseEntity<ListDataBean> getVerificationMethods(@RequestParam("verifyIncome") boolean verifyIncome) {
        if (verifyIncome) {
            return new ResponseEntity<>(new ListDataBean(VerificationMethodDropdownDictionary.getVoiVerificationMethods()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ListDataBean(VerificationMethodDropdownDictionary.getVoeVerificationMethods()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "options/unable-to-verify-reasons", method = RequestMethod.GET)
    public ResponseEntity<ListDataBean> getUnableToVerifyReasons(
            @RequestParam("verifyIncome") boolean verifyIncome,
            @RequestParam("verificationMethod") String verificationMethod,
            @RequestParam(value = "clarificationNeeded", required = false, defaultValue = "false") boolean clarificationNeeded,
            @RequestParam(value = "noTwnRelation", required = false, defaultValue = "false") Boolean noTwnRelation,
            @RequestParam(value = "processVersion", required = false, defaultValue = "257") Long processVersion) {
        if ("Fax".equalsIgnoreCase(verificationMethod) || "Email".equalsIgnoreCase(verificationMethod)) {
            if (clarificationNeeded) {
                return new ResponseEntity<>(new ListDataBean(UnableToVerifyReasonDropdownDictionary.getUnableToVerifyReasonClarification()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ListDataBean(UnableToVerifyReasonDropdownDictionary.getUnableToVerifyReason()), HttpStatus.OK);
            }
        }

        return Optional.of(verificationMethod)
                .map(VerificationMethodEnum::fromString)
                .map(vm -> UnableToVerifyReasonDropdownDictionary.getUnableToVerifyReasons(vm, verifyIncome, noTwnRelation, processVersion))
                .map(ListDataBean::new)
                .map(results -> new ResponseEntity<>(results, HttpStatus.OK))
                .orElse(new ResponseEntity<>(new ListDataBean(new ArrayList<>()), HttpStatus.OK));
    }

    @RequestMapping(value = "options/us-states", method = RequestMethod.GET)
    public ResponseEntity<ListDataBean> getUsStates() {
        return ResponseEntity.ok(new ListDataBean(UsStatesDropdownDictionary.getUsStates()));
    }

}
