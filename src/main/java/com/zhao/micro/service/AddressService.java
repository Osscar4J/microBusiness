package com.zhao.micro.service;

import com.zhao.micro.entity.Address;

public interface AddressService extends BaseService<Address> {

    Address getByUserId(Long userId);
}
