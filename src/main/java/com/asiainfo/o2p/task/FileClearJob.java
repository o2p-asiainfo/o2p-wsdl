/** 
 * Project Name:o2p-wsdl 
 * File Name:FileClearJob.java 
 * Package Name:com.asiainfo.o2p.task 
 * Date:2014年11月17日上午11:08:13 
 * Copyright (c) 2014, www.asiainfo.com All Rights Reserved. 
 * 
 */

package com.asiainfo.o2p.task;

import java.io.File;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.Logger;
import com.asiainfo.foundation.util.ExceptionUtils;
import com.asiainfo.o2p.wsdl.util.CommonUtil;
import com.asiainfo.o2p.wsdl.util.FileUtils;

/**
 * ClassName:FileClearJob <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014年11月17日 上午11:08:13 <br/>
 * 
 * @author wuwz
 * @version
 * @since JDK 1.6
 * @see
 */
public class FileClearJob  {
	private static final Logger log = Logger.getLog(FileClearJob.class);

	
	public void execute()
			 {

		try {
			// 获取前缀路径
			String path = CommonUtil.wsdlDownDir;

			if (path == null) {
				path = System.getProperty("user.dir") + File.separator + "down";
			}

			// 获取所有文件及目录
			File file = new File(path);

			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					Date now = new Date();
					// 超过一天的临时文件
					if (now.getTime() - files[i].lastModified() > 1000 * 60 * 60 * 24) {
						// 删除子文件
						if (files[i].isFile()) {
							FileUtils.deleteFile(files[i].getAbsolutePath());
						}
						// 删除子目录
						else {
							FileUtils.deleteDir(files[i].getAbsolutePath());
						}
						if (log.isDebugEnabled()) {
							log.debug("clear file success, filePath="
									+ files[i].getAbsolutePath());
						}
					}

				}
			}

		} catch (BusinessException e) {
			log.error("clear wsdl temp file error:",
					ExceptionUtils.populateExecption(e, 500));
		}

	}

}
