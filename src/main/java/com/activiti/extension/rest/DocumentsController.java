package com.activiti.extension.rest;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/mus/documents")
public class DocumentsController {

    private final RestTemplate restTemplate;
    private final String apiBaseUrl;

    public DocumentsController(@Qualifier("oauthRestTemplate") RestTemplate restTemplate,
                               @Value("${mus.hostname}") String backendServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = backendServiceBaseUrl;
    }

    @RequestMapping(value = "/consent", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getConsentDocument(@RequestParam("productRequestRefId") String productRequestRefId,
                                                     @RequestParam("applicantRefId") long applicantId) {
        String url = String.format("/api/productrequest/%s/applicant/%s/consentdocument", productRequestRefId, applicantId);

        return processDocumentRequest(url);
    }

    @RequestMapping(value = "/getform", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getForm(@RequestParam("productRequestRefId") String productRequestRefId,
                                          @RequestParam("applicantRefId") long applicantRefId,
                                          @RequestParam("employmentRefId") long employmentRefId,
                                          @RequestParam("includeCoverSheet") boolean includeCoverSheet) {
        String url = String.format("/api/productrequest/%s/applicant/%s/employment/%s/getform?includeCoverSheet=%s",
                productRequestRefId, applicantRefId, employmentRefId, includeCoverSheet);

        return processDocumentRequest(url);
    }

    @RequestMapping(value = "/get-mail-form", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getMailForm(@RequestParam("productRequestRefId") String productRequestRefId,
                                          @RequestParam("applicantRefId") long applicantRefId,
                                          @RequestParam("employmentRefId") long employmentRefId,
                                          @RequestParam("includeCoverSheet") boolean includeCoverSheet) {
        String url = String.format("/api/productrequest/%s/applicant/%s/employment/%s/get-mail-form?includeCoverSheet=%s",
                productRequestRefId, applicantRefId, employmentRefId, includeCoverSheet);

        return processDocumentRequest(url);
    }

    private ResponseEntity<byte[]> processDocumentRequest(String url) {
        this.restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                apiBaseUrl + url, HttpMethod.GET, entity, byte[].class);

        return response;
    }
}
