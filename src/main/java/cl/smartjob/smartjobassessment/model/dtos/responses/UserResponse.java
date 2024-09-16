package cl.smartjob.smartjobassessment.model.dtos.responses;

import java.time.LocalDateTime;

public record UserResponse(
        String id,
        LocalDateTime created,
        LocalDateTime modified,
        LocalDateTime lastLogin,
        String token,
        Boolean isActive
) {
}
