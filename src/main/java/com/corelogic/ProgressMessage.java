package com.corelogic;

import java.util.HashMap;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

public class ProgressMessage implements TaskListener, ExecutionListener {
    private Expression applicantRefId;
    private Expression employmentRefId;
    private Expression summary;
    private Expression authenticatedUserId;
    private Expression requestid;
    private Expression voeState;

    public void notify(DelegateTask task) {
        DelegateExecution execution = task.getExecution();
        String erid = (String) employmentRefId.getValue(execution);
        String arid = (String) applicantRefId.getValue(execution);
        String sm = (String) summary.getValue(execution);
        String au = (String) authenticatedUserId.getValue(execution);
        String rId = (String) requestid.getValue(execution);
        String vs = (String) voeState.getValue(execution);
        String tid = task.getId();
        String tnm = task.getName();
        String eid = task.getExecutionId();
        System.out.println("Execution Values.....");
        System.out.println("Task ID: " + tid);
        System.out.println("Task Name: " + tnm);
        System.out.println("Exec ID from within class: " + eid);

        //task.name = task.getExecution().getVariable('taskName');
        HashMap progress = new HashMap();
        progress.put("applicantRefId", arid);
        progress.put("employmentRefId", erid);
        progress.put("summary", sm);
        progress.put("taskId", tid);
        progress.put("taskName",tnm);
        progress.put("taskDueDate", task.getDueDate());
        progress.put("requestid", rId);
        progress.put("voeState", vs);
        progress.put("executionId", eid);
        String ti = execution.getTenantId();
        execution.getEngineServices().getIdentityService().setAuthenticatedUserId(au);
        execution.getEngineServices().getRuntimeService().startProcessInstanceByKeyAndTenantId("SaveVOEProgressUpdate", progress, ti);
    }

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String erid = (String) employmentRefId.getValue(delegateExecution);
        String arid = (String) applicantRefId.getValue(delegateExecution);
        String sm = (String) summary.getValue(delegateExecution);
        String au = (String) authenticatedUserId.getValue(delegateExecution);
        String rId = (String) requestid.getValue(delegateExecution);
        String vs = (String) voeState.getValue(delegateExecution);
        String eid = delegateExecution.getId();
        System.out.println("Execution Values.....");
        System.out.println("Exec ID from within class: " + eid);

        //task.name = task.getExecution().getVariable('taskName');
        HashMap progress = new HashMap();
        progress.put("applicantRefId", arid);
        progress.put("employmentRefId", erid);
        progress.put("summary", sm);
        progress.put("requestid", rId);
        progress.put("voeState", vs);
        progress.put("executionId", eid);
        String ti = delegateExecution.getTenantId();
        delegateExecution.getEngineServices().getIdentityService().setAuthenticatedUserId(au);
        delegateExecution.getEngineServices().getRuntimeService().startProcessInstanceByKeyAndTenantId("SaveVOEProgressUpdate", progress, ti);
    }
}
