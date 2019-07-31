package com.activiti.extension.bean;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("envVariables")
public class EnvironmentVariables implements JavaDelegate {
    @Value("${mus.hostname}")
    private String mushostname;

    @Value("${aps.hostname}")
    private String apshostname;

    private Expression musEnvName;
    private Expression apsEnvName;

    //Retrieve an environment variable
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String musenv = (String) musEnvName.getValue(execution);
        System.out.println("MUS Hostname is ... " + mushostname);
        execution.setVariable(musenv, mushostname);

        String apsenv = (String) apsEnvName.getValue(execution);
        System.out.println("APS Hostname is ... " + apshostname);
        execution.setVariable(apsenv, apshostname);
    }
}