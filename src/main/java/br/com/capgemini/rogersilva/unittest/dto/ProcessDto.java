package br.com.capgemini.rogersilva.unittest.dto;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
public class ProcessDto extends Dto {

    private static final long serialVersionUID = -7822832149520156343L;

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @Max(value = 50, message = "Name must be less than or equal to 50")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotEmpty(message = "Evaluator ids cannot be empty")
    @JsonProperty(access = Access.READ_WRITE)
    private List<Long> evaluatorIds;
}