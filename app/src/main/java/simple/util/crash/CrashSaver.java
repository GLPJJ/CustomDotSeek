package simple.util.crash;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import simple.util.til.AppDirUtil;
import simple.util.til.ToolUtil;
import simple.util.tools.DateTools;

class CrashSaver {

    public static final void save(Context context, Throwable ex,
                                  boolean uncaught) {

        if (!AppDirUtil.isSDAviableWrite()) {// 如果没有sdcard，则不存储
            return;
        }
        Writer writer = null;
        PrintWriter printWriter = null;
        String stackTrace = "";
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            stackTrace = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
        }
        String signature = stackTrace.replaceAll("\\([^\\(]*\\)", "");
        String filename = ToolUtil.GetMd5(signature);
        if (TextUtils.isEmpty(filename)) {
            return;
        }
        String timestamp = DateTools.GetStrTimeFmt(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
        BufferedWriter mBufferedWriter = null;
        try {
            File mFile = new File(AppDirUtil.getDumpDirFile().getAbsoluteFile() + filename + ".crashlog");
            int count = 1;
            if (mFile.exists()) {
                LineNumberReader reader = null;
                try {
                    reader = new LineNumberReader(new FileReader(mFile));
                    String line = reader.readLine();
                    if (line.startsWith("count")) {
                        int index = line.indexOf(":");
                        if (index != -1) {
                            String count_str = line.substring(++index);
                            if (count_str != null) {
                                count_str = count_str.trim();
                                count = Integer.parseInt(count_str);
                                count++;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                mFile.delete();
            }

            mFile.createNewFile();

            mBufferedWriter = new BufferedWriter(new FileWriter(mFile, true));// 追加模式写文件
            mBufferedWriter.append(CrashSnapshot.snapshot(context, uncaught, timestamp, stackTrace, count));
            mBufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mBufferedWriter != null) {
                try {
                    mBufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
