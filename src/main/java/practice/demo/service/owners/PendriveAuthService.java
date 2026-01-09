package practice.demo.service.owners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.owner.AddPendriveRequest;
import practice.demo.entity.Pendrive;
import practice.demo.repository.PendriveRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PendriveAuthService {

    private final PendriveRepository pendriveRepository;

    public ApiResponse addPendrive(AddPendriveRequest request) {

        // Check duplicate
        if (pendriveRepository.existsBySerialNumber(request.getSerialNumber())) {
            return new ApiResponse(false, "Pendrive already registered");
        }

        // Create new pendrive
        Pendrive pendrive = new Pendrive();
        pendrive.setSerialNumber(request.getSerialNumber());
        pendrive.setLabelName(request.getLabelName());
        pendrive.setCreatedAt(LocalDateTime.now());
        pendrive.setActive(true);
        pendrive.setInsertCount(1);

        pendriveRepository.save(pendrive);

        return new ApiResponse(true, "Pendrive registered successfully");
    }
}
