package com.activiti.extension.bean;

import com.activiti.domain.runtime.RelatedContent;
import com.activiti.service.runtime.RelatedContentService;
import com.corelogic.BorrowerInsightsResponse;
import com.corelogic.clp.starters.document.DocumentClient;
import com.corelogic.clp.starters.document.dto.DocumentMetadataResponse;
import com.corelogic.clp.starters.document.dto.DocumentRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component("documentStorage")
@Slf4j
public class DocumentStorage {

    public static final int PAGE_SIZE = 15;

    @Value("${mus.hostname}")
    private String url;

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    RelatedContentService relatedContentService;

    @Autowired
    DocumentClient documentClient;

    public DocumentStorage(DocumentClient docClient, OAuthService oAuthService) {
        this.documentClient = docClient;
        this.oAuthService = oAuthService;
    }

    public void getDocument(DelegateExecution execution) {
        log.info(execution.getCurrentActivityName());
    }

    public void saveDocument(DelegateExecution execution, String fieldId, String productRequestRefId, String applicantRefId, String employmentRefId) {
        Object documentType = execution.getVariable("documentType");
        saveDocument(execution, fieldId, productRequestRefId, applicantRefId, employmentRefId, documentType != null ? documentType.toString() : null);
    }

    public void saveDocument(DelegateExecution execution, String fieldId, String productRequestRefId, String applicantRefId, String employmentRefId, String documentType) {
        try {
            List<RelatedContent> contentList = relatedContentService.getFieldContentForProcessInstance(
                    execution.getProcessInstanceId(), fieldId, PAGE_SIZE, 0).getContent();

            if (contentList != null) {
                for (RelatedContent relCon : contentList) {
                    String fileName = relCon.getName();
                    log.info("Content file: " + fileName + ", Content type: " + relCon.getMimeType() + ", Created: " + relCon.getCreated());
                    log.info("Document Id: " + relCon.getContentStoreId());

                    String requestId = execution.getVariable("requestid").toString();
                    String applicantId = execution.getVariable("applicantRefId").toString();
                    String employmentId = execution.getVariable("employmentRefId").toString();

                    saveDocumentInfoToAutomatIQ(relCon.getContentStoreId(), relCon.getName(), requestId, applicantId, employmentId, documentType);
                }
            }
        } catch (Exception e) {
            log.error("Failed to save document: ", e);
            throw new IllegalStateException("Unable to update document info to mus", e);
        }
    }

    private void sendToDocService(InputStream content, String fileName, String contentType) throws IOException {
        DocumentRequest dr = new DocumentRequest(content, fileName, contentType);
        dr.setUseEncrypted(true);
        DocumentMetadataResponse docSrvcResp = documentClient.saveDocument(dr, new HashMap<>());
        IOUtils.closeQuietly(dr.getInputStream());
        log.info(docSrvcResp.getFilename());
    }

    private void saveToAutomatIQ(byte[] data, String fileName, String contentType, String productRequestRefId, String applicantId, String employmentRefId) throws IOException {
        try {
            String saveDocUrl = url + String.format("/api/productrequest/%s/applicant/%s/employment/%s",
                    productRequestRefId, applicantId, employmentRefId);

            System.out.println("The save doc url is ... " + saveDocUrl);

            final String accessToken = oAuthService.getOAuthToken();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource contentsAsResource = new ByteArrayResource(data) {
                @Override
                public String getFilename() {
                    return fileName; // Filename has to be returned in order to be able to post.
                }
            };

            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("contentsAsResource length is:" + contentsAsResource.getByteArray().length);

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
            map.add("file", contentsAsResource);

            BorrowerInsightsResponse response = new RestTemplateBuilder().build()
                    .postForObject(saveDocUrl, new HttpEntity<>(map, headers), BorrowerInsightsResponse.class);

            if (BorrowerInsightsResponse.Status.SUCCESS == response.getStatus()) {
                String documentId = (String) response.getData();
                System.out.println("Saved document id is ... " + documentId);
            } else {
                throw new IllegalStateException("Failed to save document to AutomatIQ, Error: " +
                        response.getErrorInfo());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save document to AutomatIQ", e);
        }
    }

    private void saveDocumentInfoToAutomatIQ(String documentId, String fileName, String productRequestRefId, String applicantId, String employmentRefId, String documentType) throws IOException {
        try {
            String saveDocUrl = url + String.format("/api/productrequest/%s/applicant/%s/employment/%s?documentId=%s&fileName=%s",
                    productRequestRefId, applicantId, employmentRefId, documentId, fileName);

            if (documentType != null) {
                saveDocUrl += "&documentType=" + documentType;
            }
            System.out.println("The save doc url is ... " + saveDocUrl);

            final String accessToken = oAuthService.getOAuthToken();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            ResponseEntity<BorrowerInsightsResponse> response = new RestTemplateBuilder().build()
                    .exchange(saveDocUrl, HttpMethod.PUT,  new HttpEntity<>(null, headers), BorrowerInsightsResponse.class);

            if (BorrowerInsightsResponse.Status.SUCCESS == response.getBody().getStatus()) {
            } else {
                throw new IllegalStateException("Failed to save Document Info to AutomatIQ, Error: " +
                        response.getBody().getErrorInfo());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save Document Info to AutomatIQ", e);
        }
    }

}
