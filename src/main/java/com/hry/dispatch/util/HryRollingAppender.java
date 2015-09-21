package com.hry.dispatch.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class HryRollingAppender extends DailyRollingFileAppender {
	public static final String LOGGER_NAME = HryRollingAppender.class.getName();
	private SimpleDateFormat sdf;
	private int zipDays = 7;
	private long zipMiliSeconds = zipDays * 24 * 60 * 60 * 1000L;
	private int delDays = 14;
	private long delMiliSeconds = delDays * 24 * 60 * 60 * 1000L;
	// private static ZipLogThreadPool zipLogThreadPool;
	private static List<String> zipFileList = Collections.synchronizedList(new ArrayList<String>());

	private String logFileUnCompressDays = null;
	private String logFileRemainDays = null;
	RollingCalendar rc = new RollingCalendar();
	// package or private visiable in DailyRollingFileAppender
	/**
	 * The next time we estimate a rollover should occur.
	 */
	private long nextCheck = System.currentTimeMillis() - 1;
	Date now = new Date();
	// The gmtTimeZone is used only in computeCheckPeriod() method.
	static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
	// The code assumes that the following constants are in a increasing
	// sequence.
	static final int TOP_OF_TROUBLE = -1;
	static final int TOP_OF_MINUTE = 0;
	static final int TOP_OF_HOUR = 1;
	static final int HALF_DAY = 2;
	static final int TOP_OF_DAY = 3;
	static final int TOP_OF_WEEK = 4;
	static final int TOP_OF_MONTH = 5;

	// static {
	// zipLogThreadPool = new ZipLogThreadPool(2);
	// }

	public HryRollingAppender() {
	}

	public void activateOptions() {
		super.activateOptions();
		if (getDatePattern() != null && fileName != null) {
			now.setTime(System.currentTimeMillis());
			sdf = new SimpleDateFormat(getDatePattern());
			int type = computeCheckPeriod();
			rc.setType(type);
		}
	}

	// This method computes the roll over period by looping over the
	// periods, starting with the shortest, and stopping when the r0 is
	// different from from r1, where r0 is the epoch formatted according
	// the datePattern (supplied by the user) and r1 is the
	// epoch+nextMillis(i) formatted according to datePattern. All date
	// formatting is done in GMT and not local format because the test
	// logic is based on comparisons relative to 1970-01-01 00:00:00
	// GMT (the epoch).

	int computeCheckPeriod() {
		RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());
		// set sate to 1970-01-01 00:00:00 GMT
		Date epoch = new Date(0);
		if (getDatePattern() != null) {
			for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getDatePattern());
				simpleDateFormat.setTimeZone(gmtTimeZone); // do all date formatting in GMT
				String r0 = simpleDateFormat.format(epoch);
				rollingCalendar.setType(i);
				Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
				String r1 = simpleDateFormat.format(next);
				// System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
				if (r0 != null && r1 != null && !r0.equals(r1)) {
					return i;
				}
			}
		}
		return TOP_OF_TROUBLE; // Deliberately head for trouble...
	}

	@Override
	protected void subAppend(LoggingEvent event) {
		super.subAppend(event);
		if (zipMiliSeconds <= 0 && delMiliSeconds <= 0)
			return;
		long n = System.currentTimeMillis();
		if (n >= nextCheck) {
			// 更换文件
			now.setTime(n);
			nextCheck = rc.getNextCheckMillis(now);

			// sdf = new SimpleDateFormat(this.getDatePattern());
			File file = new File(fileName);
			long lastModified = file.lastModified();
			String zipFileName = fileName + sdf.format(new Date(lastModified - zipMiliSeconds));
			zipFile(zipFileName);
			
			zipOldFiles(file);

			String delFileName = fileName + sdf.format(new Date(lastModified - delMiliSeconds)) + ".zip";
			delFile(delFileName);

			delOldFiles(file);
		}
	}

	void zipFile(String zipFileName) {
		File zipFile = new File(zipFileName);
		if (zipFile.exists()) {
			if (zipFileList.contains(zipFileName))
				return;
			synchronized (zipFileList) {
				zipFileList.add(zipFileName);
			}
			ZipLogThread zfThread = new ZipLogThread(zipFileName);
			ZipLogThreadPool.getInstance().execute(zfThread);
		}
	}
	
	void zipOldFiles(File file) {
		File p = file.getParentFile();
		File[] fs = p.listFiles(new OldLogFileFilter(fileName, zipMiliSeconds));
		for(File f:fs) {
			zipFile(f.getAbsolutePath());
		}
	}

	void delFile(String delFileName) {
		File delFile = new File(delFileName);
		if (delFile.exists())
			delFile.delete();
	}

	void delOldFiles(File file) {
		File p = file.getParentFile();
		File[] fs = p.listFiles(new OldZipFileFilter(fileName, delMiliSeconds));
		for(File f:fs) {
			delFile(f.getAbsolutePath());
		}
	}

	public String getLogFileUnCompressDays() {
		return logFileUnCompressDays;
	}

	public void setLogFileUnCompressDays(String logFileUnCompressDays) {
		this.logFileUnCompressDays = logFileUnCompressDays;
		zipDays = Integer.parseInt(this.logFileUnCompressDays);
		zipMiliSeconds = zipDays * 24 * 60 * 60 * 1000L;
	}

	public String getLogFileRemainDays() {
		return logFileRemainDays;
	}

	public void setLogFileRemainDays(String logFileRemainDays) {
		this.logFileRemainDays = logFileRemainDays;
		delDays = Integer.parseInt(this.logFileRemainDays);
		delMiliSeconds = delDays * 24 * 60 * 60 * 1000L;
	}

	public static List<String> getZipFileList() {
		return zipFileList;
	}

}

