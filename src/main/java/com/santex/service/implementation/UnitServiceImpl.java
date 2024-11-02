package com.santex.service.implementation;

import com.santex.dao.UnitDao;
import com.santex.entity.Unit;
import com.santex.dto.Event;
import com.santex.service.UnitService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Repository
@Transactional
public class UnitServiceImpl implements UnitService {
    private final UnitDao unitDao;
    private final ApplicationEventPublisher publisher;

    public UnitServiceImpl(UnitDao unitDao, ApplicationEventPublisher publisher) {
        this.unitDao = unitDao;
        this.publisher = publisher;
    }

    @Override
    public void add(String unitName) {
        unitDao.save(new Unit(unitName));
        publisher.publishEvent(new Event());
    }

    @Override
    public void edit(int id, String unitName) {
        Optional<Unit> unit = unitDao.findById(id);
        if (unit.isPresent() && unitName != null) {
            unit.get().setUnitName(unitName);
            unitDao.save(unit.get());
            publisher.publishEvent(new Event());
        }
    }

    @Override
    public void remove(int id) {
        unitDao.deleteById(id);
        publisher.publishEvent(new Event());
    }

    @Override
    @Transactional(readOnly = true)
    public Unit findById(int id) {
        return unitDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Unit> findAll() {
        return unitDao.findAll();
    }
}
