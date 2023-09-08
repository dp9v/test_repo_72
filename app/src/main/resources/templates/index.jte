@import hexlet.code.dto.BasePage
@param BasePage page

@template.layout.page(
    page = page,
header = @`
    <section>
        <h2>Анализатор страниц</h2>
    </section>
`,
content = @`
    <section>
        <form action="/urls" method="post">
            <div class="mb-3">
                <label for="url" class="form-label">Ссылка</label>
                <input type="text" class="form-control" id="url" name="url">
            </div>
            <button type="submit" class="btn btn-primary">Проверить</button>
        </form>
    </section>
`
)