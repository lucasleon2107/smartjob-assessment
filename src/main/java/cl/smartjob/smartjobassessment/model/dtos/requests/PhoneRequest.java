package cl.smartjob.smartjobassessment.model.dtos.requests;

public record PhoneRequest(
        String number,
        String cityCode,
        String countryCode
) {
}
