@import gg.jte.Content
@import hexlet.code.dto.UrlCheckPage
@import hexlet.code.dto.UrlPage

@param UrlPage page
@param UrlCheckPage urlChecks

@template.layout.page(
    page = page,
    header = null,
content = @`
    <section>
        <div class="container-lg mt-5">
            <h2>Запись о сайте</h2>

            <table class="table table-bordered table-hover mt-3">
                <tr>
                    <td>ID</td>
                    <td>${page.getUrl().getId()}</td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>${page.getUrl().getName()}</td>
                </tr>
                <tr>
                    <td>Дата создания</td>
                    <td>${page.getUrl().getCreatedAt().toString()}</td>
                </tr>
            </table>

            <h2 class="mt-5">Проверки</h2>
            <form action="/urls/${page.getUrl().getId()}/checks?id=${page.getUrl().getId()}" method="post">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th class="col-3">title</th>
                    <th class="col-3">h1</th>
                    <th class="col-3">description</th>
                    <th class="col-2">Дата проверки</th>
                </tr>
                </thead>
                @if(urlChecks.getUrlChecks() != null)
                @for(var check : urlChecks.getUrlChecks())
                <tbody>
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${check.getTitle()}</td>
                        <td>${check.getH1()}</td>
                        <td>${check.getDescription()}</td>
                        <td>${check.getCreatedAt().toString()}</td>
                    </tr>
                </tbody>
                @endfor
                @endif
            </table>
`
)