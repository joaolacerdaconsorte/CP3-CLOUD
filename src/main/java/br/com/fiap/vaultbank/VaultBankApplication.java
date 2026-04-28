package br.com.fiap.vaultbank;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "VaultBank API",
        version = "1.0.0",
        description = "API REST bancária fictícia — CP2 DevOps | FIAP 2TDSPW 2026\n\n"
                + "Sistema de gestão de contas e transações bancárias com CRUD completo, "
                + "conectado a banco MySQL em container Docker.",
        contact = @Contact(
            name = "João Vitor Lacerda Consorte",
            url = "https://github.com/joaovlc"
        )
    )
)
public class VaultBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaultBankApplication.class, args);
    }
}
