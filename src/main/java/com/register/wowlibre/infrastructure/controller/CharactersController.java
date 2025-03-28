package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.port.in.characters.*;
import com.register.wowlibre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/characters")
public class CharactersController {
    private final CharactersPort charactersPort;

    public CharactersController(CharactersPort charactersPort) {
        this.charactersPort = charactersPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<CharactersDto>> characters(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId) {

        CharactersDto characters = charactersPort.characters(userId, accountId, serverId, transactionId);

        if (characters != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<CharactersDto>
                            (transactionId).ok(characters).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/loan/bank")
    public ResponseEntity<GenericResponse<CharactersDto>> loanApplicationCharacters(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId) {

        CharactersDto characters = charactersPort.loanApplicationCharacters(userId, accountId, serverId, transactionId);

        if (characters != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<CharactersDto>
                            (transactionId).ok(characters).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @DeleteMapping("/friend")
    public ResponseEntity<GenericResponse<Void>> deleteFriend(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid DeleteFriendDto deleteFriendDto) {

        charactersPort.deleteFriend(userId, deleteFriendDto.getAccountId(), deleteFriendDto.getServerId(),
                deleteFriendDto.getCharacterId(), deleteFriendDto.getFriendId(), transactionId);


        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping(path = "/mails")
    public ResponseEntity<GenericResponse<MailsDto>> mails(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = "account_id") final Long accountId,
            @RequestParam(name = "server_id") final Long serverId,
            @RequestParam(name = "character_id") final Long characterId) {

        final MailsDto mails = charactersPort.mails(userId, accountId, serverId, characterId, transactionId);

        if (mails != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<MailsDto>(transactionId).ok(mails).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/social/friends")
    public ResponseEntity<GenericResponse<CharacterSocialDto>> friends(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = "account_id") final Long accountId,
            @RequestParam(name = "server_id") final Long serverId,
            @RequestParam(name = "character_id") final Long characterId) {

        final CharacterSocialDto characterSocialDto = charactersPort.friends(userId, accountId, serverId, characterId,
                transactionId);

        if (characterSocialDto != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<CharacterSocialDto>(transactionId).ok(characterSocialDto).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping(path = "/account/change-password")
    public ResponseEntity<GenericResponse<Void>> changePassword(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid ChangePasswordDto changePasswordDto) {

        charactersPort.changePassword(userId,
                changePasswordDto.getAccountId(), changePasswordDto.getServerId(), changePasswordDto.getPassword(),
                changePasswordDto.getNewPassword(), transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    @GetMapping(path = "/professions")
    public ResponseEntity<GenericResponse<List<CharacterProfessionsDto>>> professions(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId,
            @RequestParam(name = PARAM_CHARACTER_ID) final Long characterId) {

        final List<CharacterProfessionsDto> professions = charactersPort.professions(userId, accountId, serverId,
                characterId, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<List<CharacterProfessionsDto>>(transactionId).ok(professions).build());

    }


    @PostMapping(path = "/social/send/level")
    public ResponseEntity<GenericResponse<Void>> sendLevel(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid SendLevelDto request) {


        charactersPort.sendLevel(userId, request.getAccountId(), request.getServerId(),
                request.getCharacterId(), request.getFriendId(), request.getLevel(),
                transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());

    }

    @PostMapping(path = "/social/send/money")
    public ResponseEntity<GenericResponse<Void>> sendMoney(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid SendMoneyDto sendMoneyDto) {

        charactersPort.sendMoney(userId, sendMoneyDto.getAccountId(), sendMoneyDto.getServerId(),
                sendMoneyDto.getCharacterId(), sendMoneyDto.getFriendId(), sendMoneyDto.getMoney(),
                transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());

    }

    @PostMapping(path = "/profession/announcement")
    public ResponseEntity<GenericResponse<Void>> announcementProfession(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid AnnouncementDto sendMoneyDto) {

        charactersPort.sendAnnouncement(userId, sendMoneyDto.getAccountId(), sendMoneyDto.getServerId(),
                sendMoneyDto.getCharacterId(), sendMoneyDto.getSkillId(), sendMoneyDto.getMessage(), transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    @GetMapping(path = "/inventory")
    public ResponseEntity<GenericResponse<List<CharacterInventoryResponse>>> inventory(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId,
            @RequestParam(name = PARAM_CHARACTER_ID) final Long characterId) {

        final List<CharacterInventoryResponse> items = charactersPort.getCharacterInventory(userId, accountId, serverId,
                characterId, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<List<CharacterInventoryResponse>>(transactionId).ok(items).build());

    }

    @PostMapping("/inventory/transfer")
    public ResponseEntity<GenericResponse<Void>> transferInventoryItem(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid TransferItemDto request) {

        charactersPort.transferInventoryItem(userId, request.getAccountId(),
                request.getServerId(), request.getCharacterId(), request.getFriendId(), request.getCount(),
                request.getItemId(),
                transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId)
                        .ok().build());
    }
}
