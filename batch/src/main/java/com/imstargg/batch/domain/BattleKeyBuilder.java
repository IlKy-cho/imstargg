package com.imstargg.batch.domain;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BattleKeyBuilder {

    private final LocalDateTime battleTime;

    private final List<String> playerTags = new ArrayList<>();

    public BattleKeyBuilder(LocalDateTime battleTime) {
        this.battleTime = battleTime;
    }

    public BattleKeyBuilder addPlayerTag(String playerTag) {
        playerTags.add(playerTag);
        return this;
    }

    public String build() {
        byte[] keySeed = createKeySeed(battleTime, playerTags);
        MessageDigest md = createMessageDigest();
        byte[] hash = md.digest(keySeed);
        return bytesToHex(hash);
    }

    private byte[] createKeySeed(LocalDateTime battleTime, List<String> playerTags) {
        byte[] joinedPlayerTagBytes = (playerTags.stream()
                .sorted()
                .collect(Collectors.joining()))
                .getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(8 + joinedPlayerTagBytes.length);
        buffer.putLong(battleTime.toEpochSecond(ZoneOffset.UTC));
        buffer.put(joinedPlayerTagBytes);
        return buffer.array();
    }

    private MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
