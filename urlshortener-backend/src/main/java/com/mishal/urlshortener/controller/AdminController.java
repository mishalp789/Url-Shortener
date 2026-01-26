package com.mishal.urlshortener.controller;

import com.mishal.urlshortener.service.UrlAdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/urls")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UrlAdminService service;

    public AdminController(UrlAdminService service){
        this.service = service;
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException{
        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=short_urls.csv"
        );
        PrintWriter writer = response.getWriter();
        service.writeCsv(writer);
        writer.flush();
    }

}
