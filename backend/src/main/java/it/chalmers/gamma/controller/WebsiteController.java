package it.chalmers.gamma.controller;

import it.chalmers.gamma.db.entity.Website;
import it.chalmers.gamma.response.website.GetAllWebsitesResponse;
import it.chalmers.gamma.response.website.GetWebsiteResponse;
import it.chalmers.gamma.response.website.WebsiteNotFoundResponse;
import it.chalmers.gamma.service.WebsiteService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/websites")
public class WebsiteController {

    private final WebsiteService websiteService;

    public WebsiteController(WebsiteService websiteService) {
        this.websiteService = websiteService;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Website> getWebsite(@PathVariable("id") String id) {
        Website website = this.websiteService.getWebsiteById(id);
        if (website == null) {
            throw new WebsiteNotFoundResponse();
        }
        return new GetWebsiteResponse(website);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Website>> getAllWebsites() {
        return new GetAllWebsitesResponse(this.websiteService.getAllWebsites());
    }
}
