package com.careagle.sdk.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.careagle.sdk.Config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lida on 2017/6/20 0020.
 */

public class FileUtils {
    public static String PATH_PHOTOGRAPH = "/SYC/";

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    public static Uri file2Uri(File file, String fileProviderAuthority) {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT >= 24) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(Config.getContext(), fileProviderAuthority, file);
        }
        return imageUri;
    }

//    public static void writeTxtFile(final String strcontent, final String strFilePath) {
//        Observable.create(new ObservableOnSubscribe<Boolean>() {
//            @Override
//            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
//                try {
//                    //每次写入时，都换行写
//                    String strContent = strcontent + "\n";
//                    File file = new File(strFilePath);
//                    if (!file.exists()) {
//                        Log.e("TestFile", "Create the file:" + strFilePath);
//                        file.createNewFile();
//                    }
//                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
//                    raf.seek(file.length());
//                    raf.write(strContent.getBytes());
//                    raf.close();
//                    e.onNext(true);
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                    e.onNext(false);
//                }
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean isSuccess) throws Exception {
//                        if (isSuccess) {
//                            MyToast.toast("成功");
//                        } else {
//                            MyToast.toast("失败");
//                        }
//                    }
//                });
//
//    }

    public static String readSDFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                if (Character.isWhitespace(c)) {//判断是否是空格
                    continue;
                }
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(sb.toString());
        return m.replaceAll("");
    }

