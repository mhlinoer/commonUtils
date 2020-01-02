package com.linoer.app.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.cli.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class CommonHelper {

    public static String getCurrentDateTimeStr(){
        return System.currentTimeMillis() + "";
    }

    public static Map convertToMap(Object obj) {
        try {
            if (obj instanceof Map) {
                return (Map) obj;
            }
            Map returnMap = BeanUtils.describe(obj);
            returnMap.remove("class");
            return returnMap;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
            e1.getMessage();
        }
        return null;
    }

    /**
     * 参考方法，对命令行进行解析
     * @param args  命令行参数
     * @return      解析后的参数
     * @throws ParseException   Base for Exceptions thrown during parsing of a command-line
     */
    public CommandLine getOptionsCommandLine(String[] args) throws ParseException {
        Options options = new Options();

        Option option = new Option("n", "name", true, "the name of this agent");
        option.setRequired(true);
        options.addOption(option);

        option = new Option("f", "conf-file", true, "specify a conf file");
        option.setRequired(true);
        options.addOption(option);

        option = new Option(null, "no-reload-conf", false, "do not reload " +
                "conf file if changed");
        options.addOption(option);

        option = new Option("h", "help", false, "display help text");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();

        return parser.parse(options, args);
    }

}
