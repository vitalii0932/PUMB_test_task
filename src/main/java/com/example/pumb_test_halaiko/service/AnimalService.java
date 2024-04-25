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
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * service class for Animal
 */
@Service
@RequiredArgsConstructor
public class AnimalService {

    /* get entityManager for create a dynamic select query */
    @PersistenceContext
    private EntityManager entityManager;

    private final AnimalRepository animalRepository;
    private final CategoryRepository categoryRepository;
    private final TypeRepository typeRepository;

    /**
     * save new animal with some data
     *
     * @param values - data from users file
     * @throws IllegalArgumentException if data is incorrect
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void save(String[] values) throws IllegalArgumentException {
        // create a new Animal Entity
        Animal animal = new Animal();

        // set name
        animal.setName(values[0]);

        // set existing type or create a new type
        var type = typeRepository.findByName(values[1])
                .orElseGet(() -> typeRepository.save(Type.builder().name(values[1]).build()));
        animal.setType(type);

        // set sex
        values[2] = values[2].toUpperCase();
        if (Sex.contains(values[2])) {
            animal.setSex(values[2].toLowerCase());
        }

        // set weight
        animal.setWeight(Double.parseDouble(values[3]));

        // set cost
        animal.setCost(Double.parseDouble(values[4]));

        // set category
        if (animal.getCost() > 0 && animal.getCost() <= 20) {
            // if 0 < cost <= 20
            animal.setCategory(categoryRepository.findById(1).orElseThrow());
        } else if (animal.getCost() > 20 && animal.getCost() <= 40) {
            // if 20 < cost <= 40
            animal.setCategory(categoryRepository.findById(2).orElseThrow());
        } else if (animal.getCost() > 40 && animal.getCost() <= 60) {
            // if 40 < cost <= 60
            animal.setCategory(categoryRepository.findById(3).orElseThrow());
        } else if (animal.getCost() > 60) {
            // if 60 < cost
            animal.setCategory(categoryRepository.findById(4).orElseThrow());
        } else {
            // throw ex is incorrect
            throw new NumberFormatException("price must be greater then 0");
        }

        // save entity in db
        animalRepository.save(animal);
    }

    /**
     * dynamic query for searching animal by params function
     *
     * @param filter   - searching filter param
     * @param filterBy - searching filter value
     * @param sort     - searching sort param
     * @param sortBy   - sort desc or asc
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
            switch (filter) {
                case "type" ->
                    // if filtering by 'type', retrieve Type entity by name and add a predicate
                        criteriaQuery.where(builder.equal(
                                root.get(filter),
                                typeRepository.findByName(filterBy).orElseThrow(
                                        () -> new RuntimeException("Type not found exception")))
                        );
                case "category" ->
                    // if filtering by 'category', retrieve Category entity by name and add a predicate
                        criteriaQuery.where(builder.equal(
                                root.get(filter),
                                categoryRepository.findByName(filterBy).orElseThrow(
                                        () -> new RuntimeException("Category not found exception")))
                        );
                case "sex" ->
                    // if filtering by 'sex', directly add a predicate with the provided value
                        criteriaQuery.where(builder.equal(
                                root.get(filter),
                                filterBy
                        ));
                default ->
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
            } else {
                throw new RuntimeException("Sort type not found exception");
            }
        }

        // execute the query and return the result list
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
