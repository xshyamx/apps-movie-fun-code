package org.superbiz.moviefun;

import org.junit.Test;
import org.superbiz.moviefun.blobstore.ServiceCredentials;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ServiceCredentialsTest {

    @Test
    public void testDataSource() throws Exception {
        //language=JSON
        String vcapServices =
            "{\n" +
                "  \"aws-s3\": [\n" +
                "    {\n" +
                "      \"credentials\": {\n" +
                "        \"access_key_id\": \"AOG4VEUKIAIAJFM6FHIA\",\n" +
                "        \"bucket\": \"cf-813f2ksj-b28a-4427-baab-822f11s25f30\",\n" +
                "        \"region\": \"us-east-1\",\n" +
                "        \"secret_access_key\": \"hCte2uMZiAqV74oxdwfGHNd+3G0C11hEe2HFh4w4\"\n" +
                "      },\n" +
                "      \"label\": \"aws-s3\",\n" +
                "      \"name\": \"s3\",\n" +
                "      \"plan\": \"standard\",\n" +
                "      \"provider\": null,\n" +
                "      \"syslog_drain_url\": null,\n" +
                "      \"tags\": [],\n" +
                "      \"volume_mounts\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ServiceCredentials serviceCredentials = new ServiceCredentials(vcapServices);

        String s3AccessKey = serviceCredentials.getCredential("s3", "aws-s3", "access_key_id");
        String s3SecretKey = serviceCredentials.getCredential("s3", "aws-s3", "secret_access_key");
        String s3BucketName = serviceCredentials.getCredential("s3", "aws-s3", "bucket");

        assertThat(s3AccessKey, equalTo("AOG4VEUKIAIAJFM6FHIA"));
        assertThat(s3SecretKey, equalTo("hCte2uMZiAqV74oxdwfGHNd+3G0C11hEe2HFh4w4"));
        assertThat(s3BucketName, equalTo("cf-813f2ksj-b28a-4427-baab-822f11s25f30"));
    }
}
