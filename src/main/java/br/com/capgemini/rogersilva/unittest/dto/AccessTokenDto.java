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
public class AccessTokenDto extends Dto {

    private static final long serialVersionUID = 5027918646218594220L;

    private String accessToken;

    private String tokenType;
}