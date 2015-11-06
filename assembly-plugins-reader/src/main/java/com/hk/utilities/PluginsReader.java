package com.hk.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * Description.
 * @author hk
 */
public class PluginsReader {

    // public static File folder = new File("C:/Users/hk016213/Downloads/XXXX-WAR/WEB-INF/lib");

    public static void main(final String[] args) throws IOException, XmlPullParserException {
        final File folder = new File(args[0]);
        System.out.println("Path: " + args[0]);
        System.out.println("Reading files under the folder: " + folder.getAbsolutePath());
        listFilesForFolder(folder);
    }

    public static void listFilesForFolder(final File folder) throws IOException, XmlPullParserException {
        String temp = "";
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                // System.out.println("Reading files under the folder "+folder.getAbsolutePath());
                listFilesForFolder(fileEntry);
            } else {
                if (fileEntry.isFile()) {
                    temp = fileEntry.getName();
                    if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("jar")) {
                        // System.out.println("File= " + folder.getAbsolutePath() + "\\" + fileEntry.getName());
                        processFile(fileEntry);
                    }
                }

            }
        }
    }

    public static void processFile(final File fileEntry) throws IOException, XmlPullParserException {

        final ZipFile file = new ZipFile(fileEntry);
        if (file != null) {
            final Enumeration<? extends ZipEntry> entries = file.entries(); // get entries from the zip file...

            if (entries != null) {
                while (entries.hasMoreElements()) {
                    final ZipEntry entry = entries.nextElement();
                    final String temp = entry.getName();
                    if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("xml")
                            && temp.contains("pom")) {
                        getVersion(file.getInputStream(entry));

                    }
                }
            }
        }

    }

    public static synchronized String getVersion(final InputStream file) throws IOException, XmlPullParserException {
        final MavenXpp3Reader mavenreader = new MavenXpp3Reader();
        final Model model = mavenreader.read(file);
        if (model.getGroupId() != null) {
            System.out.println(model.getGroupId() + "." + model.getArtifactId());
        } else {
            System.out.println(model.getParent().getGroupId() + "." + model.getArtifactId());
        }
        return null;
    }

    /***
     * <dependency> <groupId>org.apache.maven</groupId> <artifactId>maven-core</artifactId> <version>3.0.3</version>
     * </dependency>
     */

}