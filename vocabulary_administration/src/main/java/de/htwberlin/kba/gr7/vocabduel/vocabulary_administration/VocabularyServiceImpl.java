package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage.*;

@Service
public class VocabularyServiceImpl implements VocabularyService {

    private HashMap<String, SupportedLanguage> languageMapping;
    private HashMap<SupportedLanguage, String[]> reverseLanguageMapping;

    private final Pattern THREE_BRACKETS_PATTERN = Pattern.compile("\\{\\{\\{(.*?)}}}");
    private final Pattern ONE_BRACKET_PATTERN = Pattern.compile("\\{(.*?)}");

    private final EntityManager ENTITY_MANAGER;

    VocabularyServiceImpl() {
        initializeLangMapping();

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VocabduelJPA_PU_vocabulary");
        ENTITY_MANAGER = emf.createEntityManager();
    }

    @Override
    public int importGnuVocableList(String gnuContent, User triggeringUser) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException, UnknownLanguagesException, InvalidVocableListException {
        final List<String> lines = Arrays.stream(gnuContent.split("\n")).collect(Collectors.toList());
        if (lines.size() < 2) throw new IncompleteVocableListException("Not enough lines, expected at least two");

        final List<String> headline = parseGnuHeadlineData(lines.get(0));
        if (headline.size() != 4 || headline.stream().anyMatch(e -> e == null || e.isEmpty())) {
            throw new IncompleteVocableListException("Expected four non-empty/null elements in headline");
        }

        lines.remove(0);
        final SupportedLanguage langFrom = mapLanguageReference(headline.get(1));
        final SupportedLanguage langTo = mapLanguageReference(headline.get(2));
        final VocableUnit unit = getOrCreateLanguageUnit(headline.get(3), langFrom, langTo);
        final String listName = headline.get(0);

        if (unit.getVocableLists() == null) unit.setVocableLists(new ArrayList<>());
        else if (unit.getVocableLists().stream().anyMatch(vl -> vl.getTitle().equals(listName))) {
            throw new DataAlreadyExistsException("List named \"" + listName + "\" does already exist!");
        }

        final List<Vocable> vocables = parseGnuVocableData(lines);
        validateVocables(vocables);

        final VocableList list = new VocableList();
        list.setTitle(listName);
        list.setAuthor(triggeringUser);
        list.setVocables(parseGnuVocableData(lines));
        list.setTimestamp(new Date());
        unit.getVocableLists().add(list);

        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.persist(unit);
        ENTITY_MANAGER.getTransaction().commit();

        return 0;
    }

    @Override
    public int deleteVocableList(VocableList vocables, User triggeringUser) throws DifferentAuthorException {
        return 0;
    }

    @Override
    public VocableList getVocableListById(Long id) {
        ENTITY_MANAGER.getTransaction().begin();
        final VocableList vocableList = ENTITY_MANAGER.find(VocableList.class, id);
        ENTITY_MANAGER.getTransaction().commit();
        return vocableList;
    }

