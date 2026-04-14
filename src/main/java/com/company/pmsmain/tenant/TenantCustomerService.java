package com.company.pmsmain.tenant;

import com.company.pmsmain.entity.Customer;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantCustomerService {

    @Autowired
    private DataManager dataManager;

    public List<Customer> loadCustomers() {
        return dataManager.load(Customer.class)
                .all()
                .list();
    }
}