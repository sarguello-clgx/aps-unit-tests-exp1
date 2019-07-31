package com.activiti.extension.bean;

import com.activiti.model.editor.form.RequestHeaderRepresentation;
import com.activiti.model.runtime.FormValueRepresentation;
import com.activiti.service.runtime.RestFieldService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class CustomApsRestFieldService extends RestFieldService {

    private final OAuthService oAuthService;

    public CustomApsRestFieldService(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }
    @Override
    public List<FormValueRepresentation> invokeRestUrlWithBasicAuth(String fieldId, String restUrl, String restResponsePath, String restIdProperty,
                                                                    String restLabelProperty, String username, String password,
                                                                    List<RequestHeaderRepresentation> requestHeaders) {
        if (oAuthService.isEndpointNeedToken(restUrl)) {
            final String accessToken = oAuthService.getOAuthToken();
            requestHeaders.add(new RequestHeaderRepresentation("Authorization", "Bearer " + accessToken));
        }

        return super.invokeRestUrlWithBasicAuth(fieldId, restUrl, restResponsePath, restIdProperty,
                restLabelProperty, username, password, requestHeaders);
    }
}
