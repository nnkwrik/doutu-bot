package com.github.nnkwrik.doutuBot.LP;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import org.junit.Test;

public class TestHanLP {

    @Test
    public void testLP() {

        String content = "看小主人笑的这么开心，我的心里也花儿一样美丽呢！能让小主人快乐就是我最大的快乐。";

        System.out.println( HanLP.extractKeyword(content, 3));
        content = content.replaceAll("小主人","你");
        System.out.println(content);
        System.out.println( HanLP.extractKeyword(content, 3));


    }

    @Test
    public void testLSize(){
        // String content ="我在陪你聊天啊小主人。".replaceAll("小主人","你");
        String content = "我还不饿，吃不下饭";
        System.out.println( HanLP.extractKeyword(content, 1));
        System.out.println( HanLP.extractKeyword(content, 2));
        System.out.println( HanLP.extractKeyword(content, 3));
        String keywords="";

        for (String str : HanLP.extractKeyword(content, 3) ) {
            keywords +=str + "+";
        }
        keywords = keywords.substring(0,keywords.length()-1);

        System.out.println(keywords);
    }

    @Test
    public void testDemoCustomDictionary(){
        // 动态增加
        CustomDictionary.add("攻城狮");
        // 强行插入
        CustomDictionary.insert("白富美", "nz 1024");
        // 删除词语（注释掉试试）
//        CustomDictionary.remove("攻城狮");
        System.out.println(CustomDictionary.add("单身狗", "nz 1024 n 1"));
        System.out.println(CustomDictionary.get("单身狗"));

        String text = "攻城狮逆袭单身狗，迎娶白富美，走上人生巅峰";  // 怎么可能噗哈哈！

        // AhoCorasickDoubleArrayTrie自动机扫描文本中出现的自定义词语
        final char[] charArray = text.toCharArray();
        CustomDictionary.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>()
        {
            @Override
            public void hit(int begin, int end, CoreDictionary.Attribute value)
            {
                System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(charArray, begin, end - begin), value);
            }
        });

        // 自定义词典在所有分词器中都有效
        System.out.println(HanLP.segment(text));

    }
}
