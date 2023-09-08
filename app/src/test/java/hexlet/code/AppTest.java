package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
//import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
//import io.ebean.DB;
import io.ebean.Database;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;

//import java.io.File;
import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;

//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;


public final class AppTest {

    private static Javalin app;
    private static String baseUrl;

    private static Database database;

    private static MockWebServer server;


    @BeforeAll
    public static void setUp() throws IOException, SQLException {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

//    @BeforeAll
//    public static void beforeAll() throws IOException, SQLException {
//        app = App.getApp();
//        app.start(0);
//        int port = app.port();
//        baseUrl = "http://localhost:" + port;
//        database = DB.getDefault();
//
//        String path = "src/test/resources/test.html";
//        File file = new File(path);
//        String absolutePath = file.getAbsolutePath();
//
//        String page = Files.readString(Paths.get(absolutePath));
//
//        server = new MockWebServer();
//        MockResponse mockedResponse = new MockResponse()
//                .setBody(page);
//
//        server.enqueue(mockedResponse);
//        server.start();
//    }
//
//    @AfterAll
//    public static void afterAll() throws IOException {
//        //server.shutdown();
//        app.stop();
//    }

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
        void testIndex() throws SQLException {
            var url = new Url("https://habr.com", Timestamp.valueOf("2022-09-08 21:16:28.105"));
            UrlRepository.save(url);
            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains("https://habr.com");
            assertThat(body).contains("Previous");
            assertThat(body).contains("Next");
        }

//        @Test
//        void testShow() {
//            HttpResponse<String> response = Unirest
//                    .get(baseUrl + "/urls/1")
//                    .asString();
//            String body = response.getBody();
//
//            assertThat(response.getStatus()).isEqualTo(200);
//            assertThat(body).contains("https://habr.com");
//            assertThat(body).contains("200");
//            assertThat(body).contains("2023-09-08 22:00:28.105");
//        }
//
//
//        @Test
//        void testCreateNew() throws MalformedURLException {
//            String inputName = "https://www.google.com/search?q";
//            HttpResponse<String> responsePost = Unirest
//                    .post(baseUrl + "/urls")
//                    .field("url", inputName)
//                    .asEmpty();
//
//            assertThat(responsePost.getStatus()).isEqualTo(302);
//            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");
//
//            HttpResponse<String> response = Unirest
//                    .get(baseUrl + "/urls")
//                    .asString();
//            String body = response.getBody();
//
//            URL inputUrl = new URL(inputName);
//            String normalizedName = inputUrl.getProtocol() + "://" + inputUrl.getAuthority();
//
//            assertThat(response.getStatus()).isEqualTo(200);
//            assertThat(body).contains(normalizedName);
//            assertThat(body).contains("Страница успешно добавлена");
//
//        }
//
//        @Test
//        void testCreateExisting() {
//            String inputName = "https://habr.com";
//            HttpResponse<String> responsePost = Unirest
//                    .post(baseUrl + "/urls")
//                    .field("url", inputName)
//                    .asString();
//
//            assertThat(responsePost.getStatus()).isEqualTo(302);
//            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");
//
//            String body = responsePost.getBody();
//            assertThat(body.contains("Страница уже существует"));
//        }
//
//        @Test
//        void testCreateNonValid() {
//            String inputName = "asdfg";
//            HttpResponse<String> responsePost = Unirest
//                    .post(baseUrl + "/urls")
//                    .field("url", inputName)
//                    .asString();
//
//            assertThat(responsePost.getStatus()).isEqualTo(200);
//            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("");
//
//            String body = responsePost.getBody();
//            assertThat(body).contains("Некорректный URL");
//        }

//        @Test
//        void testcheckUrl() throws IOException {
//
//            String testUrl = server.url("/").toString();
//
//            HttpResponse response = Unirest
//                    .post(baseUrl + "/urls/")
//                    .field("url", testUrl)
//                    .asEmpty();
//
//            Url url = new QUrl()
//                    .name.equalTo(testUrl.substring(0, testUrl.length() - 1))
//                    .findOne();
//
//            assertThat(url).isNotNull();
//
//            HttpResponse<String> responsePost = Unirest
//                    .post(baseUrl + "/urls/" + url.getId() + "/checks")
//                    .asString();
//
//            String body = responsePost.getBody();
//
//            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls/" + url.getId());
//            assertThat(body.contains("Страница успешно проверена"));
//
//            String body1 = Unirest
//                    .get(baseUrl + "/urls/" + url.getId())
//                    .asString()
//                    .getBody();
//
//            assertThat(body1).contains("200");
//            assertThat(body1).contains("title text");
//            assertThat(body1).contains("description text");
//            assertThat(body1).contains("h1 text");
//
//        }
    }
}

