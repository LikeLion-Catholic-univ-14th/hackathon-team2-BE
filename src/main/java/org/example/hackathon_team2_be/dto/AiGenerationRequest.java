package org.example.hackathon_team2_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AiGenerationRequest {
    //백엔드 1 -> 백엔드 2 요청

    private Product product;

    private List<LockedDna> lockedDna;

    private FutureContext futureContext;

    @Getter
    @AllArgsConstructor
    public static class Product {
        private String name;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public static class LockedDna {
        private String name;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public static class FutureContext {
        private String name;
        private String description;
    }
}
