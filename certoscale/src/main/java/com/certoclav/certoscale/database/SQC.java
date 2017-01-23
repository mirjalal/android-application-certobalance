package com.certoclav.certoscale.database;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * Created by Enrico on 19.01.2017.
 */

public class SQC {




    SummaryStatistics statistics=new SummaryStatistics();
    private String name;




    private double nominal;
    private int sqcPT1;
    private int sqcPT2;
    private int sqcNT1;
    private int sqcNT2;


    public SQC(SummaryStatistics statistics,String name,double nominal,int sqcPT1, int sqcPT2,int sqcNT1,int sqcNT2) {
        this.statistics = statistics;
        this.name=name;
        this.nominal=nominal;
        this.sqcPT1=sqcPT1;
        this.sqcPT2=sqcPT2;
        this.sqcNT1=sqcNT1;
        this.sqcNT2=sqcNT2;

    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        name = name;
    }

    public SummaryStatistics getStatistics() {return statistics;}
    public void setStatistics(SummaryStatistics statistics) {this.statistics = statistics;}


    public double getNominal() {return nominal;}
    public void setNominal(double nominal) {this.nominal = nominal;}


    public int getSqcPT1() {return sqcPT1;}
    public void setSqcPT1(int sqcPT1) {this.sqcPT1 = sqcPT1;}
    public int getSqcPT2() {return sqcPT2;}
    public void setSqcPT2(int sqcPT2) {this.sqcPT2 = sqcPT2;}
    public int getSqcNT1() {return sqcNT1;}
    public void setSqcNT1(int sqcNT1) {this.sqcNT1 = sqcNT1;}
    public int getSqcNT2() {return sqcNT2;}
    public void setSqcNT2(int sqcNT2) {this.sqcNT2 = sqcNT2;}




}
