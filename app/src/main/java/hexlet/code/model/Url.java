package hexlet.code.model;

import hexlet.code.repository.UrlCheckRepository;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public final class Url {

    private long id;

    private String name;

    private Timestamp createdAt;

    private List<UrlCheck> urlCheckList;


    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public void setUrlCheckList() throws SQLException {
        this.urlCheckList = UrlCheckRepository.getEntities(this.getId());
    }
}
