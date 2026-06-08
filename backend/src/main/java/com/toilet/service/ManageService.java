package com.toilet.service;

import com.toilet.dto.DashboardResponse;
import com.toilet.dto.RepairOrderCreateRequest;
import com.toilet.dto.ToiletCreateRequest;
import com.toilet.entity.ConsumableStock;
import com.toilet.entity.Facility;
import com.toilet.entity.Message;
import com.toilet.entity.RepairOrder;
import com.toilet.entity.Toilet;

import java.util.List;

public interface ManageService {
    DashboardResponse dashboard();
    List<Toilet> listToilets();
    void createToilet(ToiletCreateRequest request);
    List<Facility> listFacilities();
    List<RepairOrder> listRepairs();
    void createRepair(RepairOrderCreateRequest request);
    void updateRepairStatus(Long id, String status);
    void deleteRepair(Long id);
    List<ConsumableStock> listConsumables();
    List<Message> listMessages();
}
