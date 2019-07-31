/**
 * 
 */
package com.activiti.extension.bean;

import com.activiti.domain.idm.EndpointConfiguration;
import com.activiti.model.editor.kickstart.TypedValueObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateHelper;
import org.activiti.engine.delegate.Expression;
import org.apache.chemistry.opencmis.commons.impl.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sergii Tsymbal (stsymbal@bp-3.com)
 *
 */

@Component
@Slf4j
public class CustomApsRestCallBean  extends com.activiti.runtime.activiti.bean.RestCallBean {

    private final OAuthService oAuthService;

    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CustomApsRestCallBean(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            super.execute(execution);
        } catch (Exception e) {
            log.info("Encountered error while executing APS RestCall. Throwing BpmnError(\"{}\")", e);
            throw new BpmnError(e.getMessage());
        }
    }

    @Override
    protected List<RequestHeader> composeRequestHeaders(Map<String, List<ExtensionElement>> extensionElementsMap, DelegateExecution execution) {
        List<RequestHeader> headers = super.composeRequestHeaders(extensionElementsMap, execution);

        log.info("Custom APS Rest - getting ready to update headers as needed.......");
        Expression baseEndpointExpression = DelegateHelper.getFieldExpression(execution, EXPRESSION_BASE_ENDPOINT);
        Expression baseEndpointNameExpression = DelegateHelper.getFieldExpression(execution, EXPRESSION_BASE_ENDPOINT_NAME);
        log.info("Custom APS Rest - getting the base endpoint name.......");
        log.info(baseEndpointNameExpression.getExpressionText());

        String baseEndpointIdValue = null;
        if (baseEndpointExpression != null) {
            baseEndpointIdValue = getExpressionAsString(baseEndpointExpression, execution);
        }

        String baseEndpointNameValue = null;
        if (baseEndpointNameExpression != null) {
            baseEndpointNameValue = getExpressionAsString(baseEndpointNameExpression, execution);
        }

        EndpointConfiguration endpointConfiguration = getEndpointConfiguration(baseEndpointIdValue, baseEndpointNameValue, execution);

        if (oAuthService.isEndpointNeedToken(endpointConfiguration)) {
            final String accessToken = oAuthService.getOAuthToken();
            headers.add(new RequestHeader("Authorization", "Bearer " + accessToken));
        }

        return headers;
    }

    @Override
    protected JsonNode createJSONRequestObject(List<TypedValueObject> valueObjects, String requestMappingJSONTemplate) {
        JsonNode jsonObject = null;
        if (requestMappingJSONTemplate != null && requestMappingJSONTemplate.length() > 0) {

            for (TypedValueObject valueObject : valueObjects) {
                String varName = valueObject.getName();

                String varValue = valueObject.getValue();

                if (StringUtils.isEmpty(varValue)) {
                    varValue = "null";
                    requestMappingJSONTemplate = requestMappingJSONTemplate
                            .replaceAll("\"\\$\\{" + varName + "\\}\"", varValue)
                            .replaceAll("\\$\\{" + varName + "\\}", varValue);

                } else if (valueObject.getType() == null && isJson(varValue)) {
                    requestMappingJSONTemplate = requestMappingJSONTemplate
                            .replaceAll("\\$\\{" + varName + "\\}", varValue);
                } else {
                    varValue = JSONObject.escape(varValue);

                    if (StringUtils.equals(valueObject.getType(), "string")) {
                        String unquotedValue = JSONObject.escape(varValue);
                        String quotedValue = "\"" + unquotedValue + "\"";

                        requestMappingJSONTemplate = replaceStringValue(requestMappingJSONTemplate, varName , unquotedValue, quotedValue);

                    } else {
                        if (StringUtils.equals(valueObject.getType(), "boolean")) {
                            requestMappingJSONTemplate = requestMappingJSONTemplate
                                    .replaceAll("\\$\\{" + varName + "\\}", varValue);

                        } else if (StringUtils.equals(valueObject.getType(), "long")) {
                            requestMappingJSONTemplate = requestMappingJSONTemplate
                                    .replaceAll("\\$\\{" + varName + "\\}", varValue);

                        } else if (StringUtils.equals(valueObject.getType(), "integer")) {
                            requestMappingJSONTemplate = requestMappingJSONTemplate
                                    .replaceAll("\\$\\{" + varName + "\\}", varValue);

                        } else if (StringUtils.equals(valueObject.getType(), "date")) {
                            String unquotedValue =  getFormattedDateValue(valueObject);
                            String quotedValue = "\"" + unquotedValue + "\"";

                            requestMappingJSONTemplate = replaceStringValue(requestMappingJSONTemplate, varName , unquotedValue, quotedValue);
                        }

                    }

                }

            }

            // replace with null all unknown variables
            requestMappingJSONTemplate = requestMappingJSONTemplate
                    .replaceAll("\"\\$\\{(.+)\\}\"", "null")
                    .replaceAll("\\$\\{(.+)\\}", "null");

            try {
                jsonObject = objectMapper.readTree(requestMappingJSONTemplate);
            } catch (IOException e) {
                throw new UnsupportedOperationException(e);
            }
        }
        return jsonObject;
    }

    private boolean isJson(String varValue) {
        try {
            objectMapper.readTree(varValue);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
