package com.helloworld.photoeditor.gallerylib;

import java.util.ArrayList;
import java.util.List;

public class Album {
    int ID;
    long coverID;
    List<GridViewItem> gridItems;
    String id;
    long imageIdForThumb;
    List<Long> imageIdList = new ArrayList();
    String name;
    List<Integer> orientationList = new ArrayList();
}
