package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/vocabulary")
public class VocabularyServiceRestAdapter {

    private final VocabularyService VOCABULARY_SERVICE;

    @Inject
    public VocabularyServiceRestAdapter(VocabularyService vocabularyService) {
        VOCABULARY_SERVICE = vocabularyService;
    }

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here...");
        System.out.println("Vocabulary Service is" + (VOCABULARY_SERVICE == null ? "n't" : "") + " initialized");
        return "Hello REST world!";
    }
}
