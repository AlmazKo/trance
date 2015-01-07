package ru.alexlen.trance;

import java.util.*;

/**
 * @author Almazko
 */
public class Dictionary {

    //     <source,         Lang,   Unit
    TreeMap<String, TreeMap<String, Unit>> data      = new TreeMap<>();
    Set<String>                            languages = new HashSet<>();
    public Map<String, TranslateResource> resources = new HashMap<>();
    private String mName;

    void addTranslations(TranslateResource res, List<Unit> translations)
    {

        TreeMap<String, Unit> variantSources;
        if (!languages.contains(res.getLang())) {
            languages.add((res.getLang()));
        } //TODO init known langs

        resources.put(res.getLang(), res);

        mName = res.getName();

        for (final Unit trans : translations) {

            if (data.containsKey(trans.source)) {
                variantSources = data.get(trans.source);
            } else {
                variantSources = new TreeMap<>();
                data.put(trans.source, variantSources);
            }

//            if (variantSources.containsKey(lang)) {
//                variantSources.get(lang).add(trans);
//            } else {

            variantSources.put(res.getLang(), trans);
//            }

        }
    }


    public String getResName() {
        return mName;
    }
}
