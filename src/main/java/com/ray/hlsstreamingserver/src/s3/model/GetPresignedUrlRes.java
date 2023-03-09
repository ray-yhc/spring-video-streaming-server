package com.ray.hlsstreamingserver.src.s3.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public class GetPresignedUrlRes {
    public String presignedUrl;
    public String fileId;
    public String s3FileUrl;
    public Date expirationDate;
}