//    //读取本地文件  已经异步处理
//    public static void readTxtFile(final String fileName) {
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                try {
//                    String res = readSDFile(fileName);
//                    e.onNext(res);
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                    e.onNext("失败");
//                }
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String text) throws Exception {
//                        MyToast.toast(text);
//                    }
//                });
//    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return true;
        }
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }


    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtils.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtils.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    public static List<File> getAllFiles(File root) {
        ArrayList<File> list = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getAllFiles(f);
                } else {
                    list.add(f);
                    stringBuffer.append(f.getName() + "\n");
                }
            }
        }
        JLog.e(stringBuffer.toString());
        return list;
    }

    /**
     * 删除一个文件夹下的所有文件（子一级）
     *
     * @param path 文件夹路径
     * @return 删除文件的总大小
     */
    public static long deleteFolderFiles(String path) {
        long deletedFileSize = 0;
        if (path != null) {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                int length = files.length;
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        file = files[i];
                        if (file.isFile()) {
                            deletedFileSize += file.length();
                            file.delete();
                        }
                    }
                }
            }
        }

        return deletedFileSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 删除一个文件夹下的所有文件包括子文件夹的文件
     *
     * @param path 文件夹路径
     */
    public static void deleteAllFolderFiles(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                int length = files.length;
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        file = files[i];
                        if (file.isFile()) {
                            file.delete();
                        } else {
                            deleteAllFolderFiles(file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断指定的文件是否存在，不存在则新建一个
     *
     * @param path 文件路径
     * @throws IOException
     */
    public static void createFileIfNotExists(String path) throws IOException {
        if (path != null) {
            File file = new File(path);
            if (!file.exists()) {
                createParentFolder(file);
                file.createNewFile();
            }
        }
    }


    /**
     * 删除指定文件
     *
     * @param file 指定的文件对象
     * @return 删除成功返回true，否则返回false。
     */
    public static boolean deleteFile(File file) {
        if (file != null) {
            return file.delete();
        }
        return false;
    }

    /**
     * 创建指定路径的文件夹
     *
     * @param path 需要创建的文件夹的完整路径
     * @return 文件夹已经存在或创建成功返回true，否则返回false。
     */
    public static boolean createFolder(String path) {
        if (path != null) {
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                return file.mkdirs();
            }
            return true;
        }
        return false;
    }

    /**
     * 判断指定路径文件是否存在
     *
     * @param path 文件的完整路径
     * @return 存在返回true，否则返回false。
     */
    public static boolean isFileExists(String path) {
        if (path != null) {
            File file = new File(path);
            return file.exists();
        }
        return false;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 成功返回文件扩展名，失败返回null。
     */
    public static String getFileNameExtension(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf(".");
            if (index != -1) {
                return fileName.substring(index + 1, fileName.length());
            }
        }
        return null;
    }

    /**
     * 获取文件名称（不包括文件扩展名）
     *
     * @param fileName 文件名称
     * @return 返回不包含文件扩展名的文件名称
     */
    public static String getFileName(String fileName) {
        String extension = getFileNameExtension(fileName);
        if (extension != null) {
            return fileName.replace("." + extension, "");
        }
        return fileName;
    }

    /**
     * 更改文件名
     *
     * @param oldFilePath 需要修改的文件完整路径
     * @param newFilePath 新的修改的文件完整路径
     * @return 修改成功返回true，失败返回false。
     */
    public static boolean renameFile(String oldFilePath, String newFilePath) {
        File file = new File(oldFilePath);
        if (file.exists()) {
            return file.renameTo(new File(newFilePath));
        }
        return false;
    }

    /**
     * 从一个文件中读取字节数组
     *
     * @return 成功返回读取到得字节数组，失败返回null。
     */
    public static byte[] getBytesFromFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            FileInputStream fis = null;
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream(8192);
                fis = new FileInputStream(file);
                int length;
                byte[] buffer = new byte[1024];
                while ((length = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }
                return baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null)
                        fis.close();
                    if (baos != null)
                        baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从一个文件中读取字符串(UTF-8)
     *
     * @param savePath 文件路径
     * @return 成功返回读取到得字符串，失败返回null。
     */
    public static String getStringFromFile(String savePath) {
        return getStringFromFile(savePath, "UTF-8");
    }

    /**
     * 从一个文件中读取字符串
     *
     * @param savePath 文件路径
     * @param charset  字符集名称
     * @return 成功返回读取到得字符串，失败返回null。
     */
    public static String getStringFromFile(String savePath, String charset) {
        if (!isFileExists(savePath)) {
            return null;
        }
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(savePath), charset);
            br = new BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            if (sb.length() > 0) {
                return sb.substring(0, sb.length() - 1);
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 创建File的父文件夹及其目录
     *
     * @param file 当前文件或文件夹
     * @return 成功返回true，失败返回false。
     */
    public static boolean createParentFolder(File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            return parent.mkdirs();
        }
        return true;
    }

    /**
     * 输出二进制内容到文件，并且保证本次输出不会由于异常而导致原有文件顺坏
     *
     * @param data     需要输出的内容
     * @param filePath 文件完整路径
     * @return 成功返回true，否则返回false。
     */
    public static boolean outputDataToFileSafe(byte[] data, String filePath) {
        if (data != null && filePath != null) {
            File tempFile = new File(filePath + ".temp");
            try {
                createFileIfNotExists(tempFile.getAbsolutePath());
                outputDataToFile(data, tempFile.getAbsolutePath());
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
                return tempFile.renameTo(file);
            } catch (Exception e) {
                e.printStackTrace();
                tempFile.delete();
            }
        }
        return false;
    }

    /**
     * 输出二进制内容到文件
     *
     * @param data     需要输出的内容
     * @param filePath 文件完整路径
     * @return 成功返回true，否则返回false。
     */
    public static boolean outputDataToFile(byte[] data, String filePath) {
        if (data != null && filePath != null) {
            BufferedOutputStream bos = null;
            try {
                //如果不存在则创建
                createFileIfNotExists(filePath);
                FileOutputStream fos = new FileOutputStream(new File(filePath));
                bos = new BufferedOutputStream(fos, 8192);
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null)
                        bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean outputStringToFile(String savePath, String data, boolean append) {
        return outputStringToFile(savePath, data, "UTF-8", append);
    }

    public static boolean outputStringToFile(String savePath, String data) {
        return outputStringToFile(savePath, data, "UTF-8");
    }

    /**
     * 将一个字符串写到文件
     *
     * @param savePath 文件路径
     * @param data     需要保存的字符串
     * @param charset  字符集
     * @return 成功返回true，失败返回false。
     */
    public static boolean outputStringToFile(String savePath, String data, String charset) {
        return outputStringToFile(savePath, data, charset, false);
    }

    /**
     * 将一个字符串写到文件
     *
     * @param savePath 文件路径
     * @param data     需要保存的字符串
     * @param append   是否追加
     * @param charset  字符集
     * @return 成功返回true，失败返回false。
     */
    public static boolean outputStringToFile(String savePath, String data, String charset, boolean append) {
        if (savePath != null && data != null) {
            BufferedWriter bw = null;
            try {
                bw = FileUtils.getBufferedWriter(savePath, charset, append);
                if (bw != null) {
                    if (append && !isEmptyFile(savePath)) {
                        bw.newLine();
                    }
                    bw.write(data);
                    bw.flush();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null)
                        bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean isEmptyFile(String savePath) {
        if (TextUtils.isEmpty(savePath)) {
            return true;
        }
        File file = new File(savePath);
        if (file.exists()) {
            return file.length() == 0;
        }
        return true;
    }

    /**
     * 复制文件
     *
     * @param srcPath  原文件
     * @param destPath 目标文件
     * @return 成功返回true，失败返回false。
     */
    public static boolean copyFile(String srcPath, String destPath) {
        if (srcPath != null && destPath != null) {
            return copyFile(new File(srcPath), new File(destPath));
        }
        return false;
    }

    /**
     * 复制文件
     *
     * @return 成功返回true，失败返回false。
     */
    public static boolean copyFile(File srcFile, File destFile) {
        if (srcFile != null && destFile != null) {
            if (srcFile.exists()) {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    bis = new BufferedInputStream(new FileInputStream(srcFile));
                    bos = new BufferedOutputStream(new FileOutputStream(destFile));
                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = bis.read(buffer)) > 0) {
                        bos.write(buffer, 0, length);
                    }
                    bos.flush();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bis != null)
                            bis.close();
                        if (bos != null)
                            bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     * 转换文件大小
     */
    public static String formatFileSize(Long fileSize) {
        if (fileSize == null || fileSize == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1024 * 1024) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) fileSize / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取一个指定文件输入路径和文件字符集编码格式的Reader
     *
     * @param path    文件路径
     * @param charset 字符集编码
     * @return
     */
    public static BufferedReader getBufferedReader(String path, String charset) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(path), charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个指定文件输出路径和文件字符集编码格式的Writer
     *
     * @param path    文件路径
     * @param charset 字符集编码
     * @param append  是否在文件末尾追加
     * @return
     */
    public static BufferedWriter getBufferedWriter(String path, String charset, boolean append) {
        try {
            return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, append), charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个指定文件输出路径和文件字符集编码格式的Writer
     *
     * @param path    文件路径
     * @param charset 字符集编码
     * @return
     */
    public static BufferedWriter getBufferedWriter(String path, String charset) {
        return getBufferedWriter(path, charset, false);
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }


    public static File getDCIMFile(String filePath, String imageName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 文件可用
            File dirs = new File(Environment.getExternalStorageDirectory(),
                    "DCIM" + filePath);
            if (!dirs.exists())
                dirs.mkdirs();

            File file = new File(Environment.getExternalStorageDirectory(),
                    "DCIM" + filePath + imageName);
            if (!file.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    file.createNewFile();
                } catch (Exception e) {
                }
            }
            return file;
        } else {
            return null;
        }

    }


}
