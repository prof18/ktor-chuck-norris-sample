# ktor-chuck-norris-sample

ktor-chuck-norris-sample is your new project powered by [Ktor](http://ktor.io) framework.

<img src="https://repository-images.githubusercontent.com/40136600/f3f5fd00-c59e-11e9-8284-cb297d193133" alt="Ktor" width="100" style="max-width:20%;">

Company website: example.com Ktor Version: 1.5.2 Kotlin Version: 1.4.10
BuildSystem: [Gradle with Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)

# Ktor Documentation

Ktor is a framework for quickly creating web applications in Kotlin with minimal effort.

* Ktor project's [Github](https://github.com/ktorio/ktor/blob/master/README.md)
* Getting started with [Gradle](http://ktor.io/quickstart/gradle.html)
* Getting started with [Maven](http://ktor.io/quickstart/maven.html)
* Getting started with [IDEA](http://ktor.io/quickstart/intellij-idea.html)

Selected Features:

* [Routing](#routing-documentation-jetbrainshttpswwwjetbrainscom)
* [ContentNegotiation](#contentnegotiation-documentation-jetbrainshttpswwwjetbrainscom)
* [kotlinx.serialization](#kotlinx.serialization-documentation-jetbrainshttpswwwjetbrainscom)
* [Static Content](#static-content-documentation-jetbrainshttpswwwjetbrainscom)

## Routing Documentation ([JetBrains](https://www.jetbrains.com))

Allows to define structured routes and associated handlers.

### Description

Routing is a feature that is installed into an Application to simplify and structure page request handling. This page
explains the routing feature. Extracting information about a request, and generating valid responses inside a route, is
described on the requests and responses pages.

```application.install(Routing) {
    get("/") {
        call.respondText("Hello, World!")
    }
    get("/bye") {
        call.respondText("Good bye, World!")
    }

```

`get`, `post`, `put`, `delete`, `head` and `options` functions are convenience shortcuts to a flexible and powerful
routing system. In particular, get is an alias to `route(HttpMethod.Get, path) { handle(body) }`, where body is a lambda
passed to the get function.

### Usage

## Routing Tree

Routing is organized in a tree with a recursive matching system that is capable of handling quite complex rules for
request processing. The Tree is built with nodes and selectors. The Node contains handlers and interceptors, and the
selector is attached to an arc which connects another node. If selector matches current routing evaluation context, the
algorithm goes down to the node associated with that selector.

Routing is built using a DSL in a nested manner:

```kotlin
route("a") { // matches first segment with the value "a"
  route("b") { // matches second segment with the value "b"
     get {…} // matches GET verb, and installs a handler
     post {…} // matches POST verb, and installs a handler
  }
}
```

```kotlin
method(HttpMethod.Get) { // matches GET verb
   route("a") { // matches first segment with the value "a"
      route("b") { // matches second segment with the value "b"
         handle { … } // installs handler
      }
   }
}
```kotlin
route resolution algorithms go through nodes recursively discarding subtrees where selector didn't match.

Builder functions:
* `route(path)` – adds path segments matcher(s), see below about paths
* `method(verb)` – adds HTTP method matcher.
* `param(name, value)` – adds matcher for a specific value of the query parameter
* `param(name)` – adds matcher that checks for the existence of a query parameter and captures its value
* `optionalParam(name)` – adds matcher that captures the value of a query parameter if it exists
* `header(name, value)` – adds matcher that for a specific value of HTTP header, see below about quality

## Path
Building routing tree by hand would be very inconvenient. Thus there is `route` function that covers most of the use cases in a simple way, using path.

`route` function (and respective HTTP verb aliases) receives a `path` as a parameter which is processed to build routing tree. First, it is split into path segments by the `/` delimiter. Each segment generates a nested routing node.

These two variants are equivalent:

```kotlin
route("/foo/bar") { … } // (1)

route("/foo") {
   route("bar") { … } // (2)
}
```

### Parameters

Path can also contain parameters that match specific path segment and capture its value into `parameters` properties of
an application call:

```kotlin
get("/user/{login}") {
   val login = call.parameters["login"]
}
```

When user agent requests `/user/john` using `GET` method, this route is matched and `parameters` property will
have `"login"` key with value `"john"`.

### Optional, Wildcard, Tailcard

Parameters and path segments can be optional or capture entire remainder of URI.

* `{param?}` –- optional path segment, if it exists it's captured in the parameter
* `*` –- wildcard, any segment will match, but shouldn't be missing
* `{...}` –- tailcard, matches all the rest of the URI, should be last. Can be empty.
* `{param...}` –- captured tailcard, matches all the rest of the URI and puts multiple values for each path segment
  into `parameters` using `param` as key. Use `call.parameters.getAll("param")` to get all values.

Examples:

```kotlin
get("/user/{login}/{fullname?}") { … }
get("/resources/{path...}") { … }
```

## Quality

It is not unlikely that several routes can match to the same HTTP request.

One example is matching on the `Accept` HTTP header which can have multiple values with specified priority (quality).

```kotlin
accept(ContentType.Text.Plain) { … }
accept(ContentType.Text.Html) { … }
```

The routing matching algorithm not only checks if a particular HTTP request matches a specific path in a routing tree,
but it also calculates the quality of the match and selects the routing node with the best quality. Given the routes
above, which match on the Accept header, and given the request header `Accept: text/plain; q=0.5, text/html` will
match `text/html` because the quality factor in the HTTP header indicates a lower quality fortext/plain (default is 1.0)
.

The Header `Accept: text/plain, text/*` will match `text/plain`. Wildcard matches are considered less specific than
direct matches. Therefore the routing matching algorithm will consider them to have a lower quality.

Another example is making short URLs to named entities, e.g. users, and still being able to prefer specific pages
like `"settings"`. An example would be

* `https://twitter.com/kotlin` -– displays user `"kotlin"`
* `https://twitter.com/settings` -- displays settings page

This can be implemented like this:

```kotlin
get("/{user}") { … }
get("/settings") { … }
```

The parameter is considered to have a lower quality than a constant string, so that even if `/settings` matches both,
the second route will be selected.

### Options

No options()

## ContentNegotiation Documentation ([JetBrains](https://www.jetbrains.com))

Provides automatic content conversion according to Content-Type and Accept headers.

### Description

The `ContentNegotiation` feature serves two primary purposes:

* Negotiating media types between the client and server. For this, it uses the `Accept` and `Content-Type` headers.
* Serializing/deserializing the content in the specific format, which is provided by either the
  built-in `kotlinx.serialization` library or external ones, such as `Gson` and `Jackson`, amongst others.

### Usage

## Installation

To install the `ContentNegotiation` feature, pass it to the `install` function in the application initialization code.
This can be the `main` function ...

```kotlin
import io.ktor.features.*
// ...
fun Application.main() {
  install(ContentNegotiation)
  // ...
}
```

... or a specified `module`:

```kotlin
import io.ktor.features.*
// ...
fun Application.module() {
    install(ContentNegotiation)
    // ...
}
```

## Register a Converter

To register a converter for a specified `Content-Type`, you need to call the register method. In the example below, two
custom converters are registered to deserialize `application/json` and `application/xml` data:

```kotlin
install(ContentNegotiation) {
    register(ContentType.Application.Json, CustomJsonConverter())
    register(ContentType.Application.Xml, CustomXmlConverter())
}
```

### Built-in Converters

Ktor provides the set of built-in converters for handing various content types without writing your own logic:

* `Gson` for JSON

* `Jackson` for JSON

* `kotlinx.serialization` for JSON, Protobuf, CBOR, and so on

See a corresponding topic to learn how to install the required dependencies, register, and configure a converter.

## Receive and Send Data

### Create a Data Class

To deserialize received data into an object, you need to create a data class, for example:

```kotlin
data class Customer(val id: Int, val firstName: String, val lastName: String)
```

If you use `kotlinx.serialization`, make sure that this class has the `@Serializable` annotation:

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)
```

### Receive Data

To receive and convert a content for a request, call the `receive` method that accepts a data class as a parameter:

```kotlin
post("/customer") {
    val customer = call.receive<Customer>()
}
```

The `Content-Type` of the request will be used to choose a converter for processing the request. The example below shows
a sample HTTP client request containing JSON data that will be converted to a `Customer` object on the server side:

```kotlin
post http://0.0.0.0:8080/customer
Content-Type: application/json

{
  "id": 1,
  "firstName" : "Jet",
  "lastName": "Brains"
}
```

### Send Data

To pass a data object in a response, you can use the `respond` method:

```kotlin
post("/customer") {
    call.respond(Customer(1, "Jet", "Brains"))
}
```

In this case, Ktor uses the `Accept` header to choose the required converter.

## Implement a Custom Converter

In Ktor, you can write your own converter for serializing/deserializing data. To do this, you need to implement
the `ContentConverter` interface:

```kotlin
interface ContentConverter {
    suspend fun convertForSend(context: PipelineContext<Any, ApplicationCall>, contentType: ContentType, value: Any): Any?
    suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any?
}
```

Take a look at
the [GsonConverter](https://github.com/ktorio/ktor/blob/master/ktor-features/ktor-gson/jvm/src/io/ktor/gson/GsonSupport.kt)
class as an implementation example.

### Options

No options()

## kotlinx.serialization Documentation ([JetBrains](https://www.jetbrains.com))

Handles JSON serialization using kotlinx.serialization library

### Description

ContentNegotiation allows you to use content converters provided by the `kotlinx.serialization` library. This library
supports `JSON`, `CBOR`, `ProtoBuf`, and other formats.

### Usage

## Register the JSON Converter

To register the JSON converter in your application, call the `json` method:

```kotlin
import io.ktor.serialization.*

install(ContentNegotiation) {
    json()
}
```

Inside the `json` method, you can access
the [JsonBuilder](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-json/kotlinx-serialization-json/kotlinx.serialization.json/-json-builder/index.html)
API, for example:

```kotlin
install(ContentNegotiation) {
    json(Json {
        prettyPrint = true
        isLenient = true
        // ...
    })
}
```

## Register an Arbitrary Converter

To register an arbitrary converter from the kotlinx.serialization library (such as Protobuf or CBOR), call
the `serialization` method and pass two parameters:

* The required `ContentType` value.
* An object of the class implementing the required encoder/decoder.

For example, you can register
the [Cbor](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-cbor/kotlinx-serialization-cbor/kotlinx.serialization.cbor/-cbor/index.html)
converter in the following way:

```kotlin
install(ContentNegotiation) {
    serialization(ContentType.Application.Cbor, Cbor.Default)
}
```

### Options

No options()

## Static Content Documentation ([JetBrains](https://www.jetbrains.com))

Serves static files from defined locations.

### Description

Ktor has built-in support for serving static content. This can come in useful when you want to serve style sheets,
scripts, images, etc.

### Usage

Whether we're creating a website or an HTTP endpoint, many applications need to serve files (such as stylesheets,
scripts, images, etc.), While it is certainly possible with Ktor to load the contents of a file and send it in response
to a request, given this is such a common functionality, Ktor simplifies the entire process for us with the `static`
Feature.

The first step in defining a static route is to define the path under which the content should be served. For instance,
if you want everything under the route `assets` to be treated as static content, you need to add the following to your
application setup:

```kotlin
routing {
    static("assets") {

    }
}
```

The next step is to define where we want the content to be served from, which can be either

* [A folder](Serving_Static_Content.md#folders)
* [Embedded application resources](Serving_Static_Content.md#embedded-application-resources)

## Folders

In order to serve the contents from a folder, we need to specify the folder name using the `files` function. The path
is **always relative to the application path**:

```kotlin
routing {
    static("assets") {
      files("css")
    }
}
```

`files("css")` would then allow for any file located in the folder `css` to be served as static content under the given
URL pattern, which in this case is `assets`. This means that a request to

`/assets/stylesheet.css` would serve the file `/css/stylesheet.css`

We can have as many folders as we like under a single path. For instance the following would also be valid:

```kotlin
routing {
    static("assets") {
      files("css")
      files("js")
    }
}
```

### Serving individual files

In addition to serving files from folders, we can also specify individuals files we would like to make available by
using the `file` function. Optionally this takes a second argument which allows us to map a physical filename to a
virtual one:

```kotlin
routing {
    static("static") {
        file("image.png")
        file("random.txt", "image.png")
    }
}
```

### Defining a default file

For a specific path, we can also define the default file to be loaded:

```kotlin
routing {
    static("assets") {
      files("css")
      default("index.html")
    }
}
```

which would cause a request to `/assets/` to serve `index.html`.

### Changing the default root folder

Ktor also provides us the ability to specify a different root folder from where contents is served. This is useful for
instance if we want to dynamically define where contents should be served from, or even use absolute paths.

We can do this by setting the value of the `staticRootFolder` property:

```kotlin
static("docs") {
    staticRootFolder = File("/system/folder/docs")
    files("public")
}
```

which would then map any request to `/docs` to the physical folder `/system/folder/docs/public`.

## Embedded Application Resources

We can embed content as resources in our applications and serve these using the `resource` and `resources` functions:

```kotlin
static("assets") {
    resources("css")
}
```

`resources("css")` would then allow for any file located under the resource `css` to be served as static content under
the given URL pattern, which in this case is `assets`. This means that a request to

`/assets/stylesheet.cs` would serve the file `/css/stylesheet.cs`

We can have as many resources as we like under a single path. For instance the following would also be valid:

```kotlin
routing {
    static("assets") {
      resources("css")
      resources("js")
    }
}
```

### Serving individual resources

In addition to serving files from resources, we can also specify individuals files we would like to make available by
using the `resource` function. Optionally this takes a second argument which allows us to map a physical filename to a
virtual one:

```kotlin
routing {
    static("static") {
        resource("image.png")
        resource("random.txt", "image.png")
    }
}
```

### Defining a default resource

For a specific path, we can also define the default file to be loaded:

```kotlin
routing {
    static("assets") {
      resources("css")
      defaultResource("index.html")
    }
}
```

### Changing the default resource package

Ktor also provides us the ability to specify a different base resource package from where contents is served.

We can do this by setting the value of the `staticBasePackage` property:

```kotlin
static("docs") {
    staticBasePackage = File("/system/folder/docs")
    files("public")
}
```

## Sub-routes

If we want to have sub-routes, we can nest `static` functions:

```kotlin
static("assets") {
    files("css")
    static("themes") {
        files("data")
    }
}
```

allowing for `/assets/themes` to load files from the `/data`

## Handling errors

If the request content is not found, Ktor will automatically respond with a `404 Not Found` HTTP status code. For more
information about personalising error handling, please see [status pages](status_pages.md)

## Customising Content Type header

Ktor automatically looks up the content type of a file based on its extension and sets the appropriate `Content-Type`
header. The list of supported MIME types is defined in the `mimelist.csv` resource file located in `ktor-server-core`
artifact.

### Options

No options()

# Reporting Issues / Support

Please use [our issue tracker](https://youtrack.jetbrains.com/issues/KTOR) for filing feature requests and bugs. If
you'd like to ask a question, we recommmend [StackOverflow](https://stackoverflow.com/questions/tagged/ktor) where
members of the team monitor frequently.

There is also community support on the [Kotlin Slack Ktor channel](https://app.slack.com/client/T09229ZC6/C0A974TJ9)

# Reporting Security Vulnerabilities

If you find a security vulnerability in Ktor, we kindly request that you reach out to the JetBrains security team via
our [responsible disclosure process](https://www.jetbrains.com/legal/terms/responsible-disclosure.html).

# Contributing

Please see [the contribution guide](CONTRIBUTING.md) and the [Code of conduct](CODE_OF_CONDUCT.md) before contributing.

TODO: contribution of features guide (link)