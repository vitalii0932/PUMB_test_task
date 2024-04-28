package com.example.pumb_test_halaiko.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * open API (swagger) configuration class
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("halaikovitalii@ukr.net");
        contact.setName("Галайко Віталій");
        contact.setUrl("https://t.me/VitaliiGalayko");

        Info info = new Info()
                .title("Тестове завдання")
                .version("1.0")
                .contact(contact)
                .description("Завдання:\n" +
                        "1. створити АПІ, з методом POST по шляху files/uploads, який матиме підтримку CSV і XML файлів (приклади файлів з тестовими даними у атачментах)\n" +
                        "\n" +
                        "2. замапити об‘єкти з файлів на POJO\n" +
                        "\n" +
                        "3. виконати валідацію вхідних об‘єктів, і приймати тільки ті що мають ім‘я, тип, стать, вагу та вартість. Об‘єкти, що не виконують вимогам - скіпаються\n" +
                        "\n" +
                        "4. назначити категорії тваринам по вартості\n" +
                        "\n" +
                        "5. 0 - 20 - 1ша категорія, 21-40 - 2га, 41-60 - 3тя, від 61 - 4та\n" +
                        "\n" +
                        "6. зробити 1 метод GET який буде мати параметри фільтрації по типу тваринки, категорії, статі, а також можливість сортування по будь якому з присутніх в об‘єкті полів та повертати відповідь у json форматі в body\n" +
                        "\n" +
                        "7. зробити swagger file з описом API\n" +
                        "\n" +
                        "8. покрити юніт тестами\n" +
                        "\n" +
                        "9. тип сховища на ваш розсуд, але пам‘ятайте, ще сервіс після рестарту має не втратити об‘єктів");

        return new OpenAPI().info(info);
    }
}
