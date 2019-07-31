package com.corelogic;

import java.util.HashMap;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateDisableCancelListener implements TaskListener {

    private static final Logger logger = LoggerFactory.getLogger(UpdateDisableCancelListener.class);

    private Expression applicantRefId;
    private Expression employmentRefId;
    private Expression authenticatedUserId;
    private Expression requestid;
    private Expression disableCancel;

    @Override
    public void notify(DelegateTask delegateTask) {
        DelegateExecution execution = delegateTask.getExecution();
        String employmentRefIdValue = (String) employmentRefId.getValue(execution);
        String arid = (String) applicantRefId.getValue(execution);
        String au = (String) authenticatedUserId.getValue(execution);
        String rId = (String) requestid.getValue(execution);
        String disableCancelValue = (String) disableCancel.getValue(execution);

        boolean disableCancel = Boolean.valueOf(disableCancelValue) && delegateTask.getAssignee() != null;
        logger.info("Execution Values.....");
        logger.info("Updating disableCancel for Employment {} to {}", employmentRefIdValue, disableCancelValue);

        HashMap data = new HashMap();
        data.put("applicantRefId", arid);
        data.put("employmentRefId", employmentRefIdValue);
        data.put("requestid", rId);
        data.put("disableCancel", Boolean.toString(disableCancel));
        String ti = execution.getTenantId();
        execution.getEngineServices().getIdentityService().setAuthenticatedUserId(au);
        execution.getEngineServices().getRuntimeService().startProcessInstanceByKeyAndTenantId("ToggleCancelState", data, ti);
    }
}
