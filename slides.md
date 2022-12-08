---
marp: true
theme: default
paginate: true
style: |
  .columns {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 1rem;
  }

---

# Domain Specific Language (DSL)
## In Kotlin

---

> A Domain-Specific Language (DSL) is a computer language that's targeted to a 
> particular kind of problem, rather than a general purpose language that's aimed 
> at any kind of software problem.
> 
> *-Martin Fowler*

---

# DSL

 - Language for a particular problem or domain
 - External vs Internal
 - Configuration
 - Business rules
 - Fluent Api

<!--
Hva er en DSL?
 - Språk for et spesefikt problem eller domene
 - Motsetning til Generelt programeringsspråk
 - Kan vøre et språk for å beskrive konfigurasjon eller
   forretningsregler for et større system.
 - Skilles ofte mellom Interne og Eksterne
 - Externe krever en full parser og interpreter eller kompilator
 - Må bygges verktøy rundt som ide eller syntax highlightning
 - Interne utnytter syntax i et vertspråk
 - Får automatisk vertøystøtte, syntax highlithing, code completion, etc.
 - Mye mindre arbeid å lage en å utvikle et eksternt språk
-->

# Why make a DSL
 - Easier to change
 - Configuration as code
 - Can be written by non-programmers

<!--
Hvorfor?
 - Kan gjøre spesielle deler av koden mer modifiserbar
 - lettere å skrive
 - lettere å legge til nye "regler"
 - lettere å lese
 - deklerativ
 - Løftet om DSL for forettningssiden
 - Kan gjøre endringer runtime?
 - Kan la kunde tilpasse deler av koden uten å ha tilgang til hele
-->

---

# Some External DSL's

- Sql
- PlantUml
- Html
- CSS

<!--
 - Sql er ikke generelt programmeringspråk, selv om jeg har sett
   at noen løser advent of code oppgaver med det.
-->

---

# Some Internal DSL's

- Gradle with Kotlin
- Test frameworks (Kotest)
- Mock frameworks (Mockk)
- Ktor (Routing / Html / Css)
- Jetpack Compose UI

<!--
-->

---

# Why Kotlin?

 - Extension Functions
 - Lambda syntax
 - Infix functions
 - Named arguments
 - Operator overloading

<!--
 - fleksibel syntax, som kan tilpasses på forskjellige måter
-->

---

# Example: Gradle

```kotlin
plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.1"
}

group = "com.example"
version = "0.0.1"
```

<!--
 - plugins -> funksjonskall som tar en lambda (med receiver)
 - application -> property getter på this (som legger til en dependency!)
 - version -> infix funksjon
 - alt er gyldig kotlin kode
-->
---

# Named arguments

## Example: Ktor Html DSL

```kotlin
head {
    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
}
```

<!--
- kan ikke hindre possisjonsparameter
- kan dermed rote med rekkefølgen om man ikke er forsiktig
-->

---

# Infix function

```kotlin
infix fun String.repeated (n: Int) =
        this.repeat(n)

val string = "foo" repeated 10      // "foo".repeated(10)

string shouldStartWith "foo" 
```
<!--
- som vi så i gradle dsl'en
- mye brukt i testrammeverk assertions
- kan også kalles som prefix funksjoner (syntaktisk sukker)
-->

---

# Operator overloading 

```kotlin
operator fun String.times(n: Int) =
        this.repeated(n)

val s = "foo" * 10                // "foo".times(10)
```

<!--
- kan overloade forskjellige innebygde operatorer
- også her kan vi se at det er syntaktisk sukker for prefix funksjoner
- men kan gjøre dsl / kode enklere å lese
-->
---

# Extension Functions

```kotlin
fun String.toUUID() = UUID.fromString(this)

val String.uuid get() = UUID.fromString(this)
```

in Use:
```kotlin
val uuid = "e49c499b-d9d0-46fb-8b35-a3d3ecf89569".toUUID()

val uuid2 = "e49c499b-d9d0-46fb-8b35-a3d3ecf89569".uuid
```

<!--
- legg til funksjonalitet på innebygde klasser, eller egne klasser
- getter syntax
-->

