package com.santex.service.implementation;

import com.santex.dao.OrderEntryDao;
import com.santex.dto.StatisticsDto;
import com.santex.service.StatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.santex.service.PriceService.toDouble;

@Service
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {
    private final OrderEntryDao orderEntryDao;

    public StatisticsServiceImpl(OrderEntryDao orderEntryDao) {
        this.orderEntryDao = orderEntryDao;
    }

    @Override
    public StatisticsDto getStatistic() {
        Integer sum = orderEntryDao.getWholeSum();
        if (sum == null) sum = 0;
        LocalDateTime startDate = orderEntryDao.getMinDate();
        if (startDate == null) startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now();
        return new StatisticsDto(
                toDouble(sum),
                startDate.toLocalDate(),
                endDate.toLocalDate());
    }

    @Override
    public StatisticsDto getStatistic(StatisticsDto statisticsDto) {
        LocalDate startDate = statisticsDto.getFrom();
        LocalDate endDate = statisticsDto.getTo();
        if (endDate.isEqual(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            endDate = LocalDate.now();
        } else if (endDate.isBefore(startDate)) {
            endDate = startDate;
        }
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        Integer sum = orderEntryDao.getSumByDates(start, end);
        if (sum == null) sum = 0;
        statisticsDto.setSum(toDouble(sum));
        statisticsDto.setFrom(start.toLocalDate());
        statisticsDto.setTo(end.toLocalDate());
        return statisticsDto;
    }
}
