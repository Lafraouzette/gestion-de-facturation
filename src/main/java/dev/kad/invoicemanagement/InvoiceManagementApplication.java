package dev.kad.invoicemanagement;

import dev.kad.invoicemanagement.model.entities.User;
import dev.kad.invoicemanagement.model.enums.Role;
import dev.kad.invoicemanagement.model.repository.ProduitRepository;
import dev.kad.invoicemanagement.model.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;

@SpringBootApplication
public class InvoiceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, ProduitRepository produitRepository) {
		return args -> {
			try {
				if(userRepository.findUserByUsername("admin").isEmpty()) {
					userRepository.save(new User(null, "admin", "admin@gmail.com", "admin", Role.ADMIN, null));
				}
//				Service service1 = new Service();
//				service1.setName("Cloud Backup");
//				service1.setPrice(50.0);
//				service1.setTva(20.0); // 20% VAT
//
//				Service service2 = new Service();
//				service2.setName("Network Monitoring");
//				service2.setPrice(100.0);
//				service2.setTva(15.0); // 15% VAT
//
//				Service service3 = new Service();
//				service3.setName("Cybersecurity Audit");
//				service3.setPrice(300.0);
//				service3.setTva(20.0); // 20% VAT
//
//				Service service4 = new Service();
//				service4.setName("Database Management");
//				service4.setPrice(200.0);
//				service4.setTva(18.0); // 18% VAT
//				service4.setQuantite(8);
//				produitRepository.saveAll(List.of(service1, service2, service3, service4));
//				Article article1 = new Article();
//				article1.setName("Laptop");
//				article1.setPrice(800.0);
//				article1.setTva(20.0); // 20% VAT
//				article1.setQuantiteStock(25);
//
//				Article article2 = new Article();
//				article2.setName("Router");
//				article2.setPrice(100.0);
//				article2.setTva(18.0); // 18% VAT
//				article2.setQuantiteStock(20);
//
//				Article article3 = new Article();
//				article3.setName("External Hard Drive");
//				article3.setPrice(75.0);
//				article3.setTva(15.0); // 15% VAT
//				article3.setQuantiteStock(25);
//
//				Article article4 = new Article();
//				article4.setName("Smartphone");
//				article4.setPrice(600.0);
//				article4.setTva(25.0); // 25% VAT
//				article4.setQuantiteStock(10);
//
//				Article article5 = new Article();
//				article5.setName("Monitor");
//				article5.setPrice(250.0);
//				article5.setTva(22.0); // 22% VAT
//				article5.setQuantiteStock(18);
//				produitRepository.saveAll(List.of(article1, article2, article3, article4, article5));
			} catch (ObjectOptimisticLockingFailureException e) {
				System.out.println("--------- "+e.getMessage()+" ---------");
			}
		};
	}
}
