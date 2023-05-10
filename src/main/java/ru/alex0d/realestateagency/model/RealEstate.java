package ru.alex0d.realestateagency.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.alex0d.realestateagency.dto.OwnerDto;

import java.math.BigDecimal;

@Entity
@Table(name = "real_estates")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RealEstate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public OwnerDto getOwner() {
        return new OwnerDto(owner);
    }
}