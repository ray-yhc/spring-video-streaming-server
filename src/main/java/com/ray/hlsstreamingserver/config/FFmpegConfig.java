package com.ray.hlsstreamingserver.config;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class FFmpegConfig {
    @Value("${ffmpeg.location}")
    private String ffmpegLocation;
    @Value("${ffprobe.location}")
    private String ffprobeLocation;

    @Bean(name = "ffmpeg")
    public FFmpeg ffmpeg() throws IOException {
        FFmpeg ffmpeg = new FFmpeg(ffmpegLocation);
        Assert.isTrue(ffmpeg.isFFmpeg());
        return ffmpeg;
    }

    @Bean(name = "ffprobe")
    public FFprobe ffprobe() throws IOException {
        FFprobe ffprobe = new FFprobe(ffprobeLocation);
        Assert.isTrue(ffprobe.isFFprobe());
        return ffprobe;
    }
}



//        FFmpeg ffMPeg = null;
//        String osName = System.getProperty("os.name");
//
//        // 운영체제가 Window인 경우 jar에 내장되어있는 ffmpeg 를 이용
//        if (osName.toLowerCase().contains("win")) {
//            ClassPathResource classPathResource = new ClassPathResource(ffmpegLocation);
//            ffMPeg = new FFmpeg(classPathResource.getURL().getPath());
//        } else if(osName.toLowerCase().contains("unix") || osName.toLowerCase().contains("linux")) {
//            ffMPeg = new FFmpeg(ffmpegLocation);
//        }


//        FFprobe ffprobe = null;
//
//        String osName = System.getProperty("os.name");
//
//        // 운영체제가 Window인 경우 jar에 내장되어있는 ffmpeg 를 이용
//        if (osName.toLowerCase().contains("win")) {
//            ClassPathResource classPathResource = new ClassPathResource(ffprobeLocation);
//            ffprobe = new FFprobe(classPathResource.getURL().getPath());
//        } else if(osName.toLowerCase().contains("unix") || osName.toLowerCase().contains("linux")) {
//            ffprobe = new FFprobe(ffmpegLocation);
//        }