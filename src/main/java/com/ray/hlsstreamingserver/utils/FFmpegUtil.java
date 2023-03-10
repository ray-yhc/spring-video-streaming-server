package com.ray.hlsstreamingserver.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class FFmpegUtil {
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    private final ObjectMapper objectMapper;

    @Autowired
    public FFmpegUtil(FFmpeg ffmpeg, FFprobe ffprobe, ObjectMapper objectMapper) {
        this.ffmpeg = ffmpeg;
        this.ffprobe = ffprobe;
        this.objectMapper = objectMapper;
    }

    @Value("ffmpeg.upload.path")
    private String convertSavePath;

    /**
     * get Media Info
     * @param filePath
     * @return
     * @throws IOException
     */
    public FFmpegProbeResult getMediaInfo(String filePath) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(filePath);

        if(log.isDebugEnabled()){
            log.debug("========== VideoFileUtils.getMediaInfo() ==========");
            log.debug("filename : {}", probeResult.getFormat().filename);
            log.debug("format_name : {}", probeResult.getFormat().format_name);
            log.debug("format_long_name : {}", probeResult.getFormat().format_long_name);
            log.debug("tags : {}", probeResult.getFormat().tags.toString());
            log.debug("duration : {} second", probeResult.getFormat().duration);
            log.debug("size : {} byte", probeResult.getFormat().size);

            log.debug("width : {} px", probeResult.getStreams().get(0).width);
            log.debug("height : {} px", probeResult.getStreams().get(0).height);
            log.debug("===================================================");
        }
        return probeResult;
    }

    /**
     * ????????? Prop??? ??????????????? ??????
     * @param probeResult
     * @param format ?????? ?????? (????????? ex - mp4, mpeg )
     * @param codec ????????? ??????
     * @param audioChannel ????????? ?????? ( 1: ??????, 2: ???????????? )
     * @param width ????????? ( ?????? )
     * @param height ????????? ( ?????? )
     * @return ?????? ??????
     */
    public boolean convertVideoProp(FFmpegProbeResult probeResult, String format, String codec, int audioChannel, int width, int height) {
        boolean result = false;

        FFmpegBuilder builder = new FFmpegBuilder().setInput(probeResult)
                .overrideOutputFiles(true)
                .addOutput(convertSavePath + "/temp."+format)
                .setFormat(format)
                .setVideoCodec(codec)
                .setAudioChannels(audioChannel)
                .setVideoResolution(width, height)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

//        StopWatch stopWatch = new StopWatch("convertVideoTimer");

        FFmpegExecutor executable = new FFmpegExecutor(ffmpeg, ffprobe);
        FFmpegJob job = executable.createJob(builder);
        job.run();

        if (job.getState() == FFmpegJob.State.FINISHED) {
            result = true;
        }

        return result;
    }

    public void createThumbnail(String filePath, String thumbnailPath){
        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true) // ??????????????? ??????
                .setInput(filePath) // ????????? ???????????? ??????
                .addExtraArgs("-ss", "00:00:05") // ????????? ?????? ?????????
                .addOutput(thumbnailPath) // ????????? ????????? Path
                .setFrames(100) // ????????? ???
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }
}
