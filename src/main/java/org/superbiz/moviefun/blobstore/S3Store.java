package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {
    private AmazonS3Client s3Client;
    private String s3BucketName;

    public S3Store(AmazonS3Client s3Client, String s3BucketName) {
        this.s3Client = s3Client;
        this.s3BucketName = s3BucketName;
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(blob.contentType);
        PutObjectRequest r = new PutObjectRequest(s3BucketName, blob.name, blob.inputStream, metadata);
        s3Client.putObject(r);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        S3Object obj = s3Client.getObject(s3BucketName, name);
        return obj == null
                ? Optional.empty()
                : Optional.of(new Blob(name, obj.getObjectContent(), obj.getObjectMetadata().getContentType()));
    }

    @Override
    public void deleteAll() {
        ObjectListing listing = s3Client.listObjects(s3BucketName);
        for ( S3ObjectSummary sum : listing.getObjectSummaries() ) {
            s3Client.deleteObject(s3BucketName, sum.getKey());
        }
    }
}
