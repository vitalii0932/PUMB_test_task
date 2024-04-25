package com.example.pumb_test_halaiko.service;

import com.example.pumb_test_halaiko.enums.Sex;
import com.example.pumb_test_halaiko.model.Animal;
import com.example.pumb_test_halaiko.model.Type;
import com.example.pumb_test_halaiko.repository.AnimalRepository;
import com.example.pumb_test_halaiko.repository.CategoryRepository;
import com.example.pumb_test_halaiko.repository.TypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Query;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * service class for Animal
 */
@Service
@RequiredArgsConstructor
public class AnimalService {

    @PersistenceContext
    private EntityManager entityManager;

    private final AnimalRepository animalRepository;
    private final CategoryRepository categoryRepository;
    private final TypeRepository typeRepository;

    /**
     * read file and save the correct data
     *
     * @param file - file with data about animals from user
     * @throws Exception
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void readFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String fileExtension = null;
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        if (fileExtension != null && fileExtension.equals("csv")) {
            readFromCsv(file);
        } else if (fileExtension != null && fileExtension.equals("xml")) {
            readFromXml(file);
        } else {
            throw new IOException("This file extension is not supported");
        }
    }

    /**
     * read the csv file and save the correct data
     *
     * @param file - file with animal data from user
     * @throws Exception if something is wrong when reading the file
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void readFromCsv(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = Arrays.stream(line.split(","))
                        .filter(str -> !str.isEmpty())
                        .toArray(String[]::new);
                if (values.length == 5) {
                    try {
                        save(values);
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
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void readFromXml(MultipartFile file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file.getInputStream());

        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("animal");

        String[] params = {"name", "type", "sex", "weight", "cost"};

        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    Element element = (Element) node;

                    String[] values = new String[5];

                    for (int i = 0; i < params.length; i++) {
                        values[i] = element.getElementsByTagName(params[i]).item(0).getTextContent();
                    }

                    save(values);
                } catch (NullPointerException ex) {
                    ex.fillInStackTrace();
                }
            }
        }

    }

    /**
     * save new animal with some data
     *
     * @param values - data from users file
     * @throws IllegalArgumentException if data is incorrect
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void save(String[] values) throws IllegalArgumentException {
        Animal animal = new Animal();
        animal.setName(values[0]);

        var type = typeRepository.findByName(values[1])
                .orElseGet(() -> typeRepository.save(Type.builder().name(values[1]).build()));

        animal.setType(type);

        values[2] = values[2].toUpperCase();

        if (Sex.contains(values[2])) {
            animal.setSex(values[2].toLowerCase());
        }

        animal.setWeight(Double.parseDouble(values[3]));
        animal.setCost(Double.parseDouble(values[4]));

        if (animal.getCost() > 0 && animal.getCost() <= 20) {
            animal.setCategory(categoryRepository.findById(1).orElseThrow());
        } else if (animal.getCost() > 20 && animal.getCost() <= 40) {
            animal.setCategory(categoryRepository.findById(2).orElseThrow());
        } else if (animal.getCost() > 40 && animal.getCost() <= 60) {
            animal.setCategory(categoryRepository.findById(3).orElseThrow());
        } else if (animal.getCost() > 60) {
            animal.setCategory(categoryRepository.findById(4).orElseThrow());
        } else {
            throw new NumberFormatException("price must be greater then 0");
        }

        animalRepository.save(animal);
    }

    /**
     * dynamic query for searching animal by params function
     *
     * @param filter - searching filter param
     * @param filterBy - searching filter value
     * @param sort - searching sort param
     * @param sortBy - sort desc or asc
     * @return a list of animals
     * @throws RuntimeException if param is incorrect
     */
    @Transactional(readOnly = true)
    public List<Animal> findAnimalsByParams(String filter, String filterBy, String sortBy, String sort) throws RuntimeException {
        /*
        get CriteriaBuilder from EntityManager,
        create a CriteriaQuery for the Animal entity
        and specify the root entity for the query
         */
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Animal> criteriaQuery = builder.createQuery(Animal.class);
        Root<Animal> root = criteriaQuery.from(Animal.class);

        // select the root entity
        criteriaQuery.select(root);

        // apply filtering if filter and filterBy are provided
        if (filter != null && filterBy != null && !filter.isEmpty() && !filterBy.isEmpty()) {
            // handle filtering based on different fields
            if (filter.equals("type")) {
                // if filtering by 'type', retrieve Type entity by name and add a predicate
                criteriaQuery.where(builder.equal(
                        root.get(filter),
                        typeRepository.findByName(filterBy).orElseThrow(
                                () -> new RuntimeException("Type not found exception")))
                );
            } else if (filter.equals("category")) {
                // if filtering by 'category', retrieve Category entity by name and add a predicate
                criteriaQuery.where(builder.equal(
                        root.get(filter),
                        categoryRepository.findByName(filterBy).orElseThrow(
                                () -> new RuntimeException("Category not found exception")))
                );
            } else if (filter.equals("sex")) {
                // if filtering by 'sex', directly add a predicate with the provided value
                criteriaQuery.where(builder.equal(
                        root.get(filter),
                        filterBy
                ));
            } else {
                // if the filter field is not recognized, throw an exception
                throw new RuntimeException("Filter not found exception");
            }
        }

        // apply sorting if sortBy and sort are provided
        if (sortBy != null && sort != null && !sortBy.isEmpty() && !sort.isEmpty()) {
            // handle sorting based on the specified field and order
            if ("asc".equalsIgnoreCase(sort)) {
                // sort in ascending order
                criteriaQuery.orderBy(builder.asc(root.get(sortBy)));
            } else if ("desc".equalsIgnoreCase(sort)) {
                // sort in descending order
                criteriaQuery.orderBy(builder.desc(root.get(sortBy)));
            }
        }

        // execute the query and return the result list
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
