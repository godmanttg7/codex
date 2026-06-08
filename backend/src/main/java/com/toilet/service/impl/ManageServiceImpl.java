package com.toilet.service.impl;

import com.toilet.dto.DashboardResponse;
import com.toilet.dto.RepairOrderCreateRequest;
import com.toilet.dto.ToiletCreateRequest;
import com.toilet.entity.ConsumableStock;
import com.toilet.entity.Facility;
import com.toilet.entity.Message;
import com.toilet.entity.RepairOrder;
import com.toilet.entity.Toilet;
import com.toilet.mapper.ManageMapper;
import com.toilet.service.ManageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
public class ManageServiceImpl implements ManageService {
    private static final Set<String> ALLOWED = Set.of("PENDING", "REPAIRING", "CHECKING", "FINISHED", "CANCELED");
    private static final DateTimeFormatter ORDER_NO_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final ManageMapper manageMapper;

    public ManageServiceImpl(ManageMapper manageMapper) {
        this.manageMapper = manageMapper;
    }

    public DashboardResponse dashboard() {
        return manageMapper.dashboard();
    }

    public List<Toilet> listToilets() {
        return manageMapper.listToilets();
    }

    public void createToilet(ToiletCreateRequest request) {
        Toilet t = new Toilet();
        t.setToiletCode(request.getToiletCode());
        t.setName(request.getName());
        t.setAddress(request.getAddress());
        t.setDistrict(request.getDistrict());
        t.setOpenTime(request.getOpenTime());
        manageMapper.insertToilet(t);
    }

    public List<Facility> listFacilities() {
        return manageMapper.listFacilities();
    }

    public List<RepairOrder> listRepairs() {
        return manageMapper.listRepairs();
    }

    @Transactional
    public void createRepair(RepairOrderCreateRequest request) {
        RepairOrder order = new RepairOrder();
        order.setOrderNo(generateOrderNo());
        order.setToiletId(request.getToiletId());
        order.setFacilityId(request.getFacilityId());
        order.setFaultDesc(request.getFaultDesc());
        order.setReporter(request.getReporter());
        order.setReporterName(defaultText(request.getReporterName(), request.getReporter()));
        order.setAssigneeName(defaultText(request.getAssigneeName(), "维修人员"));
        order.setStatus("PENDING");
        manageMapper.insertRepair(order);
        if (request.getFacilityId() != null) {
            manageMapper.updateFacilityStatus(request.getFacilityId(), "FAULT");
        }
        sendMessage(order.getAssigneeName(), "新维修工单", "工单" + order.getOrderNo() + "已创建，请及时处理。故障：" + order.getFaultDesc());
    }

    @Transactional
    public void updateRepairStatus(Long id, String status) {
        if (!ALLOWED.contains(status)) {
            throw new IllegalArgumentException("非法状态");
        }
        RepairOrder existing = manageMapper.getRepairById(id);
        if (existing == null) {
            throw new IllegalArgumentException("工单不存在");
        }
        manageMapper.updateRepairStatus(id, status);
        syncFacilityByOrderStatus(existing, status);
        notifyRepairStatus(existing, status);
    }

    @Transactional
    public void deleteRepair(Long id) {
        RepairOrder existing = manageMapper.getRepairById(id);
        if (existing == null) {
            return;
        }
        manageMapper.deleteRepair(id);
        if (existing.getFacilityId() != null) {
            manageMapper.updateFacilityStatus(existing.getFacilityId(), "NORMAL");
        }
        sendMessage(existing.getReporterName(), "维修工单已取消", "工单" + existing.getOrderNo() + "已删除，关联设施已恢复为正常状态。 ");
    }

    public List<ConsumableStock> listConsumables() {
        return manageMapper.listConsumables();
    }

    public List<Message> listMessages() {
        return manageMapper.listMessages();
    }

    private String generateOrderNo() {
        return "RO" + LocalDateTime.now().format(ORDER_NO_TIME_FORMAT) + (System.nanoTime() % 10000);
    }

    private void syncFacilityByOrderStatus(RepairOrder order, String status) {
        if (order.getFacilityId() == null) {
            return;
        }
        if ("FINISHED".equals(status) || "CANCELED".equals(status)) {
            manageMapper.updateFacilityStatus(order.getFacilityId(), "NORMAL");
            return;
        }
        if ("REPAIRING".equals(status) || "CHECKING".equals(status)) {
            manageMapper.updateFacilityStatus(order.getFacilityId(), "REPAIR");
            return;
        }
        manageMapper.updateFacilityStatus(order.getFacilityId(), "FAULT");
    }

    private void notifyRepairStatus(RepairOrder order, String status) {
        String title = "维修工单状态更新";
        if ("REPAIRING".equals(status)) {
            sendMessage(order.getAssigneeName(), title, "工单" + order.getOrderNo() + "已进入维修中。 ");
            return;
        }
        if ("CHECKING".equals(status)) {
            sendMessage("管理员", title, "工单" + order.getOrderNo() + "已维修完成，请验收。 ");
            return;
        }
        if ("FINISHED".equals(status)) {
            sendMessage(order.getReporterName(), title, "工单" + order.getOrderNo() + "已验收通过，设施状态已恢复正常。 ");
            return;
        }
        if ("CANCELED".equals(status)) {
            sendMessage(order.getReporterName(), title, "工单" + order.getOrderNo() + "已取消，设施状态已恢复正常。 ");
            return;
        }
        sendMessage(order.getAssigneeName(), title, "工单" + order.getOrderNo() + "状态已更新为" + status + "。 ");
    }

    private void sendMessage(String receiverName, String title, String content) {
        Message message = new Message();
        message.setReceiverName(defaultText(receiverName, "管理员"));
        message.setTitle(title);
        message.setContent(content);
        message.setReadFlag(false);
        manageMapper.insertMessage(message);
    }

    private String defaultText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }
}
