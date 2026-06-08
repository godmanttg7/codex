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
    void assignOrder(Long id, String assigneeName);
    void updateStatus(Long id, String status);
    void cancelOrder(Long id);
    void deleteOrder(Long id);
    List<ConsumableStock> listConsumables();
    List<Message> listMessages();
}
