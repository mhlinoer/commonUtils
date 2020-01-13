package com.linoer.app.utils.file.ftp;

import com.jcraft.jsch.*;
import com.linoer.app.model.ftp.FtpConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;


public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private ChannelSftp sftp;
    private Session sshSession;

    public boolean initFtp(FtpConnection connection){
        boolean flag = true;
        try {
            JSch jsch = new JSch();
            jsch.getSession(connection.getUsername(), connection.getHost());
            sshSession = jsch.getSession(connection.getUsername(), connection.getHost());
            sshSession.setPassword(connection.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            flag = false;
            close();
            logger.error("SFtpThread error:" + e.toString());
        }
        return flag;
    }
    public void close() {
        if (sftp != null) {
            sftp.quit();
            logger.info("sftp is closed");
        }
        if (sshSession != null && sshSession.isConnected()) {
            sshSession.disconnect();
            logger.info("sshSession is closed");
        }
    }

    public void test(String collectSourceDir, int splitNum, int localTag){
        Vector<ChannelSftp.LsEntry> vectors;
        List<String> RemoteFileNameList = new ArrayList<>();
        try {
            vectors = sftp.ls(collectSourceDir);
            for (ChannelSftp.LsEntry vector : vectors) {
                if (vector.getFilename().startsWith(".")) {
                    continue;
                }
                String fileName = vector.getFilename();
                if (!fileName.contains(".")){
                    continue;
                }
                String formatFileName = fileName.substring(0, fileName.lastIndexOf("."));
                System.out.println(Integer.parseInt(formatFileName) % splitNum == localTag);
                if (Integer.parseInt(formatFileName) % splitNum == localTag){
                    System.out.println("this flume thread:" + localTag + " add file:" + vector.getFilename());
                    RemoteFileNameList.add(vector.getFilename());
                }
            }
        } catch (SftpException e) {
            close();
            e.printStackTrace();
            logger.error("get the file list failed : " + e.toString());
        }
    }

    public static void main(String[] args) {
//        FtpUtil ftpUtil = new FtpUtil();
//        ftpUtil.initFtp(new FtpConnection(
//                "muhenglv",
//                "10.211.55.3",
//                "lvmuheng"
//        ));
//        ftpUtil.test("/home/muhenglv/hello", 4, 1);

        System.out.println(1 % 4 == 0);
        System.out.println(2 % 4 == 0);
        System.out.println(3 % 4 == 0);
        System.out.println(4 % 4 == 0);
        System.out.println(5 % 4 == 0);
        System.out.println(6 % 4 == 0);
        System.out.println(7 % 4 == 0);
        System.out.println(8 % 4 == 0);
    }
}
