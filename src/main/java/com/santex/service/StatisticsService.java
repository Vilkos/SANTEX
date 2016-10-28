package com.santex.service;

import com.santex.dto.StatisticsDto;

public interface StatisticsService {

    StatisticsDto getStatistic(StatisticsDto statisticsDto);

    StatisticsDto getStatistic();
}
