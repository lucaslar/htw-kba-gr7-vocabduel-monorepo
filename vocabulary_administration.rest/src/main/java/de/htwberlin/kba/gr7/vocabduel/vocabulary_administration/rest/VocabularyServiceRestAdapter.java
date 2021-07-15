package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/vocabulary")
public class VocabularyServiceRestAdapter {

    @Autowired
    private VocabularyService vocabularyService;

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here..." + (vocabularyService == null));
        return "Hello REST world!";
    }
}
