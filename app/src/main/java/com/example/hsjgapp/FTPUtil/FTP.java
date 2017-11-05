package com.example.hsjgapp.FTPUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.example.hsjgapp.LoginActivity;

import android.os.Handler;
import android.os.Message;

public class FTP {
	// 服务器名
	private String hostName;
	// 端口号
	private int serverPort;
	// 用户名
	private String userName;
	// 密码
	private String password;
	// FTP连接.
	private FTPClient ftpClient;
	// handler消息
	private Handler handler;
	private Message message;
	private final String FTP_CONNECT_SUCCESSS = "ftp连接成功";
	private final String FTP_CONNECT_FAIL = "ftp连接失败";
	private final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
	private final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";
	//private final String FTP_DOWN_LOADING = "ftp文件正在下载";
	private final String FTP_DOWN_TXT_SUCCESS = "txt文件下载成功";
	private final String FTP_DOWN_APK_SUCCESS = "apk文件下载成功";
	private final String FTP_DOWN_FAIL = "ftp文件下载失败";

	public FTP() {
		this.serverPort = 21;
		this.userName = "updateftp";
		this.password = "81236238";
		this.ftpClient = new FTPClient();

	}

	/**
	 * 下载单个文件，可实现断点下载.
	 * 
	 * @param serverPath
	 *            Ftp目录及文件路径
	 * @param localPath
	 *            本地目录
	 * @param fileName
	 *            下载之后的文件名称
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public void downloadSingleFile(String serverPath, String localPath, String fileName, Handler handler,String ftpip) throws Exception {
		this.handler = handler;
		this.hostName = ftpip;
		// 打开FTP服务
		try {
			this.openConnect();
			getmessages(LoginActivity.FTP_GX_UI, FTP_CONNECT_SUCCESSS);
		} catch (IOException e1) {
			e1.printStackTrace();
			getmessages(LoginActivity.FTP_GX_UI, FTP_CONNECT_FAIL);
			return;
		}

		// 先判断服务器文件是否存在
		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			getmessages(LoginActivity.FTP_GX_UI, FTP_FILE_NOTEXISTS);
			return;
		}

		// 创建本地文件夹
		File mkFile = new File(localPath);
		if (!mkFile.exists()) {
			mkFile.mkdirs();
		}

		localPath = localPath + fileName;
		// 接着判断下载的文件是否能断点下载
		long serverSize = files[0].getSize(); // 获取远程文件的长度
		File localFile = new File(localPath);
		long localSize = 0;

		// 进度
		long step = serverSize / 100;
		long process = 0;
		long currentSize = 0;
		// 开始准备下载文件
		OutputStream out = new FileOutputStream(localFile, true);
		ftpClient.setRestartOffset(localSize);
		InputStream input = ftpClient.retrieveFileStream(serverPath);
		byte[] b = new byte[1024];
		int length = 0;
		while ((length = input.read(b)) != -1) {
			out.write(b, 0, length);
			/*currentSize = currentSize + length;
			if (currentSize / step != process) {
				process = currentSize / step;
				if (process % 5 == 0) { // 每隔%5的进度返回一次
					// getmessages(LoginActivity.FTP_GX_UI, FTP_DOWN_LOADING);
				}
			}*/
		}
		out.flush();
		out.close();
		input.close();

		
		// 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
		if (ftpClient.completePendingCommand()) {
			if (fileName.equals("version.txt")) {
				getmessages(LoginActivity.FTP_XZ_TXT, FTP_DOWN_TXT_SUCCESS);
			} else {
				getmessages(LoginActivity.FTP_AZ_APK, FTP_DOWN_APK_SUCCESS);
			}
		} else {
			getmessages(LoginActivity.FTP_GX_UI, FTP_DOWN_FAIL);
		}

		// 下载完成之后关闭连接
		this.closeConnect();
		getmessages(LoginActivity.FTP_GX_UI, FTP_DISCONNECT_SUCCESS);
		return;
	}

	// -------------------------------------------------------打开关闭连接------------------------------------------------

	/**
	 * 打开FTP服务.
	 * 
	 * @throws IOException
	 */
	public void openConnect() throws IOException {
		// 中文转码
		ftpClient.setControlEncoding("GBK");
		int reply; // 服务器响应值
		// 连接至服务器
		ftpClient.connect(hostName, serverPort);
		// 获取响应值
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// 断开连接
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		}
		// 登录到服务器
		ftpClient.login(userName, password);
		// 获取响应值
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// 断开连接
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		} else {
			// 获取登录信息
			FTPClientConfig config = new FTPClientConfig(ftpClient.getSystemType().split(" ")[0]);
			config.setServerLanguageCode("zh");
			ftpClient.configure(config);
			// 使用被动模式设为默认
			ftpClient.enterLocalPassiveMode();
			// 二进制文件支持
			ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
		}
	}

	/**
	 * 关闭FTP服务.
	 * 
	 * @throws IOException
	 */
	public void closeConnect() throws IOException {
		if (ftpClient != null) {
			// 退出FTP
			ftpClient.logout();
			// 断开连接
			ftpClient.disconnect();
		}
	}

	public void getmessages(int msgnumber, String neirong) {
		message = new Message();
		message.what = msgnumber;
		message.obj = neirong;
		handler.sendMessage(message);
	}
}
