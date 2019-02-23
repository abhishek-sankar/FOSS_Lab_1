/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    public ArrayList<String> wordList = new ArrayList<>();
    public HashSet<String> wordSet=new HashSet<>();
    public HashMap<String,ArrayList<String>> lettersToWord = new HashMap<>();
    public HashMap<Integer,ArrayList<String>> sizeToWords= new HashMap<>();
    private Random random = new Random();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String k = sortLetters(word);
            if (lettersToWord.containsKey(k))
                lettersToWord.get(k).add(word);
            else {
                ArrayList<String> a = new ArrayList<>();
                a.add(word);
                lettersToWord.put(k, a);
            }
            int l = k.length();
            if (sizeToWords.containsKey(l))
                sizeToWords.get(l).add(word);
            else {
                ArrayList<String>B =new ArrayList<>();
                B.add(word);
                sizeToWords.put(l,B);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
       return (wordSet.contains(word)&&!(word.contains(base)));
    }
    public static ArrayList<String> permutation(String s) {
        // The result
        ArrayList<String> res = new ArrayList<String>();
        // If input string's length is 1, return {s}
        if (s.length() == 1) {
            res.add(s);
        } else if (s.length() > 1) {
            int lastIndex = s.length() - 1;
            // Find out the last character
            String last = s.substring(lastIndex);
            // Rest of the string
            String rest = s.substring(0, lastIndex);
            // Perform permutation on the rest string and
            // merge with the last character
            res = merge(permutation(rest), last);
        }
        return res;
    }

    public static ArrayList<String> merge(ArrayList<String> list, String c) {
        ArrayList<String> res = new ArrayList<String>();
        // Loop through all the string in the list
        for (String s : list) {
            // For each string, insert the last character to all possible postions
            // and add them to the new list
            for (int i = 0; i <= s.length(); ++i) {
                String ps = new StringBuffer(s).insert(i, c).toString();
                res.add(ps);
            }
        }
        return res;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> res = new ArrayList<>();
        String k = sortLetters(targetWord);
        result.addAll(permutation(targetWord));
        for(String i:result)
        {
            if(isGoodWord(i,targetWord))
                res.add(i);
        }
        return res;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        String sort;
        for(char i='a';i<='z';i++){
            sort= sortLetters(word.concat(""+i));
            if(lettersToWord.containsKey(sort)) {
                result.addAll(lettersToWord.get(sort));
            }
        }
        ArrayList<String> res1=new ArrayList<>();
        Iterator itr = result.iterator();
        while(itr.hasNext())
        {
            String S = (String) itr.next();
            if (!S.contains(word)&&!(res1.contains(S))) {
                res1.add(S);
            }
        }
        Collections.sort(res1);
        return res1;
    }

    public String pickGoodStarterWord() {
        while(true){
            ArrayList<String>nLetterWords=new ArrayList<>();
            nLetterWords= sizeToWords.get(DEFAULT_WORD_LENGTH);

            int index = random.nextInt(nLetterWords.size());
            String randomWord=nLetterWords.get(index);
            ArrayList<String> oneMoreLetterWords= new ArrayList<>();
            oneMoreLetterWords.addAll(getAnagramsWithOneMoreLetter(randomWord));
            if(oneMoreLetterWords.size()>MIN_NUM_ANAGRAMS)
            {
                if(DEFAULT_WORD_LENGTH==MAX_WORD_LENGTH)
                    DEFAULT_WORD_LENGTH=3+random.nextInt(3);
                if(DEFAULT_WORD_LENGTH<MAX_WORD_LENGTH)
                    DEFAULT_WORD_LENGTH+=1;

                return randomWord;
            }
        }

    }

    String sortLetters(String word){
        char sortedWord[]=word.toCharArray();
        Arrays.sort(sortedWord);
        String s= new String(sortedWord);
        return s;
    }
    public List<String> getAnagramsWithTwoMoreLetters(String word) {
        ArrayList<String> result = new ArrayList<>();
        String sort;
        ArrayList<String >res1= new ArrayList<>();
        for(char i='a';i<='z';i++)
            for(char j='a';j<='z';j++) {
                sort = sortLetters(word.concat("" + i + j));
                if (lettersToWord.containsKey(sort)) {
                    result.addAll(lettersToWord.get(sort));
                }
            }
                Iterator itr = result.iterator();
                while(itr.hasNext()) {
                    String S = (String) itr.next();
                    if (!S.contains(word)&&!(res1.contains(S))) {
                        res1.add(S);
                        }
                    }
                    Collections.sort(res1);
                    return res1;
                }
}
//this is a test comment