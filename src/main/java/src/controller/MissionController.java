package src.controller;


import src.data.MissionInfo;
import src.enums.MissionType;
import src.model.CosmicStation;
import src.model.Mission;
import src.repository.CosmicStationRepository;
import src.repository.MissionRepository;
import src.validator.MissionInfoValidationStep1;
import src.validator.MissionInfoValidationStep2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/missions")
@SessionAttributes({"missionInfo","createdMission"})
public class MissionController {

    private final MissionRepository missionRepository;
    private final CosmicStationRepository stationRepository;

    @ModelAttribute("missionInfo")
    public MissionInfo missionInfo() {
        return new MissionInfo();
    }

    @GetMapping("/new")
    public String showCreateForm(SessionStatus status) {
        status.setComplete();
        if (stationRepository.count() == 0) {
            return "c-stations/no-stations";
        }

        if (!stationRepository.existsFreeStation()) {
            return "c-stations/no-free-stations";
        }

        return "missions/form";
    }

    @PostMapping("/new")
    public String processCreateForm(
            @Validated(MissionInfoValidationStep1.class)
            @ModelAttribute("missionInfo") MissionInfo missionInfo,
            BindingResult bindingResult,
            Model model
    ) {


        if (bindingResult.hasErrors()) {
            return "missions/form";
        }

        model.addAttribute("missionInfo", missionInfo);
        return "redirect:/missions/new/type";
    }

    @GetMapping("/new/type")
    public String showTypePicker(
            @ModelAttribute("missionInfo") MissionInfo missionInfo,
            Model model
    ) {
        model.addAttribute("types", MissionType.values());
        return "missions/select-type";
    }

    @PostMapping("/new/type")
    public String processTypeSelection(
            @Validated(MissionInfoValidationStep2.class)
            @ModelAttribute("missionInfo") MissionInfo missionInfo,
            BindingResult br,
            Model model
    ) {

        if (br.hasErrors()) {
            model.addAttribute("types", MissionType.values());
            return "missions/select-type";
        }

        List<CosmicStation> freeStations = stationRepository
                .findFreeStationsWithPerformedOn();

        List<CosmicStation> freeStationsForType = freeStations.stream()
                .filter(station -> station.isHasCapacityForMission(missionInfo.getType()))
                .toList();

        if(freeStationsForType.isEmpty()) {
            return "c-stations/no-free-stations-for-type";
        }

        Mission newMission = new Mission();
        newMission.createMission(missionInfo);
        Mission savedMission = missionRepository.save(newMission);

        model.addAttribute("missionInfo", missionInfo);
        model.addAttribute("createdMission", savedMission);
        return "redirect:/missions/new/stations";
    }

    @GetMapping("/new/stations")
    public String showStationPicker(
            @ModelAttribute("createdMission") Mission createdMission,
            @RequestParam(name="page", defaultValue="1") int page,
            Model model
    ) {


        Page<CosmicStation> stationPage = stationRepository
                .findFreeStationsWithPerformedOn(PageRequest.of(page-1, 10));

        List<CosmicStation> freeStationsForType = stationPage.getContent().stream()
                .filter(station -> station.isHasCapacityForMission(createdMission.getType()))
                .toList();


        model.addAttribute("stations", freeStationsForType);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", stationPage.getTotalPages());
        return "c-stations/select-stations";
    }

    @PostMapping("/new/stations")
    public String processStationSelection(
            @ModelAttribute("createdMission") Mission createdMission,
            @RequestParam("stationId") Long stationId
    ) {

        CosmicStation selectedStation = stationRepository
                .findByIdWithPerformedOn(stationId)
                .orElse(null);

        if(selectedStation == null) {
            return "redirect:/missions/new/stations";
        }

        if (!selectedStation.isHasCapacityForMission(createdMission.getType())) {
            return "redirect:/missions/new/stations";
        }

        return "redirect:/missions/new/station-missions?stationId=" + stationId;
    }

    @GetMapping("/new/station-missions")
    public String showStationMissions(
            @ModelAttribute("createdMission") Mission createdMission,
            @RequestParam("stationId") Long stationId,
            @RequestParam(name="page", defaultValue="1") int page,
            Model model
    ) {
        CosmicStation station = stationRepository.findById(stationId).orElse(null);

        if(station == null) {
            return "redirect:/missions/new/stations";
        }

        Page<Mission> stationMissions = missionRepository
                .findByStationId(stationId, PageRequest.of(page-1, 10));

        model.addAttribute("station", station);
        model.addAttribute("missions", stationMissions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", stationMissions.getTotalPages());
        return "missions/station-missions";
    }

    @PostMapping("/new/check-station-selection")
    public String checkStationSelection(
            @ModelAttribute("createdMission") Mission createdMission
    ) {
        if (createdMission.getPerformedBy() != null && !createdMission.getPerformedBy().isEmpty()) {
            return "redirect:/missions/new/stations/add-more-station";
        } else {
            return "redirect:/missions/new/stations";
        }
    }

    @PostMapping("/new/stations/add-to-mission")
    public String addStationToMission(
            @RequestParam("stationId") Long stationId,
            @ModelAttribute("createdMission") Mission createdMission
    ) {
        CosmicStation station = stationRepository.findByIdWithPerformedOn(stationId).orElse(null);

        if (station == null || createdMission == null) {
            return "redirect:/missions/new/stations";
        }

        if (!createdMission.getPerformedBy().contains(station)) {
            createdMission.addPerformedBy(station);
            stationRepository.save(station);
        }

        return "redirect:/missions/new/stations/add-more-station";
    }

    @GetMapping("/new/stations/add-more-station")
    public String showAddMoreStationPage(
            @ModelAttribute("createdMission") Mission createdMission
    ) {
        return "missions/add-more-station";
    }

    @GetMapping("/new/confirm")
    public String showConfirmPage(SessionStatus status) {
        status.setComplete();
        return "missions/confirm";
    }
}
