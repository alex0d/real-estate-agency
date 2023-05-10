package ru.alex0d.realestateagency.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.alex0d.realestateagency.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OwnerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public OwnerDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
    }
}
