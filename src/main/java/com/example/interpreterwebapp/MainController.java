package com.example.interpreterwebapp;

import interpreter.lexer.Lexer;
import interpreter.parser.Parser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.UnknownHostException;

@Controller
public class MainController {

    @GetMapping
    public String getMain(@RequestParam(required = false, name = "src_string") String srcString, Model model) {
        String resString = "";

        Lexer lexer = new Lexer();
        Parser parser = new Parser(lexer);

        if (srcString != null) {
            try {
                resString = parser.program(srcString);
            } catch (Error e) {
                resString = e.toString();
            }
        } else {
            srcString = "";
        }

        model.addAttribute("src_string", srcString);
        model.addAttribute("res_string", resString);
        return "main";
    }
}

