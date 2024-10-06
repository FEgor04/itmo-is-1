package com.jellyone.lab1.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class SpaController {
        @RequestMapping(value = ["/{path:[^\\.]*}"])
        fun redirect(): String {
            return "forward:/"
        }
}