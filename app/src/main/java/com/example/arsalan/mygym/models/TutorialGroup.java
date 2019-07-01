package com.example.arsalan.mygym.models;

import com.example.arsalan.mygym.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsalan on 23-02-2018.
 */
/*1.  پا
2.  حرکات سینه
3.  زیر بغل
4.  سر شانه
5.  جلو بازو
6.  پشت بازو
7.  شکم
8.  ساعد
9.  ساق پا
10.  سرکول*/
public class TutorialGroup {
    private static List<TutorialGroup> tutorialGroupList;
    private final int id;
    private final String name;
    private String thumb;
    private final int thumbRes;

    public TutorialGroup(int id,String name, int thumbRes) {
        this.id = id;
        this.name = name;
        this.thumbRes = thumbRes;

    }

    public static List<TutorialGroup> getList() {
        if (tutorialGroupList != null) return tutorialGroupList;
        tutorialGroupList = new ArrayList<>();
        tutorialGroupList.add(new TutorialGroup(1,"پا", R.drawable.jelo_pa));
        tutorialGroupList.add(new TutorialGroup(2,"سینه", R.drawable.sineh));
        tutorialGroupList.add(new TutorialGroup(3,"زیر بغل", R.drawable.zire_baghal));
        tutorialGroupList.add(new TutorialGroup(4,"سر شانه", R.drawable.sarshane));
        tutorialGroupList.add(new TutorialGroup(5,"جلو بازو", R.drawable.jelo_bazoo));
        tutorialGroupList.add(new TutorialGroup(6,"پشت بازو", R.drawable.poshte_pa));
        tutorialGroupList.add(new TutorialGroup(7,"شکم", R.drawable.shekam));
        tutorialGroupList.add(new TutorialGroup(8,"ساعد", R.drawable.saaed));
        tutorialGroupList.add(new TutorialGroup(9,"ساق پا", R.drawable.saghe_pa));
        tutorialGroupList.add(new TutorialGroup(10,"سرکول", R.drawable.sarkool));
        return tutorialGroupList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumb() {
        return thumb;
    }

    public int getThumbRes() {
        return thumbRes;
    }

}
