package com.ruike.hme.infra.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 15:01
 */
public class FileUtils {

    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String path) {
        OutputStream out = null;
        try {
            // 弹出下载框，并处理中文
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(fileName, "utf-8"));
            // 下载
            out = response.getOutputStream();
            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
            InputStream is = new FileInputStream(path);
            byte[] bytes = new byte[4096];
            int size = is.read(bytes);
            while (size > 0) {
                out.write(bytes, 0, size);
                size = is.read(bytes);
            }
            out.flush();
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadWorkbook(HSSFWorkbook wk, HttpServletRequest request, HttpServletResponse response, String fileName) {
        String path = request.getSession().getServletContext().getRealPath("") + "/" + fileName;
        FileOutputStream name = null;
        try {
            name = new FileOutputStream(path);
            wk.write(name);
            wk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        downloadFile(request, response, fileName, path);
    }

    public static void downloadWorkbook(XSSFWorkbook wk, HttpServletRequest request, HttpServletResponse response, String fileName) {
        String path = request.getSession().getServletContext().getRealPath("") + "/" + fileName;
        FileOutputStream name = null;
        try {
            name = new FileOutputStream(path);
            wk.write(name);
            wk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        downloadFile(request, response, fileName, path);
    }

    /**
     * 上传常量值
     */
    public static class UploadValue {
        private UploadValue() {
        }

        /**
         * 桶名
         */
        public static final String BUCKET_NAME = "hpfm";

        /**
         * 目录
         */
        public static final String DIRECTORY = "hpfm01/";

        /**
         * 桶名
         */
        public static final String FILE_TYPE = "rkxlsx";
    }
}
