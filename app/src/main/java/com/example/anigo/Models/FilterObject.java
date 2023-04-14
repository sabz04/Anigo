package com.example.anigo.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterObject implements Serializable {
    public int Page;
    public String Search;
    public ArrayList<String> Genres = new ArrayList<>();
    public ArrayList<Integer> Years = new ArrayList<>();
    public ArrayList<String> Type = new ArrayList<>();
    public ArrayList<String> Studios = new ArrayList<>();
    public Boolean Ongoing;
    public String SortKey;
}
