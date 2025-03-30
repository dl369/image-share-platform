# Image Sharing Platform

This is a simple image-sharing backend built with Spring Boot and PostgreSQL. Images are stored in an AWS S3 bucket, and metadata is stored in a PostgreSQL database.

## Features
- Upload images associated with users
- Retrieve user images
- Serve images dynamically via URL
- Store image metadata in PostgreSQL
- Store image files in AWS S3


### Configuration
Create an `application.properties` file in `src/main/resources/` with the following:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

aws.s3.bucket=your-bucket-name
aws.accessKeyId=your-access-key
aws.secretAccessKey=your-secret-key
aws.region=your-region
```
