package br.com.capgemini.rogersilva.unittest.dto;

import java.util.List;

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
public class ErrorDto extends Dto {

    private static final long serialVersionUID = -7746326891794675775L;

    private String error;

    private String errorDescription;

    private List<ValidationErrorDto> validationErrors;
}