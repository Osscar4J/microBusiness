package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Domain;
import com.zhao.micro.mapper.DomainMapper;
import com.zhao.micro.service.DomainService;
import com.zhao.micro.service.MyBaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DomainServiceImpl extends MyBaseService<DomainMapper, Domain> implements DomainService {

    // 缓存
    private List<Domain> domains = new ArrayList<>();
    private Map<Integer, List<Domain>> domainMap = new HashMap<>();

    @Override
    public boolean updateStatus(Long id, Integer status) {
        Domain entity = this.getById(id);
        if (entity == null)
            return false;
        entity.setStatus(status);
        if (this.updateById(entity)) {
            // 更新缓存
            this.initCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(Domain entity) {
        if (super.updateById(entity)) {
            this.initCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean save(Domain entity) {
        if (super.save(entity)) {
            this.initCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean saveOrUpdate(Domain entity) {
        if (entity.getId() == null)
            return this.save(entity);
        return this.updateById(entity);
    }

    @Override
    public Domain getRandom(Integer type) {
        if (domains.isEmpty())
            return null;
        if (type == null || type == -1)
            return domains.get((int) Math.floor(Math.random() * domains.size()));
        List<Domain> temp = domainMap.get(type);
        if (temp == null)
            return null;
        return temp.get((int) Math.floor(Math.random() * temp.size()));
    }

    @Override
    public void initCache() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("status", SysConstants.DomainStatus.NORMAL);
        domains = this.list(wrapper);
        domainMap.clear();
        for (Domain domain : domains) {
            if (domain.getType() != null) {
                if (domainMap.get(domain.getType()) == null)
                    domainMap.put(domain.getType(), new ArrayList<>());
                domainMap.get(domain.getType()).add(domain);
            }
        }
    }
}
