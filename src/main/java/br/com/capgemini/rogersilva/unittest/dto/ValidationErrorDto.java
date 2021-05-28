package br.com.capgemini.rogersilva.unittest.dto;

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
public class ValidationErrorDto extends Dto {

    private static final long serialVersionUID = 5959755884711380643L;

    private String object;

    private String field;

    private transient Object rejectedValue;

    private String message;
}