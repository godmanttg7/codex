package com.toilet.mapper;

import com.toilet.dto.DashboardResponse;
import com.toilet.entity.ConsumableStock;
import com.toilet.entity.RepairOrder;
import com.toilet.entity.Toilet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManageMapper {
    List<Toilet> listToilets();
    void insertToilet(Toilet toilet);
    List<RepairOrder> listRepairs();
    void insertRepair(RepairOrder repairOrder);
    void updateRepairStatus(@Param("id") Long id, @Param("status") String status);
    List<ConsumableStock> listConsumables();
    DashboardResponse dashboard();
}
