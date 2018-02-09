package com.passwd.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.passwd.dao.AnalyzedDao;
import com.passwd.dao.impl.AnalyzedDaoImpl;
import com.passwd.model.AnalysisRec;

import analysisGraph.AnalysisPasswords;

/**
 * @author Admin
 *
 */
public class AnalyzedAction extends SuperAction {

	
	
	private static final long serialVersionUID = 1L;
	private File analysis;// 目标文件
	private String analysisFileName;
	private String savePath;// 文件保存路径
	private String analysisFileType;
	private List<AnalysisRec> AnalysisList = new ArrayList<AnalysisRec>();
	private AnalyzedDao ad = new AnalyzedDaoImpl();

	// 提交分析任务
	public String execute() {
		int userid = (Integer) session.getAttribute("com.passwd.userid");
		String uuid = uploadFile(analysis);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = df.format(new Date());
		AnalysisRec ar = new AnalysisRec(userid, analysisFileName, uuid, ctime);
		ad.addAnalysis(ar);
		return SUCCESS;
	}

	// 分析列表
	public String getAnalyzsisList() {
		int uid = (Integer) session.getAttribute("com.passwd.userid");
		this.AnalysisList = ad.getAnalysisRecList(uid);
		return SUCCESS;
	}

	public String uploadFile(File file) {
		final String uuid = UUID.randomUUID().toString();
		final String dstPath = ServletActionContext.getServletContext().getRealPath(this.getSavePath()) + File.separator
				+ uuid;
		try {
			FileUtils.copyFile(file, new File(dstPath + ".txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread() {
			public void run() {
				final String analysisFilePath = dstPath + ".txt";
				final String reportFile = dstPath + ".doc";
				AnalysisPasswords.analysisPwd(analysisFilePath, reportFile, 1);
				ad.updateAnalysisStatus(1, uuid);
			}
		}.start();
		return uuid;
	}

	// 删除分析任务
	public String deleteAnalysis() {
		int uid = (Integer) session.getAttribute("com.passwd.userid");
		ad.deleteAnalysis(uid, fileName);
		String url = ServletActionContext.getServletContext().getRealPath(inputPath) + File.separator + fileName
				+ ".doc";
		File file = new File(url);
		if (file.exists()) {
			file.delete();
		}
		return SUCCESS;
	}

	private String fileName;
	private String inputPath = "analysis";

	public InputStream getTargetFile() {
		inputPath += File.separator + fileName + ".doc";
		return ServletActionContext.getServletContext().getResourceAsStream(inputPath);
	}

	public String downloadAnalysis() {
		return SUCCESS;
	}

	public File getAnalysis() {
		return analysis;
	}

	public void setAnalysis(File analysis) {
		this.analysis = analysis;
	}

	public String getAnalysisFileName() {
		return analysisFileName;
	}

	public void setAnalysisFileName(String analysisFileName) {
		this.analysisFileName = analysisFileName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getAnalysisFileType() {
		return analysisFileType;
	}

	public void setAnalysisFileType(String analysisFileType) {
		this.analysisFileType = analysisFileType;
	}

	public List<AnalysisRec> getAnalysisList() {
		return AnalysisList;
	}

	public void setAnalysisList(List<AnalysisRec> analysisList) {
		AnalysisList = analysisList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
