package hexlet.code;

import hexlet.code.domain.Url;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;
import io.ebean.DB;
import io.ebean.Transaction;

import java.net.MalformedURLException;
import java.net.URL;

import hexlet.code.domain.Url;
import hexlet.code.domain.query.QUrl;

public class AppTest {

    @Test
    void testInit() {
        assertThat(true).isEqualTo(true);
    }

    private static Javalin app;
    private static String baseUrl;
    private static Url existingUrl;
    private static Transaction transaction;

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;

        existingUrl = new Url("https://javalin.io/");
        existingUrl.save();
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        transaction = DB.beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Nested
    class RootTest {

        @Test
        void testIndex() {
            HttpResponse<String> response = Unirest.get(baseUrl).asString();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Анализатор");
        }

    }

    @Nested
    class UrlTest {

        @Test
        void testIndex() {
            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(existingUrl.getName());
        }

        @Test
        void testShow() {
            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls/" + existingUrl.getId())
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(existingUrl.getName());
        }


        @Test
        void testCreate() throws MalformedURLException {
            String inputName = "https://www.google.com/search?q";
            HttpResponse<String> responsePost = Unirest
                    .post(baseUrl + "/urls")
                    .field("name", inputName)
                    .asEmpty();

            assertThat(responsePost.getStatus()).isEqualTo(302);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(inputName);
            assertThat(body).contains("Страница успешно добавлена");

            URL inputUrl = new URL(inputName);
            String normalizedName = inputUrl.getProtocol() + "://" + inputUrl.getAuthority();
            Url actualUrl = new QUrl()
                    .name.equalTo(normalizedName)
                    .findOne();

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(normalizedName);
        }
    }
}