class ZipLogThreadPool {
	static final int POOL_SIZE = 2;
	private ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);
	private static ZipLogThreadPool instance = new ZipLogThreadPool();

	private ZipLogThreadPool() {
	}

	public static ZipLogThreadPool getInstance() {
		return instance;
	}

	public void execute(Runnable task) {
		executorService.execute(task);
	}
}

class ZipLogThread implements Runnable {
	private String zipFileName;

	ZipLogThread(String zipFileName) {
		this.zipFileName = zipFileName;
	}

	@Override
	public void run() {
		try {
			execute();
		} catch (Exception e) {
			LogLog.error("zip error:" + zipFileName, e);
		}
	}

	private void execute() {
		zipFile();
	}

	void zipFile() {
		File zipFile = new File(zipFileName);
		if (zipFile.exists()) {
			LogLog.debug("compress file " + zipFileName);
			String zfname = null;
			int index = zipFileName.lastIndexOf('/');
			if (index > 0)
				zfname = zipFileName.substring(index + 1);
			else {
				index = zipFileName.lastIndexOf('\\');
				if (index > 0)
					zfname = zipFileName.substring(index + 1);
				else
					zfname = zipFileName;
			}
			ZipEntry zipEntry = new ZipEntry(zfname);
			ZipOutputStream zipOut = null;
			InputStream input = null;
			try {
				byte[] buf = new byte[8192];
				int length = 0;
				zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName + ".zip", false)));
				input = new BufferedInputStream(new FileInputStream(zipFile));
				zipOut.putNextEntry(zipEntry);
				while ((length = input.read(buf)) != -1) {
					zipOut.write(buf, 0, length);
				}
				zipOut.flush();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} finally {
				close(input);
				close(zipOut);
			}
			zipFile.delete();
			synchronized (HryRollingAppender.getZipFileList()) {
				HryRollingAppender.getZipFileList().remove(zipFileName);
			}
			LogLog.debug("compress file " + zipFileName + ".zip ok.");
			// System.out.println("compress file " + zipFileName + ".zip ok.");
		}
	}

	void close(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				LogLog.debug("close stream error:" + stream.getClass().getName(), e);
			}
		}
	}
}

class OldLogFileFilter implements FileFilter {
	private long zipMiliSeconds;
	private String fileName;

	public OldLogFileFilter(String fileName, long zipMiliSeconds) {
		this.zipMiliSeconds = zipMiliSeconds;
		this.fileName = fileName.replaceAll("\\\\", "/") + ".";
	}

	@Override
	public boolean accept(File pathname) {
		String name = pathname.getPath().replaceAll("\\\\", "/");
		if (!name.startsWith(fileName) || name.endsWith("zip"))
			return false;

		if ((zipMiliSeconds + pathname.lastModified()) > System.currentTimeMillis())
			return false;
		return true;
	}
}

class OldZipFileFilter implements FileFilter {
	private long delMiliSeconds;
	private String fileName;

	public OldZipFileFilter(String fileName, long delMiliSeconds) {
		this.delMiliSeconds = delMiliSeconds;
		this.fileName = fileName.replaceAll("\\\\", "/") + ".";
	}

	@Override
	public boolean accept(File pathname) {
		String name = pathname.getPath().replaceAll("\\\\", "/");
		if (!name.startsWith(fileName) || !name.endsWith("zip"))
			return false;

		if ((delMiliSeconds + pathname.lastModified()) > System.currentTimeMillis())
			return false;
		return true;
	}

}