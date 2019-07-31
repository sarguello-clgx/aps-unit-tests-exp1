package com.corelogic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class UpdateApplicantWithExecutionId implements ExecutionListener {
    private Expression applicant;
    private Expression requestid;
    private Expression authenticatedUserId;

    private static final Logger logger = LoggerFactory.getLogger(UpdateApplicantWithExecutionId.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String applicant = (String) this.applicant.getValue(execution);
        String rId = (String) requestid.getValue(execution);
        String eid = execution.getId();
        String au = (String) authenticatedUserId.getValue(execution);
        log.debug("AUTHENTICATED USER: {}", au);
        System.out.println("Execution Values.....");
        System.out.println("Exec ID from within class: " + eid);

        String applicantRefId = null;
        try {
            JsonNode applicantJson = objectMapper.readTree(applicant);
            applicantRefId = applicantJson.get("applicantRefId").asText();
            System.out.println("ApplicantRefId: " + applicantRefId);
        } catch (IOException e) {
            logger.error("error parsing applicant json", e);
        }
        HashMap applicantUpdate = new HashMap();
        applicantUpdate.put("applicantRefId", applicantRefId);
        applicantUpdate.put("requestid", rId);
        applicantUpdate.put("executionId", eid);
        log.error("Appliucant Update: {}", applicantUpdate);

        String ti = execution.getTenantId();
        log.debug("TENANT ID: {}", ti);
        execution.getEngineServices().getIdentityService().setAuthenticatedUserId(au);
        execution.getEngineServices().getRuntimeService().startProcessInstanceByKeyAndTenantId("SaveExecutionIdForTheApplicant", applicantUpdate, ti);

    }
}
