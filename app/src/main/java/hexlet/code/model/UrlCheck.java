package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public final class UrlCheck {

    private long id;
    private int statusCode;
    private String h1;
    private String title;
    private String description;
    private long urlId;
    private Timestamp createdAt;

    public UrlCheck(int statusCode, String h1, String title, String description, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UrlCheck(int statusCode, String h1, String title, String description, long urlId, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.h1 = h1;
        this.title = title;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = createdAt;
    }

    public UrlCheck(long id, int statusCode, String h1, String title, String description, Timestamp createdAt) {
        this.id = id;
        this.statusCode = statusCode;
        this.h1 = h1;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }
}
