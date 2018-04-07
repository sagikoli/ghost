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

package com.google.engedu.ghost;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private Random random=new Random();

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<Character, TrieNode> hm = children;
        for (int i = 0; i < s.length(); i++)
        {
            if(!hm.containsKey(s.charAt(i)))
            {
                hm.put(s.charAt(i),new TrieNode());
            }
            if(i==s.length()-1)
            {
                hm.get(s.charAt(i)).isWord=true;
            }
            hm=hm.get(s.charAt(i)).children;
        }
    }

    public boolean isWord(String s) {
        TrieNode t=searchNode(s);
        if(t==null)
        return false;
       else
           return  t.isWord;
    }
    private TrieNode searchNode(String s)
    {
        TrieNode t=this;
        for(int i=0;i<s.length();i++)
        {
            if(!t.children.containsKey(s.charAt(i)))
            {
                return null;
            }
            t=t.children.get(s.charAt(i));
        }
        return t;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode t=searchNode(s);
        if(t==null)
        {
            return null;
        }
        while (!t.isWord){
            for (Character c:t.children.keySet())
            {
                t=t.children.get(c);
                s+=c;
                break;
            }
        }
        return s;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode t=searchNode(s);
        if(t==null)
        {
            return null;
        }
        ArrayList<Character> charNoWord=new ArrayList<>();
        ArrayList<Character> charWord=new ArrayList<>();
        Character c;
        while (true)
        {
            charNoWord.clear();
            charWord.clear();
            for(Character ch:t.children.keySet())
            {
                if(t.children.get(ch).isWord)
                {
                    charWord.add(ch);
                }
                else {
                    charNoWord.add(ch);
                }
            }
            if(charNoWord.size()==0)
            {
                s+=charWord.get(random.nextInt(charWord.size()));
                break;
            }
            else {
                c=charNoWord.get(random.nextInt(charNoWord.size()));
                s+=c;
                t=t.children.get(c);
            }
        }
        return s;
    }

}
