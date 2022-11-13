package com.qiwenshare.file.helper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

/**
 *
 * @author zrx
 */
public class RAR5Extract {
	public static List<String> unrar(File sourceFile, String destDirPath) throws IOException {
		IInArchive archive;
		RandomAccessFile randomAccessFile;
		// 第一个参数是需要解压的压缩包路径，第二个参数参考JdkAPI文档的RandomAccessFile
		//r代表以只读的方式打开文本，也就意味着不能用write来操作文件
		randomAccessFile = new RandomAccessFile(sourceFile, "r");
		// null - autodetect
		archive = SevenZip.openInArchive(null,
				new RandomAccessFileInStream(randomAccessFile));
		int[] in = new int[archive.getNumberOfItems()];
		for (int i = 0; i < in.length; i++) {
			in[i] = i;
		}
		archive.extract(in, false, new ExtractCallback(archive, destDirPath + "/"));
		archive.close();
		randomAccessFile.close();
		List<String> allFiles = new ArrayList<>(10);
		listAllFile(new File(destDirPath), destDirPath, allFiles);
		return allFiles;
	}

	private static void listAllFile(File dir, String basePath, List<String> allFiles) {
		//列出所有的子文件
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isFile()) {
				allFiles.add(file.getAbsolutePath().replace(basePath, "").replaceAll("\\\\","/"));
			} else if (file.isDirectory()) {
				allFiles.add(file.getAbsolutePath().replace(basePath, "").replaceAll("\\\\","/"));
				//递归遍历
				listAllFile(file, basePath, allFiles);
			}
		}
	}
}
