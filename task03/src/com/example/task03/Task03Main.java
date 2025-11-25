package com.example.task03;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

public class Task03Main {

    private static final Pattern RUSSIAN_WORD = Pattern.compile("^[а-яё]+$");

    public static void main(String[] args) throws IOException {

        List<Set<String>> anagrams = findAnagrams(new FileInputStream("task03/resources/singular.txt"), Charset.forName("windows-1251"));
        for (Set<String> anagram : anagrams) {
            System.out.println(anagram);
        }

    }

    public static List<Set<String>> findAnagrams(InputStream inputStream, Charset charset) {
        Set<String> uniqueWords = new HashSet<>();

        Map<String, Set<String>> groups = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();

                if (word.length() < 3) {
                    continue;
                }

                if (!RUSSIAN_WORD.matcher(word).matches()) {
                    continue;
                }

                if (!uniqueWords.add(word)) {
                    continue;
                }

                String key = sortChars(word);

                Set<String> group = groups.get(key);
                if (group == null) {
                    group = new LinkedHashSet<>();
                    groups.put(key, group);
                }
                group.add(word);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<List<String>> groupedLists = new ArrayList<>();
        for (Set<String> set : groups.values()) {
            if (set.size() < 2) {
                continue;
            }
            List<String> list = new ArrayList<>(set);
            Collections.sort(list);
            groupedLists.add(list);
        }

        groupedLists.sort(Comparator.comparing(l -> l.get(0)));

        List<Set<String>> result = new ArrayList<>();
        for (List<String> list : groupedLists) {
            Set<String> orderedSet = new LinkedHashSet<>(list);
            result.add(orderedSet);
        }

        return result;
    }

    private static String sortChars(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}