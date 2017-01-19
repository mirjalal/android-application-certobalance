package com.certoclav.certoscale.database;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * Created by Enrico on 19.01.2017.
 */

public class SQC {


    SummaryStatistics statistics=new SummaryStatistics();
    private String name;

    public SQC(SummaryStatistics statistics,String name) {
        this.statistics = statistics;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }


}
