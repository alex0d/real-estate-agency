package ru.alex0d.realestateagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex0d.realestateagency.model.RealEstate;
import ru.alex0d.realestateagency.model.User;

import java.util.List;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
    List<RealEstate> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String name, String address);

    List<RealEstate> findByOwner(User owner);
}
