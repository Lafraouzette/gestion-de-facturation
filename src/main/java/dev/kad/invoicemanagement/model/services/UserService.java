package dev.kad.invoicemanagement.model.services;

import dev.kad.invoicemanagement.model.entities.Client;
import dev.kad.invoicemanagement.model.entities.User;
import dev.kad.invoicemanagement.model.enums.Role;
import dev.kad.invoicemanagement.model.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ClientService clientService;

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User findUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }
        return user.get();
    }

    public void register(User user, Client client) {
        Client client1 = this.clientService.createClient(client);
        user.setClientId(client1.getId());
        this.register(user);

    }

    public User register(User user) {
        Optional<User> existingUser = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail()))
                .findFirst();
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username or email already exists.");
        }
        return userRepository.save(user);
    }

    public User login(String username, String password, HttpSession session) {
        Optional<User> user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        session.setAttribute("loggedInUser", user.get());
        return user.get();
    }

    public boolean logout(HttpSession session) {
        session.invalidate();
        return true;
    }

    public User updateUser(Long id, User updatedUser, HttpSession session) {
        User loggedInUser = this.getSessionUser(session);

        if (!loggedInUser.getRole().equals(Role.ADMIN) && !loggedInUser.getId().equals(id)) {
            throw new SecurityException("Access denied.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());

        return userRepository.save(user);
    }

    public boolean deleteUser(Long id, HttpSession session) {
        User loggedInUser = getSessionUser(session);

        if (!loggedInUser.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("Access denied.");
        }

        userRepository.deleteById(id);
        return true;
    }

    public User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

}