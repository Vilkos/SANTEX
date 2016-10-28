package com.santex.service.implementation;

import com.santex.dao.CompanyCredentialsDao;
import com.santex.entity.CompanyCredentials;
import com.santex.dto.Event;
import com.santex.service.CompanyCredentialsService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Repository
@Transactional
public class CompanyCredentialsServiceImpl implements CompanyCredentialsService {
    private final CompanyCredentialsDao companyCredentialsDao;
    private final ApplicationEventPublisher publisher;

    public CompanyCredentialsServiceImpl(CompanyCredentialsDao companyCredentialsDao, ApplicationEventPublisher publisher) {
        this.companyCredentialsDao = companyCredentialsDao;
        this.publisher = publisher;
    }

    @Override
    public void edit(CompanyCredentials cr) {
        CompanyCredentials credentials = companyCredentialsDao.findById(1).orElse(new CompanyCredentials());
        credentials.setName(cr.getName());
        credentials.setAddress(cr.getAddress());
        credentials.setPhone1(cr.getPhone1());
        credentials.setPhone2(cr.getPhone2());
        credentials.setPhone3(cr.getPhone3());
        credentials.setEmail(cr.getEmail());
        credentials.setTaxcode(cr.getTaxcode());
        credentials.setBankDetails(cr.getBankDetails());
        credentials.setShipmentInfo(cr.getShipmentInfo());
        credentials.setDescription(cr.getDescription());
        companyCredentialsDao.save(credentials);
        publisher.publishEvent(new Event());
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyCredentials getCredentials() {
        return companyCredentialsDao.findById(1).orElse(new CompanyCredentials());
    }
}
