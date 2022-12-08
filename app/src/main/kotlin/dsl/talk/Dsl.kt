package dsl.talk

@DslMarker
annotation class HtmlDsl

fun main() {

    val page =
            html {
                head { title ("My Page") }
                body {
                    p {
                        text("Hello, ")
                        b {
                            text("world")
                        }
                    }
                }
            }

    println(page)

}

@HtmlDsl
class HtmlBuilder {

    private var head: String = ""
    private var body: String = ""

    fun head(init: HeadBuilder.() -> Unit) {
        this.head = HeadBuilder().apply(init).toString()
    }

    fun body(init: BodyBuilder.() -> Unit) {
        this.body = BodyBuilder().apply(init).toString()
    }

    override fun toString() = """
        <html>
            $head
            $body
        </html>
        """.trimIndent()
}

@HtmlDsl
class HeadBuilder {

    var title: String = ""

    fun title(text: String) {
        this.title = text
    }

    override fun toString() = "<head>$title</head>"
}

@HtmlDsl
class BodyBuilder {

    var body: String = ""

    fun p(init: PBuilder.() -> Unit) {
        this.body += PBuilder().apply(init)
    }

    override fun toString() = "<body>$body</body>"

}

@HtmlDsl
class PBuilder {

    var text: String = ""

    fun text(content: String) {
        text += content
    }

    fun b(init: BBuilder.() -> Unit) {
        this.text += BBuilder().apply(init)
    }

    override fun toString() = "<p>$text</p>"

}

@HtmlDsl
class BBuilder {

    var text: String = ""

    fun text(content: String) {
        text += content
    }

    override fun toString() = "<b>$text</b>"

}

fun html(content: HtmlBuilder.() -> Unit): HtmlBuilder {
    return HtmlBuilder().apply(content)
}

