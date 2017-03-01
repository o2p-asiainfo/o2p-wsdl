package com.asiainfo.o2p.wsdl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;

import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.Logger;
import com.asiainfo.foundation.util.ExceptionUtils;
import com.asiainfo.o2p.wsdl.exception.WsdlExceptionCode;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

public final class FileUtils {
	private static final Logger LOG = Logger.getLog(FileUtils.class);
	private static final int BUFFEREDSIZE = 1024;
	private static String backslash = "\\\\";

	private FileUtils() {
	}

	public static String uFile(final String zipFileName) {
		String outputDirectory = "";
		try {
			if (!StringUtils.isEmpty(zipFileName.trim())) {
				File file = new File(zipFileName);
				String parentFilePath = file.getParent();// 父路径
				int i = file.getName().lastIndexOf(".");
				String fileName = file.getName().substring(0, i);
				String fileSuffix = file.getName().substring(i + 1);
				outputDirectory = parentFilePath + File.separator + fileName
						+ File.separator;// 新文件路径
				if (LOG.isDebugEnabled()) {
					LOG.debug("old File path : {0}", zipFileName);
					LOG.debug("parent path : {0}", parentFilePath);
					LOG.debug("New File path : {0}", outputDirectory);
				}
				// 解压文件
				if (fileSuffix != null && "zip".equals(fileSuffix)) {
					uzipFile(zipFileName, outputDirectory);
				} else if ("rar".equals(fileSuffix)) {
					urarFile(zipFileName, outputDirectory);
				}
			}

			return getFile(outputDirectory);
		} catch (BusinessException e) {
			LOG.error("uFile exception {0}",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					"o2p.wsdl.uFile", null, e);
		}
	}

	private static String getFile(String outputDirectory) {
		// 获取WSDL
		if (!StringUtils.isEmpty(outputDirectory.trim())) {
			File outFile = new File(outputDirectory);

			String[] fileList = outFile.list();
			File[] files = outFile.listFiles();

			if (files.length == 1 && files[0].isFile()) {
				return outFile.listFiles()[0].getPath();
			}

			if (files.length == 1 && files[0].isDirectory()) {
				fileList = outFile.listFiles()[0].list();
				outputDirectory = outFile.listFiles()[0].getPath();
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("outputDirectory dir {0}", outputDirectory);
			}
			
			Map<String,String> wsdlFileUrls = new HashMap<String,String>();
			for (String name : fileList) {
				if ("wsdl".equals(name.split("\\.")[1])) {
					StringBuffer buf = new StringBuffer();
					buf.append(outputDirectory);
					buf.append(File.separator);
					buf.append(name);
					 
					
					wsdlFileUrls.put(name,buf.toString());
				}
			}
			
			return findMainWsdl(wsdlFileUrls);
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("outputDirectory dir {0}", outputDirectory);
		}
		
		
		return outputDirectory;
	}

	


