package com.ibm.mj.core.p8Connection;

import com.filenet.api.authentication.OpenTokenCredentials;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.*;
import com.filenet.api.util.Id;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivilegedExceptionAction;
import java.util.Base64;
import java.util.Properties;

public class Authentication {

    ObjectStore objectStore;
    String configFile;


    String getProperty(String attribute) throws IOException {
        FileInputStream in = new FileInputStream(configFile);
        Properties prop = new Properties();
        prop.load(in);
        in.close();
        return prop.getProperty(attribute);
    }


    public void connectwithoAuth(String testUser, String  tokenUri, String clientId, String clientSecret, String testPwd) throws Exception {
        OIDCOAuthClient client = new OIDCOAuthClient(tokenUri, clientId, clientSecret);
       String oauthToken = client.getAccessToken();


        // Create instance of class used to pass OAuth token to WSI requests using CE API
        OpenTokenCredentials otc = new OpenTokenCredentials(testUser, oauthToken, null);


        // ------------------------------------------------
        // Get Domain
        // ------------------------------------------------
        PrivilegedExceptionAction<Domain> getDomainPEA = new PrivilegedExceptionAction<Domain>() {


            public Domain run() throws Exception
            {
                try
                {
                    Domain domain = Factory.Domain.fetchInstance(Factory.Connection.getConnection(
                                    getProperty("ce.URI")),
                            getProperty("ce.domain"),
                            null);

                    System.out.println("Got domain: " + domain);
                    return domain;
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unable to get domain", e);
                }
            }
        };
        final Domain domain = otc.doAs(getDomainPEA);

        // ------------------------------------------------
        // Get ObjectStore
        // ------------------------------------------------
        final String objectStoreName = getProperty("ce.objectStoreName");
        PrivilegedExceptionAction<ObjectStore> getObjectStorePEA = new PrivilegedExceptionAction<ObjectStore>() {


            public ObjectStore run() throws Exception
            {
                try
                {
                    ObjectStore os = Factory.ObjectStore.fetchInstance(domain,
                            objectStoreName,
                            null);
                    objectStore = os;
                    return os;
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unable to get objectstore: " + objectStoreName, e);
                }
            }
        };
        final ObjectStore os = otc.doAs(getObjectStorePEA);

        // ------------------------------------------------
        // Create Document with Id
        // ------------------------------------------------
        final Id docId = Id.createId();
        String str = "This is a test";
        final InputStream inputStream = new ByteArrayInputStream(str.getBytes());

        PrivilegedExceptionAction<Object> createDocumentPEA = new PrivilegedExceptionAction<Object>() {
            public Document run() throws Exception
            {
                try
                {
                    Document doc = Factory.Document.createInstance(os, null, docId);
                    ContentElementList cel = Factory.ContentElement.createList();
                    ContentTransfer ct = Factory.ContentTransfer.createInstance();
                    ct.setCaptureSource(inputStream);
                    cel.add(ct);
                    doc.set_ContentElements(cel);
                    doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
                    doc.save(RefreshMode.NO_REFRESH);
                    return null;
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unable to create document", e);
                }
            }
        };
        otc.doAs(createDocumentPEA);

        // ------------------------------------------------
        // Retrieve Document with Id
        // ------------------------------------------------
        PrivilegedExceptionAction<Document> retrieveDocumentPEA = new PrivilegedExceptionAction<Document>() {
            public Document run() throws Exception
            {
                try
                {
                    Document doc = Factory.Document.fetchInstance(os, docId,null);
                    return doc;
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unable to retrieve document", e);
                }
            }
        };
        final Document document = otc.doAs(retrieveDocumentPEA);

        // ------------------------------------------------
        // Read Document content
        // ------------------------------------------------
        PrivilegedExceptionAction<InputStream> retrieveDocInputStreamPEA = new PrivilegedExceptionAction<InputStream>() {
            public InputStream run() throws Exception
            {
                try
                {
                    InputStream is = document.accessContentStream(0);
                    return is;


                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unable to access document content", e);
                }
            }
        };
        InputStream docIS = otc.doAs(retrieveDocInputStreamPEA);
        BufferedReader reader = new BufferedReader(new InputStreamReader(docIS));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        System.out.println(out.toString());
        reader.close();



        // ------------------------------------------------
        // Delete document
        // ------------------------------------------------
        PrivilegedExceptionAction<Object> deleteDocumentPEA = new PrivilegedExceptionAction<Object>() {
            public Document run() throws Exception
            {
                try
                {
                    document.delete();
                    document.save(RefreshMode.NO_REFRESH);


                    return null;
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unable to delete document", e);
                }
            }
        };
        otc.doAs(deleteDocumentPEA);
    }


    class OIDCOAuthClient {
        private   String clientId = "your_client_id";
        private  String clientSecret = "your_client_secret";
        private   String tokenUri = "https://your_auth_url/oauth/token";

        private   String SERVICE_URL = "https://your_service_url/";

        public OIDCOAuthClient(String tokenUri, String clientId, String clientSecret) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.tokenUri = tokenUri;
        }


        private  String getAccessToken() throws Exception {
            String authString = clientId + ":" + clientSecret;
            String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

            URL url = new URL(tokenUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + encodedAuthString);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String body = "grant_type=client_credentials";
            byte[] bodyBytes = body.getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(bodyBytes.length));

            connection.setDoOutput(true);
            connection.getOutputStream().write(bodyBytes);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getString("access_token");
        }

    }


}
