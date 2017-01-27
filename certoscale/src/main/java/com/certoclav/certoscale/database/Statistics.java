package com.certoclav.certoscale.database;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enrico on 27.01.2017.
 */

public class Statistics {


    private SummaryStatistics statistic=new SummaryStatistics();

    public SummaryStatistics getStatistic() {return statistic;}
    public void setStatistic(SummaryStatistics statistic) {this.statistic = statistic;}



    private List<Double> samples=new ArrayList<Double>();

    public List<Double> getSamples() {return samples;}
    public void setSamples(List<Double> samples) {this.samples = samples;}
}
