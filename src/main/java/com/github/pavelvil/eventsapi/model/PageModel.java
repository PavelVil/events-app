package com.github.pavelvil.eventsapi.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageModel {

    @NotNull(message = "Page number must be set!")
    @PositiveOrZero(message = "Page number must be positive or zero!")
    @Builder.Default
    private Integer number = 0;

    @NotNull(message = "Page size must be set")
    @Positive(message = "Page size must be positive!")
    @Builder.Default
    private Integer size = 10;

    public PageRequest toPageRequest() {
        return PageRequest.of(number, size);
    }

}
