package hexlet.code.dto;

import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UrlCheckPage extends BasePage {
    private List<UrlCheck> urlChecks;
}
