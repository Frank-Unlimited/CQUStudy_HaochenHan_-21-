package com.test;



import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class MinioTests {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        minioTest2();
    }
    // 官方服务
    @Test
    void minioTest() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("https://play.min.io") //.region("us-east-2")
                            .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
                            .build();

            // Make 'asiatrip' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("asiatrip").build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("asiatrip")
                            .object("1.jpg")
                            .filename("D://1.jpg")
                            .build());
            System.out.println(
                    "上传成功");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
    @Test
    static void minioTest2() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // 创建连接
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://localhost:9000") //.region("us-east-2")
                            .credentials("minioadmin", "minioadmin")
                            .build();
            // 如果使用的 桶不存在 则创建 桶
            boolean found =  minioClient.bucketExists(BucketExistsArgs.builder().bucket("huike-crm").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("huike-crm").build());
            }
//            // 上传文件
//            minioClient.uploadObject(
//                    UploadObjectArgs.builder()
//                            .bucket("huike-crm")
//                            .object("1.jpg")
//                            .filename("D://1.jpg")
//                            .build());
//            System.out.println(
//                    "上传成功");
            // 指定需要扫描的文件夹路径
            File folder = new File("D:\\Major Design\\software\\MinIO\\img_to_upload");  // 修改为你实际的文件夹路径

            // 获取文件夹下所有的图片文件（假设图片以 jpg、png 后缀结尾）
            File[] imageFiles = folder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

            // 判断文件夹是否为空或无图片文件
            if (imageFiles == null || imageFiles.length == 0) {
                System.out.println("没有找到任何图片文件！");
                return;
            }

            // 遍历图片文件，逐个上传并删除
            for (File imageFile : imageFiles) {
                // 上传文件到 Minio
                String objectName = imageFile.getName();  // 使用文件名作为对象名
                try {
                    minioClient.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket("huike-crm")
                                    .object(objectName)  // 目标 Minio 对象名
                                    .filename(imageFile.getAbsolutePath())  // 本地文件路径
                                    .build()
                    );
                    System.out.println("文件上传成功: " + objectName);

                    // 删除已上传的本地图片文件
                    if (imageFile.delete()) {
                        System.out.println("文件已删除: " + imageFile.getAbsolutePath());
                    } else {
                        System.out.println("文件删除失败: " + imageFile.getAbsolutePath());
                    }

                } catch (MinioException e) {
                    System.err.println("上传文件失败: " + imageFile.getName());
                    e.printStackTrace();
                }
            }
        } catch (MinioException e) {
           e.printStackTrace();
        }
    }

}
