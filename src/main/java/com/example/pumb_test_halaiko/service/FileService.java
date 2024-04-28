package com.example.pumb_test_halaiko.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * service class for Files logic
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private final AnimalService animalService;

    /**
     * read file and save the correct data
     *
     * @param file - file with data about animals from user
     * @throws Exception if something is wrong when reading the file
     */
    public void readFile(MultipartFile file) throws Exception {
        // check the file is not empty
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        // get the file extension
        String fileName = file.getOriginalFilename();
        String fileExtension = null;
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        // check file extension
        if (fileExtension != null && fileExtension.equals("csv")) {
            readFromCsv(file);
        } else if (fileExtension != null && fileExtension.equals("xml")) {
            readFromXml(file);
        } else {
            // throw if extension is not supported
            throw new IOException("This file extension is not supported");
        }
    }

    /**
     * read the csv file and save the correct data
     *
     * @param file - file with animal data from user
     * @throws Exception if something is wrong when reading the file
     */
    private void readFromCsv(MultipartFile file) throws Exception {
        /*
        get input stream from file,
        create a buffered reader
         */
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            /* read file while reader has a new line */
            while ((line = reader.readLine()) != null) {
                // get values from string and remove empty strings
                String[] values = Arrays.stream(line.split(","))
                        .filter(str -> !str.isEmpty())
                        .toArray(String[]::new);

                // check do values count is correct
                if (values.length == 5) {
                    try {
                        // try to save entity with this values
                        animalService.save(values);
                    } catch (IllegalArgumentException ex) {
                        ex.fillInStackTrace();
                    }
                }
            }
        }
    }

    /**
     * read the xml file and save the correct data
     *
     * @param file - file with animal data from user
     * @throws Exception if something is wrong when processing the file
     */
    private void readFromXml(MultipartFile file) throws Exception {
        /*
        create a DocumentBuilderFactory instance,
        create a DocumentBuilder instance,
        parse the XML file into a Document object
         */
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file.getInputStream());

        /*
        normalize the document structure,
        get a NodeList of all elements with the tag name "animal",
        define the parameters to extract from each "animal" element
         */
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("animal");
        String[] params = {"name", "type", "sex", "weight", "cost"};

        // loop through each "animal" element in the NodeList
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    // cast the Node to an Element
                    Element element = (Element) node;

                    // create an array to store the values of each parameter
                    String[] values = new String[5];

                    // extract values for each parameter from the current "animal" element
                    for (int i = 0; i < params.length; i++) {
                        values[i] = element.getElementsByTagName(params[i]).item(0).getTextContent();
                    }

                    // save the extracted values
                    animalService.save(values);
                } catch (NullPointerException ex) {
                    // handle any NullPointerExceptions that might occur during value extraction
                    ex.fillInStackTrace();
                }
            }
        }
    }
}
