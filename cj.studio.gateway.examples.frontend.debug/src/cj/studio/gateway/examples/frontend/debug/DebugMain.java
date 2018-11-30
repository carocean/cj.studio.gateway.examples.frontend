package cj.studio.gateway.examples.frontend.debug;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cj.studio.ecm.Assembly;
import cj.studio.ecm.IAssembly;
import cj.studio.ecm.adapter.IActuator;
import cj.studio.ecm.adapter.IAdaptable;

public class DebugMain {


	private static String fileName;
	public static void main(String[] args) throws ParseException, IOException {
		fileName = "cj.studio.gateway.console-1.0";
		Options options = new Options();
//		Option h = new Option("h", "host",false, "要绑定的ip地址（一台服务器上可能有多网卡，默认采用localhost)，格式：-h ip:port，port可以省去");
//		options.addOption(h);
		Option  l = new Option("l","log", false, "充许网络日志输出到控制台");
		options.addOption(l);
		Option  m = new Option("m","man", false, "帮助");
		options.addOption(m);
		Option  u = new Option("nohup","nohup", false, "使用nohup后台启动");
		options.addOption(u);
//		Option  p = new Option("p","pwd", true, "密码，如果密码前有!符，请将密码前后加引号'");
//		options.addOption(p);
//		Option  db = new Option("db","database", true, "mongodb的库名，有权限访问的");
//		options.addOption(db);
		Option debug = new Option("d","debug", true, "调试命令行程序集时使用，需指定以下jar包所在目录\r\n"+fileName);
		options.addOption(debug);

		// GnuParser
		// BasicParser
		// PosixParser
		GnuParser parser = new GnuParser();
		CommandLine line = parser.parse(options, args);
		if (line.hasOption("m")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("gateway", options);
			return;
		}
		
		// 取属性的方式line.getOptionProperties("T").get("boss")
		// System.out.println(line.getOptionProperties("T").get("boss"));
//		if(StringUtil.isEmpty(line.getOptionValue("h")))
//			throw new ParseException("参数-h是host为必需，但为空");
		
		String usr = System.getProperty("user.dir");
		File f = new File(usr);
		File[] arr = f.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.startsWith(fileName)) {
					return true;
				}
				return false;
			}
		});
		if (arr.length < 1 && !line.hasOption("debug")) {
			throw new IOException(fileName + " 程序集不存在.");
		}
		if (line.hasOption("debug")) {
			File[] da = new File(line.getOptionValue("debug")).listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					if (name.startsWith(fileName)) {
						return true;
					}
					return false;
				}
			});
			if (da.length < 0)
				throw new IOException("调试时不存在指定的必要jar包" + fileName);
			f = da[0];
		} else {
			f = arr[0];
		}

		IAssembly assembly = Assembly.loadAssembly(f.toString());
		assembly.start();
		Object main = assembly.workbin().part("gatewayEntrypoint");
		IAdaptable a = (IAdaptable) main;
		IActuator act = a.getAdapter(IActuator.class);
		act.exactCommand("setHomeDir", new Class<?>[]{String.class}, f.getParent());
		act.exeCommand("main", line);

	}

}
