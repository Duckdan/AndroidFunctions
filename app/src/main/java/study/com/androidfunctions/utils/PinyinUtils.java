package study.com.androidfunctions.utils;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import study.com.androidfunctions.adapter.RvHeaderAdapter;
import study.com.androidfunctions.model.ListHeaderEntity;
import study.com.androidfunctions.model.PersonInfo;

/**
 * 拼音工具类
 */

public class PinyinUtils {

    public static Map<String, List<ListHeaderEntity<PersonInfo>>> hashMap = new HashMap<>();

    //键值的集合
    public static Set<String> keySet = new TreeSet<>();
    //键值索引
    public static Map<String, Integer> keyMap = new HashMap<>();

    /**
     * 获取汉字字符串的第一个字母
     */
    public static String getPinYinFirstLetter(String str) {
        char c = str.charAt(0);
        String changeStr = Pinyin.toPinyin(c);
        return TextUtils.isEmpty(changeStr) ? "#" : changeStr.charAt(0) + "";
    }

    /**
     * 获取经过转化的联系人列表
     *
     * @param list
     * @return
     */
    public static List<ListHeaderEntity<PersonInfo>> getContactsList(ArrayList<PersonInfo> list) {
        if (list == null) {
            return null;
        }
        hashMap.clear();
        keySet.clear();

        List<ListHeaderEntity<PersonInfo>> adapterList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            PersonInfo personInfo = list.get(i);
            String firstLetter = getPinYinFirstLetter(personInfo.getName());
            if (keySet.contains(firstLetter)) { //键值集合中已包含该字母
                hashMap.get(firstLetter).add(new ListHeaderEntity<PersonInfo>(personInfo, RvHeaderAdapter.TYPE_DATA, personInfo.getName()));
            } else { //键值集合不包含该字母
                keySet.add(firstLetter);
                //创建该键值对应的集合
                ArrayList<ListHeaderEntity<PersonInfo>> valueList = new ArrayList<>();
                PersonInfo keyInfo = new PersonInfo(firstLetter);
                ListHeaderEntity<PersonInfo> lhe = new ListHeaderEntity<>(keyInfo, RvHeaderAdapter.TYPE_HEADER, keyInfo.getName());
                valueList.add(lhe);
                valueList.add(new ListHeaderEntity<PersonInfo>(personInfo, RvHeaderAdapter.TYPE_DATA, personInfo.getName()));
                hashMap.put(firstLetter, valueList);
            }
        }

        sortList(adapterList, keySet, hashMap);
        return adapterList;
    }

    /**
     * 排序集合
     *
     * @param adapterList
     * @param keySet
     * @param hashMap
     */
    private static void sortList(List<ListHeaderEntity<PersonInfo>> adapterList, Set<String> keySet, Map<String, List<ListHeaderEntity<PersonInfo>>> hashMap) {
        Iterator<String> iterator = keySet.iterator();
        keyMap.clear();
        while (iterator.hasNext()) {
            String letter = iterator.next();
            List<ListHeaderEntity<PersonInfo>> valueList = hashMap.get(letter);
            for (int i = 0; i < valueList.size(); i++) {
                ListHeaderEntity<PersonInfo> lhe = valueList.get(i);
                adapterList.add(lhe);
                if (lhe.getItemType() == RvHeaderAdapter.TYPE_HEADER) {
                    keyMap.put(lhe.getListHeaderName(), adapterList.size() - 1);
                }
            }
        }
    }
}
