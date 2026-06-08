package com.toilet.controller;

import com.toilet.dto.ApiResponse;
import com.toilet.dto.DashboardResponse;
import com.toilet.dto.RepairOrderAssignRequest;
import com.toilet.dto.RepairOrderCreateRequest;
import com.toilet.dto.RepairOrderStatusRequest;
import com.toilet.dto.ToiletCreateRequest;
import com.toilet.entity.ConsumableStock;
import com.toilet.entity.Facility;
import com.toilet.entity.Message;
import com.toilet.entity.RepairOrder;
import com.toilet.entity.Toilet;
import com.toilet.service.ManageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manage")
@CrossOrigin
public class ManageController {
    private final ManageService manageService;

    public ManageController(ManageService manageService) {
        this.manageService = manageService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard() {
        return ApiResponse.ok(manageService.dashboard());
    }

    @GetMapping("/toilets")
    public ApiResponse<List<Toilet>> toilets() {
        return ApiResponse.ok(manageService.listToilets());
    }

    @PostMapping("/toilets")
    public ApiResponse<Void> createToilet(@Valid @RequestBody ToiletCreateRequest request) {
        manageService.createToilet(request);
        return ApiResponse.ok(null);
    }

    @GetMapping("/facilities")
    public ApiResponse<List<Facility>> facilities() {
        return ApiResponse.ok(manageService.listFacilities());
    }

    @GetMapping("/repairs")
    public ApiResponse<List<RepairOrder>> repairs() {
        return ApiResponse.ok(manageService.listRepairs());
    }

    @PostMapping("/repairs")
    public ApiResponse<Void> createRepair(@Valid @RequestBody RepairOrderCreateRequest request) {
        manageService.createRepair(request);
        return ApiResponse.ok(null);
    }

    @PatchMapping("/repairs/{id}/assign")
    public ApiResponse<Void> assignOrder(@PathVariable Long id, @Valid @RequestBody RepairOrderAssignRequest request) {
        manageService.assignOrder(id, request.getAssigneeName());
        return ApiResponse.ok(null);
    }

    @PatchMapping("/repairs/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody RepairOrderStatusRequest request) {
        manageService.updateStatus(id, request.getStatus());
        return ApiResponse.ok(null);
    }

    @PatchMapping("/repairs/{id}/cancel")
    public ApiResponse<Void> cancelOrder(@PathVariable Long id) {
        manageService.cancelOrder(id);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/repairs/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable Long id) {
        manageService.deleteOrder(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/consumables")
    public ApiResponse<List<ConsumableStock>> consumables() {
        return ApiResponse.ok(manageService.listConsumables());
    }

    @GetMapping("/messages")
    public ApiResponse<List<Message>> messages() {
        return ApiResponse.ok(manageService.listMessages());
    }
}
