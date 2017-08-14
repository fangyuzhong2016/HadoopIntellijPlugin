package com.fangyuzhong.intelliJ.hadoop.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class WordTokenizer
{
    private static final Pattern PATTERN2 = Pattern.compile("\\B");
    private static final Pattern PATTERN1 = Pattern.compile("\\b");
    List<String> tokens = new ArrayList();

    public WordTokenizer(String string)
    {
        String[] tokens1 = PATTERN1.split(string);
        for (String token1 : tokens1)
        {
            token1 = token1.trim();
            if (token1.length() > 0)
            {
                if (isSplittableToken(token1))
                {
                    String[] tokens2 = PATTERN2.split(token1);
                    for (String token2 : tokens2)
                    {
                        token2 = token2.trim();
                        if (token2.length() > 0)
                        {
                            this.tokens.add(token2);
                        }
                    }
                } else
                {
                    this.tokens.add(token1);
                }
            }
        }
    }

    private static boolean isSplittableToken(String token)
    {
        if (token.length() > 1)
        {
            char chr = token.charAt(0);
            return (!Character.isLetter(chr)) && (!Character.isDigit(chr)) && (chr != '_');
        }
        return false;
    }

    public List<String> getTokens()
    {
        return this.tokens;
    }

    public static void main(String[] args)
    {
        WordTokenizer wordTokenizer = new WordTokenizer("return decimal(5,2), p_customer_id INT, p_effective_date DATETIME");
    }
}
