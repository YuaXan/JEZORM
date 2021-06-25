package org.jezorm.core.controller;

import org.jezorm.core.Tools;

public class Setting {
	public final String PORJECT_NAME = ""; // package name
	public final String[] INSTALL_APP = {}; // App simple name
	public final String DB_PATH = ""; // 数据库Path

	public String getPath() {
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		if (System.getProperty("os.name").contains("dows")) {
			path = path.substring(1, path.length());
		}
		if (path.contains("jar")) {
			path = path.substring(0, path.lastIndexOf("."));
			return path.substring(0, path.lastIndexOf("target/"));
		}
		if (path.contains("exe")) {
			path = path.substring(0, path.lastIndexOf("."));
			return path.substring(0, path.lastIndexOf("/"));
		}
		return path.replace("target/classes/", "");
	}

	public String getPorjectName() {
		return (String) Tools.getClassField("PORJECT_NAME", this.getClass());
	}

	public String[] getinstallApp() {
		return (String[]) Tools.getClassField("INSTALL_APP", this.getClass());
	}

	public String getDbPath() {
		return (String) Tools.getClassField("DB_PATH", this.getClass());
	}
}
