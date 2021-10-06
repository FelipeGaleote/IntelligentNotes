package com.felipeg.intelligentnotes.users.controllers;

import com.felipeg.intelligentnotes.error_handling.ErrorResponse;
import com.felipeg.intelligentnotes.error_handling.exceptions.UsernameAlreadyInUseException;
import com.felipeg.intelligentnotes.security.JwtTokenUtil;
import com.felipeg.intelligentnotes.users.dtos.input.LoginInput;
import com.felipeg.intelligentnotes.users.dtos.input.SignUpInput;
import com.felipeg.intelligentnotes.users.dtos.output.LoginOutput;
import com.felipeg.intelligentnotes.users.dtos.output.SignUpOutput;
import com.felipeg.intelligentnotes.users.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "user")
public class UserController {


    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationService authenticationService;

    public UserController(JwtTokenUtil jwtTokenUtil, AuthenticationService authenticationService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authenticates the user with username and password and returns JWT token on header")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated",
                    content = {
                            @Content(schema = @Schema(implementation = LoginOutput.class))
                    },
                    headers = {
                            @Header(name = "Authorization", description = "JWT token that must be used to do authenticated requests")
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Failed to authenticate user",
                    content = {
                            @Content(schema = @Schema(hidden = true))
                    })
    })
    public ResponseEntity<LoginOutput> login(@RequestBody @Valid LoginInput request) {
        try {
            var user = authenticationService.authenticateUser(request.getUsername(), request.getPassword());
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            var responseBody = LoginOutput.from(user);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .body(responseBody);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a new user account with the information provided on request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = {
                            @Content(schema = @Schema(implementation = SignUpOutput.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public ResponseEntity<SignUpOutput> signUp(@RequestBody @Valid SignUpInput signUpInput) {
        SignUpOutput signUpOutput;
        try {
            var user = authenticationService.signUp(signUpInput.getUsername(), signUpInput.getEmail(), signUpInput.getPassword());
            signUpOutput = new SignUpOutput(user.getId());
        } catch (UsernameAlreadyInUseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already in use.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpOutput);
    }


}
