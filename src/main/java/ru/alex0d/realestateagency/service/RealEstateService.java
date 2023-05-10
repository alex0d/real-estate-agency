package ru.alex0d.realestateagency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alex0d.realestateagency.exception.RealEstateNotFoundException;
import ru.alex0d.realestateagency.model.RealEstate;
import ru.alex0d.realestateagency.model.User;
import ru.alex0d.realestateagency.repository.RealEstateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RealEstateService {
    private final RealEstateRepository repository;

    public List<RealEstate> findAll() {
        return repository.findAll();
    }

    public List<RealEstate> search(String query) {
        return repository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(query, query);
    }

    public RealEstate findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RealEstateNotFoundException("Real estate with id " + id + " not found"));
    }

    public RealEstate save(RealEstate realEstate) {
        return repository.save(realEstate);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<RealEstate> findByOwner(User owner) {
        return repository.findByOwner(owner);
    }
}