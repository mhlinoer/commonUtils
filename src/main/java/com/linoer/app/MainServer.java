package com.linoer.app;

import com.linoer.app.model.OptionModel;
import com.linoer.app.model.ServerConfigModel;
import com.linoer.app.utils.CommonHelper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 主程序入口
 */
public class MainServer {
    private static final Logger logger = LoggerFactory.getLogger(MainServer.class);

    private static File stopFileTag = new File("./.stop");

    private ServerConfigModel serverConfigModel = new ServerConfigModel();

    private void init() throws IOException {
        getLocalConf();
    }

    private void start(){

    }

    private void getLocalConf() throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream("./conf/main.properties"))) {
            Properties properties = new Properties();
            properties.load(in);
            serverConfigModel.setServerName(properties.getProperty("serverName"));
            serverConfigModel.setDebug(Boolean.parseBoolean(properties.getProperty("debug")));
        }
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("./conf/log4j.properties");
        try {
            List<OptionModel> optionModels = new ArrayList<>();
            optionModels.add(new OptionModel("n", "serverName", true, true));
            optionModels.add(new OptionModel("h", "host", true, false));
            CommandLine commandLine = CommonHelper.getOptionsCommandLine(args, optionModels);
            optionModels.forEach(optionModel ->
                    optionModel.setArgValue(commandLine.getOptionValue(
                            optionModel.getAbbreviatedName()))
            );
            if (stopFileTag.exists()){
                logger.debug("main process start ..., stop tag cleaned:{}", stopFileTag.delete());
            }
            MainServer server = new MainServer();
            server.init();
            server.start();
            logger.info("main process running ...");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            if (!stopFileTag.exists()){
                try {
                    logger.debug("main process start failed, stop tag created:{}", stopFileTag.createNewFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    logger.debug("main process start failed, stop tag created error:{}", ex.getMessage());
                }
            }
        }
    }
}
