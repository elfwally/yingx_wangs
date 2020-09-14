
package com.wang.util;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/*
* java方式  截取视频帧数 （截取视频封面） 工具类
*
*
* 需要导入jar
*
<dependency>
    <groupId>org.bytedeco</groupId>
    <artifactId>javacv</artifactId>
    <version>0.8</version>
</dependency>
* */

public class InterceptVideoCoverUtil {

/**
     * 获取指定视频的帧并保存为图片至指定目录（指定目录只能为本地目录）
     * @param videofile  源视频文件路径  可以为远程路径
     * @param framefile  截取帧的图片存放路径   只支持存放到本地路径
     * @throws Exception
     */

    public static void fetchFrame(String videofile, String framefile)  //远程路径   存储本地路径
            throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = new File(framefile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
        ff.start();
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            f = ff.grabFrame();
            if ((i > 5) && (f.image != null)) {
                break;
            }
            i++;
        }
        opencv_core.IplImage img = f.image;
        int owidth = img.width();
        int oheight = img.height();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
                0, 0, null);
        ImageIO.write(bi, "jpg", targetFile);
        //ff.flush();
        ff.stop();
        System.out.println(System.currentTimeMillis() - start);
    }

    public static void main(String[] args) {
        try {
            //当前方法支持获取远程视频的封面，但是只能将封面存放到本地
            InterceptVideoCoverUtil.fetchFrame("https://yingx-yefz.oss-cn-beijing.aliyuncs.com/video/1.mp4", "F:\\img\\test5.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

