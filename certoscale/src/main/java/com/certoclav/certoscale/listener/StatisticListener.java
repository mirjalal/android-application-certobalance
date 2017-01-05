package com.certoclav.certoscale.listener;


import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public interface StatisticListener {
 void onStatisticChanged(SummaryStatistics statistic);
}
