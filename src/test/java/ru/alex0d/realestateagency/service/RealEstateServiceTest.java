package ru.alex0d.realestateagency.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alex0d.realestateagency.exception.RealEstateNotFoundException;
import ru.alex0d.realestateagency.model.RealEstate;
import ru.alex0d.realestateagency.model.User;
import ru.alex0d.realestateagency.repository.RealEstateRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RealEstateServiceTest {

    @Mock
    private RealEstateRepository repositoryMock;

    @InjectMocks
    private RealEstateService service;

    @Test
    public void testFindAll() {
        // Arrange
        List<RealEstate> expected = Arrays.asList(
                new RealEstate(1L, "Real Estate 1", "Description 1", "Address 1", BigDecimal.valueOf(10), 0d, 0d, new User()),
                new RealEstate(2L, "Real Estate 2", "Description 2", "Address 2", BigDecimal.valueOf(20), 0d, 0d, new User())
        );
        Mockito.when(repositoryMock.findAll()).thenReturn(expected);

        // Act
        List<RealEstate> actual = service.findAll();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSearch() {
        // Arrange
        String query = "Real";
        List<RealEstate> expected = Arrays.asList(
                new RealEstate(1L, "Real Estate 1", "Description 1", "Address 1", BigDecimal.valueOf(10), 0d, 0d, new User()),
                new RealEstate(2L, "Real Estate 2", "Description 2", "Address 2", BigDecimal.valueOf(20), 0d, 0d, new User())
        );
        Mockito.when(repositoryMock.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(query, query)).thenReturn(expected);

        // Act
        List<RealEstate> actual = service.search(query);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testFindById() {
        // Arrange
        Long id = 1L;
        RealEstate expected = new RealEstate(1L, "Real Estate 1", "Description 1", "Address 1", BigDecimal.valueOf(10), 0d, 0d, new User());
        Mockito.when(repositoryMock.findById(id)).thenReturn(Optional.of(expected));

        // Act
        RealEstate actual = service.findById(id);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIdNotFound() {
        assertThrows (
                RealEstateNotFoundException.class,
                () -> service.findById(1L),
                "Expected findById() to throw, but it didn't"
        );
    }

    @Test
    public void testSave() {
        // Arrange
        RealEstate realEstate = new RealEstate(1L, "Real Estate 1", "Description 1", "Address 1", BigDecimal.valueOf(10), 0d, 0d, new User());
        Mockito.when(repositoryMock.save(realEstate)).thenReturn(new RealEstate(1L, "Real Estate 1", "Description 1", "Address 1", BigDecimal.valueOf(10), 0d, 0d, new User()));

        // Act
        RealEstate savedRealEstate = service.save(realEstate);

        // Assert
        assertEquals(Long.valueOf(1), savedRealEstate.getId());
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        service.deleteById(id);

        // Assert
        Mockito.verify(repositoryMock).deleteById(id);
    }

    @Test
    public void testFindByOwner() {
        // Arrange
        User owner = new User();
        List<RealEstate> expected = Arrays.asList(
                new RealEstate(1L, "Real Estate 1", "Description 1", "Address 1", BigDecimal.valueOf(10), 0d, 0d, new User()),
                new RealEstate(2L, "Real Estate 2", "Description 2", "Address 2", BigDecimal.valueOf(20), 0d, 0d, new User())
        );
        Mockito.when(repositoryMock.findByOwner(owner)).thenReturn(expected);

        // Act
        List<RealEstate> actual = service.findByOwner(owner);

        // Assert
        assertEquals(expected, actual);
    }
}