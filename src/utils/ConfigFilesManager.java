package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ConfigFilesManager {

	private List<String> attributes;
	private String directoryPath;

	public ConfigFilesManager(String directoryPath) {
		this.attributes = new ArrayList<>();
		this.directoryPath = directoryPath;
	}

	public File[] getConfigFiles() throws IOException {
		final File directory = new File(directoryPath);
		final File[] filesList = directory.listFiles();

		return filesList;
	}

	public void loadAttributes() throws IOException {

		final File[] filesList = getConfigFiles();

		for (final File file : filesList) {
			attributes.addAll(getAttributesFromFile(file));
		}
	}

	public List<String> getAttributesFromFile(File file) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(file));

		final List<String> attributesFromFile = new ArrayList<String>();
		String currentLine;

		while ((currentLine = reader.readLine()) != null) {

			if (currentLine.startsWith("ATTRIBUTE")) {
				attributesFromFile.add(currentLine.split("\t")[1]);
			}
		}
		reader.close();

		return attributesFromFile;
	}

	public void printAttributes() {
		for (final String attribute : attributes) {
			System.out.println(attribute);
		}
	}

	public boolean containsAttribute(String attribute) {
		return attributes.contains(attribute);
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public File findAttributeFile(String attribute) throws IOException {

		final File[] filesList = getConfigFiles();

		for (final File file : filesList) {

			final List<String> attributesFromFile = getAttributesFromFile(file);

			if (attributesFromFile.contains(attribute)) {
				return file;
			}
		}

		return null;
	}

	public void removeAttribute(String attribute) throws IOException {

		attributes.remove(attribute);

		final File file = findAttributeFile(attribute);

		if (file != null) {
			final BufferedReader reader = new BufferedReader(new FileReader(file));
			final File outputFile = new File(directoryPath + "\\temp.txt");
			final PrintWriter pw = new PrintWriter(outputFile);
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				if (!currentLine.startsWith("ATTRIBUTE") || (!currentLine.split("\t")[1].equals(attribute))) {
					pw.println(currentLine);
				}
			}

			pw.flush();
			pw.close();
			reader.close();

			file.delete();

			final Path source = Paths.get(outputFile.getAbsolutePath());
			Files.move(source, source.resolveSibling(file.getName()), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	public void addAttribute(String attribute) throws IOException {

		if (!containsAttribute(attribute)) {
			attributes.add(attribute);

			final File attributesFile = new File(directoryPath + "\\attributes.txt");
			if (!attributesFile.exists()) {
				attributesFile.createNewFile();
			}

			final PrintWriter pw = new PrintWriter(attributesFile);

			pw.printf("%s\r\n", attribute);

			pw.flush();
			pw.close();
		}
	}

}
