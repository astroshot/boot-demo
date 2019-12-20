package com.boot.common.helper;

import com.boot.common.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;

public abstract class FileHelper {

    protected static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    /**
     * Write content to file
     *
     * @param path     file path
     * @param content  content
     * @param encoding file encoding
     * @return file
     */
    public static File createFile(String path, String content, String encoding) {

        if (content == null || content.length() < 1) {
            return null;
        }

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            PrintStream printStream = new PrintStream(outputStream, true, encoding);
            printStream.print(content);
            printStream.flush();
            printStream.close();
            outputStream.close();
            return file;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static File createFile(String path, String content) {
        return createFile(path, content, CommonConstant.DEFAULT_CHARSET);
    }


    /**
     * append content to given file
     *
     * @param file    file
     * @param content content
     */
    public static void appendToFile(File file, String content) {
        if (content == null || content.length() < 1) {
            return;
        }

        FileWriter writer;
        try {
            if (!file.exists()) {
                file = createFile(file.getAbsolutePath(), content);
            }
            writer = new FileWriter(file, true);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
