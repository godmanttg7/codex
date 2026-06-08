package com.toilet.mapper;

import com.toilet.dto.DashboardResponse;
import com.toilet.entity.ConsumableStock;
import com.toilet.entity.Facility;
import com.toilet.entity.Message;
import com.toilet.entity.RepairOrder;
import com.toilet.entity.Toilet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManageMapper {
    List<Toilet> listToilets();
    void insertToilet(Toilet toilet);
    List<Facility> listFacilities();
    Facility getFacilityById(Long id);
    List<RepairOrder> listRepairs();
    RepairOrder getRepairById(Long id);
    void insertRepair(RepairOrder repairOrder);
    void updateRepairStatus(@Param("id") Long id, @Param("status") String status);
    void updateRepairAssignee(@Param("id") Long id, @Param("assigneeName") String assigneeName);
    void deleteRepair(Long id);
    void recomputeFacilityStatus(@Param("facilityId") Long facilityId);
    List<ConsumableStock> listConsumables();
    List<Message> listMessages();
    void insertMessage(Message message);
    DashboardResponse dashboard();
}
