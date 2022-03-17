package com.zmj.demo.common;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class GzipUtil {

    private static final int BYTE_LEN = 256;
    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    /**
     * 解压
     * @param bytes 待解压byte数组
     * @return
     * @throws IOException
     */
    public static byte[] uncompress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[BYTE_LEN];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }

        return out.toByteArray();
    }

    /**
     * 解压返回字符串
     * @param bytes 待解压byte数组
     * @return
     */
    public static String uncompressToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[BYTE_LEN];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(GZIP_ENCODE_UTF_8);
        } catch (IOException e) {
            log.error("gzip uncompress to string error.", e);
        }
        return null;
    }

    /**
     * 压缩
     * @param str 待压缩字符串
     * @return
     * @throws IOException
     */
    public static byte[] compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes(GZIP_ENCODE_UTF_8));
        gzip.close();
        return out.toByteArray();
    }

    public static void main(String[] args) throws IOException {
        byte[] b1 = compress("{\"ping\":\"1642487108646\"}");
        String s1 = uncompressToString(b1);
        System.out.println(s1);
    }


}