	private static String findMainWsdl(Map<String,String> wsdlFileUrls){
		
		if(wsdlFileUrls!=null){
			
			Set<String> fileNames = wsdlFileUrls.keySet();
			Map<String,String> map = new HashMap<String,String>();
			
			for (Iterator iterator = fileNames.iterator(); iterator.hasNext();) {
				String fileName = (String) iterator.next();
				map.put(fileName, fileName);
			}
			
			for (Iterator iterator = fileNames.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				
				File file = new File(wsdlFileUrls.get(key));
		        InputStream in = null;
		        StringBuffer stb = new StringBuffer();
		        try {
		            in = new FileInputStream(file);
		            int tempbyte;
		            
		            while ((tempbyte = in.read()) != -1) {
		            	stb.append((char)tempbyte);
		            }
		            in.close();
		            
		        } catch (IOException e) {
		            LOG.error("find Main Wsdl error", e);
		        }
		        
		        
		        isContain(stb.toString(), map);
			
			}
			
			
			
			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
				String fileName = (String) iterator.next();
				return wsdlFileUrls.get(fileName);
			}
		}
		return null;
	}
	
	private static void isContain(String fileContent,Map<String,String> map){
		
		Set<String> fileNames = map.keySet();
		
		System.out.println(fileContent);
		for (Iterator iterator = fileNames.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			
			if(fileContent.contains(key)){
				iterator.remove();
			}
		}
	}
	/**
	 * rar文件解压
	 * 
	 * @param zipFilename
	 * @param outputDirectory
	 */
	public static void urarFile(final String zipFilename, String outputDirectory) {
		Archive a = null;
		FileOutputStream fos = null;
		try {
			a = new Archive(new File(zipFilename));
			FileHeader fh = a.nextFileHeader();
			boolean b = false;
			while (fh != null) {
				if (!fh.isDirectory()) {
					// 根据不同的操作系统拿到相应的 destFileName
					String compressFileName = fh.getFileNameString().trim();

					String destFileName = "";
					// 非windows系统
					if ("/".equals(File.separator)) {
						destFileName = outputDirectory
								+ compressFileName.replaceAll(backslash, "/");
						// windows系统
					} else {
						destFileName = outputDirectory
								+ compressFileName.replaceAll("/", backslash);
					}
					if (compressFileName.split(backslash).length == 2 && !b) {
						outputDirectory = outputDirectory
								+ compressFileName.split(backslash)[0];
					}
					// 创建文件夹
					File dir = new File(outputDirectory);
					if (!dir.exists() || !dir.isDirectory()) {
						boolean mk = dir.mkdirs();
						if (!mk) {
							LOG.error("urarFile mkdirs error {0}",
									dir.getPath());
						}
					}
					// 解压缩文件
					fos = new FileOutputStream(new File(destFileName));
					a.extractFile(fh, fos);
					if (!b) {
						outputDirectory = outputDirectory.substring(0,
								outputDirectory.lastIndexOf(File.separator))
								+ File.separator;
					}
					b = true;
				}
				fh = a.nextFileHeader();
			}
		} catch (IOException e) {
			LOG.error("urarFile IOException {0}",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					"o2p.wsdl.rarFile", null, e);
		} catch (RarException e) {
			LOG.error("urarFile RarException {0}",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					"o2p.wsdl.rarFile", null, e);
		} finally {
			fileOutputStreamClose(fos);
			archiveClose(a);
		}
	}

	private static void archiveClose(Archive a) {
		if (a != null) {
			try {
				a.close();
			} catch (IOException e) {
				LOG.error("archiveClose Exception {0}",
						ExceptionUtils.populateExecption(e, 500));
				throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
						"o2p.wsdl.archive", null, e);
			} finally {
				try {
					a.close();
				} catch (IOException e) {
					LOG.error("archiveClose finally IOException {0}",
							ExceptionUtils.populateExecption(e, 500));
				}
			}
		}
	}

	private static void fileOutputStreamClose(FileOutputStream fos) {
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				LOG.error("fileOutputStreamClose IOExcepiont {0}",
						ExceptionUtils.populateExecption(e, 500));
				throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
						"o2p.wsdl.fileOutputStream", null, e);
			}
		}
	}

	/**
	 * zip文件解压
	 * 
	 * @param zipFilename
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void uzipFile(final String zipFilename, String outputDirectory) {
		ZipFile zipFile = null;
		try {

			File outFile = new File(outputDirectory);
			if (!outFile.exists()) {
				boolean ob = outFile.mkdirs();
				if (!ob) {
					LOG.error("uzipFile outFile mkdirs error {0}",
							outFile.getPath());
				}
			}
			zipFile = new ZipFile(zipFilename);
			Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zipFile
					.entries();
			ZipEntry zipEntry = null;

			while (en.hasMoreElements()) {
				zipEntry = (ZipEntry) en.nextElement();
				if (zipEntry.isDirectory()) {
					// mkdir directory
					String dirName = zipEntry.getName();
					dirName = dirName.substring(0, dirName.length() - 1);
					File f = new File(outFile.getPath() + File.separator
							+ dirName);
					if (f.mkdirs()) {
						LOG.debug("directory judge {0}", f.getName());
					}
				} else {
					// unzip file
					String strFilePath = outFile.getPath() + File.separator
							+ zipEntry.getName();
					File f = new File(strFilePath);

					// 判断文件不存在的话，就创建该文件所在文件夹的目录
					if (!f.exists()) {
						String[] arrFolderName = zipEntry.getName().split("/");
						StringBuffer strRealFolder = new StringBuffer();
						for (int i = 0; i < (arrFolderName.length - 1); i++) {
							strRealFolder.append(arrFolderName[i]).append(
									File.separator);
						}
						final String filedir = outFile.getPath()
								+ File.separator + strRealFolder.toString();
						File tempDir = new File(filedir);
						// 此处使用.mkdirs()方法，而不能用.mkdir()
						if (!tempDir.mkdirs()) {
							LOG.debug("Failed to create the directory");
						}
					}

					boolean cf = f.createNewFile();
					if (!cf) {
						LOG.error("uzipFile create file error {0}", f.getPath());
					}
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);
					try {
						int c;
						byte[] by = new byte[BUFFEREDSIZE];
						while ((c = in.read(by)) != -1) {
							out.write(by, 0, c);
						}
					} catch (IOException e) {
						LOG.error("uzipFile IOException {0}",
								ExceptionUtils.populateExecption(e, 500));
						throw new BusinessException(
								WsdlExceptionCode.RUNTIME_ERROR,
								"o2p.wsdl.zipFIle", null, e);
					} finally {
						out.close();
						in.close();
					}
				}
			}

		} catch (IOException e) {
			LOG.error("uzipFile IOException {0}",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					"o2p.wsdl.zipFIle", null, e);
		} finally {
			if (null != zipFile) {
				try {
					zipFile.close();
				} catch (IOException e) {
					LOG.error("uzipFile finally IOException {0}",
							ExceptionUtils.populateExecption(e, 500));
				}
			}
		}

	}

	// 删除文件
	public static void deleteFile(final String path) {
		if (!StringUtils.isEmpty(path.trim())) {
			File file = new File(path);
			if (!file.exists()) {
				return;
			} else {
				if (file.isFile() && file.exists()) {

					if (LOG.isDebugEnabled()) {
						LOG.debug("file delete : {0}", path);
					}
					boolean db = file.delete();
					if (!db) {
						LOG.error("delete file error {0}", path);
					}
				}
			}
		}
	}

	// 删除目录
	public static void deleteDir(String path) {
		if (!StringUtils.isEmpty(path.trim())) {
			String deletePath = path;
			if (!deletePath.endsWith(File.separator)) {
				deletePath = deletePath + File.separator;
			}
			final String newDeletePath = deletePath;
			File file = new File(newDeletePath);
			// 判断目录是否存在
			if (file.exists()) {
				if (!file.exists() || !file.isDirectory()) {
					return;
				}
				// 获取所有文件及目录
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					// 删除子文件
					if (files[i].isFile()) {
						deleteFile(files[i].getAbsolutePath());
					}
					// 删除子目录
					else {
						deleteDir(files[i].getAbsolutePath());
					}
				}
				if (file.delete()) {
					return;
				}

			}
		}
	}

}
