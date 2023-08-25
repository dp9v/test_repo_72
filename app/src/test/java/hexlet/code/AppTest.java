package hexlet.code;

import hexlet.code.domain.Url;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import hexlet.code.domain.Url;
import hexlet.code.domain.query.QUrl;

public class AppTest {

//    @Test
//    void testInit() {
//        assertThat(true).isEqualTo(true);
//    }

    private static Javalin app;
    private static String baseUrl;
    private static Url existingUrl;
    private static Transaction transaction;


    @BeforeAll
    public static void beforeAll() throws IOException {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;

        existingUrl = new Url("https://javalin.io/");
        existingUrl.save();

    }

    @AfterAll
    public static void afterAll() throws IOException {
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
            assertThat(response.getBody()).contains("Анализатор страниц");
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
        void testShowUrls() {
            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String bodyGet = response.getBody();

            assertThat(bodyGet).contains(existingUrl.getName());
            assertThat(bodyGet).contains("Previous");
            assertThat(bodyGet).contains("Next");
        }


        @Test
        void testCreateNew() throws MalformedURLException {
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

            URL inputUrl = new URL(inputName);
            String normalizedName = inputUrl.getProtocol() + "://" + inputUrl.getAuthority();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(normalizedName);
            assertThat(body).contains("Страница успешно добавлена");

            Url actualUrl = new QUrl()
                    .name.equalTo(normalizedName)
                    .findOne();

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(normalizedName);
        }

        @Test
        void testCreateExisting() {
            String inputName = existingUrl.getName();
            HttpResponse<String> responsePost = Unirest
                    .post(baseUrl + "/urls")
                    .field("name", inputName)
                    .asString();

            assertThat(responsePost.getStatus()).isEqualTo(302);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");

            String body = responsePost.getBody();
            assertThat(body.contains("Страница уже существует"));

            Url actualUrl = new QUrl()
                    .name.equalTo(existingUrl.getName())
                    .findOne();

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(existingUrl.getName());
        }

        @Test
        void testCreateNonValid() {
            String inputName = "asdfg";
            HttpResponse<String> responsePost = Unirest
                    .post(baseUrl + "/urls")
                    .field("name", inputName)
                    .asString();

            assertThat(responsePost.getStatus()).isEqualTo(200);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("");

            String body = responsePost.getBody();
            assertThat(body).contains("Некорректный URL");
        }

        @Test
        void testcheckUrl() throws IOException {

            String path = "src/test/resources/test.html";
            File file = new File(path);
            String absolutePath = file.getAbsolutePath();

            String page = Files.readString(Paths.get(absolutePath));

            MockWebServer server = new MockWebServer();
            MockResponse mockedResponse = new MockResponse()
                    .setBody(page)
                    .setBodyDelay(20, TimeUnit.SECONDS);

            server.enqueue(mockedResponse);
            server.start();

            String testUrl = server.url("/").toString();

            HttpResponse response = Unirest
                    .post(baseUrl + "/urls/")
                    .field("name", testUrl)
                    .asEmpty();

            Url url = new QUrl()
                    .name.equalTo(testUrl.substring(0, testUrl.length() - 1))
                    .findOne();

            assertThat(url).isNotNull();

            HttpResponse<String> responsePost = Unirest
                    .post(baseUrl + "/urls/" + url.getId() + "/checks")
                    .asString();

            assertThat(responsePost.getStatus()).isEqualTo(302);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls/" + url.getId());
            String body1 = responsePost.getBody();
            assertThat(body1.contains("Страница успешно проверена"));


//            HttpResponse<String> responseGet = Unirest
//                    .get(baseUrl + "/urls/" + url.getId())
//                    .asString();
//            String body = responseGet.getBody();

//            String body = Unirest
//                    .get(baseUrl + "/urls/" + url.getId())
//                    .asString()
//                    .getBody();

            //assertThat(body1).contains("200");
//            assertThat(body1).contains("title text");
//            assertThat(body1).contains("description text");
//            assertThat(body1).contains("h1 text");

            server.shutdown();

        }
    }
}