    @Override
    public List<VocableList> getVocableListsOfUser(User user) {
        List<VocableList> vocableLists = null;
        try {
            ENTITY_MANAGER.getTransaction().begin();
            vocableLists = (List<VocableList>) ENTITY_MANAGER
                    .createQuery("select vl from VocableList vl inner join vl.author a where a.id = :id")
                    .setParameter("id", user.getId())
                    .getResultList();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return vocableLists;
    }

    @Override
    public List<LanguageSet> getAllLanguageSets() {
        List<LanguageSet> languageSets = null;
        try {
            ENTITY_MANAGER.getTransaction().begin();
            languageSets = (List<LanguageSet>) ENTITY_MANAGER
                    .createQuery("from LanguageSet")
                    .getResultList();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return languageSets;
    }

    @Override
    public List<SupportedLanguage> getAllSupportedLanguages() {
        return new ArrayList<>(new HashSet<>(languageMapping.values()));
    }

    @Override
    public List<String> getSupportedLanguageReferences(SupportedLanguage language) {
        return Arrays.stream(reverseLanguageMapping.get(language)).collect(Collectors.toList());
    }

    private void initializeLangMapping() {
        reverseLanguageMapping = new HashMap<>();
        reverseLanguageMapping.put(AR, new String[]{"ar", "arabic", "arabisch", "العربية"});
        reverseLanguageMapping.put(CMN, new String[]{"cmn", "chinese", "chinesisch", "中国人"});
        reverseLanguageMapping.put(DE, new String[]{"de", "german", "deutsch"});
        reverseLanguageMapping.put(EN, new String[]{"en", "english", "englisch"});
        reverseLanguageMapping.put(ES, new String[]{"es", "spanish", "spanisch", "español"});
        reverseLanguageMapping.put(FR, new String[]{"fr", "french", "französisch", "français"});
        reverseLanguageMapping.put(HI, new String[]{"hi", "hindi", "हिंदी"});
        reverseLanguageMapping.put(IT, new String[]{"it", "italian", "italienisch", "italiano"});
        reverseLanguageMapping.put(JA, new String[]{"ja", "japanese", "japanisch", "日本語"});
        reverseLanguageMapping.put(KO, new String[]{"ko", "korean", "koreanisch", "한국어"});
        reverseLanguageMapping.put(PT, new String[]{"pt", "portuguese", "portugiesisch", "portugês"});
        reverseLanguageMapping.put(RU, new String[]{"ru", "russian", "russisch", "русский"});

        languageMapping = new HashMap<>();
        reverseLanguageMapping.keySet().forEach(key -> {
            for (final String l : reverseLanguageMapping.get(key)) languageMapping.put(l, key);
        });
    }

    private List<String> parseGnuHeadlineData(final String line) {
        final Matcher hlMatcher = THREE_BRACKETS_PATTERN.matcher(line);
        ArrayList<String> hlData = new ArrayList<>();
        while (hlMatcher.find()) hlData.add(hlMatcher.group(1).trim());
        return hlData;
    }

    private List<Vocable> parseGnuVocableData(final List<String> lines) throws InvalidVocableListException {
        final List<String[]> splitted = lines.stream()
                .map(l -> (l.charAt(0) == '#' ? l.substring(1) : l).split(":"))
                .collect(Collectors.toList());

        if (splitted.stream().anyMatch(s -> s.length != 2)) {
            throw new InvalidVocableListException("Invalid vocable format (must be split by colon character)");
        }

        List<Vocable> vocables = new ArrayList<>();
        for (final String[] vocAndTranslations : splitted) {
            final TranslationGroup vocable = getVocableFromString(vocAndTranslations[0]);
            final List<TranslationGroup> translations = getTranslationVocableTranslationsFromString(vocAndTranslations[1]);
            vocables.add(new Vocable(vocable, translations));
        }
        return vocables;
    }

    private TranslationGroup getVocableFromString(final String str) throws InvalidVocableListException {
        final Matcher vocMatcher = ONE_BRACKET_PATTERN.matcher(str);
        final List<String> vocData = new ArrayList<>();
        while (vocMatcher.find()) vocData.add(vocMatcher.group(1).trim());
        if (vocData.size() == 0) {
            throw new InvalidVocableListException("Invalid vocable data in learnt language (no match for " + ONE_BRACKET_PATTERN + "):\n" + str);
        }

        final TranslationGroup tg = new TranslationGroup();
        tg.setSynonyms(asSynonymList(vocData.get(0)));
        vocData.remove(0);
        if (vocData.size() > 0) tg.setExemplarySentencesOrAdditionalInfo(vocData);
        return tg;
    }

    private List<TranslationGroup> getTranslationVocableTranslationsFromString(final String str) throws InvalidVocableListException {
        final Matcher vocMatcher = ONE_BRACKET_PATTERN.matcher(str);

        final List<List<String>> vocData = new ArrayList<>();
        final Map<List<String>, List<String>> explanations = new HashMap<>();

        while (vocMatcher.find()) {
            if (vocMatcher.group(0).matches("\\{\\{\\{(.*?)}")) {
                final String exp = vocMatcher.group(1).substring(2).trim();
                if (!exp.isEmpty()) explanations.put(vocData.get(vocData.size() - 1), asSynonymList(exp));
            } else vocData.add(asSynonymList(vocMatcher.group(1).trim()));
        }

        if (vocData.size() == 0) {
            throw new InvalidVocableListException("Invalid vocable data in known language (no match for " + ONE_BRACKET_PATTERN + "):\n" + str);
        }

        return vocData.stream().map((synonyms) -> {
            final TranslationGroup tg = new TranslationGroup(synonyms);
            if (explanations.get(synonyms) != null) {
                tg.setExemplarySentencesOrAdditionalInfo(explanations.get(synonyms));
            }
            return tg;
        }).collect(Collectors.toList());
    }

    private List<String> asSynonymList(final String gnuVocable) {
        return Arrays.stream(gnuVocable.split("(?<!\\\\),\\s"))
                .map(v -> v.replace("\\,", ",").replace("\\;", ";").trim())
                .collect(Collectors.toList());
    }

    private SupportedLanguage mapLanguageReference(final String reference) throws UnknownLanguagesException {
        final SupportedLanguage lang = languageMapping.get(reference.toLowerCase());
        if (lang == null) throw new UnknownLanguagesException("Unknown or wrongly referred language: " + reference);
        else return lang;
    }

    private VocableUnit getOrCreateLanguageUnit(final String unitName, final SupportedLanguage from, final SupportedLanguage to) {
        final LanguageSet ls = getOrCreateLangaugeSet(from, to);
        VocableUnit unit = null;
        Optional<VocableUnit> foundUnit = null;
        if (ls.getVocableUnits() == null) {
            ls.setVocableUnits(new ArrayList<>());
        } else foundUnit = ls.getVocableUnits().stream().filter(u -> u.getTitle().equals(unitName)).findFirst();

        if (foundUnit != null && foundUnit.isPresent()) unit = foundUnit.get();
        else {
            unit = new VocableUnit(unitName);
            ls.getVocableUnits().add(unit);
            ENTITY_MANAGER.getTransaction().begin();
            ENTITY_MANAGER.persist(ls);
            ENTITY_MANAGER.getTransaction().commit();
        }

        return unit;
    }

    private LanguageSet getOrCreateLangaugeSet(final SupportedLanguage from, final SupportedLanguage to) {
        LanguageSet languageSet = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final String query = "from LanguageSet as l where l.learntLanguage like :learntLanguage and l.knownLanguage like :knownLanguage";
            languageSet = (LanguageSet) ENTITY_MANAGER
                    .createQuery(query)
                    .setParameter("learntLanguage", from)
                    .setParameter("knownLanguage", to)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            languageSet = new LanguageSet(from, to);
            ENTITY_MANAGER.persist(languageSet);
        }

        ENTITY_MANAGER.getTransaction().commit();
        return languageSet;
    }

    private void validateVocables(final List<Vocable> vocables) throws DuplicateVocablesInSetException {
        final List<String> mappedVocables = vocables.stream().map(vocable -> vocable.getVocable().getSynonyms().get(0)).collect(Collectors.toList());
        final int unique = new ArrayList<>(new HashSet<>(mappedVocables)).size();
        if (unique != vocables.size()) {
            final HashSet<String> stringSet = new HashSet<>();
            final HashSet<String> duplicates = new HashSet<>();
            for (String element : mappedVocables) if (!stringSet.add(element)) duplicates.add(element);
            throw new DuplicateVocablesInSetException("The given list contains duplicate values: " + new ArrayList<>(duplicates));
        }
    }
}
