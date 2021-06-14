import java.io.IOException;

import utils.ConfigFilesManager;

public class Main {

	public static void main(String[] args) {
		final ConfigFilesManager filesManager = new ConfigFilesManager("D:\\Temp\\Config files");

		try {
			filesManager.loadAttributes();
//			filesManager.printAttributes();

			System.out.println("------------------");

//			filesManager.removeAttribute("TEST 6");
//			filesManager.printAttributes();

			filesManager.addAttribute("NEW TEST 3");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
