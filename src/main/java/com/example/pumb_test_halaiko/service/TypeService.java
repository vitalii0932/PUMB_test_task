package com.example.pumb_test_halaiko.service;

import com.example.pumb_test_halaiko.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;

    @Transactional(readOnly = true)
    public List<String> getAllNames() {
        List<String> allTypeNamesWithPrefix = typeRepository.getAllTypesNames().stream()
                .map(name -> "type." + name)
                .collect(Collectors.toList());

        return allTypeNamesWithPrefix;

    }
}
