package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary
{
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null)
        {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    private Random random = new Random();

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        int n;
        //int listLen = words.size();
        if (prefix == null) {
            n = random.nextInt(10000) + 1;
            return words.get(n);
        }
        else {
            int i;
            int first = 0;
            int last =9999 ;
            int mid =  (first + last) / 2;
            i = 0;
            while (i < prefix.length())
            {
            while (first <= last)
            {
                if (words.get(mid).startsWith(prefix))
                    return words.get(mid);
                else if (words.get(mid).charAt(i) < prefix.charAt(i)) {
                    first = mid + 1;
                    i++;
                } else if (words.get(mid).charAt(i) > prefix.charAt(i)) {
                    last = mid - 1;
                    i++;
                }
                mid = (first + last) / 2;
            }
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
