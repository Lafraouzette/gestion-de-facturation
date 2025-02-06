package dev.kad.invoicemanagement.model.services;

import dev.kad.invoicemanagement.model.entities.Client;
import dev.kad.invoicemanagement.model.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return this.clientRepository.findAll();
    }

    public Client getClientById(String clientId) {
        return clientRepository.findById(clientId).orElseThrow(()-> new IllegalArgumentException("Client with ID " + clientId + " does not exist."));
    }
    
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    
    public Client updateClient(String id, Client client) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client with ID " + id + " does not exist.");
        }
        return clientRepository.save(client);
    }
    
    public void deleteClient(String clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new IllegalArgumentException("Client with ID " + clientId + " does not exist.");
        }
        clientRepository.deleteById(clientId);
    }

}
