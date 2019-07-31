package com.activiti.extension.rest;


import com.activiti.extension.bean.OAuthService;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/corelogic")
public class CLDecryptionREST {

    @Value("${mus.hostname}")
    private String url;

    private final OAuthService oAuthService;

    public CLDecryptionREST(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @RequestMapping(value = "/cldecrypt", method= RequestMethod.GET)
    public String decrypt(@RequestParam(value="cipher", required=false) String cipher) throws IOException {

        //Create the client
        HttpClient client = HttpClientBuilder.create().build();

        try {
            String decryptUrl = url + "/api/decrypt";
            System.out.println("The decryption url in java is ... " + decryptUrl);

            //Create the new request
            HttpPost postRequest = new HttpPost(decryptUrl);
            final String accessToken = oAuthService.getOAuthToken();
            postRequest.setHeader("Authorization", "Bearer " + accessToken);
            postRequest.setHeader("Content-Type","application/json");
            StringEntity entity = new StringEntity(cipher);
            postRequest.setEntity(entity);

            //Execute the request
            HttpResponse response = client.execute(postRequest);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity resp = response.getEntity();
                System.out.println("Content is ... " + resp.getContent());
                String decrypted = EntityUtils.toString(resp);
                return decrypted;
            }
                throw new IllegalStateException("Failed to retrieve decrypted value, status code: " +
                    response.getStatusLine().getStatusCode());
            } catch (IOException e) {
                throw new IllegalStateException("Failed to get decrypted value", e);
            }
        }
}
