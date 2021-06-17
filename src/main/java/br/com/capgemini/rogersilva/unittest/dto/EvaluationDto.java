package br.com.capgemini.rogersilva.unittest.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_WRITE;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
public class EvaluationDto extends Dto {

    private static final long serialVersionUID = -2841048000812434490L;

    @NotNull(message = "Process id cannot be null")
    @JsonProperty(access = READ_WRITE)
    private Long processId;

    @NotBlank(message = "Feedback cannot be blank")
    private String feedback;
}