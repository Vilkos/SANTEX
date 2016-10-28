package com.santex.service;

import com.santex.entity.Unit;
import java.util.List;

public interface UnitService {

    void add(String unitName);

    void edit(int id, String unitName);

    void remove(int id);

    Unit findById(int id);

    List<Unit> findAll();

}
