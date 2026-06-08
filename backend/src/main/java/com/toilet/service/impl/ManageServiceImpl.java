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
        recomputeFacilityStatus(order.getFacilityId());
        sendMessage(order.getAssigneeName(), "新维修工单", "工单" + order.getOrderNo() + "已创建，请及时处理。故障：" + order.getFaultDesc());
    }

    @Transactional
    public void assignOrder(Long id, String assigneeName) {
        RepairOrder existing = requireOrder(id);
        String safeAssignee = defaultText(assigneeName, "维修人员");
        manageMapper.updateRepairAssignee(id, safeAssignee);
        manageMapper.updateRepairStatus(id, "REPAIRING");
        recomputeFacilityStatus(existing.getFacilityId());
        sendMessage(safeAssignee, "维修工单已指派", "工单" + existing.getOrderNo() + "已指派给你，并进入维修中。 ");
    }

    @Transactional
    public void updateStatus(Long id, String status) {
        if (!ALLOWED.contains(status)) {
            throw new IllegalArgumentException("非法状态");
        }
        RepairOrder existing = requireOrder(id);
        manageMapper.updateRepairStatus(id, status);
        recomputeFacilityStatus(existing.getFacilityId());
        notifyRepairStatus(existing, status);
    }

    @Transactional
    public void cancelOrder(Long id) {
        RepairOrder existing = requireOrder(id);
        manageMapper.updateRepairStatus(id, "CANCELED");
        recomputeFacilityStatus(existing.getFacilityId());
        sendMessage(existing.getReporterName(), "维修工单已取消", "工单" + existing.getOrderNo() + "已取消，设施状态已按剩余工单重新计算。 ");
    }

    @Transactional
    public void deleteOrder(Long id) {
        RepairOrder existing = manageMapper.getRepairById(id);
        if (existing == null) {
            return;
        }
        Long facilityId = existing.getFacilityId();
        manageMapper.deleteRepair(id);
        recomputeFacilityStatus(facilityId);
        sendMessage(existing.getReporterName(), "维修工单已删除", "工单" + existing.getOrderNo() + "已删除，设施状态已按剩余工单重新计算。 ");
    }

    public List<ConsumableStock> listConsumables() {
        return manageMapper.listConsumables();
    }

    public List<Message> listMessages() {
        return manageMapper.listMessages();
    }

    private RepairOrder requireOrder(Long id) {
        RepairOrder existing = manageMapper.getRepairById(id);
        if (existing == null) {
            throw new IllegalArgumentException("工单不存在");
        }
        return existing;
    }

    private String generateOrderNo() {
        return "RO" + LocalDateTime.now().format(ORDER_NO_TIME_FORMAT) + (System.nanoTime() % 10000);
    }

    private void recomputeFacilityStatus(Long facilityId) {
        if (facilityId != null) {
            manageMapper.recomputeFacilityStatus(facilityId);
        }
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
            sendMessage(order.getReporterName(), title, "工单" + order.getOrderNo() + "已验收通过，设施状态已按剩余工单重新计算。 ");
            return;
        }
        if ("CANCELED".equals(status)) {
            sendMessage(order.getReporterName(), title, "工单" + order.getOrderNo() + "已取消，设施状态已按剩余工单重新计算。 ");
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
