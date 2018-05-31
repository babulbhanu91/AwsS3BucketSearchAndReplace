package com.main;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class AwsS3Bucket {

	/*public static void main(String a[]){
		AwsS3Bucket obj= new AwsS3Bucket();
		obj.getfiles();
	}*/
	private AmazonS3 s3client = null;
	
	public AmazonS3 getSeclient(){
			return s3client;
		
	}
	public AwsS3Bucket(String accessKey, String secretKet ){
		AWSCredentials credentials = new BasicAWSCredentials(
				accessKey, 
				secretKet
				);
		try{
		 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_2)
				  .build();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void delete(){
		AWSCredentials credentials = new BasicAWSCredentials(
				  "AKIAI3BCSIYMP3CEUSBA", 
				  "70kHXCYnzVVW5PPbvTbzsCr3LfM0z1JJ0kF3aeWP"
				);
		String bucketName = "babulbhanu91bucket2";
		try{
		AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_WEST_2)
				  .build();
		s3client.deleteBucket(bucketName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public JSONArray getfiles(){
		JSONArray output = new JSONArray();
		/*AWSCredentials credentials = new BasicAWSCredentials(
				  "AKIAI3BCSIYMP3CEUSBA", 
				  "70kHXCYnzVVW5PPbvTbzsCr3LfM0z1JJ0kF3aeWP"
				);*/
		try{
		/*AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_2)
				  .build();*/
		
		List<Bucket> buckets = s3client.listBuckets();
	
		for(Bucket bucket : buckets) {
		    System.out.println(bucket.getName());
		    ListObjectsV2Result result = s3client.listObjectsV2(bucket.getName());
		    List<S3ObjectSummary> objects = result.getObjectSummaries();
		    for (S3ObjectSummary os: objects) {
		        System.out.println("* " + os.getKey());
		        // download object
		        S3Object o = s3client.getObject(bucket.getName(), os.getKey());
		        S3ObjectInputStream s3is = o.getObjectContent();
		        FileOutputStream fos = new FileOutputStream(new File("D:/aws-files/"+os.getKey()));
		        byte[] read_buf = new byte[1024];
		        int read_len = 0;
		        while ((read_len = s3is.read(read_buf)) > 0) {
		            fos.write(read_buf, 0, read_len);
		        }
		        s3is.close();
		        fos.close();
		        
		        JSONObject jobj = writeFile(new File("D:/aws-files/"+os.getKey()));
		        output.add(jobj);
		        uploadFile(new File("D:/aws-files/"+os.getKey()),bucket.getName());
		      
		    }
		   
		}
	   }catch(Exception e){
		e.printStackTrace();
	    }
		 return output;
	}
	public void uploadFile(File f,String bucketName){
		PutObjectRequest request = new PutObjectRequest(bucketName, f.getName(), f);
        s3client.putObject(request);

	}
	public JSONObject writeFile(File f){
		FindReplace obj = new FindReplace();
		JSONArray jarr =obj.findReplaceUtil(f);
		JSONObject jobj = new JSONObject();
		jobj.put(f.getName(), jarr);
		System.out.println("===JSONArray==="+ jarr);
		return jobj;
	}

}
