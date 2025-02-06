package dev.kad.invoicemanagement.model.dto;

import dev.kad.invoicemanagement.model.entities.Client;
import dev.kad.invoicemanagement.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class UserClient{
    private User user;
    private Client client;
}
