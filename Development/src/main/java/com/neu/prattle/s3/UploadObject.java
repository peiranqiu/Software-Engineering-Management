package com.neu.prattle.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

public class UploadObject {

  public static void main(String[] args) throws IOException {
    Regions clientRegion = Regions.US_EAST_1;
    String bucketName = "cs5500";
    String stringObjKeyName = "test a string";
    String fileObjKeyName = "a test file";
    String fileName = "/Users/daniehao/Desktop/CS_5500/summer1_cs5500/team-4-su20/Development/src/Group/1_06172020.txt";
    String regionName = "us-east-1";
    Region region = Region.getRegion(Regions.fromName(regionName));

    try {
      //This code expects that you have AWS credentials set up per:
      // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
      AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
              .withRegion(clientRegion)
              .build();

      // Upload a text string as a new object.
//      s3Client.putObject(bucketName, stringObjKeyName, "Uploaded String Object");

      // Upload a file as a new object with ContentType and title specified.
      PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, new File(fileName));
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType("plain/text");
      metadata.addUserMetadata("title", "someTitle");
      request.setMetadata(metadata);
      s3Client.putObject(request);
    } catch (AmazonServiceException e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      e.printStackTrace();
    } catch (SdkClientException e) {
      // Amazon S3 couldn't be contacted for a response, or the client
      // couldn't parse the response from Amazon S3.
      e.printStackTrace();
    }
  }
}