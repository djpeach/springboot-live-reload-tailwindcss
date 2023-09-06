# SpringBoot Live Reloading with TailwindCSS

## Context

This is all based on a setup running via the default runner in IntelliJ, which uses the `bootRun`
Gradle task, as well as some quality of life features such as refreshing classes and resources when
focus is lost from the IDE. In place of this, some other "sync" Gradle task that is run as a
continuous build could be used, which takes the resources as inputs and the appropriate locations as
outputs.

## Plan

With this setup, SpringBoot will automatically refresh the built resources when the focus is
switched from the IDE. This is perfect when switching from the IDE to the browser to check `css`
or `html` changes. Using this functionality, we will also run a `--watch` script using
the `tailwindcss` cli through `npx` in order to update the output styles whenever changes are made
to `.css` or `.html` files. These changes will then be reflected in the built resources served by
SpringBoot when they are refreshed by switching the focus back to the browser.

Optionally, we can also use the live reload server, but I find this is actually slower than just
hitting reload. Although there are benefits in some situations where a reload would slow us down.

## Setup

### SpringBoot

Use the initializer to create a project with Spring Boot Dev Tools, Spring Web, and Thymeleaf.

### Resources

Create a `css` directory as a child of `resources/static`. Add a `input.css` and `output.css` here.

Create a `index.html` file to `resources/templates`.

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title th:text="${title}">Hello World</title>
</head>
<body>
  <h1 class="underline text-red-400 text-4xl" th:text="${title}">Hello World</h1>
</body>
</html>
```

### Tailwind

From [Tailwind Installation](https://tailwindcss.com/docs/installation)

Install `tailwind`:

```shell
npm i -D tailwind
```

Add a script to the created `package.json` for running the tailwind cli utility.

```json
{
  "scripts": {
    "tailwind": "npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --watch"
  }
}
```

Create a `tailwind.config.js` file. Point it's content toward any templates in `src/main/resources/templates`.

```js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/templates/**/*.html"],
}
```

Fill in input.css

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

Test out the script by running it from the IDE and seeing the `output.css` file created alongside `input.css`. 

### Controller
Create a controller to serve the index template and optionally use Spring Web MVC to add properties to the model and consume them in the template with Thymeleaf.

```kotlin
@SpringBootApplication
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}

@Controller
@RequestMapping("")
class RootController {
  @GetMapping
  fun index(model: Model): String {
    model["title"] = "Spring Boot Live Reload!"
    return "index"
  }
}
```

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">

  <link rel="stylesheet" href="../static/css/output.css" th:href="@{/css/output.css}">

  <title th:text="${title}">Hello World</title>
</head>
<body>
  <h1 class="underline text-red-400 text-4xl" th:text="${title}">Hello World</h1>
</body>
</html>
```

### Testing it all
Run the application from the IDE, and see the build files reflecting what was in the source files. Hit http://localhost:8080/ and see the template served. Start changing the tailwind classes, html content, or model properties in the controller to see tailwindcss cli, live reload, or SpringBoot auto restart in action.


### Live Reload Server
SpringBoot automatically runs with a live reload server. Add it to your template, use a plugin, or whatever other option you want.

```html
<script src="http://localhost:35729/livereload.js"></script>
```