---

# Real example
## Ktor CSS DSL

```kotlin
body {
  backgroundColor = Color.darkBlue
  margin(1.5.em)
}
```

<!--
- eksempel fra css ktor dsl
- finnes tilsvarende for pixel og andre enheter i css
- økt typesikkerhet fremfor å ta inn kun en primitiv type
- mer lesbart en en som tar verdi og enhet som to parameter
-->

---

# Lambda Syntax

```kotlin
// transform: (T) -> CharSequence
val s = (1 .. 10).joinToString("/", transform = { i -> "($i)" })

val s = (1 .. 10).joinToString("/") { "<$it>" }
```

<!--
- enkel lambda syntaks.
- kan bruke implisitt åparameter it om det kun er en parameter
- lambda som siste parameter i et funksjonskall kan flyttes utenfor parantesr
- om lambda eneste parameter kan paranteser utelates helt
- passer veldig bra til dsl hvor ting kan nøstes med krøllparanteser
-->

---

# Lambda With receiver

```kotlin
// A.(B) -> C
val sum: Int.(Int) -> Int = { other -> this + other }

var x = 10.sum(20) // x = 30
```

<!--
- videre fra enkel lambda,
- setter konteksten lambda body utføres i som this
-->

---

```kotlin
data class Counter(var n: Int) {
  fun doubleIt() { n *= 2 }
}

val y = Counter(0)

fun withCounter(counter: Counter, f: Counter.() -> Unit) {
  counter.apply(f)
}

withCounter(y) {
  n += 10
  doubleIt()
}

// y.n = 20
```

<!--
blir nyttig når en slik lambda tas som parameter til en funksjon
kan manipulere state via this
gi tilgangen til egne funksjoner i denne konteksten
-->

---

# Overloading .invoke()

```kotlin
operator fun StringBuilder.invoke(body: StringBuilder.() -> Unit) {
    this.apply(body)
}

val aString = StringBuilder()

aString {
    append("Hello, ")
    append("World!")
}
```

<!--
- tilbake til operator overloading
- kan overloade .invoke, og altså
  kalle på et objekt som om det var en funksjon
-->

---
# A Simple Html DSL

```kotlin
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
```

<!--
lett å se strukturen
kan minne om json
-->

---

```kotlin
fun html(content: HtmlBuilder.() -> Unit): HtmlBuilder {
  return HtmlBuilder().apply(content)
}

```

<!--
- inngangsfunksjonen til dsl'en.
- evaluerer en body (content) i contexten av HtmlBuilder klassen
-->
---

```kotlin
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
```

<!--
Hvorfor @HtmlDsl?
ungå tilgang til yttre scope
bygger opp statt med head og body
head og body tar da nye lambdaer med en ny receiver, en for header
og en for body.
vi kan da styre hva som er lovlig i hver
-->

---

```kotlin
@HtmlDsl
class HeadBuilder {

    var title: String = ""

    fun title(text: String) {
        this.title = text
    }

    override fun toString() = "<head>$title</head>"
}
```

<!--
- i head er det kun å sette tittel
-->

---

```kotlin

@HtmlDsl
class BodyBuilder {

    var body: String = ""

    fun p(init: PBuilder.() -> Unit) {
        this.body += PBuilder().apply(init)
    }

    override fun toString() = "<body>$body</body>"

}
```

<!--
i body kan vi legge til paragrafer
(ja dsl'en er langt fra komplett, men prinsipper er det samme for å legg til flere ting
-->

---

```kotlin
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
```

<!--
I paragrafen kan vi legge til text eller noe som er wrappet i bold
-->

---

```kotlin
@HtmlDsl
class BBuilder {

    var text: String = ""

    fun text(content: String) {
        text += content
    }

    override fun toString() = "<b>$text</b>"

}
```

<!--
Og i bold kan vi legge til text
-->

---

```kotlin
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
```
<!--
andre har gjort dette som en komplett dsl for http
og for css, feks ktor, så vi trenger ikke gjøre det på nytt
men det hvis hvor enkelt man gan lage en syntaks for et gitt
formål
-->