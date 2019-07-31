package com.activiti.extension.bean;

import com.activiti.content.storage.api.ContentObject;
import com.activiti.content.storage.api.ContentStorage;
import com.activiti.content.storage.exception.ContentStorageException;
import com.corelogic.clp.starters.document.DocumentClient;
import com.corelogic.clp.starters.document.dto.DocumentMetadataResponse;
import com.corelogic.clp.starters.document.dto.DocumentRequest;
import com.corelogic.clp.starters.document.dto.DocumentResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Primary
@Order(1)
@ConditionalOnProperty(value = "contentstorage.documentservice.enabled", havingValue = "true")
public class DocumentServiceStorage implements ContentStorage {

    private static final int MAX_CONTENT_LENGTH = 1024000;

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceStorage.class);
    private final RestTemplate restTemplate;
    private DocumentClient documentClient;

    public DocumentServiceStorage(@Qualifier("oauthRestTemplate") RestTemplate restTemplate,
                                  DocumentClient documentClient) {
        this.restTemplate = restTemplate;
        this.documentClient = documentClient;
    }

    @Override
    public ContentObject createContentObject(InputStream contentStream, Long lengthHint) {
        return createContentObject(contentStream, lengthHint, true);
    }

    @Override
    public ContentObject createContentObject(InputStream contentStream, Long lengthHint, boolean closeStream) {
        return saveOrUpdate(Optional.empty(), contentStream, lengthHint, closeStream);
    }

    @Override
    public ContentObject updateContentObject(String id, InputStream contentStream, Long lengthHint) {
        return saveOrUpdate(Optional.of(id), contentStream, lengthHint, false);
    }

    @Override
    public ContentObject getContentObject(String id) {
        try {
            DocumentResponse documentResponse = this.documentClient.getDocument(id);

            byte[] content = IOUtils.toByteArray(documentResponse.getInputStream(), documentResponse.getContentLength());
            long length = content.length;

            IOUtils.closeQuietly(documentResponse.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(content);

            LOG.info("Document retrieved, id: {}, size: {}", id, length);

            return new DocumentServiceContentObject(id, length, inputStream);
        } catch (IOException ex) {
            final String errMsg = String.format("Error retrieving document, id: %s", id);
            LOG.error(errMsg, ex);
            throw new ContentStorageException(errMsg, ex);
        }
    }

    @Override
    public void deleteContentObject(String s) {
        //should be empty for Alfresco to be able to delete template documents
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private ContentObject saveOrUpdate(Optional<String> id, InputStream contentStream, Long lengthHint, boolean closeStream) {
        byte[] content = copy(contentStream, lengthHint);

        try {
            String documentId;
            if (id.isPresent()) {
                documentId = id.get();
            } else {
                documentId = UUID.randomUUID().toString().replace("-", "");
            }

            DocumentRequest documentRequest = new DocumentRequest(new ByteArrayInputStream(content), documentId, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            documentRequest.setUseEncrypted(true);
            DocumentMetadataResponse documentMetadataResponse = documentClient.saveDocument(documentRequest, new HashMap<>());
            LOG.info(documentMetadataResponse.getFilename());

            long length = content.length;
            InputStream inputStream = new ByteArrayInputStream(content);

            if (closeStream) {
                IOUtils.closeQuietly(contentStream);
            }

            LOG.info("Document saved or updated, id: {}, size: {}", documentId, length);

            return new DocumentServiceContentObject(documentId, length, inputStream);
        } catch (IOException ex) {
            final String errMsg = "Error saving or updating document";
            LOG.error(errMsg, ex);
            throw new ContentStorageException(errMsg, ex);
        }
    }

    public static final class DocumentServiceContentObject implements ContentObject {
        private final String id;
        private final long length;
        private final InputStream content;

        public DocumentServiceContentObject(String id, long length, InputStream content) {
            this.id = id;
            this.length = length;
            this.content = content;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public long getContentLength() {
            return length;
        }

        @Override
        public InputStream getContent() {
            return content;
        }
    }

    public static byte[] copy(InputStream inputStream, Long lengthHint) {
        try (ByteArrayOutputStream inputStreamCopy = new ByteArrayOutputStream(lengthHint != null && lengthHint > 0 ? lengthHint.intValue() : MAX_CONTENT_LENGTH)) {
            IOUtils.copyLarge(inputStream, inputStreamCopy);
            return inputStreamCopy.toByteArray();
        } catch (IOException ex) {
            throw new ContentStorageException("IO Error: " + ex.getMessage(), ex);
        }
    }

}