package com.corelogic.spike

import org.activiti.engine.test.ActivitiRule
import org.activiti.engine.test.Deployment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import java.util.*

class FirstTest {
    @Rule
    @JvmField
    val activitiRule = ActivitiRule()

    @Test
    @Deployment(resources = arrayOf(
            "EIQF/bpmn-models/Confirm Borrower Authorization-44062.bpmn20.xml",
            "EIQF/bpmn-models/Update ExecutionId for the applicant-44060.bpmn20.xml"
    ))
    fun test() {
        val runtimeService = activitiRule.getRuntimeService()
        val variables = HashMap<String, Any>()
        variables.put("applicant", """
        {
            "applicantRefId": "1",
            "firstName": "David",
            "lastName": "Kowis",
            "ssn": "123456789",
            "ssnLastFour": "6789",
            "loanNumber": "million-dollar-loan"
        }
        """.trimIndent())
        variables.put("requestid", "1")
        variables.put("loanNumber", "million-dollar-loan")
        variables.put("apsHostName", "the-weather-channel")
        variables.put("containsReverification", "false") // <- not sure about that one
        variables.put("customerName", "customerName")
        variables.put("requesterEmail", "requesterEmailVal")
        variables.put("requester", "requesterVal")
        variables.put("dateCreated", "2019-07-30")


        val processInstance = runtimeService.startProcessInstanceByKey("ConfirmBorrowerAuthorization", variables)


        assertThat(processInstance).isNotNull()

        val task = activitiRule.getTaskService().createTaskQuery().singleResult()
        assertThat(task.getName()).isEqualTo("Activiti is awesome!")
    }
}
