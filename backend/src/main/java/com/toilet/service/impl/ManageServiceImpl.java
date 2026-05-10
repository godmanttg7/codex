package com.toilet.service.impl;

import com.toilet.dto.DashboardResponse;
import com.toilet.dto.RepairOrderCreateRequest;
import com.toilet.dto.ToiletCreateRequest;
import com.toilet.entity.ConsumableStock;
import com.toilet.entity.RepairOrder;
import com.toilet.entity.Toilet;
import com.toilet.mapper.ManageMapper;
import com.toilet.service.ManageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ManageServiceImpl implements ManageService {
    private static final Set<String> ALLOWED = Set.of("PENDING", "REPAIRING", "CHECKING", "FINISHED");
    private final ManageMapper manageMapper;

    public ManageServiceImpl(ManageMapper manageMapper) { this.manageMapper = manageMapper; }

    public DashboardResponse dashboard() { return manageMapper.dashboard(); }
    public List<Toilet> listToilets() { return manageMapper.listToilets(); }
    public void createToilet(ToiletCreateRequest request) {
        Toilet t = new Toilet();
        t.setToiletCode(request.getToiletCode());
        t.setName(request.getName());
        t.setAddress(request.getAddress());
        t.setDistrict(request.getDistrict());
        t.setOpenTime(request.getOpenTime());
        manageMapper.insertToilet(t);
    }
    public List<RepairOrder> listRepairs() { return manageMapper.listRepairs(); }
    public void createRepair(RepairOrderCreateRequest request) {
        RepairOrder o = new RepairOrder();
        o.setToiletId(request.getToiletId());
        o.setFaultDesc(request.getFaultDesc());
        o.setReporter(request.getReporter());
        o.setStatus("PENDING");
        manageMapper.insertRepair(o);
    }
    public void updateRepairStatus(Long id, String status) {
        if (!ALLOWED.contains(status)) throw new IllegalArgumentException("非法状态");
        manageMapper.updateRepairStatus(id, status);
    }
    public List<ConsumableStock> listConsumables() { return manageMapper.listConsumables(); }
}
