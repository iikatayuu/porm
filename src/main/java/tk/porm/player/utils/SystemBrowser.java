package tk.porm.player.utils;

public class SystemBrowser {
	public static void open(String url) {
		String os = System.getProperty("os.name").toLowerCase();
		Runtime runtime = Runtime.getRuntime();

		try {
			if (os.indexOf("win") >= 0) {
				runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0) {
				// Was not tested yet
				runtime.exec("xdg-open " + url);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
