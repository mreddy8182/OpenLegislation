package gov.nysenate.openleg.model.sourcefiles.xml;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nysenate.openleg.model.sourcefiles.BaseSourceFile;
import gov.nysenate.openleg.model.sourcefiles.sobi.InvalidSobiNameEx;

/**
 * Represents the 'newer' source files that LBDC sends.
 */
public class XmlFile extends BaseSourceFile {
    /**
     * The format for the XML files
     */
    private static final String xmlPattern = "yyyy'-'MM'-'dd'T'HH'.'mm'.'ss'.'SSSSSS";

    private static final Pattern fileNamePattern = Pattern.compile(
            "(?<date>[0-9-]{10})-(?<time>[0-9.]{15})_(?<type>[A-Z]+)_(?<target>.+)\\.XML");
    private final String stagingDir="/data/openleg/staging/xmls/";

    /**
     * --- Constructors ---
     */

    public XmlFile(File xmlFile) throws IOException {
        super(xmlFile);
        standingDir=new File(stagingDir);
    }

    public XmlFile(File file, String encoding) throws IOException {
        super(file, encoding);
        standingDir=new File(stagingDir);
    }

    /** --- Overrides --- */

    /**
     * Get the published date time from the file name.
     *
     * @return LocalDateTime
     */
    @Override
    public LocalDateTime getPublishedDateTime() throws InvalidSobiNameEx {
        try {
            String fileName=getFileName();
            Matcher m = fileNamePattern.matcher(fileName);
            DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern(xmlPattern);
            if (m.matches()) {
                return LocalDateTime.parse(m.group("date")+ "T" + m.group("time"),
                        dateTimeFormatter);
            }
        } catch (DateTimeParseException ex) {
            throw new IllegalStateException(
                    "Failed to parse published datetime from Sobi XML: " + ex.getMessage());
        }
        throw new IllegalStateException(
                "Failed to parse published datetime from Sobi XML because the filename" +
                        " did not match the required format.");
    
    }
}