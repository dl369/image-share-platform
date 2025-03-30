import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.region}")
    private String region;

    private AmazonS3 s3Client;

    public StorageService() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public String saveImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        fileName = sanitizeFileName(fileName);
        File tempFile = File.createTempFile(fileName, null);
        file.transferTo(tempFile);

        s3Client.putObject(new PutObjectRequest(bucketName, fileName, tempFile));
        tempFile.delete();

        return fileName;
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    
    public Resource loadImage(String filename) throws MalformedURLException {
        S3Object s3Object = s3Client.getObject(bucketName, filename);
        Path path = Paths.get(s3Object.getKey());
        return new UrlResource(path.toUri());
    }
}
