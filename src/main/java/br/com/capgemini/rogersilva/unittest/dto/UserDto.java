package br.com.capgemini.rogersilva.unittest.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_WRITE;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.capgemini.rogersilva.unittest.model.Role;
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
public class UserDto extends Dto {

    private static final long serialVersionUID = -3232703420237052030L;

    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Max(value = 50, message = "Username must be less than or equal to 50")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = READ_WRITE)
    private String password;

    @NotNull(message = "Role cannot be null")
    private Role role;
}