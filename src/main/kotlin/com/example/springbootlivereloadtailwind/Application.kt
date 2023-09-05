package com.example.springbootlivereloadtailwind

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

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
