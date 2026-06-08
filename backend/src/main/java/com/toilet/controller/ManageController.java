package com.toilet.controller;

import com.toilet.dto.ApiResponse;
import com.toilet.dto.DashboardResponse;
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

    @PatchMapping("/repairs/{id}/status")
    public ApiResponse<Void> updateRepairStatus(@PathVariable Long id, @Valid @RequestBody RepairOrderStatusRequest request) {
        manageService.updateRepairStatus(id, request.getStatus());
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/repairs/{id}")
    public ApiResponse<Void> deleteRepair(@PathVariable Long id) {
        manageService.deleteRepair(id);
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
